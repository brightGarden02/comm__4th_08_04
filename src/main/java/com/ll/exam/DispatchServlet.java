package com.ll.exam;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/usr/*")
public class DispatchServlet extends HttpServlet {


    // 조회
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        ControllerManager.runAction(request, response);
    }

    // 등록
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        doGet(request, response);
    }

    // 삭제
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        doGet(request, response);
    }

    // 수정
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        doGet(request, response);
    }

}
