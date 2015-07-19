<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title></title>
</head>
<body>

<h1 style="float:left">
    Test - ${editTest.name}
    <br/>
    Author - ${editTest.accountByIdTutor.getFirstName()}
    <c:if test="${editTest.accountByIdTutor.getMiddleName()!=null}">
        ${editTest.accountByIdTutor.getMiddleName()}
    </c:if>
    ${editTest.accountByIdTutor.getLastName()}
    <br/>
    Total question count - ${editTest.getCountActiveQuestion()}
    <br/>
    Time for one question - ${editTest.durationInSecond} sec.
</h1>
<h1 style="float:right" align="right">
    id - ${editTest.idTest}
    <h1/>
    <h1/>
    <h1/>
    <br/>
    <br/>
</h1>
<br/>

<br/>
<br/>
<br/>
<button class="left_button"
        onclick="window.location.href ='${context}/student/beginTestAction/${editTest.idTest}'">
    Begin test
</button>

<button class="right_button" onclick="window.location.href ='${context}/student/mainPage'">
    Close
</button>
</body>
</html>
