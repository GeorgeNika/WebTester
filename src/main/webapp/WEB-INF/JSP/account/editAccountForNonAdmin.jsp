<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title></title>
</head>
<body>
<table class="login_table">
    <tr>
        <td> State</td>
        <td> Data</td>
        <td> Role</td>
    </tr>
    <tr>
        <td class="active_block">
            <c:choose>
                <c:when test="${editAccount.active}">
                    <img src="${context}/resources/images/yes.png" class="logo_state">
                </c:when>
                <c:otherwise>
                    <img src="${context}/resources/images/no.png" class="logo_state">
                </c:otherwise>
            </c:choose>
        </td>
        <td class="text_block">
            <form:form method="POST" action="${context}/editAccountForNonAdminAction/${editAccount.idAccount}"
                       commandName="editAccountForNonAdminForm">
                <table>
                    <tr>
                        <h1> Edit account id - ${editAccount.idAccount} :: Login - ${editAccount.login}</h1>
                    </tr>
                    <tr>
                        <td colspan="2" class="errors"><form:errors path="*"/></td>
                    </tr>
                    <tr>
                        <td><form:label path="password">Password</form:label></td>
                        <td><form:password path="password"/></td>
                    </tr>
                    <tr>
                        <td><form:label path="confirmPassword">Confirm password</form:label></td>
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
                            <input type="submit" class="simple_button" value="Edit"/>
                        </td>
                    </tr>
                </table>
            </form:form>
        </td>
        <td class="role_block">
            <br/>
            <table>
                <tr>

                </tr>
                <c:forEach var="role" items="${editAccount.roleSet}">
                    <tr>
                        <td colspan="2">
                                ${role.name}
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <br/>
            <br/>
            <br/>
            <br/>
            <br/>
        </td>
    </tr>
</table>
<br/>
<button class="right_button" onclick="window.location.href ='${context}/welcomeAction'">
    Close
</button>
<br/>
<br/>
</body>
</html>
