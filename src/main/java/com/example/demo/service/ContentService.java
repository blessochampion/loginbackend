package com.example.demo.service;

import com.example.demo.model.Content;
import org.springframework.context.annotation.Bean;

import java.util.List;

public interface ContentService {
    List<Content> findContentsByUserId(long userId);
    Content createContent(Content content);
}
