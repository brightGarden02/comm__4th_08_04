package com.ll.exam;

import com.ll.exam.util.Ut;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Rq {
    private final HttpServletRequest request;
    private final HttpServletResponse response;


    @Setter @Getter
    private RouteInfo routeInfo;

    public Rq(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;

        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
    }

    public String getPathParam(String paramName, String defaultValue) {
        if ( routeInfo == null ) {
            return defaultValue;
        }

        String path = routeInfo.getPath();

        String[] pathBits = path.split("/");

        int index = -1;

        for ( int i = 0; i < pathBits.length; i++ ) {
            String pathBit = pathBits[i];

            if ( pathBit.equals("{" + paramName + "}") ) {
                index = i - 4;
                break;
            }
        }

        if ( index != -1 ) {
            return getPathValueByIndex(index, defaultValue);
        }

        return defaultValue;
    }

    public String getParam(String paramName, String defaultValue) {
        String value = request.getParameter(paramName);

        if ( value == null ) {
            value = getPathParam(paramName, null);
        }

        if (value == null || value.trim().length() == 0) {
            return defaultValue;
        }

        return value;
    }

    public long getLongParam(String paramName, long defaultValue) {

        String value = getParam(paramName, null);

        if (value == null) {
            return defaultValue;
        }

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public int getIntParam(String paramName, int defaultValue) {
        String value = getParam(paramName, null);

        if (value == null) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public void print(String str) {
        try {
            response.getWriter().append(str);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void println(String str) {
        print(str + "\n");
    }

    public void setAttr(String name, Object value) {
        request.setAttribute(name, value);
    }

    public void view(String path) {
        // gugudan2.jsp 에게 나머지 작업을 토스
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/jsp/" + path + ".jsp");
        try {
            requestDispatcher.forward(request, response);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPath() {
        return request.getRequestURI();
    }

    public String getActionPath() {
        String[] bits = request.getRequestURI().split("/");

        return "/%s/%s/%s".formatted(bits[1], bits[2], bits[3]);
    }

    public String getRouteMethod() {
        String method = getParam("_method", "");

        if (method.length() > 0) {
            return method.toUpperCase();
        }

        return request.getMethod();
    }

    public long getLongPathValueByIndex(int index, long defaultValue) {
        String value = getPathValueByIndex(index, null);

        if (value == null) {
            return defaultValue;
        }

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public String getPathValueByIndex(int index, String defaultValue) {
        String[] bits = request.getRequestURI().split("/");

        try {
            return bits[4 + index];
        } catch (ArrayIndexOutOfBoundsException e) {
            return defaultValue;
        }
    }

    public void replace(String uri, String msg) {
        if (msg != null && msg.trim().length() > 0) {
            println("""
                    <script>
                    alert("%s");
                    </script>
                    """.formatted(msg));
        }

        println("""
                <script>
                location.replace("%s");
                </script>
                """.formatted(uri));
    }

    public void historyBack(String msg) {
        if (msg != null && msg.trim().length() > 0) {
            println("""
                    <script>
                    alert("%s");
                    </script>
                    """.formatted(msg));
        }

        println("""
                <script>
                history.back();
                </script>
                """);
    }

    public void json(Object resultData) {
        response.setContentType("application/json; charset=utf-8");

        String jsonStr = Ut.json.toStr(resultData, "");
        println(jsonStr);
    }

    public void json(Object data, String resultCode, String msg) {
        json(new ResultData(resultCode, msg, data));
    }

    public void successJson(Object data) {
        json(data, "S-1", "성공");
    }

    public void failJson(Object data) {
        json(data, "F-1", "실패");
    }

    public void setRouteInfo(RouteInfo routeInfo) {

    }

    public RouteInfo getRouteInfo() {


        return null;
    }
}
