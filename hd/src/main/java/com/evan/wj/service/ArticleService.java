package com.evan.wj.service;

import com.evan.wj.dao.ArticleDAO;
import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.models.Article;
import com.evan.wj.models.Plant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {
    @Autowired
    ArticleDAO articleDAO;
    
    public Article getArticleDetail(int id) {
        return articleDAO.findById(id);
    }

    public void addNewArticle(Article article) {
        articleDAO.save(article);
    }

    public void deleteArticleById(Integer id) {
        articleDAO.deleteById(id);
    }

    public PaginationDTO<Article> getAllArticlesByPage(int page, int limit) {
        List<Article> articles = articleDAO.findAll();
        List<Article> curArticleList = new ArrayList<>();
        for (int i = (page - 1) * limit; i < Math.min(limit * page, articles.size()); i++) {
            curArticleList.add(articles.get(i));
        }
        return new PaginationDTO<>(articles.size(), curArticleList);
    }
}
