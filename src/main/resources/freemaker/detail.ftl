<table rules="all" border="1" >
	<tr>
		<td>书名</td>
		<td>${book.title}</td>
	</tr>
	<tr>
		<td>分类</td>
		<td>${book.category}</td>
	</tr>
	<tr>
		<td>作者</td>
		<td>${book.author}</td>
	</tr>
	<tr>
		<td>介绍</td>
		<td>${book.description}</td>
	</tr>
	<tr>
		<td>出版社</td>
		<td>${book.press}</td>
	</tr>
	<tr>
		<td>剩余数量</td>
		<td>${book.remaining}</td>
	</tr>
</table>
</br>
<table rules="all" border="1" >
	<tr>
		<td>借阅人</td>
		<td>借阅时间</td>
	</tr>
	<#list book.borrowHistory as item>
		<tr>
			<td>${item.borrower}</td>
			<td>${item.borrowTime?string("yyyy-MM-dd HH:mm:ss")}</td>
		</tr>
	</#list>
</table>

