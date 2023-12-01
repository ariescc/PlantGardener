package com.evan.wj.dao;

import com.evan.wj.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleDAO extends JpaRepository<Article, Integer> {
    Article findById(int id);
    void deleteById(int id);
}
