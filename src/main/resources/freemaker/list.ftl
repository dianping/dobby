<table rules="all" border="1" >
	<tr><th>HELP<th><td>@@help</td></tr>
	<tr><th>借书<th><td>@@borrow isbn</td></tr>
	<tr><th>还书<th><td>@@return isbn</td></tr>
</table>
</br>
<table rules="all" border="1" >
	<tr>
		<td>编号</td>
		<td>书名</td>
		<td>作者</td>
		<td>出版社</td>
		<td>剩余数量</td>
	</tr>
	<#list books as item>
		<tr>
			<td>${item.isbn}</td>
			<td>${item.title}</td>
			<td>${item.author}</td>
			<td>${item.press}</td>
			<td>${item.remaining}</td>
		</tr>
	</#list>
</table>

