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
            <form:form method="POST" action="${context}/backupPasswordAction" commandName="backupPasswordForm">
                <div class="login_table">
                    <table>
                        <tr>
                            <td></td>
                            <td>Please enter you email</td>
                        </tr>
                        <tr>
                            <td colspan="2" class="errors"><form:errors path="*"/></td>
                        </tr>

                        <tr>
                            <td><form:label path="email">E-mail</form:label></td>
                            <td><form:input path="email"/></td>
                        </tr>

                        <tr>
                            <td colspan="2" style="text-align:match-parent;">
                                <input type="submit" class="simple_button" value="Send Email"/>
                            </td>
                        </tr>
                    </table>
                </div>
            </form:form>
        </td>
        <td width="10%">
        </td>
    </tr>

</table>


