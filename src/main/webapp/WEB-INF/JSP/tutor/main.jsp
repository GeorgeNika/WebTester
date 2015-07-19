<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>

<html>
<head>
    <title></title>
</head>
<body>
<button class="right_button" onclick="window.location.href ='${context}/tutor/createNewTestPage'">
    + Create new test
</button>
<br/>
<br/>

<div class="tutor_table">
    <table>
        <tr>
            <td></td>
            <td onclick="Sort('id')">№</td>
            <td width="20%" onclick="Sort('name')">Name</td>
            <td width="5%">Duration</td>
            <td width="40%">Comment</td>
            <td width="5%">Question</td>
            <td width="10%">Author</td>
            <td width="10%">created</td>
            <td width="10%">updated</td>
        </tr>

        <c:forEach var="test" items="${testList}">
            <tr onclick="window.location.href='${context}/tutor/editTestPage/${test.idTest}'">
                <td>
                    <c:choose>
                        <c:when test="${test.active}">
                            <img src="${context}/resources/images/yes.png" class="admin_table_active_account">
                        </c:when>
                        <c:otherwise>
                            <img src="${context}/resources/images/no.png" class="admin_table_active_account">
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>${test.idTest}</td>
                <td>${test.name}</td>
                <td>${test.durationInSecond}</td>
                <td>${test.comment}</td>
                <td>${test.questionList.size()}</td>
                <td>${test.accountByIdTutor.login}</td>
                <td>${test.created}</td>
                <td>${test.updated}</td>
            </tr>
        </c:forEach>
    </table>
</div>
<div class="tutor_table">
    <tag:paging></tag:paging>
</div>
<script>
    function Sort(valueSort) {
        window.location.href = '${context}/tutor/mainPage?sort=' + valueSort;
    }
    function Page(valuePage) {
        window.location.href = '${context}/tutor/mainPage?page=' + valuePage;
    }
    function IdLike(valueLike) {
        window.location.href = '${context}/tutor/mainPage?idLike=' + valueLike;
    }
    function NameLike(valueLike) {
        window.location.href = '${context}/tutor/mainPage?nameLike=' + valueLike;
    }
    function ClearLike() {
        window.location.href = '${context}/tutor/mainPage?idLike=&nameLike=';
    }
</script>
</body>
</html>
