<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title></title>
</head>
<body>
<table class="tutor_table">
    <tr>
        <td> State</td>
        <td> Data</td>
    </tr>
    <tr>
        <td class="active_block">
            <table>
                <tr></tr>
                <tr>
                    <td>
                        <c:choose>
                            <c:when test="${editQuestion.active}">
                                <img src="${context}/resources/images/yes.png" class="logo_state">
                            </c:when>
                            <c:otherwise>
                                <img src="${context}/resources/images/no.png" class="logo_state">
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <button class="table_button"
                                onclick="window.location.href ='${context}/tutor/enableQuestionAction/${editTest.idTest}/${editQuestion.idQuestion}'">
                            + Enable Question
                        </button>
                        <br/>
                        <br/>
                        <button class="table_button"
                                onclick="window.location.href ='${context}/tutor/disableQuestionAction/${editTest.idTest}/${editQuestion.idQuestion}'">
                            - Disable Question
                        </button>
                        <br/>
                        <br/>
                        <button class="table_button"
                                onclick="window.location.href ='${context}/tutor/deleteQuestionAction/${editTest.idTest}/${editQuestion.idQuestion}'">
                            x Delete Question
                        </button>
                    </td>
                </tr>
            </table>
        </td>
        <td class="text_block">
            <form:form method="POST"
                       action="${context}/tutor/editQuestionAction/${editTest.idTest}/${editQuestion.idQuestion}"
                       commandName="editQuestionForm">
                <table>
                    <tr>
                        <h4 style="float:left">
                            Test - ${editTest.name}
                            <br/>
                            Question - ${editQuestion.question}
                        </h4>
                        <h4 style="float:right" align="right">
                            id - ${editTest.idTest} :: Author - ${editTest.accountByIdTutor.login}
                            <br/>
                            id - ${editQuestion.idQuestion}
                        </h4>
                    </tr>
                    <tr>
                        <td><form:label path="question">Question</form:label></td>
                        <td><form:input size="50" path="question"/></td>
                    </tr>
                    <tr>
                        <td colspan="2" style="text-align:match-parent;">
                            <input type="submit" class="table_button" value="Edit"/>
                        </td>
                    </tr>
                </table>
            </form:form>
        </td>
    </tr>
</table>
<br/>
<br/>
<button class="tutor_right_button"
        onclick="window.location.href ='${context}/tutor/createNewAnswerAction/${editTest.idTest}/${editQuestion.idQuestion}'">
    + Create new Answer
</button>
<br/>
<br/>

<div class="tutor_table">
    <table>
        <form:form method="post" modelAttribute="answerForm" name="answer_table">
            <tr>
                <td width="7%">No.</td>
                <td width="0%"></td>
                <td width="10%">right?</td>
                <td width="0%"></td>
                <td width="55%">Answer</td>
                <td width="10%">created</td>
                <td width="10%">updated</td>
                <td width="8%">action</td>
            </tr>
            <c:forEach items="${editAnswerForm.answerList}" var="answer" varStatus="status">
                <tr>
                    <script>
                        function pressChecked(ind){
                            var nam1 = "check["+ind+"]";
                            var nam2 = "hiddenCheck["+ind+"]";
                            var c1 = document.getElementById(nam1);
                            var c2 = document.getElementById(nam2);
                            if (c1.checked){
                                c2.value = "${true}";
                            } else {
                                c2.value = "false";
                            }
                        }
                    </script>
                    <td>${answer.idAnswer}</td>
                    <td>
                        <input type="hidden" name="answerList[${status.index}].idAnswer" value="${answer.idAnswer}">
                    </td>
                    <td>
                        <div align="center" style="text-align: center">
                        <input type="checkbox"  id="check[${status.index}]"
                               <c:if test="${answer.checkRight}"> checked </c:if> value="${answer.checkRight}"
                            onclick="pressChecked(${status.index})"/>
                        </div>
                    </td>
                    <td><input type="hidden" name="answerList[${status.index}].checkRight"
                               value="${answer.checkRight}" id="hiddenCheck[${status.index}]" \></td>
                    <td><input name="answerList[${status.index}].answer" value="${answer.answer}" size="50"/></td>
                    <td>${answer.created}</td>
                    <td>${answer.updated}</td>
                    <td>
                        <script>
                            function deleteSubmit() {
                                document.answer_table.action = '${context}/tutor/deleteAnswerAction/${editTest.idTest}/${editQuestion.idQuestion}/${answer.idAnswer}';
                                document.answer_table.submit();
                            }
                        </script>
                        <button class="table_button"
                                onclick="deleteSubmit()">
                            delete
                        </button>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <script>
                    function editSubmit() {
                        document.answer_table.action = "${context}/tutor/editAnswerAction/${editTest.idTest}/${editQuestion.idQuestion}";
                        document.answer_table.submit();
                    }
                </script>
                <td colspan="8" style="text-align:match-parent;">
                    <input type="button" class="table_button" value="Edit" onclick="editSubmit()"/>
                </td>
            </tr>
        </form:form>
    </table>

</div>
<br/>
<button class="tutor_right_button" onclick="window.location.href ='${context}/tutor/editTestPage/${editTest.idTest}'">
    Close
</button>
<br/>
<br/>
</body>
</html>
