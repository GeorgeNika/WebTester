<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<html>
<head>
    <title></title>
</head>
<body>

<div class="tutor_table">
    <table>
        <tr>
            <td onclick="Sort('id')">№</td>
            <td width="70%" onclick="Sort('name')">Name</td>
            <td width="10%">Duration</td>
            <td width="5%">Questions</td>
            <td width="5%">All Right</td>
            <td width="5%">Your right</td>
            <td width="5%">Your wrong</td>
        </tr>

        <c:forEach var="test" items="${testResultList}">
            <tr>
                <td>${test.idResult}</td>
                <td>${test.accountByIdAccount.login}</td>
                <td>${test.data}</td>
                <td>${test.countQuestion}</td>
                <td>${test.allRightAnswer}</td>
                <td>${test.rightAnswer}</td>
                <td>${test.wrongAnswer}</td>
            </tr>
        </c:forEach>
    </table>
</div>
<div class="tutor_table">
    <tag:paging></tag:paging>
</div>
<br/>
<button class="right_button" onclick="window.location.href ='${context}/tutor/mainPage'">
    Close
</button>
<br/>
<script>
    function Sort(valueSort) {
        window.location.href = '${context}/tutor/viewResultPage/${idTest}?sort=' + valueSort;
    }
    function Page(valuePage) {
        window.location.href = '${context}/tutor/viewResultPage/${idTest}?page=' + valuePage;
    }
    function IdLike(valueLike) {
        window.location.href = '${context}/tutor/viewResultPage/${idTest}?idLike=' + valueLike;
    }
    function NameLike(valueLike) {
        window.location.href = '${context}/tutor/viewResultPage/${idTest}?nameLike=' + valueLike;
    }
    function ClearLike() {
        window.location.href = '${context}/tutor/viewResultPage/${idTest}?idLike=&nameLike=';
    }
</script>
</body>
</html>

