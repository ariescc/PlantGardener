package com.evan.wj.controller;

import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.models.Article;
import com.evan.wj.models.Plant;
import com.evan.wj.result.Result;
import com.evan.wj.result.ResultFactory;
import com.evan.wj.service.ArticleService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
public class ArticleController {
    @Autowired
    ArticleService articleService;

    @ApiOperation(value = "获取单篇文章信息")
    @GetMapping("/api/article/{id}")
    public Result getArticleDetail(@PathVariable("id") int id) {
        Article article = articleService.getArticleDetail(id);
        return ResultFactory.buildSuccessResult(article);
    }

    @ApiOperation(value = "发布一篇新的动态文章")
    @PostMapping("/api/article/add")
    public Result publishNewArticle(@RequestBody Article article) {
        articleService.addNewArticle(article);
        return ResultFactory.buildSuccessResult("发布成功！");
    }

    @ApiOperation(value = "删除文章")
    @PostMapping("/api/article/del")
    public Result deleteArticleById(@RequestBody Map map) {
        articleService.deleteArticleById((Integer) map.get("id"));
        return ResultFactory.buildSuccessResult("删除成功！");
    }

    @ApiOperation(value = "分页查询文章信息")
    @GetMapping("/api/article/list")
    public Result getAllArticles(@RequestParam("page") int page,
                               @RequestParam("limit") int limit,
                               @RequestParam("sort") String sort) {
        PaginationDTO<Article> allArticles = articleService.getAllArticlesByPage(page, limit);
        return ResultFactory.buildSuccessResult(allArticles);
    }
}
