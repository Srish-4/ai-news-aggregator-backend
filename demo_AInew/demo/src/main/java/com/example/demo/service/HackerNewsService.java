package com.example.demo.service;

import com.example.demo.model.Article;
import com.example.demo.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class HackerNewsService {

  @Autowired
  private ArticleRepository articleRepository;

  private final RestTemplate restTemplate = new RestTemplate();

  public List<Article> fetchTopStories() {
    // Check cache
    LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
    List<Article> cachedArticles = articleRepository.findBySourceAndFetchedAtAfter("HackerNews", oneHourAgo);

    if (!cachedArticles.isEmpty()) {
      System.out.println("HackerNews Cache HIT");
      return cachedArticles;
    }

    System.out.println("🔄 HackerNews Cache MISS - Fetching");

    // Get top story IDs
    String topStoriesUrl = "https://hacker-news.firebaseio.com/v0/topstories.json";
    Integer[] storyIds = restTemplate.getForObject(topStoriesUrl, Integer[].class);

    List<Article> articles = new ArrayList<>();

    // Fetch top 20 stories
    if (storyIds != null) {
      for (int i = 0; i < Math.min(20, storyIds.length); i++) {
        try {
          String itemUrl = "https://hacker-news.firebaseio.com/v0/item/" + storyIds[i] + ".json";
          HNItem item = restTemplate.getForObject(itemUrl, HNItem.class);

          if (item != null && item.getUrl() != null) {
            // Check if already exists
            if (articleRepository.findByUrl(item.getUrl()).isEmpty()) {
              Article article = new Article(
                item.getTitle(),
                "HackerNews discussion with " + (item.getScore() != null ? item.getScore() : 0) + " points",
                item.getUrl(),
                "HackerNews",
                item.getTime() != null ?
                  LocalDateTime.ofInstant(Instant.ofEpochSecond(item.getTime()), ZoneId.systemDefault()) :
                  LocalDateTime.now()
              );
              articleRepository.save(article);
              articles.add(article);
            }
          }
        } catch (Exception e) {
          System.err.println("Error fetching HN item " + storyIds[i] + ": " + e.getMessage());
        }
      }
    }

    return articles;
  }

  // DTO for HackerNews API response
  static class HNItem {
    private String title;
    private String url;
    private Long time;
    private Integer score;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public Long getTime() { return time; }
    public void setTime(Long time) { this.time = time; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
  }
}
