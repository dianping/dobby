<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="w" uri="http://www.unidal.org/web/core"%>
<jsp:useBean id="ctx" type="com.dianping.dobby.ticket.page.home.Context" scope="request"/>
<jsp:useBean id="payload" type="com.dianping.dobby.ticket.page.home.Payload" scope="request"/>
<jsp:useBean id="model" type="com.dianping.dobby.ticket.page.home.Model" scope="request"/>

<a:layout>
	<br>
	
	<ul class="nav nav-tabs">
	  <li class="active"><a href="#all" data-toggle="tab">All</a></li>
	  <li><a href="#open" data-toggle="tab">Open</a></li>
	  <li><a href="#closed" data-toggle="tab">Closed</a></li>
	</ul>
	
	<div class="tab-content">
	  <div class="tab-pane active" id="all">
	  	<table class="table table-hover">
	  		<thead>
	  			<tr>
	  				<th>ID</th>
	  				<th>Subject</th>
	  				<th>State</th>
	  				<th>Assigned To</th>
	  				<th>Last Modified Date</th>
	  			</tr>
	  		</thead>
	  		<tbody>
	  			<c:forEach var="ticket" items="${model.tickets}">
		  			<tr>
		  				<td><a href="#${ticket.id}" role="button" data-toggle="modal">${ticket.id}</a></td>
		  				<td>${ticket.subject}</td>
		  				<td>${ticket.state}</td>
		  				<td>${ticket.assignedTo}</td>
		  				<td>${w:format(ticket.lastModifiedDate, 'yyyy-MM-dd HH:mm:ss')}</td>
		  			</tr>
	  			</c:forEach>
	  		</tbody>
	  	</table>
	  </div>
	  <div class="tab-pane" id="open">
	  	<table class="table table-hover">
	  		<thead>
	  			<tr>
	  				<th>ID</th>
	  				<th>Subject</th>
	  				<th>State</th>
	  				<th>Assigned To</th>
	  				<th>Last Modified Date</th>
	  			</tr>
	  		</thead>
	  		<tbody>
	  			<c:forEach var="ticket" items="${model.tickets}">
	  				<c:if test="${not(ticket.state eq 'IGNORED' or ticket.state eq 'RESOLVED')}">
			  			<tr>
			  				<td><a href="#${ticket.id}" role="button" data-toggle="modal">${ticket.id}</a></td>
			  				<td>${ticket.subject}</td>
			  				<td>${ticket.state}</td>
			  				<td>${ticket.assignedTo}</td>
			  				<td>${w:format(ticket.lastModifiedDate, 'yyyy-MM-dd hh:mm:ss')}</td>
			  			</tr>
	  				</c:if>
	  			</c:forEach>
	  		</tbody>
	  	</table>
	  </div>
	  <div class="tab-pane" id="closed">
	  	<table class="table table-hover">
	  		<thead>
	  			<tr>
	  				<th>ID</th>
	  				<th>Subject</th>
	  				<th>State</th>
	  				<th>Assigned To</th>
	  				<th>Last Modified Date</th>
	  			</tr>
	  		</thead>
	  		<tbody>
	  			<c:forEach var="ticket" items="${model.tickets}">
	  				<c:if test="${ticket.state eq 'IGNORED' or ticket.state eq 'RESOLVED'}">
			  			<tr>
			  				<td><a href="#${ticket.id}" role="button" data-toggle="modal">${ticket.id}</a></td>
			  				<td>${ticket.subject}</td>
			  				<td>${ticket.state}</td>
			  				<td>${ticket.assignedTo}</td>
			  				<td>${w:format(ticket.lastModifiedDate, 'yyyy-MM-dd hh:mm:ss')}</td>
			  			</tr>
	  				</c:if>
	  			</c:forEach>
	  		</tbody>
	  	</table>
	  </div>
	  
	  <c:forEach var="ticket" items="${model.tickets}">
		<div id="${ticket.id}" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		  <div class="modal-header">
		    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		    <h3 id="myModalLabel">[${ticket.id}] ${ticket.subject}</h3>
		  </div>
		  <div class="modal-body">
  			<c:forEach var="action" items="${ticket.actions}">
  				<h5>[${w:format(action.at, 'yyyy-MM-dd hh:mm:ss')}] ${action.by}</h5>
  				<c:if test="${not empty action.comment}">
  					<div class="blockquote">${action.comment}</div>
  				</c:if>
  				<hr>
  			</c:forEach>
		  </div>
		  <div class="modal-footer">
		    <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
		  </div>
		</div>
	  </c:forEach>
	</div>
</a:layout>