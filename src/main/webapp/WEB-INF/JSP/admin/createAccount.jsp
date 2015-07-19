<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title></title>
</head>
<body>
<h1>Create new ACCOUNT</h1>


<form:form method="POST" action="${context}/admin/createNewAccountAction" commandName="registerNewAccountForm">
    <div class="admin_table">
        <table>
            <tr>
                <td><br/></td>
                <td><br/></td>
            </tr>
            <tr>
                <td colspan="2" class="errors">
                    <form:errors path="*"/>
                </td>
            </tr>
            <tr>
                <td><form:label path="login">Login</form:label></td>
                <td><form:input path="login"/></td>
            </tr>
            <tr>
                <td><form:label path="password">Password</form:label></td>
                <td><form:password path="password"/></td>
            </tr>
            <tr>
                <td><form:label path="confirmPassword">Confirm Password</form:label></td>
                <td><form:password path="confirmPassword"/></td>
            </tr>
            <tr>
                <td><form:label path="email">E-mail</form:label></td>
                <td><form:input path="email"/></td>
            </tr>
            <tr>
                <td><form:label path="firstName">First Name</form:label></td>
                <td><form:input path="firstName"/></td>
            </tr>
            <tr>
                <td><form:label path="middleName">Middle Name</form:label></td>
                <td><form:input path="middleName"/></td>
            </tr>
            <tr>
                <td><form:label path="lastName">Last Name</form:label></td>
                <td><form:input path="lastName"/></td>
            </tr>

            <tr>
                <td colspan="2" style="text-align:match-parent;">
                    <input type="submit" class="simple_button" value="Create new account with role Student"/>
                </td>
            </tr>
        </table>
    </div>
</form:form>
<br/>
<button class="right_button" onclick="window.location.href ='${context}/admin/mainPage'">
    Close without save
</button>
</body>
</html>
