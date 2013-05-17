<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld" %>
<jsp:useBean id="ctx" type="com.dianping.dobby.book.page.home.Context" scope="request"/>
<jsp:useBean id="payload" type="com.dianping.dobby.book.page.home.Payload" scope="request"/>
<jsp:useBean id="model" type="com.dianping.dobby.book.page.home.Model" scope="request"/>

<a:layout>
	<br>
	
	<ul class="nav nav-tabs">
	  <li class="active"><a href="#all" data-toggle="tab">All</a></li>
	  <li><a href="#book" data-toggle="tab">Books</a></li>
	  <li><a href="#borrow" data-toggle="tab">Borrows</a></li>
	</ul>

</a:layout>