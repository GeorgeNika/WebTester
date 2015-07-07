<%@ page import="ua.george_nika.webtester.util.WebTesterConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: George
  Date: 06.07.2015
  Time: 11:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>

<br/>

<h1 style="text-align: center"> ${question} </h1>
<br/>
<br/>
<br/>

<div class="student_table">
    <table align="center">
        <form:form modelAttribute="studentAnswerForm" method="POST" action="${context}/student/nextQuestionAction">

            <tr>
                <td width="20%">
                    <br/>
                </td>
                <td width="80%"></td>
            </tr>
            <c:forEach items="${studentAnswerForm.studentAnswerMap}" var="answer">
                <tr>
                    <div align="center" style="text-align: center">
                        <td><form:checkbox path="studentAnswerList" value="${answer.key}"/></td>
                    </div>
                    <td><c:out value="${answer.value}"/></td>
                </tr>
            </c:forEach>
            <tr>
                <td colspan="2" style="text-align:match-parent;">
                    <input type="submit" class="table_button" value="Answer"/>
                </td>
            </tr>

        </form:form>
    </table>
</div>
<br/>
<br/>
<script>
    function timeOut(){
        window.location.href ='${context}/student/nextQuestionAction'
    }
    <c:set var="duration" value="<%=WebTesterConstants.SESSION_DURATION_FOR_QUESTION%>"/>
    setTimeout(timeOut, ${sessionScope.get(duration)});
</script>
<button class="student_right_button"
        onclick="window.location.href ='${context}/student/nextQuestionAction'">
    Don't know. Next question.
</button>

</body>
</html>
