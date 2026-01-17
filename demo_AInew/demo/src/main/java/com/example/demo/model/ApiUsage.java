package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "api_usage")
public class ApiUsage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String source;
  private LocalDate date;
  private Integer requestCount;

  public ApiUsage() {}

  public ApiUsage(String source, LocalDate date, Integer requestCount) {
    this.source = source;
    this.date = date;
    this.requestCount = requestCount;
  }

  // Getters and setters
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getSource() { return source; }
  public void setSource(String source) { this.source = source; }

  public LocalDate getDate() { return date; }
  public void setDate(LocalDate date) { this.date = date; }

  public Integer getRequestCount() { return requestCount; }
  public void setRequestCount(Integer requestCount) { this.requestCount = requestCount; }
}
