<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title></title>
</head>
<body>
<table class="tutor_table">
    <tr>
        <td> State</td>
        <td> Data</td>
    </tr>
    <tr>
        <td class="active_block">
            <table>
                <tr></tr>
                <tr>
                    <td>
                        <c:choose>
                            <c:when test="${editTest.active}">
                                <img src="${context}/resources/images/yes.png" class="logo_state">
                            </c:when>
                            <c:otherwise>
                                <img src="${context}/resources/images/no.png" class="logo_state">
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <button class="table_button"
                                onclick="window.location.href ='${context}/tutor/enableTestAction/${editTest.idTest}'">
                            + Enable Test
                        </button>
                        <br/>
                        <br/>
                        <button class="table_button"
                                onclick="window.location.href ='${context}/tutor/disableTestAction/${editTest.idTest}'">
                            - Disable Test
                        </button>
                        <br/>
                        <br/>
                        <button class="table_button"
                                onclick="window.location.href ='${context}/tutor/deleteTestAction/${editTest.idTest}'">
                            x Delete Test
                        </button>
                    </td>
                </tr>
            </table>
        </td>
        <td class="text_block">
            <form:form method="POST" action="${context}/tutor/editTestAction/${editTest.idTest}"
                       commandName="editTestForm">
                <table>
                    <tr>
                        <h4 style="float:left"> Test - ${editTest.name}</h4>
                        <h4 style="float:right" align="right">
                            id - ${editTest.idTest} ::
                            Author - ${editTest.accountByIdTutor.login}
                        </h4>
                    </tr>
                    <tr>
                        <td><form:label path="name">Name</form:label></td>
                        <td><form:input path="name"/></td>
                    </tr>
                    <tr>
                        <td><form:label path="comment">Comment</form:label></td>
                        <td><form:input size="50" path="comment"/></td>
                    </tr>
                    <tr>
                        <td><form:label path="time">time in sec.</form:label></td>
                        <td><form:input path="time"/></td>
                    </tr>
                    <tr>
                        <td colspan="2" style="text-align:match-parent;">
                            <input type="submit" class="table_button" value="Edit"/>
                        </td>
                    </tr>
                </table>
            </form:form>
        </td>
    </tr>
</table>
<br/>
<button class="tutor_left_button"
        onclick="window.location.href ='${context}/tutor/viewResultPage/${editTest.idTest}'">
    Show results of test
</button>
<br/>
<button class="tutor_right_button"
        onclick="window.location.href ='${context}/tutor/createNewQuestionPage/${editTest.idTest}'">
    + Create new question
</button>
<br/>
<br/>

<div class="tutor_table">
    <table>
        <tr>
            <td></td>
            <td>№</td>
            <td width="75%">Question</td>
            <td width="5">Answer</td>
            <td width="10%">created</td>
            <td width="10%">updated</td>
        </tr>

        <c:forEach var="question" items="${questionList}">
            <tr onclick="window.location.href='${context}/tutor/editQuestionPage/${editTest.idTest}/${question.idQuestion}'">
                <td>
                    <c:choose>
                        <c:when test="${question.active}">
                            <img src="${context}/resources/images/yes.png" class="tutor_table_active_account">
                        </c:when>
                        <c:otherwise>
                            <img src="${context}/resources/images/no.png" class="tutor_table_active_account">
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>${question.idQuestion}</td>
                <td>${question.question}</td>
                <td>${question.answerList.size()}</td>
                <td>${question.created}</td>
                <td>${question.updated}</td>
            </tr>
        </c:forEach>
    </table>
</div>
<br/>
<button class="tutor_right_button" onclick="window.location.href ='${context}/tutor/mainPage'">
    Close
</button>
<br/>
<br/>
</body>
</html>
