<table rules="all" border="1" >
	<tr><th>命令<th><th>格式</th><th>示例</th></tr>
	<tr><td>帮助<td><td>@@help</td><td>@@help</td></tr>
	<tr><td>借书<td><td>@@borrow &lt;id&gt;</td><td>@@borrow 101</td></tr>
	<tr><td>还书<td><td>@@return &lt;id&gt;</td><td>@@return 101</td></tr>
</table>

<hr>

<table rules="all" border="1" >
	<caption>你已借的图书</caption>
	<tr>
		<th>还书指令</th>
		<th>编号</th>
		<th>书名</th>
		<th>ISBN</th>
		<th>作者</th>
		<th>出版社</th>
		<th>剩余数量</th>
	</tr>
	<#list borrowed as item>
		<tr>
			<th>@@return ${item.id}</th>
			<td>${item.id}</td>
			<td>${item.title}</td>
			<td>${item.isbn}</td>
			<td>${item.author}</td>
			<td>${item.press}</td>
			<td>${item.remaining}</td>
		</tr>
	</#list>
</table>

<hr>

<table rules="all" border="1" >
	<caption>所有可借的图书</caption>
	<tr>
		<th>借书指令</th>
		<th>编号</th>
		<th>书名</th>
		<th>ISBN</th>
		<th>作者</th>
		<th>出版社</th>
		<th>剩余数量</th>
	</tr>
	<#list all as item>
		<tr>
			<th>@@borrow ${item.id}</th>
			<td>${item.id}</td>
			<td>${item.title}</td>
			<td>${item.isbn}</td>
			<td>${item.author}</td>
			<td>${item.press}</td>
			<td>${item.remaining}</td>
		</tr>
	</#list>
</table>

