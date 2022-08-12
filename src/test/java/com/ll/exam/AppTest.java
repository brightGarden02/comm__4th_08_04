package com.ll.exam;

import com.ll.exam.Container;
import com.ll.exam.ControllerManager;
import com.ll.exam.RouteInfo;
import com.ll.exam.article.controller.ArticleController;
import com.ll.exam.article.repository.ArticleRepository;
import com.ll.exam.home.controller.HomeController;
import com.ll.exam.article.service.ArticleService;
import com.ll.exam.util.Ut;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AppTest {

    @Test
    public void junit_assertThat() {

        int rs = 10 + 20;

        assertThat(rs).isEqualTo(30);
    }


    @Test
    public void ioc__articleController() {
        ArticleController articleController = Container.getObj(ArticleController.class);

        assertThat(articleController).isNotNull();
    }


    @Test
    public void ioc__articleContoller__싱글톤() {
        ArticleController articleController1 = Container.getObj(ArticleController.class);
        ArticleController articleController2 = Container.getObj(ArticleController.class);


        assertThat(articleController2).isEqualTo(articleController1);
    }

    @Test
    public void ioc__homeContoller() {
        HomeController homeController = Container.getObj(HomeController.class);
        assertThat(homeController).isNotNull();
    }


    @Test
    public void ioc__homeController__싱글톤() {
        HomeController homeController1 = Container.getObj(HomeController.class);
        HomeController homeController2 = Container.getObj(HomeController.class);

        assertThat(homeController2).isEqualTo(homeController1);
    }


//    @Test
//    public void ioc__Controller들을_스캔하여_수집() {
//        List<String> names = Container.getControllerNames();
//
//        assertThat(names).contains("home");
//        assertThat(names).contains("article");
//
//    }

    @Test
    public void ioc__articleService() {
        ArticleService articleService = Container.getObj(ArticleService.class);

        assertThat(articleService).isNotNull();
    }

    @Test
    public void ioc__articleService__싱글톤() {
        ArticleService articleService1 = Container.getObj(ArticleService.class);
        ArticleService articleService2 = Container.getObj(ArticleService.class);

        assertThat(articleService2).isEqualTo(articleService1);
    }

    @Test
    public void articleController를_생성할때_articleService도_같이_생성() {
        ArticleController articleController = Container.getObj(ArticleController.class);

        ArticleService articleService = Ut.reflection.getFieldValue(articleController, "articleService", null);

        assertThat(articleService).isNotNull();
    }

    @Test
    public void articleService를_생성할때_articleRepository도_같이_생성() {
        ArticleService articleService = Container.getObj(ArticleService.class);

        ArticleRepository articleRepository = Ut.reflection.getFieldValue(articleService, "articleRepository", null);

        assertThat(articleRepository).isNotNull();
    }

    @Test
    public void ControllerManager__scanMappings() {
        ControllerManager.init(); // 클래스를 강제로 로딩되게 하려는 목적
    }

    @Test
    public void ControllerManager__라우트정보_개수() {
        Map<String, RouteInfo> routeInfos = ControllerManager.getRouteInfosForTest();

//        assertThat(routeInfos.size()).isEqualTo(4);
    }

}
