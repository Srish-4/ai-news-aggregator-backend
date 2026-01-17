package com.example.demo.repository;

import com.example.demo.model.ApiUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ApiUsageRepository extends JpaRepository<ApiUsage, Long> {
  Optional<ApiUsage> findBySourceAndDate(String source, LocalDate date);
}
