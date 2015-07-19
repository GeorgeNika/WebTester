<%@ page import="ua.george_nika.webtester.util.WebTesterConstants" %>
<%@ page import="ua.george_nika.webtester.entity.AccountEntity" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>

<html>
<head>
    <title></title>
</head>
<body>
<c:set var="account_entity"
       value="<%=((AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT))%>"/>
<button class="right_button" onclick="window.location.href ='${context}/student/viewResultPage/${account_entity.idAccount}'">
    View previous results
</button>
<br/>
<br/>

<div class="student_table">
    <table>
        <tr>
            <td width="5%" onclick="Sort('id')">№</td>
            <td width="20%" onclick="Sort('name')">Name</td>
            <td width="5%">Duration</td>
            <td width="35%">Comment</td>
            <td width="5%">Question</td>
            <td width="30%">Author</td>
        </tr>

        <c:forEach var="test" items="${testList}">
            <tr onclick="window.location.href='${context}/student/beginTestPage/${test.idTest}'">

                <td>${test.idTest}</td>
                <td>${test.name}</td>
                <td>${test.durationInSecond}</td>
                <td>${test.comment}</td>
                <td>${test.getCountActiveQuestion()}</td>
                <td>
                        ${test.accountByIdTutor.getFirstName()}
                    <c:if test="${test.accountByIdTutor.getMiddleName()!=null}">
                        ${test.accountByIdTutor.getMiddleName()}
                    </c:if>
                        ${test.accountByIdTutor.getLastName()}
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
<div class="student_table">
    <tag:paging></tag:paging>
</div>
<content tag = "bs">
    <script>
    function a(){

    }
    </script>
</content>
<script>
    function Sort(valueSort) {
        window.location.href = '${context}/student/mainPage?sort=' + valueSort;
    }
    function Page(valuePage) {
        window.location.href = '${context}/student/mainPage?page=' + valuePage;
    }
    function IdLike(valueLike) {
        window.location.href = '${context}/student/mainPage?idLike=' + valueLike;
    }
    function NameLike(valueLike) {
        window.location.href = '${context}/student/mainPage?nameLike=' + valueLike;
    }
    function ClearLike() {
        window.location.href = '${context}/student/mainPage?idLike=&nameLike=';
    }
</script>
</body>
</html>
