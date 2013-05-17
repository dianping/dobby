<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="w" uri="http://www.unidal.org/web/core"%>
<jsp:useBean id="ctx" type="com.dianping.dobby.book.page.home.Context"
	scope="request" />
<jsp:useBean id="payload"
	type="com.dianping.dobby.book.page.home.Payload" scope="request" />
<jsp:useBean id="model" type="com.dianping.dobby.book.page.home.Model"
	scope="request" />

<a:layout>
	<br>
	<ul class="nav nav-tabs">
		<li class="active"><a href="#book" data-toggle="tab">Books</a></li>
		<li><a href="#borrow" data-toggle="tab">Borrows</a></li>
	</ul>

	<div class="tab-content">
		<div class="tab-pane active" id="book">
			<table class="table table-hover">
				<thead>
					<tr>
						<th>ID</th>
						<th>书名</th>
						<th>作者</th>
						<th>出版社</th>
						<th>简介</th>
						<th>分类</th>
						<th>ISBN</th>
						<th>总数</th>
						<th>剩余</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="book" items="${model.books}">
						<tr>
							<td><a href="?id=${book.key}">${book.key}</a></td>
							<td>${book.value.title}</td>
							<td>${book.value.author}</td>
							<td>${book.value.press}</td>
							<td>${book.value.description}</td>
							<td>${book.value.category }</td>
							<td>${book.value.isbn }</td>
							<td>${book.value.total}</td>
							<td>${book.value.remaining}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

		<div class="tab-pane" id="borrow">
			<table class="table table-hover">
				<thead>
					<tr>
						<th>ID</th>
						<th>状态</th>
						<th>借阅人</th>
						<th>借出时间</th>
						<th>归还时间</th>
						<th>超期时间</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="book" items="${model.books}">
						<c:forEach var="borrow" items="${book.value.borrowHistory }">
						<tr>
							<td>#</td>
							<td>${borrow.status}</td>
							<td>${borrow.borrower }</td>
							<td>[${w:format(borrow.borrowTime, 'yyyy-MM-dd hh:mm:ss')}]</td>
							<td>[${w:format(borrow.returnTime, 'yyyy-MM-dd hh:mm:ss')}]</td>
							<td>[${w:format(borrow.expiredTime, 'yyyy-MM-dd hh:mm:ss')}]</td>
						</tr>
						</c:forEach>

					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

</a:layout>