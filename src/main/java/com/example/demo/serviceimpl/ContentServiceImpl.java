package com.example.demo.serviceimpl;

import com.example.demo.model.Content;
import com.example.demo.repository.ContentRepository;
import com.example.demo.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    private ContentRepository contentRepository;

    @Autowired
    public void setContentRepository(ContentRepository contentRepository){
        this.contentRepository = contentRepository;
    }

    @Override
    public List<Content> findContentsByUserId(long userId) {
        return contentRepository.findByUserId(userId);
    }

    @Override
    public Content createContent(Content content) {
        return contentRepository.save(content);
    }
}
