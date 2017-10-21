package com.example.demo.repository;

import com.example.demo.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository  extends JpaRepository<Content, Long> {
    
    List<Content> findByUserId(long userId);
    
    List<Content> findByDataOwnerEmail(String email);
}
