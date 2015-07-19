<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<table width="100%">
    <tr>
        <td width="30%">
            <jsp:include page="loginBlock.jsp"/>
        </td>
        <td width="10%">
        </td>
        <td width="50%">
            <br/>

            <div class="login_table">

                <table>
                    <form:form method="POST" action="${context}/registerNewAccountAction"
                               commandName="registerNewAccountForm">
                        <tr>
                            <td></td>
                            <td>Please enter registration data</td>
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
                                <input type="submit" class="simple_button" value="Register"/>
                            </td>
                        </tr>
                    </form:form>
                </table>

            </div>

        </td>
        <td width="10%">
        </td>
    </tr>

</table>


