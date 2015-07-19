<%--
  Created by IntelliJ IDEA.
  User: George
  Date: 06.07.2015
  Time: 11:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>

<h1 style="text-align: center">  Your result  </h1>
<h1>
  All questions - ${result.countQuestion}
  <br/>
  All right answers - ${result.allRightAnswer}
  <br/>
  <br/>
  Your right answers - ${result.rightAnswer}
  <br/>
  Your wrong answers - ${result.wrongAnswer}
  <br/>
</h1>


<br/>
<br/>


<button class="right_button" onclick="window.location.href ='${context}/student/mainPage'">
  Close
</button>
</body>
</html>
