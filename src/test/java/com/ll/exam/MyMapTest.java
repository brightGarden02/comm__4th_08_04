package com.ll.exam;

import com.ll.exam.mymap.MyMap;
import com.ll.exam.mymap.SecSql;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MyMapTest {
    private MyMap myMap;

    private void createArticleTable() {
        myMap.run("DROP TABLE IF EXISTS article");

        myMap.run("""                                                
                CREATE TABLE article (
                    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
                    PRIMARY KEY(id),
                    createdDate DATETIME NOT NULL,
                    modifiedDate DATETIME NOT NULL,
                    title VARCHAR(100) NOT NULL,
                    `body` TEXT NOT NULL,
                    isBlind BIT(1) NOT NULL DEFAULT(0)
                )
                """);
    }

    private void makeArticleTestData() {
        IntStream.rangeClosed(1, 6).forEach(no -> {
            boolean isBlind = no > 3;
            String title = "제목%d".formatted(no);
            String body = "내용%d".formatted(no);

            myMap.run("""
                    INSERT INTO article
                    SET createdDate = NOW(),
                    modifiedDate = NOW(),
                    title = ?,
                    `body` = ?,
                    isBlind = ?
                    """, title, body, isBlind);
        });
    }

    private void truncateArticleTable() {
        myMap.run("TRUNCATE article");
    }

    @BeforeAll
    public void beforeAll() {
        myMap = new MyMap("localhost", "lldj", "lldj1234", "my_map");
        // 개발모드를 켠다.
        // 개발모드를 켜면 실행되는 쿼리가 콘솔에 출력됨
        myMap.setDevMode(true);

        createArticleTable();
    }

    @BeforeEach
    public void beforeEach() {
        truncateArticleTable();
        makeArticleTestData();
    }

    @Test
    public void insert() {
        SecSql sql = myMap.genSecSql();
        sql
                .append("INSERT INTO article")
                .append("SET createdDate = NOW()")
                .append(", modifiedDate = NOW()")
                .append(", title = ?", "제목 new")
                .append(", body = ?", "내용 new");

        long newId = sql.insert();

        assertThat(newId).isGreaterThan(0);
    }

    @Test
    public void update() {
        SecSql sql = myMap.genSecSql();

        // id가 0, 1, 2, 3인 글 수정
        // id가 0인 글은 없으니, 실제로는 3개의 글이 삭제됨
        sql
                .append("UPDATE article")
                .append("SET title = ?", "제목 new")
                .append("WHERE id IN (?, ?, ?, ?)", 0, 1, 2, 3);

        // 수정된 row 개수
        long affectedRowsCount = sql.update();

        assertThat(affectedRowsCount).isEqualTo(3);
    }

    @Test
    public void delete() {
        SecSql sql = myMap.genSecSql();

        // id가 0, 1, 3인 글 삭제
        // id가 0인 글은 없으니, 실제로는 2개의 글이 삭제됨
        sql
                .append("DELETE")
                .append("FROM article")
                .append("WHERE id IN (?, ?, ?)", 0, 1, 3);

        // 삭제된 row 개수
        long affectedRowsCount = sql.delete();

        assertThat(affectedRowsCount).isEqualTo(2);
    }

    @Test
    public void selectDatetime() {
        SecSql sql = myMap.genSecSql();
        sql
                .append("SELECT NOW()");

        LocalDateTime datetime = sql.selectDatetime();

        long diff = ChronoUnit.SECONDS.between(datetime, LocalDateTime.now());

        assertThat(diff).isLessThanOrEqualTo(1L);
    }

    @Test
    public void selectLong() {
        SecSql sql = myMap.genSecSql();
        sql
                .append("SELECT id")
                .append("FROM article")
                .append("WHERE id = 1");

        Long count = sql.selectLong();

        assertThat(count).isEqualTo(1);
    }

    @Test
    public void selectString() {
        SecSql sql = myMap.genSecSql();
        sql
                .append("SELECT title")
                .append("FROM article")
                .append("WHERE id = 1");

        String title = sql.selectString();

        assertThat(title).isEqualTo("제목1");
    }

    @Test
    public void selectRow() {
        SecSql sql = myMap.genSecSql();
        sql
                .append("SELECT * FROM article WHERE id = 1");
        Map<String, Object> articleMap = sql.selectRow();

        assertThat(articleMap.get("id")).isEqualTo(1L);
        assertThat(articleMap.get("title")).isEqualTo("제목1");
        assertThat(articleMap.get("body")).isEqualTo("내용1");
        assertThat(articleMap.get("createdDate")).isInstanceOf(LocalDateTime.class);
        assertThat(articleMap.get("modifiedDate")).isInstanceOf(LocalDateTime.class);
        assertThat(articleMap.get("isBlind")).isEqualTo(false);
    }

    @Test
    public void selectArticle() {
        SecSql sql = myMap.genSecSql();
        sql
                .append("SELECT * FROM article WHERE id = 1");
        ArticleDto articleDto = sql.selectRow(ArticleDto.class);

        assertThat(articleDto.getId()).isEqualTo(1L);
        assertThat(articleDto.getTitle()).isEqualTo("제목1");
        assertThat(articleDto.getBody()).isEqualTo("내용1");
        assertThat(articleDto.getCreatedDate()).isNotNull();
        assertThat(articleDto.getModifiedDate()).isNotNull();
        assertThat(articleDto.isBlind()).isFalse();
    }

    @Test
    public void selectArticles() {
        SecSql sql = myMap.genSecSql();
        sql
                .append("SELECT * FROM article ORDER BY id ASC LIMIT 3");
        List<ArticleDto> articleDtoList = sql.selectRows(ArticleDto.class);

        IntStream.range(0, articleDtoList.size()).forEach(i -> {
            long id = i + 1;

            ArticleDto articleDto = articleDtoList.get(i);

            assertThat(articleDto.getId()).isEqualTo(id);
            assertThat(articleDto.getTitle()).isEqualTo("제목%d".formatted(id));
            assertThat(articleDto.getBody()).isEqualTo("내용%d".formatted(id));
            assertThat(articleDto.getCreatedDate()).isNotNull();
            assertThat(articleDto.getModifiedDate()).isNotNull();
            assertThat(articleDto.isBlind()).isFalse();
        });
    }

    @Test
    public void selectBind() {
        SecSql sql = myMap.genSecSql();
        sql
                .append("SELECT COUNT(*)")
                .append("FROM article")
                .append("WHERE id BETWEEN ? AND ?", 1, 3)
                .append("AND title LIKE CONCAT('%', ? '%')", "제목");

        long count = sql.selectLong();

        assertThat(count).isEqualTo(3);
    }

    @Test
    public void selectIn() {
        SecSql sql = myMap.genSecSql();
        sql
                .append("SELECT COUNT(*)")
                .append("FROM article")
                .appendIn("WHERE id IN (?)", Arrays.asList(1L, 2L, 3L));

        long count = sql.selectLong();

        assertThat(count).isEqualTo(3);
    }

    @Test
    public void selectOrderByField() {
        List<Long> ids = Arrays.asList(2L, 3L, 1L);

        SecSql sql = myMap.genSecSql();
        sql
                .append("SELECT id")
                .append("FROM article")
                .appendIn("WHERE id IN (?)", ids)
                .appendIn("ORDER BY FIELD (id, ?)", ids);

        List<Long> foundIds = sql.selectLongs();

        assertThat(foundIds).isEqualTo(ids);
    }
}
