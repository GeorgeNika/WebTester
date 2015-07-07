<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title></title>
</head>
<body>
<h1>Create new    TEST</h1>


<form:form method="POST" action="${context}/tutor/createNewTestAction" commandName="createNewTestForm">
    <div class="tutor_table">
        <table>
            <tr>
                <td><br/></td>
                <td><br/></td>
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
                    <input type="submit" class="table_button" value="Create new   TEST  "/>
                </td>
            </tr>
        </table>
    </div>
</form:form>
<br/>
<button class="tutor_right_button" onclick="window.location.href ='${context}/tutor/mainPage'">
    Close without save
</button>
</body>
</html>
