package com.example.demo.repository;

import com.example.demo.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository
        extends JpaRepository<Content, Long> {
    List<Content> findByUserId(long userId);
}
