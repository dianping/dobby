package com.dianping.dobby.book.biz;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.mail.internet.InternetAddress;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.unidal.helper.Dates;
import org.unidal.helper.Threads;
import org.unidal.helper.Threads.Task;
import org.unidal.lookup.annotation.Inject;

import com.dianping.cat.Cat;
import com.dianping.dobby.DobbyConstants;
import com.dianping.dobby.book.model.entity.Book;
import com.dianping.dobby.book.model.entity.Borrow;
import com.dianping.dobby.email.EmailChannel;
import com.dianping.dobby.email.MessageHandler;
import com.dianping.dobby.email.MessagePayload;
import com.dianping.dobby.view.FreeMarkerView;

public class BookMessageHandler implements MessageHandler, Initializable, LogEnabled, DobbyConstants {
   public static final String ID = ID_BOOK;

   @Inject(ID_BOOK)
   private EmailChannel m_emailChannel;

   @Inject
   private BookManager m_bookManager;

   @Inject
   private FreeMarkerView m_view;

   private boolean m_debug = true; // true means all reminder emails will be limited

   private Logger m_logger;

   @Override
   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public BookManager getBookManager() {
      return m_bookManager;
   }

   @Override
   public String getId() {
      return ID;
   }

   @Override
   public void handle(MessagePayload payload) throws Exception {
      BookCommand cmd = BookCommand.getByName(payload.getCommand(), BookCommand.HELP);
      BookCommandContext ctx = new BookCommandContext(this, payload);

      log(String.format("Received command: %s from %s.", cmd, payload.getFrom()));

      cmd.execute(ctx);
   }

   @Override
   public void initialize() throws InitializationException {
      Threads.forGroup("Dobby").start(new ReminderTask());
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

   public void onShowAllAvailableBookList(MessagePayload payload, List<Book> all, List<Book> borrowed) {
      String htmlContent = m_view.render("book/book_list.ftl", "all", all, "borrowed", borrowed);

      sendReplyEmail(payload, htmlContent);
   }

   private void sendNewEmail(MessagePayload payload, String subject, String htmlContent) {
      try {
         InternetAddress[] to = InternetAddress.parse(payload.getFrom());

         m_emailChannel.send(to, null, null, subject, null, htmlContent);
      } catch (Exception e) {
         e.printStackTrace();
         Cat.logError(e);
      }
   }

   private void sendReplyEmail(MessagePayload payload, String htmlContent) {
      try {
         InternetAddress[] to = InternetAddress.parse(payload.getFrom());

         m_emailChannel.send(to, null, payload.getMessageId(), payload.getSubject(), null, htmlContent);
      } catch (Exception e) {
         e.printStackTrace();
         Cat.logError(e);
      }
   }

   public class ReminderTask implements Task {
      @Override
      public String getName() {
         return getClass().getSimpleName();
      }

      @Override
      public void run() {
         while (true) {
            try {
               if (m_debug) {
                  TimeUnit.SECONDS.sleep(10);
               } else {
                  TimeUnit.HOURS.sleep(1);
               }
            } catch (InterruptedException e) {
               break;
            }

            try {
               boolean dirty = false;

               for (Book book : m_bookManager.getModel().getBooks().values()) {
                  for (Borrow borrow : book.getBorrowHistory()) {
                     if (shouldRemind(borrow)) {
                        sendRemindEmail(book, borrow);
                        dirty = true;
                     }
                  }
               }

               if (dirty) {
                  m_bookManager.save();
               }
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      }

      private void sendRemindEmail(Book book, Borrow borrow) throws Exception {
         InternetAddress[] to = InternetAddress.parse(borrow.getBorrower());
         String htmlContent = m_view.render("book/book_to_return.ftl", "book", book, "borrow", borrow);
         String subject;

         if (borrow.getExpiredTime().after(new Date())) {
            subject = String.format("你借的书《%s》即将到期，请尽快阅读！", book.getTitle());
         } else {
            subject = String.format("你借的书《%s》已经到期了，请马上归还！", book.getTitle());
         }

         m_emailChannel.send(to, null, null, subject, null, htmlContent);
         borrow.setLastRemindTime(new Date());
      }

      private boolean shouldRemind(Borrow borrow) {
         Date now = new Date();
         boolean shouldRemind = false;

         if (borrow.getReturnTime() == null) { // not returned
            Date expiredTime = borrow.getExpiredTime();

            if (Dates.from(expiredTime).day(-7).asDate().before(now)) { // within 7 days to expired time
               Date lastRemindTime = borrow.getLastRemindTime();

               if (lastRemindTime == null) {
                  shouldRemind = true;
               } else if (Dates.from(lastRemindTime).day(7).asDate().before(now)) { // reminded every 7 days
                  shouldRemind = true;
               }
            }
         }

         if (m_debug && !borrow.getBorrower().equals("qimin.wu@dianping.com")) { // for internal test purpose
            shouldRemind = false;
         }

         return shouldRemind;
      }

      @Override
      public void shutdown() {
      }
   }
}
