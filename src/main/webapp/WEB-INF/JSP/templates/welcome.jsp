<%@ page import="ua.george_nika.webtester.util.WebTesterConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="/WEB-INF/JSP/account/error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<link rel="shortcut icon" href="${context}/favicon.ico" type="image/x-icon">
<html>
<head>
    <link rel="stylesheet" href="${context}/resources/css/login_page.css" type="text/css">
    <link rel="stylesheet" href="${context}/resources/css/login_table.css" type="text/css">
    <title></title>
</head>
<body class="body">
<div>
    <table class="first_table_with_logo">
        <tr>
            <td class="logo_column">
                <img src="${context}/resources/images/exam_timer.png" alt="logo" class="logo">
            </td>
            <td class="second_table_column">
                <table class="second_table_with_buttons">
                    <tr>
                        <td class="text_column">
                            <img src="${context}/resources/images/webtester.gif" alt="logo">
                        </td>
                        <td class="buttons_column">
                            <br/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text_column">
                            <br/>
                        </td>
                        <td>
                        </td>
                    <tr>
                        <td class="text_column">
                            <br/>
                        </td>
                        <td>
                            <br/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</div>
<hr class="line"/>
<div>
    <c:set var="request_info" value="<%=WebTesterConstants.REQUEST_INFO%>"/>
    <c:set var="request_error" value="<%=WebTesterConstants.REQUEST_ERROR%>"/>
    <c:choose>
        <c:when test="${sessionScope.SPRING_SECURITY_LAST_EXCEPTION != null }">
            <div class="error_line" id="error_session_div">
                <script>
                    function clearError() {
                        var obj1 = document.getElementById("error_session_div");
                        var obj2 = document.getElementById('empty_div');
                        obj1.parentNode.replaceChild(obj2, obj1);
                    }
                    setTimeout(clearError, 5000);
                </script>
                    ${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message }
            </div>
        </c:when>
        <c:when test="${not empty requestScope.get(request_error)}">
            <div class="error_line" id="error_div">
                <script>
                    function clearError() {
                        var obj1 = document.getElementById("error_div");
                        var obj2 = document.getElementById('empty_div');
                        obj1.parentNode.replaceChild(obj2, obj1);
                    }
                    setTimeout(clearError, 5000);
                </script>
                    ${requestScope.get(request_error)}
            </div>
        </c:when>
        <c:when test="${not empty requestScope.get(request_info)}">
            <div class="info_line" id="info_div">
                <script>
                    function clearInfo() {
                        var obj1 = document.getElementById("info_div");
                        var obj2 = document.getElementById('empty_div');
                        obj1.parentNode.replaceChild(obj2, obj1);
                    }
                    setTimeout(clearInfo, 5000);
                </script>
                    ${requestScope.get(request_info)}
            </div>
        </c:when>
        <c:otherwise>
            <div>
                <br/>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<hr class="line"/>

<section>
    <decorator:body>

    </decorator:body>
</section>
<div id="empty_div">
    <br/>
</div>

</body>
</html>

