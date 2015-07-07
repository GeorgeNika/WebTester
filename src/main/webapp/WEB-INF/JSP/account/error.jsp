<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<h3>An exception has occurred</h3>
<table>
    <tr>
        <td>Exception Class:</td>
        <td><%= exception.getClass() %>
        </td>
    </tr>
    <tr>
        <td>Message:</td>
        <td><%= exception.getMessage() %>
        </td>
    </tr>
</table>
<br>
To go to main page, click main page button
<form name="f2" action="${context}/welcomeAction">
    <input type="submit" name="button1" value="main page">
</form>
</body>
</html>


