package com.ll.exam;

import com.ll.exam.article.service.ArticleService;
import com.ll.exam.dto.ArticleDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleServiceTest {

    @Test
    public void 존재한다() {
        ArticleService articleService = Container.getObj(ArticleService.class);

        assertThat(articleService).isNotNull();
    }

    @Test
    public void getArticles() {
        ArticleService articleService = Container.getObj(ArticleService.class);

        List<ArticleDto> articleDtoList = articleService.getArticles();
        assertThat(articleDtoList.size()).isEqualTo(3);
    }

}
