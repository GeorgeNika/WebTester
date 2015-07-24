<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
<button class="right_button" onclick="window.location.href ='${context}/admin/createNewAccountPage'">
    + Create new account
</button>
<br/>
<br/>

<div class="admin_table" >
    <table id="mainTable">
        <tr>
            <td width="3%"></td>
            <td width="5%" id="idSort">№</td>
            <td width="10%" id="nameSort">Login</td>
            <td width="10%">Password</td>
            <td width="9%">FirstName</td>
            <td width="9%">MiddleName</td>
            <td width="9%">LastName</td>
            <td width="12%">E-mail</td>
            <td width="2%"></td>
            <td width="9%">created</td>
            <td width="9%">updated</td>
            <td width="13%">roleSet</td>
        </tr>

    </table>
</div>
<div class="admin_table">
    <tag:ajaxPaging></tag:ajaxPaging>
</div>
<br/>
<script>
    function accountHref(data){
        var tempId = $(data.target).parent().attr('id');
        var searchId = tempId.substring(3,tempId.length);
        window.location.href='${context}/admin/editAccountPage/'+searchId;
    };
    function useObtainedData(data){
        $('#pageNumber').html(data.page);
        $('#idLike').val(data.idLike);
        $('#nameLike').val(data.nameLike);
        $("#mainTable").find("tr:gt(0)").remove();
        var entityList = data.entityList;
        var accountActive;
        var emailActive;
        var roleSet;
        var roleSetString;
        var dateCreate;
        var dateUpdate;
        var formattedDate;
        var days;
        var months;
        var hours;
        var minutes;
        var formattedTime;
        for (var ind in entityList){

            if (entityList[ind].active){
                accountActive = "<img src='${context}/resources/images/yes.png' class='admin_table_active_account'>";
            }else{
                accountActive = "<img src='${context}/resources/images/no.png' class='admin_table_active_account'>";
            }
            if (entityList[ind].emailVerified){
                emailActive = "<img src='${context}/resources/images/yes.png' id='idE" + entityList[ind].idAccount+ "'>";
            }else{
                emailActive = "<img src='${context}/resources/images/no.png' id='idE" + entityList[ind].idAccount+ "'>";
            }
            roleSet=entityList[ind].roleSet;
            roleSetString = "";
            for (var i in roleSet){
                roleSetString = roleSetString + " * " + roleSet[i].name;
            }

            dateCreate = new Date(entityList[ind].created);
            days = (dateCreate.getDate() < 10) ? "0" + dateCreate.getDate() : dateCreate.getDate();
            months = ((dateCreate.getMonth()+1) < 10) ? "0" + (dateCreate.getMonth()+1) : (dateCreate.getMonth()+1);
            formattedDate = days + "-" + months + "-" + dateCreate.getFullYear();
            hours = (dateCreate.getHours() < 10) ? "0" + dateCreate.getHours() : dateCreate.getHours();
            minutes = (dateCreate.getMinutes() < 10) ? "0" + dateCreate.getMinutes() : dateCreate.getMinutes();
            formattedTime = hours + ":" + minutes;
            dateCreate = formattedDate + " " + formattedTime;

            dateUpdate = new Date(entityList[ind].updated);
            days = (dateUpdate.getDate() < 10) ? "0" + dateUpdate.getDate() : dateUpdate.getDate();
            months = ((dateUpdate.getMonth()+1) < 10) ? "0" + (dateUpdate.getMonth()+1) : (dateUpdate.getMonth()+1);
            formattedDate = days + "-" + months + "-" + dateUpdate.getFullYear();
            hours = (dateUpdate.getHours() < 10) ? "0" + dateUpdate.getHours() : dateUpdate.getHours();
            minutes = (dateUpdate.getMinutes() < 10) ? "0" + dateUpdate.getMinutes() : dateUpdate.getMinutes();
            formattedTime = hours + ":" + minutes;
            dateUpdate = formattedDate + " " + formattedTime;


            $('#mainTable tr:last').after(""
                    +"<tr id='idA"+entityList[ind].idAccount+"'>"
                    +"<td>"+accountActive+"</td>"
                    +"<td>"+entityList[ind].idAccount+"</td>"
                    +"<td>"+entityList[ind].login+"</td>"
                    +"<td>"+entityList[ind].password+"</td>"
                    +"<td>"+entityList[ind].firstName+"</td>"
                    +"<td>"+entityList[ind].middleName+"</td>"
                    +"<td>"+entityList[ind].lastName+"</td>"
                    +"<td>"+entityList[ind].email+"</td>"
                    +"<td>"+emailActive+"</td>"
                    +"<td class='admin_table_date'>"+dateCreate+"</td>"
                    +"<td class='admin_table_date'>"+dateUpdate+"</td>"
                    +"<td>"+roleSetString+"</td>"
                    +"</tr>"
            );
            $(document).on("click","#idA"+entityList[ind].idAccount,accountHref);
            $("#idE"+entityList[ind].idAccount).addClass('admin_table_active_email');
        }
    }

    function ajaxClick(sendData){
        $.ajax({
            url : '${context}/admin/mainAjaxPage',
            type: 'POST',
            datatype: 'json',
            data: sendData,
            success: function (response){
                useObtainedData(response);
            },
            error : function(xhr, status, error) {
                alert("Sorry, but error!");
            }
        });
    }
    function toFirstPage(){
        ajaxClick({page: 'start'});
    }
    function toPrevPage(){
        ajaxClick({page: 'prev'});
    }
    function toNextPage(){
        ajaxClick({page: 'next'});
    }
    function toLastPage(){
        ajaxClick({page: 'end'});
    }
    function idLike(){
        ajaxClick({idLike: $("#idLike").val()});
    }
    function nameLike(){
        ajaxClick({nameLike: $("#nameLike").val()});
    }
    function clearLike(){
        ajaxClick({idLike: "", nameLike: ""});
    }
    function idSort(){
        ajaxClick({sort: "id"});
    }
    function nameSort(){
        ajaxClick({sort: "name"});
    }
    function clearSort(){
        ajaxClick({sort: "clear"});
    }
    $(document).ready(function () {
        toFirstPage();
        $("#toFirstPage").attr('onclick', '').click(toFirstPage);
        $("#toPrevPage").attr('onclick', '').click(toPrevPage);
        $("#toNextPage").attr('onclick', '').click(toNextPage);
        $("#toLastPage").attr('onclick', '').click(toLastPage);
        $("#idLike").attr('onchange', '').change(idLike);
        $("#nameLike").attr('onchange', '').change(nameLike);
        $("#clearLike").attr('onclick', '').click(clearLike);
        $("#idSort").attr('onclick', '').click(idSort);
        $("#nameSort").attr('onclick', '').click(nameSort);
        $("#clearSort").attr('onclick', '').click(clearSort);
    });
</script>
</body>
</html>
