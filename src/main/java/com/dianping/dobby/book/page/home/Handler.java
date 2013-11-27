package com.dianping.dobby.book.page.home;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.PageHandler;
import org.unidal.web.mvc.annotation.InboundActionMeta;
import org.unidal.web.mvc.annotation.OutboundActionMeta;
import org.unidal.web.mvc.annotation.PayloadMeta;

import com.dianping.dobby.book.BookPage;
import com.dianping.dobby.book.biz.BookManager;
import com.dianping.dobby.book.model.entity.Book;

public class Handler implements PageHandler<Context> {
   @Inject
   private JspViewer m_jspViewer;

   @Inject
   private BookManager m_manager;

   @Override
   @PayloadMeta(Payload.class)
   @InboundActionMeta(name = "home")
   public void handleInbound(Context ctx) throws ServletException, IOException {
      // display only, no action here
   }

   @Override
   @OutboundActionMeta(name = "home")
   public void handleOutbound(Context ctx) throws ServletException, IOException {
      Payload payload = ctx.getPayload();
      Model model = new Model(ctx);
      Action action = payload.getAction();

      model.setPage(BookPage.HOME);

      switch (action) {
      case EXPORT:
         exportBooks(ctx);

         break;
      default:
         List<Book> books = m_manager.findAllBooks(false);

         model.setAction(Action.VIEW);
         model.setBooks(books);
         break;
      }

      if (!ctx.isProcessStopped()) {
         m_jspViewer.view(ctx, model);
      }
   }

   private void exportBooks(Context ctx) throws IOException {
      String content = m_manager.buildCsv(false);
      byte[] data = content.getBytes("utf-8");
      HttpServletResponse res = ctx.getHttpServletResponse();
      String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

      res.setHeader("Content-Disposition", "attachment;filename=books-" + date + ".csv");
      res.setContentType("text/csv");
      res.setContentLength(data.length);
      res.getOutputStream().write(data);
      res.flushBuffer();
      ctx.stopProcess();
   }
}
