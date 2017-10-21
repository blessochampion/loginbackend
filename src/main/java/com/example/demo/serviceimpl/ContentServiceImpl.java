package com.example.demo.serviceimpl;

import com.example.demo.model.Content;
import com.example.demo.repository.ContentRepository;
import com.example.demo.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.util.List;
import javax.transaction.Transactional;

@Service
@Transactional
public class ContentServiceImpl implements ContentService {
    
    @Autowired
    private ContentRepository contentRepository;

    @Override
    public List<Content> findContentsByUserId(long userId) {
        return contentRepository.findByUserId(userId);
    }

    @Override
    public Content createContent(Content content) {
        return contentRepository.save(content);
    }

    @Override
    public List<Content> findContentsByUserEmail(String email) {
        return contentRepository.findByDataOwnerEmail(email);
    }
}
