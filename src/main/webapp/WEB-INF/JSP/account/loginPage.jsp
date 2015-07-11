<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<table class="login_table_table">
    <tr>
        <td width="30%" >
            <jsp:include page="loginBlock.jsp"/>
        </td>
        <td width="5%">
        </td>
        <td width="60%">
            <table>
                <tr>
                    <h3>
                        Для входа и ознакомления можно использвоать аккаунт guest с паролем guest.
                    </h3>
                    <br/>
                </tr>
                <tr>
                    <h4>При написании программы использовались следующие библиотеки и технологии: </h4>
                    <h4> * JPA (Hibernate)</h4>
                    <h4> * Spring MVC </h4>
                    <h4> * Spring Security</h4>
                    <h4> * Sitemesh</h4>
                    <h4> * Quartz</h4>
                    <br/>
                    <h4>База данных - PostgreSQL</h4>
                    <h4>Исходники программы - https://github.com/GeorgeNika/WebTester</h4>

                </tr>
                <tr>
                    <h4> <a href='${context}/taskPage'>Требования к системе</a> </h4>
                </tr>
            </table>
        </td>
        <td width="5%">
        </td>
    </tr>

</table>




