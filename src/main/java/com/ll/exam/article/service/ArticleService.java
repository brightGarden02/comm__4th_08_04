package com.ll.exam.article.service;

import com.ll.exam.annotation.Autowired;
import com.ll.exam.annotation.Service;
import com.ll.exam.article.repository.ArticleRepository;
import com.ll.exam.dto.ArticleDto;
import com.ll.exam.mymap.SecSql;

import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public List<ArticleDto> getArticles() {
        return articleRepository.getArticles();
    }

    public ArticleDto getArticleById(long id) {
        return articleRepository.getArticleById(id);
    }

    public long getArticlesCount() {
        return articleRepository.getArticlesCount();
    }

    public long write(String title, String body, boolean isBlind) {
        return articleRepository.write(title, body, isBlind);
    }

    public void modify(long id, String title, String body, boolean isBlind) {

        articleRepository.modify(id, title, body, isBlind);
    }


    public void delete(long id) {
        articleRepository.delete(id);
    }

    public ArticleDto getPrevArticle(ArticleDto articleDto) {
        long id = articleDto.getId();
        return articleRepository.getPrevArticle(id);
    }
}
