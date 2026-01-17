package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
public class Article {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String url;

  private String title;

  @Column(length = 1000)
  private String description;

  private String source;

  private String category;

  private LocalDateTime publishedAt;

  private LocalDateTime fetchedAt;

  private Integer clickCount = 0;

  // Constructors
  public Article() {}

  public Article(String title, String description, String url, String source, LocalDateTime publishedAt) {
    this.title = title;
    this.description = description;
    this.url = url;
    this.source = source;
    this.publishedAt = publishedAt;
    this.fetchedAt = LocalDateTime.now();
  }

  // Getters and Setters
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getUrl() { return url; }
  public void setUrl(String url) { this.url = url; }

  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public String getSource() { return source; }
  public void setSource(String source) { this.source = source; }

  public String getCategory() { return category; }
  public void setCategory(String category) { this.category = category; }

  public LocalDateTime getPublishedAt() { return publishedAt; }
  public void setPublishedAt(LocalDateTime publishedAt) { this.publishedAt = publishedAt; }

  public LocalDateTime getFetchedAt() { return fetchedAt; }
  public void setFetchedAt(LocalDateTime fetchedAt) { this.fetchedAt = fetchedAt; }

  public Integer getClickCount() { return clickCount; }
  public void setClickCount(Integer clickCount) { this.clickCount = clickCount; }
}
