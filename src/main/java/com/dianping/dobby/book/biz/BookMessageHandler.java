package com.dianping.dobby.book.biz;

import javax.mail.internet.InternetAddress;

import org.unidal.lookup.annotation.Inject;

import com.dianping.dobby.DobbyConstants;
import com.dianping.dobby.book.model.entity.Book;
import com.dianping.dobby.email.EmailService;
import com.dianping.dobby.email.MessageHandler;
import com.dianping.dobby.email.MessagePayload;
import com.dianping.dobby.view.FreeMarkerView;

public class BookMessageHandler implements MessageHandler, DobbyConstants {
   public static final String ID = ID_BOOK;

   @Inject(ID_BOOK)
   private EmailService m_service;

   @Inject
   private BookManager m_manager;

   @Inject
   private FreeMarkerView m_view;

   @Override
   public String getId() {
      return ID;
   }

   public BookManager getManager() {
      return m_manager;
   }

   @Override
   public void handle(MessagePayload payload) throws Exception {
      BookCommand cmd = BookCommand.getByName(payload.getCommand(), BookCommand.HELP);
      BookCommandContext ctx = new BookCommandContext(this, payload);

      cmd.execute(ctx);
   }

   public void onBookAlreadyBorrowed(MessagePayload payload, Book book) {

   }

   public void onBookBorrowSuccessful(MessagePayload payload, Book book) {

   }

   public void onBookNotFound(MessagePayload payload, int bookId) {
      String htmlContent = m_view.render("book/no_book.ftl", "bookId", bookId);

      try {
         m_service.send(InternetAddress.parse(payload.getFrom()), InternetAddress.parse(m_service.getAddress()), "",
               null, htmlContent);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void onNoBookToBorrow(MessagePayload payload, Book book) {

   }
}
