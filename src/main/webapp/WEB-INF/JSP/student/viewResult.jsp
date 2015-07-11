<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<html>
<head>
    <title></title>
</head>
<body>

<div class="student_table">
    <tag:paging></tag:paging>
    <table>
        <tr>
            <td width="5%" onclick="Sort('id')">№</td>
            <td width="65%" onclick="Sort('name')">Name</td>
            <td width="10%">Data</td>
            <td width="5%">Questions</td>
            <td width="5%">All Right</td>
            <td width="5%">Your right</td>
            <td width="5%">Your wrong</td>
        </tr>

        <c:forEach var="test" items="${testResultList}">
            <tr>
                <td>${test.idResult}</td>
                <td>${test.comment}</td>
                <td>${test.data}</td>
                <td>${test.countQuestion}</td>
                <td>${test.allRightAnswer}</td>
                <td>${test.rightAnswer}</td>
                <td>${test.wrongAnswer}</td>
            </tr>
        </c:forEach>
    </table>
</div>
<br/>
<br/>
<button class="student_right_button" onclick="window.location.href ='${context}/student/mainPage'">
    Close
</button>
<br/>
<br/>
<script>
    function Sort(valueSort) {
        window.location.href = '${context}/student/viewResultPage/${idAccount}?sort=' + valueSort;
    }
    function Page(valuePage) {
        window.location.href = '${context}/student/viewResultPage/${idAccount}?page=' + valuePage;
    }
    function IdLike(valueLike) {
        window.location.href = '${context}/student/viewResultPage/${idAccount}?idLike=' + valueLike;
    }
    function NameLike(valueLike) {
        window.location.href = '${context}/student/viewResultPage/${idAccount}?nameLike=' + valueLike;
    }
    function ClearLike() {
        window.location.href = '${context}/student/viewResultPage/${idAccount}?idLike=&nameLike=';
    }
</script>
</body>
</html>

