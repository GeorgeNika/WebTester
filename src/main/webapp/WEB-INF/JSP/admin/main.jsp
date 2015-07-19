<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
<button class="right_button" onclick="window.location.href ='${context}/admin/createNewAccountPage'">
    + Create new account
</button>
<br/>
<br/>

<div class="admin_table">
    <table>
        <tr>
            <td width="3%"></td>
            <td width="5%" onclick="Sort('id')">№</td>
            <td width="10%" onclick="Sort('name')">Login</td>
            <td width="10%">Password</td>
            <td width="9%">FirstName</td>
            <td width="9%">MiddleName</td>
            <td width="9%">LastName</td>
            <td width="12%">E-mail</td>
            <td width="2%"></td>
            <td width="9%">created</td>
            <td width="9%">updated</td>
            <td width="13%">roleSet</td>
        </tr>

        <c:forEach var="user" items="${userList}">
            <tr onclick="window.location.href='${context}/admin/editAccountPage/${user.idAccount}'">
                <td>
                    <c:choose>
                        <c:when test="${user.active}">
                            <img src="${context}/resources/images/yes.png" class="admin_table_active_account">
                        </c:when>
                        <c:otherwise>
                            <img src="${context}/resources/images/no.png" class="admin_table_active_account">
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>${user.idAccount}</td>
                <td>${user.login}</td>
                <td>${user.password}</td>
                <td>${user.firstName}</td>
                <td>${user.middleName}</td>
                <td>${user.lastName}</td>
                <td>${user.email}</td>
                <td>
                    <c:choose>
                        <c:when test="${user.emailVerified}">
                            <img src="${context}/resources/images/yes.png" class="admin_table_active_email">
                        </c:when>
                        <c:otherwise>
                            <img src="${context}/resources/images/no.png" class="admin_table_active_email">
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="admin_table_date">${user.created}</td>
                <td>${user.updated}</td>
                <td>
                    <table>
                        <c:forEach var="role" items="${user.roleSet}">
                            <tr>
                                    ${role.name}
                            </tr>
                        </c:forEach>
                    </table>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
<div class="admin_table">
    <tag:paging></tag:paging>
</div>
<br/>
<script>
    function Sort(valueSort) {
        window.location.href = '${context}/admin/mainPage?sort=' + valueSort;
    }
    function Page(valuePage) {
        window.location.href = '${context}/admin/mainPage?page=' + valuePage;
    }
    function IdLike(valueLike) {
        window.location.href = '${context}/admin/mainPage?idLike=' + valueLike;
    }
    function NameLike(valueLike) {
        window.location.href = '${context}/admin/mainPage?nameLike=' + valueLike;
    }
    function ClearLike() {
        window.location.href = '${context}/admin/mainPage?idLike=&nameLike=';
    }
</script>
</body>
</html>
