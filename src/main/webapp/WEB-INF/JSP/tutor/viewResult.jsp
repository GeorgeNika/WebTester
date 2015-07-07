<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title></title>
</head>
<body>

<div class="tutor_table">
    <table>
        <tr>
            <td>№</td>
            <td width="70%">Name</td>
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
<br/>
<br/>
<button class="tutor_right_button" onclick="window.location.href ='${context}/tutor/mainPage'">
    Close
</button>
<br/>
<br/>
</body>
</html>

