package com.dianping.dobby.book.biz;

import java.util.Collection;

import javax.mail.internet.InternetAddress;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.unidal.lookup.annotation.Inject;

import com.dianping.cat.Cat;
import com.dianping.dobby.DobbyConstants;
import com.dianping.dobby.book.model.entity.Book;
import com.dianping.dobby.email.EmailChannel;
import com.dianping.dobby.email.MessageHandler;
import com.dianping.dobby.email.MessagePayload;
import com.dianping.dobby.view.FreeMarkerView;

public class BookMessageHandler implements MessageHandler, LogEnabled, DobbyConstants {
   public static final String ID = ID_BOOK;

   @Inject(ID_BOOK)
   private EmailChannel m_service;

   @Inject
   private BookManager m_manager;

   @Inject
   private FreeMarkerView m_view;

   private Logger m_logger;

   @Override
   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

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

      log(String.format("Handle BookCommand: %s, context: %s.", cmd, ctx));

      cmd.execute(ctx);
   }

   public void log(String message) {
      m_logger.info(message);
   }

   public void onBookAlreadyBorrowed(MessagePayload payload, Book book) {
      String htmlContent = m_view.render("book/book_already_borrowed.ftl", "book", book);

      sendReplyEmail(payload, htmlContent);
   }

   public void onBookBorrowSuccessful(MessagePayload payload, Book book) {
      String subject = String.format("[ %s ] %s", book.getId(), book.getTitle());
      String htmlContent = m_view.render("book/book_borrow_successful.ftl", "book", book);

      sendNewEmail(payload, subject, htmlContent);
   }

   public void onBookNotFound(MessagePayload payload, String bookId) {
      String htmlContent = m_view.render("book/book_not_found.ftl", "bookId", bookId);

      sendReplyEmail(payload, htmlContent);
   }

   public void onBookReturnSuccessful(MessagePayload payload, Book book) {
      String htmlContent = m_view.render("book/book_return_successful.ftl", "book", book);

      sendReplyEmail(payload, htmlContent);
   }

   public void onNoBookToBorrow(MessagePayload payload, Book book) {
      String htmlContent = m_view.render("book/no_book_to_borrow.ftl", "book", book);

      sendReplyEmail(payload, htmlContent);
   }

   public void onNoBookToReturn(MessagePayload payload, Book book) {
      String htmlContent = m_view.render("book/no_book_to_return.ftl", "book", book);

      sendReplyEmail(payload, htmlContent);
   }

   public void onReturnBookNotBorrowed(MessagePayload payload, Book book) {
      String htmlContent = m_view.render("book/return_book_not_borrowed.ftl", "book", book);

      sendReplyEmail(payload, htmlContent);
   }

   public void onShowAllAvailableBookList(MessagePayload payload, Collection<Book> books) {
      String htmlContent = m_view.render("book/book_list.ftl", "books", books);

      sendReplyEmail(payload, htmlContent);
   }

   private void sendNewEmail(MessagePayload payload, String subject, String htmlContent) {
      try {
         InternetAddress[] to = InternetAddress.parse(payload.getFrom());

         m_service.send(to, null, null, subject, null, htmlContent);
      } catch (Exception e) {
         e.printStackTrace();
         Cat.logError(e);
      }
   }

   private void sendReplyEmail(MessagePayload payload, String htmlContent) {
      try {
         InternetAddress[] to = InternetAddress.parse(payload.getFrom());

         m_service.send(to, null, payload.getMessageId(), payload.getSubject(), null, htmlContent);
      } catch (Exception e) {
         e.printStackTrace();
         Cat.logError(e);
      }
   }
}
