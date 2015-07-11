<tr>
    <table>
        <td width="5%" onclick="Page('start')"> <<</td>
        <td width="5%" onclick="Page('prev')"> <</td>
        <td width="5%"> ${pageNumber} </td>
        <td width="5%" onclick="Page('next')"> ></td>
        <td width="5%" onclick="Page('end')"> >></td>
        <td width="15%"></td>
        <td width="20%">id like <input onchange="IdLike(value)" value="${idLikeValue}"></td>
        <td width="20%">name like <input onchange="NameLike(value)" value="${nameLikeValue}"></td>
        <td width="10%"></td>
        <td width="5%" onclick="ClearLike()"> -like</td>
        <td width="5%" onclick="Sort('clear')"> -sort</td>
    </table>
</tr>