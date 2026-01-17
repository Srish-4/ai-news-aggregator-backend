package com.example.demo.service;

import com.example.demo.model.Article;
import com.example.demo.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArxivService {

  @Autowired
  private ArticleRepository articleRepository;

  private final RestTemplate restTemplate = new RestTemplate();

  public List<Article> fetchAIPapers() {
    LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
    List<Article> cachedArticles = articleRepository.findBySourceAndFetchedAtAfter("arXiv", oneHourAgo);

    if (!cachedArticles.isEmpty()) {
      System.out.println("✅ arXiv Cache HIT");
      return cachedArticles;
    }

    System.out.println("🔄 arXiv Cache MISS - Fetching");

    List<Article> articles = new ArrayList<>();

    try {
      String url = "http://export.arxiv.org/api/query?search_query=cat:cs.AI+OR+cat:cs.LG&sortBy=lastUpdatedDate&sortOrder=descending&max_results=20";
      String xmlResponse = restTemplate.getForObject(url, String.class);

      if (xmlResponse == null || xmlResponse.isEmpty()) {
        System.err.println("⚠️ arXiv returned empty response");
        return articles;
      }

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes()));

      NodeList entries = doc.getElementsByTagName("entry");
      System.out.println("Found " + entries.getLength() + " arXiv papers");

      for (int i = 0; i < entries.getLength(); i++) {
        Element entry = (Element) entries.item(i);

        String title = getElementText(entry, "title").replaceAll("\\s+", " ");
        String summary = getElementText(entry, "summary").replaceAll("\\s+", " ");
        String link = getElementText(entry, "id");
        String published = getElementText(entry, "published");

        if (link != null && !link.isEmpty() && articleRepository.findByUrl(link).isEmpty()) {
          Article article = new Article(
            title,
            summary.length() > 500 ? summary.substring(0, 500) + "..." : summary,
            link,
            "arXiv",
            parseArxivDate(published)
          );
          article.setCategory("research");
          articleRepository.save(article);
          articles.add(article);
        }
      }
    } catch (Exception e) {
      System.err.println("Error fetching arXiv: " + e.getMessage());
      e.printStackTrace();
    }

    return articles;
  }

  private String getElementText(Element parent, String tagName) {
    NodeList nodes = parent.getElementsByTagName(tagName);
    if (nodes.getLength() > 0) {
      return nodes.item(0).getTextContent().trim();
    }
    return "";
  }

  private LocalDateTime parseArxivDate(String dateStr) {
    try {
      return LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME);
    } catch (Exception e) {
      return LocalDateTime.now();
    }
  }
}
