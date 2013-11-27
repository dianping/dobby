<#include "book_description.ftl">

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