<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title></title>
</head>
<body>
<h1>Create new     QUESTION</h1>
<form:form method="POST" action="${context}/tutor/createNewQuestionAction/${editTest.idTest}"
           commandName="createNewQuestionForm">
    <div class="tutor_table">
        <table>
            <tr>
                <td><br/></td>
                <td><br/></td>
            </tr>
            <tr>
                <td><form:label path="question">Question</form:label></td>
                <td><form:input size="50" path="question"/></td>
            </tr>
            <tr>
                <td colspan="2" style="text-align:match-parent;">
                    <input type="submit" class="simple_button" value="Create new    QUESTION "/>
                </td>
            </tr>
        </table>
    </div>
</form:form>
<br/>
<button class="right_button" onclick="window.location.href ='${context}/tutor/editTestPage/${editTest.idTest}'">
    Close without save
</button>
</body>
</html>
