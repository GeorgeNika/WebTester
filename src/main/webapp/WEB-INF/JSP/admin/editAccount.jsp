<%@ page import="ua.george_nika.webtester.util.WebTesterConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title></title>
</head>
<body>
<table class="admin_table">
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

            <br/>
            <br/>
            <button class="table_button"
                    onclick="window.location.href ='${context}/admin/enableAccountAction/${editAccount.idAccount}'">
                + Enable Account
            </button>
            <br/>
            <br/>
            <button class="table_button"
                    onclick="window.location.href ='${context}/admin/disableAccountAction/${editAccount.idAccount}'">
                - Disable Account
            </button>
            <br/>
            <br/>
            <button class="table_button"
                    onclick="window.location.href ='${context}/admin/deleteAccountAction/${editAccount.idAccount}'">
                x Delete Account
            </button>
            <br/>
            <br/>
        </td>
        <td class="text_block">
            <form:form method="POST" action="${context}/admin/editAccountAction/${editAccount.idAccount}"
                       commandName="editAccountForm">
                <table>
                    <tr>
                        <h1> Edit account id - ${editAccount.idAccount} :: Login - ${editAccount.login}</h1>
                    </tr>
                    <tr>
                        <td colspan="2" class="errors"><form:errors path="*"/></td>
                    </tr>
                    <tr>
                        <td><form:label path="login">Login</form:label></td>
                        <td><form:input path="login"/></td>
                    </tr>
                    <tr>
                        <td><form:label path="password">Password</form:label></td>
                        <td><form:input path="password"/></td>
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
                            <input type="submit" class="table_button" value="Edit"/>
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

                <tr>
                    <td>
                        <section>
                            <select class="cs-select cs-skin-underline">
                                <option value="" disabled selected>+Add role</option>
                                <c:set var="session_all_role_list"
                                       value="<%=WebTesterConstants.SESSION_ALL_ROLE_LIST%>"/>
                                <c:forEach var="role" items="${sessionScope.get(session_all_role_list)}">
                                    <option data-link="${context}/admin/addRoleAction/${editAccount.idAccount}/${role.idRole}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </section>
                    </td>
                    <td>
                        <section>
                            <select class="cs-select cs-skin-underline">
                                <option value="" disabled selected>- Delete role</option>
                                <c:forEach var="role" items="${editAccount.roleSet}">
                                    <option data-link="${context}/admin/deleteRoleAction/${editAccount.idAccount}/${role.idRole}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </section>
                    </td>
                </tr>
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
<button class="admin_right_button" onclick="window.location.href ='${context}/admin/mainPage'">
    Close
</button>
<br/>
<br/>
</body>
</html>
