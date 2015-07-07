<%@ page import="ua.george_nika.webtester.util.WebTesterConstants" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<br/>

<div class="login_table">
    <table>
        <tr>
            <td> </td>
            <td> Please login</td>
        </tr>

        <form:form method="POST" action="${context}/loginHandler" commandName="loginForm">
            <tr>
                <td><form:label path="j_username">Login</form:label></td>
                <td><form:input path="j_username"/></td>
            </tr>
            <tr>
                <td><form:label path="j_password">Password</form:label></td>
                <td><form:password path="j_password"/></td>
            </tr>
            <tr>
                <td><form:label path="idRole">Role</form:label></td>
                <td>
                    <form:select path="idRole">
                        <c:set var="session_all_role_list"
                               value="<%=WebTesterConstants.SESSION_ALL_ROLE_LIST%>"/>
                        <form:options items="${sessionScope.get(session_all_role_list)}" itemLabel="name" itemValue="idRole"/>
                    </form:select>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <input type="checkbox" value="true" name="_spring_security_remember_me" checked>remember me
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="submit" class="table_button" value="Login"/>
                </td>
            </tr>
        </form:form>
        <tr>
            <td colspan="2" style="text-align: match-parent;">
                <button class="table_button" onclick="window.location.href ='${context}/backupPasswordPage'">
                    Backup password
                </button>
            </td>
        </tr>
        <tr>
            <td colspan="2" style="text-align: match-parent;">
                <button class="table_button" onclick="window.location.href ='${context}/registerNewAccountPage'">
                    Register new Account
                </button>
            </td>
        </tr>
        <tr>
            <td colspan="2" style="text-align: match-parent;">
                <button onclick="window.location.href='${context}/fbLoginAction'" class="table_button">
                    <img src="${context}/resources/images/Facebook-Button.png" class="facebook_button"/>
                    Login with Facebook
                </button>
            </td>
        </tr>
    </table>
</div>



