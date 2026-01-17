package com.example.demo.service;

import com.example.demo.model.Article;
import com.example.demo.model.NewsResponse;
import com.example.demo.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {

  @Value("${news.api.key}")
  private String apiKey;

  @Autowired
  private ArticleRepository articleRepository;

  @Autowired
  private HackerNewsService hackerNewsService;

  @Autowired
  private ArxivService arxivService;

  private final RestTemplate restTemplate = new RestTemplate();



  public List<Article> fetchAllNews() {
    List<Article> allArticles = new ArrayList<>();

    // Fetch from NewsAPI
    allArticles.addAll(fetchNewsAPI());

    // Fetch from HackerNews
    allArticles.addAll(hackerNewsService.fetchTopStories());

    allArticles.addAll(arxivService.fetchAIPapers());

    return allArticles;
  }


  public List<Article> searchNews(String keyword) {
    return articleRepository.searchByKeyword(keyword);
  }

  public List<Article> getBySource(String source) {
    return articleRepository.findBySource(source);
  }

  public List<Article> getTrending() {
    return articleRepository.findTop20ByOrderByClickCountDesc();
  }

  public void incrementClickCount(Long articleId) {
    articleRepository.findById(articleId).ifPresent(article -> {
      article.setClickCount(article.getClickCount() + 1);
      articleRepository.save(article);
    });
  }

  private List<Article> fetchNewsAPI() {
    LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
    List<Article> cachedArticles = articleRepository.findBySourceAndFetchedAtAfter("NewsAPI", oneHourAgo);

    if (!cachedArticles.isEmpty()) {
      System.out.println("NewsAPI Cache HIT");
      return cachedArticles;
    }

    System.out.println("NewsAPI Cache MISS - Fetching");
    String url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=" + apiKey;
    NewsResponse response = restTemplate.getForObject(url, NewsResponse.class);

    List<Article> articles = new ArrayList<>();
    if (response != null && response.getArticles() != null) {
      for (NewsResponse.Article apiArticle : response.getArticles()) {
        if (articleRepository.findByUrl(apiArticle.getUrl()).isEmpty()) {
          Article article = new Article(
            apiArticle.getTitle(),
            apiArticle.getDescription(),
            apiArticle.getUrl(),
            "NewsAPI",
            parsePublishedAt(apiArticle.getPublishedAt())
          );
          articleRepository.save(article);
          articles.add(article);
        }
      }
    }

    return articles;
  }


  private LocalDateTime parsePublishedAt(String publishedAt) {
    try {
      return ZonedDateTime.parse(publishedAt).toLocalDateTime();
    } catch (Exception e) {
      return LocalDateTime.now();
    }
  }
}
