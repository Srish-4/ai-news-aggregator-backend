package com.example.demo.repository;

import com.example.demo.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
  Optional<Article> findByUrl(String url);
  List<Article> findBySourceAndFetchedAtAfter(String source, LocalDateTime time);

  // Search by title or description
  @Query("SELECT a FROM Article a WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(a.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
  List<Article> searchByKeyword(@Param("keyword") String keyword);

  // Filter by source
  List<Article> findBySource(String source);

  // Filter by category
  List<Article> findByCategory(String category);

  // Get trending (most clicked)
  List<Article> findTop20ByOrderByClickCountDesc();
}
