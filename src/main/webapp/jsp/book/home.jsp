<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="w" uri="http://www.unidal.org/web/core"%>
<jsp:useBean id="ctx" type="com.dianping.dobby.book.page.home.Context" scope="request" />
<jsp:useBean id="payload" type="com.dianping.dobby.book.page.home.Payload" scope="request" />
<jsp:useBean id="model" type="com.dianping.dobby.book.page.home.Model" scope="request" />

<a:layout>
   <br>

   <ul class="nav nav-tabs">
      <li class="active"><a href="#books" data-toggle="tab">Books</a></li>
      <li><a href="#import_export" data-toggle="tab">Import & Export</a></li>
   </ul>

   <div class="tab-content">
      <div class="tab-pane active" id="books">
         <table class="table table-hover">
            <thead>
               <tr>
                  <th>类别</th>
                  <th>书名</th>
                  <th>剩余 / 总数</th>
                  <th>作者</th>
                  <th>简介</th>
                  <th>出版社</th>
                  <th>ISBN</th>
               </tr>
            </thead>
            <tbody>
               <c:forEach var="book" items="${model.books}">
                  <tr>
                     <td width="80">${book.category }</td>
                     <td>(<a href="javascript:show('${book.id}')">${book.id}) ${book.title}</a>
                     </td>
                     <td>${book.remaining} / ${book.total}</td>
                     <td>${book.author}</td>
                     <td>${book.description}</td>
                     <td>${book.press}</td>
                     <td>${book.isbn }</td>
                  </tr>
                  <tr>
                     <td style="padding: 0px"></td>
                     <td colspan="6" style="padding: 0px">
                        <div id="${book.id}" style="display: none">
                           <table class="table table-hover">
                              <thead>
                                 <tr>
                                    <th>状态</th>
                                    <th>借阅人</th>
                                    <th>借出时间</th>
                                    <th>归还时间</th>
                                    <th>超期时间</th>
                                 </tr>
                              </thead>
                              <tbody>
                                 <c:forEach var="borrow" items="${book.borrowHistory }">
                                    <tr>
                                       <td>${borrow.status}</td>
                                       <td>${borrow.borrower }</td>
                                       <td>[${w:format(borrow.borrowTime, 'yyyy-MM-dd HH:mm:ss')}]</td>
                                       <td>[${w:format(borrow.returnTime, 'yyyy-MM-dd HH:mm:ss')}]</td>
                                       <td>[${w:format(borrow.expiredTime, 'yyyy-MM-dd HH:mm:ss')}]</td>
                                    </tr>
                                 </c:forEach>
                              </tbody>
                           </table>
                        </div>
                     </td>
                  </tr>
               </c:forEach>
            </tbody>
         </table>
      </div>
      <div class="tab-pane" id="import_export">
         <form method="post">
            <fieldset>
               <legend>Backup</legend>
               <input type="hidden" name="op" value="export">
               <label>All books and its borrow information can be exported to an .csv file, so that you can open it in Excel, or save it for backup.</label>
               <button type="submit" class="btn btn-primary">Download</button>
            </fieldset>
         </form>

         <br>
         <br>

         <form method="post" enctype="multipart/form-data" class="form-horizontal">
            <fieldset>
               <legend>Restore</legend>
               <input type="hidden" name="op" value="import">
               <label>The system can be restored from an .csv files.</label>
               <div class="form-group">
                  <input type="file" name="from">
               </div>
               <button type="submit" class="btn btn-primary">Upload</button>
            </fieldset>
         </form>
      </div>
   </div>

   <script src="${model.webapp}/js/book.js" type="text/javascript"></script>
</a:layout>