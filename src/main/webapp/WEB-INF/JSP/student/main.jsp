<%@ page import="ua.george_nika.webtester.util.WebTesterConstants" %>
<%@ page import="ua.george_nika.webtester.entity.AccountEntity" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!--
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<tag:tag></tag:tag>-->

<html>
<head>
    <title></title>
</head>
<body>
<br/>
<c:set var="account_entity"
       value="<%=((AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT))%>"/>
<button class="student_right_button" onclick="window.location.href ='${context}/student/viewResultPage/${account_entity.idAccount}'">
    View previous results
</button>
<br/>
<br/>

<div class="student_table">
    <table>
        <tr>
            <td>№</td>
            <td width="20%">Name</td>
            <td width="5%">Duration</td>
            <td width="40%">Comment</td>
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
</body>
</html>
