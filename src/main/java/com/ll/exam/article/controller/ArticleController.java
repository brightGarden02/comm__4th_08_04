package com.ll.exam.article.controller;


import com.ll.exam.Rq;
import com.ll.exam.annotation.Autowired;
import com.ll.exam.annotation.Controller;
import com.ll.exam.annotation.GetMapping;
import com.ll.exam.annotation.PostMapping;
import com.ll.exam.article.service.ArticleService;
import com.ll.exam.dto.ArticleDto;

import java.util.List;

@Controller // ArticleController가 컨트롤러이다.
public class ArticleController {

    @Autowired
    private ArticleService articleService;


    // /usr/article/list/free 와 같이 관련된 요청을 처리하는 함수이다.
    @GetMapping("/usr/article/list")
    // 아래 showList 는 Get /usr/article/list 으로 요청이 왔을 때 실행 되어야 하는 함수
    public void showList(Rq rq){
        List<ArticleDto> articleDtos = articleService.getArticles();

        rq.setAttr("articles", articleDtos);
        rq.view("usr/article/list");
    }

    @GetMapping("/usr/article/write")
    public void showWrite(Rq rq) {
        rq.view("usr/article/write");
    }

    @PostMapping("/usr/article/write")
    public void write(Rq rq) {

        String title = rq.getParam("title", "");
        String body = rq.getParam("body", "");

        if(title.length() == 0) {
            rq.historyBack("제목을 입력해주세요.");
            return;
        }

        if(body.length() == 0) {
            rq.historyBack("내용을 입력해주세요.");
            return;
        }

        long id = articleService.write(title, body);

        rq.replace("/usr/article/detail/%d".formatted(id), "%d번 게시물이 생성 되었습니다.".formatted(id));

    }


    @GetMapping("/usr/article/detail/{id}")
    public void showDetail(Rq rq) {
        long id = rq.getLongParam("id", 0);

        if (id == 0) {
            rq.historyBack("번호를 입력해주세요.");
            return;
        }

        ArticleDto articleDto = articleService.getArticleById(id);

        if (articleDto == null) {
            rq.historyBack("해당 글이 존재하지 않습니다.");
            return;
        }

        rq.setAttr("article", articleDto);
        rq.view("usr/article/detail");
    }

}
