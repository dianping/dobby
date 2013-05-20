<table rules="all" border="1" >
	<tr>
		<td>ID</td>
		<td>${book.id}</td>
	</tr>
	<tr>
		<td>书名</td>
		<td>${book.title}</td>
	</tr>
	<tr>
		<td>分类</td>
		<td>${book.category?default("")}</td>
	</tr>
	<tr>
		<td>作者</td>
		<td>${book.author?default("")}</td>
	</tr>
	<tr>
		<td>介绍</td>
		<td>${book.description?default("")}</td>
	</tr>
	<tr>
		<td>出版社</td>
		<td>${book.press?default("")}</td>
	</tr>
	<tr>
		<td>剩余数量</td>
		<td>${book.remaining}</td>
	</tr>
</table>

<hr>

<table rules="all" border="1">
	<tr>
		<td>借阅人</td>
		<td>借阅时间</td>
		<td>归还时间</td>
		<td>到期时间</td>
	</tr>
	<#list book.borrowHistory as borrow>
		<tr>
			<td>${borrow.borrower}</td>
			<td>${borrow.borrowTime?string("yyyy-MM-dd HH:mm:ss")}</td>
			<td>
				<#if borrow.returnTime?exists>
				${borrow.returnTime?string("yyyy-MM-dd HH:mm:ss")}
				</#if>
			</td>
			<td>${borrow.expiredTime?string("yyyy-MM-dd HH:mm:ss")}</td>
		</tr>
	</#list>
</table>