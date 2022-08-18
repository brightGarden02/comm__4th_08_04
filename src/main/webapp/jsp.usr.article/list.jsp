<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../common/head.jspf"%>

<meta charset="UTF-8">

<section>
    <div class="container px-3 mx-auto">
        <h1 class="font-bold text-lg">
            <i class="fa-solid fa-rectangle-list"></i> 목록
        </h1>

        <ul class="mt-5">
            <c:forEach items="${articles}" var="article">
                <li class="flex">
                    번호 : ${article.id}
                    <br />
                    등록날짜 : ${article.createdDate}
                    <br />
                    수정날짜 : ${article.createdDate}
                    <br />
                    제목 : ${article.title}
                </li>
            </c:forEach>
        </ul>
    </div>
</section>

<%@ include file="../common/foot.jspf"%>