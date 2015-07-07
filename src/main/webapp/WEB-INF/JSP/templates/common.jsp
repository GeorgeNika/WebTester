<%@ page import="ua.george_nika.webtester.util.WebTesterConstants" %>
<%@ page import="ua.george_nika.webtester.entity.AccountEntity" %>
<%@ page import="ua.george_nika.webtester.entity.RoleEntity" %>
<%@ page import="ua.george_nika.webtester.util.WebTesterRole" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page errorPage="/WEB-INF/JSP/account/error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<link rel="shortcut icon" href="${context}/favicon.ico" type="image/x-icon">
<html>
<head>
    <c:set var="session_role"
           value="<%=((RoleEntity)session.getAttribute(WebTesterConstants.SESSION_ROLE)).getName()%>"/>
    <c:set var="admin_role" value="<%=WebTesterRole.ADMINISTRATOR.getName()%>"/>
    <c:set var="advtutor_role" value="<%=WebTesterRole.ADVANCE_TUTOR.getName()%>"/>
    <c:set var="tutor_role" value="<%=WebTesterRole.TUTOR.getName()%>"/>
    <c:set var="student_role" value="<%=WebTesterRole.STUDENT.getName()%>"/>
    <link rel="stylesheet" href="${context}/resources/css/cs-select.css" type="text/css">
    <c:choose>
        <c:when test="${session_role == admin_role}">
            <link rel="stylesheet" href="${context}/resources/css/admin_page.css" type="text/css">
            <link rel="stylesheet" href="${context}/resources/css/admin_table.css" type="text/css">
            <link rel="stylesheet" href="${context}/resources/css/admin_select_role.css" type="text/css">
        </c:when>
        <c:when test="${session_role==advtutor_role}">
            <link rel="stylesheet" href="${context}/resources/css/tutor_page.css" type="text/css">
            <link rel="stylesheet" href="${context}/resources/css/tutor_table.css" type="text/css">
            <link rel="stylesheet" href="${context}/resources/css/tutor_select_role.css" type="text/css">
        </c:when>
        <c:when test="${session_role==tutor_role}">
            <link rel="stylesheet" href="${context}/resources/css/tutor_page.css" type="text/css">
            <link rel="stylesheet" href="${context}/resources/css/tutor_table.css" type="text/css">
            <link rel="stylesheet" href="${context}/resources/css/tutor_select_role.css" type="text/css">
        </c:when>
        <c:when test="${session_role==student_role}">
            <link rel="stylesheet" href="${context}/resources/css/student_page.css" type="text/css">
            <link rel="stylesheet" href="${context}/resources/css/student_table.css" type="text/css">
            <link rel="stylesheet" href="${context}/resources/css/student_select_role.css" type="text/css">
        </c:when>
        <c:otherwise>
            <link rel="stylesheet" href="${context}/resources/css/admin_page.css" type="text/css">
            <link rel="stylesheet" href="${context}/resources/css/admin_table.css" type="text/css">
            <link rel="stylesheet" href="${context}/resources/css/admin_select_role.css" type="text/css">
        </c:otherwise>
    </c:choose>


    <title></title>
</head>
<body class="body">
<div class="header">
    <table class="first_table_with_logo">
        <tr>
            <td class="logo_column">
                <img src="${context}/resources/images/exam_timer.png" alt="logo" class="logo">
            </td>
            <td class="name_column">
                <h1>${namePage}</h1>
            </td>
            <td class="second_table_column">
                <table class="second_table_with_buttons">
                    <c:set var="account_entity"
                           value="<%=((AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT))%>"/>
                    <tr>
                        <td class="text_column">
                            ${account_entity.getFirstName()}
                            <c:if test="${account_entity.getMiddleName()!=null}">
                                ${account_entity.getMiddleName()}
                            </c:if>
                            ${account_entity.getLastName()}
                        </td>
                        <td class="buttons_column">
                            <button class="logout_button"
                                    onclick="window.location.href='${context}/editAccountForNonAdminPage/${account_entity.idAccount}'">
                                Edit account
                            </button>
                        </td>
                    </tr>
                    <tr>
                        <td class="text_column">
                            You login as ${account_entity.getLogin()}
                        </td>
                        <td class="buttons_column">
                            <button class="logout_button" onclick="window.location.href ='${context}/logoutAction'">
                                Logout
                            </button>
                        </td>
                    <tr>
                        <td class="text_column">
                            with role ${session_role}
                        </td>
                        <td>
                            <section>
                                <select class="cs-select cs-skin-underline">
                                    <option value="" disabled selected>Change role</option>
                                    <c:set var="session_role_list" value="<%=WebTesterConstants.SESSION_ROLE_LIST%>"/>
                                    <c:forEach var="role" items="${sessionScope.get(session_role_list)}">
                                        <option data-link="${context}/changeRoleAction/${role.idRole}">
                                                ${role.name}
                                        </option>
                                    </c:forEach>
                                </select>
                            </section>

                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>

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
                        setTimeout(clearInfo 5000);
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
</div>
<section class="article">
    <decorator:body>

    </decorator:body>
</section>
<div id="empty_div">
    <br/>
</div>
<script src="${context}/resources/js/classie.js"></script>
<script src="${context}/resources/js/selectFx.js"></script>
<script>
    (function () {
        [].slice.call(document.querySelectorAll('select.cs-select')).forEach(function (el) {
            new SelectFx(el);
        });
    })();
</script>
</body>
</html>

