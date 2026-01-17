package com.example.demo.controller;

import com.example.demo.model.Article;
import com.example.demo.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NewsController {

  @Autowired
  private NewsService newsService;

  @GetMapping("/news")
  public List<Article> getNews() {
    return newsService.fetchAllNews();
  }

  @GetMapping("/news/search")
  public List<Article> searchNews(@RequestParam String q) {
    return newsService.searchNews(q);
  }

  @GetMapping("/news/source/{source}")
  public List<Article> getNewsBySource(@PathVariable String source) {
    return newsService.getBySource(source);
  }

  @GetMapping("/news/trending")
  public List<Article> getTrending() {
    return newsService.getTrending();
  }

  @PostMapping("/news/{id}/click")
  public void trackClick(@PathVariable Long id) {
    newsService.incrementClickCount(id);
  }
}
