<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld" %>
<jsp:useBean id="ctx" type="com.dianping.dobby.book.page.home.Context" scope="request"/>
<jsp:useBean id="payload" type="com.dianping.dobby.book.page.home.Payload" scope="request"/>
<jsp:useBean id="model" type="com.dianping.dobby.book.page.home.Model" scope="request"/>

<a:layout>
	<br>
	
	<ul class="nav nav-tabs">
	  <li><a class="active" href="/book" data-toggle="tab">Books</a></li>
	  <li><a href="/borrow" data-toggle="tab">Borrows</a></li>
	</ul>
	
	<div class="tab-content">
	  <div class="tab-pane active" id="all">
	  	<table class="table table-hover">
	  		<thead>
	  			<tr>
	  				<th>ID</th>
	  				<th>书名</th>
	  				<th>作者</th>
	  				<th>出版社</th>
	  				<th>总数</th>
	  				<th>剩余</th>
	  				<th>简介</th>
	  				<th>分类</th>
	  				<th>ISBN</th>
	  			</tr>
	  		</thead>
	  		<tbody>
	  			  <c:forEach var="book" items="${model.books}">
		  			<tr>
		  				<td><a href="?id=${book.key}" role="button" data-toggle="modal">${book.key}</a></td>
		  				<td>${book.value.title}</td>
		  				<td>${book.value.author}</td>
		  				<td>${book.value.press}</td>
		  				<td>${book.value.total}</td>
		  				<td>${book.value.remaining}</td>
		  				<td>${book.value.description}</td>
		  				<td>${book.value.category }</td>
		  				<td>${book.value.isbn }</td>
		  			</tr>
	  			 </c:forEach>
	  		</tbody>
	  	</table>
	  </div>

</a:layout>