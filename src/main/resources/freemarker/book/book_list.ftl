<table rules="all" border="1" >
	<tr><th>命令<th><th>格式</th><th>示例</th></tr>
	<tr><td>帮助<td><td>@@help</td><td>@@help</td></tr>
	<tr><td>借书<td><td>@@borrow &lt;id&gt;</td><td>@@borrow 101</td></tr>
	<tr><td>还书<td><td>@@return &lt;id&gt;</td><td>@@return 101</td></tr>
</table>

<hr>

<table rules="all" border="1" >
	<tr>
		<td>编号</td>
		<td>书名</td>
		<td>ISBN</td>
		<td>作者</td>
		<td>出版社</td>
		<td>剩余数量</td>
	</tr>
	<#list books as item>
		<tr>
			<td>${item.id}</td>
			<td>${item.title}</td>
			<td>${item.isbn}</td>
			<td>${item.author}</td>
			<td>${item.press}</td>
			<td>${item.remaining}</td>
		</tr>
	</#list>
</table>

