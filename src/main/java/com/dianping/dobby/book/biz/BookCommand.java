package com.dianping.dobby.book.biz;

import java.util.Date;

import com.dianping.dobby.book.model.entity.Book;
import com.dianping.dobby.book.model.entity.Borrow;
import com.dianping.dobby.command.Command;
import com.dianping.dobby.command.CommandException;

public enum BookCommand implements Command<BookCommandContext> {
   HELP("help", "@@help", "This help page") {
      @Override
      public void execute(BookCommandContext ctx) throws CommandException {
         // TODO
      }
   },

   BORROW("borrow", "@@borrow 123", "Borrow a book") {
      @Override
      public void execute(BookCommandContext ctx) throws CommandException {
         BookManager manager = ctx.getManager();
         int bookId = ctx.getArgInt(0, -1);
         String by = ctx.getFrom();
         Book book = manager.findBookById(bookId);

         if (book == null) {
            ctx.notify(BookMessageId.BOOK_NOT_FOUND, bookId);
            return;
         }

         Borrow borrow = findActiveBorrow(book, by);

         if (borrow != null) {
            ctx.notify(BookMessageId.BORROW_SAME_BOOK_ALREADY_BORROWED, book);
            return;
         }

         if (book.getRemaining() <= 0) {
            ctx.notify(BookMessageId.NO_BOOK_TO_BORROW, book);
         }

         book.incRemaining(-1);
         book.addBorrow(new Borrow().setBorrower(by).setStatus("borrowing") //
               .setBorrowTime(new Date()).setExpiredTime(new Date(System.currentTimeMillis() + 30 * DAY)));

         manager.save(book);

         ctx.notify(BookMessageId.BORROW_SUCCESSFUL, book);
      }

      private Borrow findActiveBorrow(Book book, String by) {
         for (Borrow b : book.getBorrowHistory()) {
            if (b.getBorrower().equals(by) && b.getStatus().equals("borrowing")) {
               return b;
            }
         }

         return null;
      }
   },

   RETURN("return", "@@return 123", "Return a book") {
      @Override
      public void execute(BookCommandContext ctx) throws CommandException {
         // TODO
      }
   };

   private static final long DAY = 24 * 60 * 60 * 1000L;

   private String m_name;

   private String m_format;

   private String m_description;

   private BookCommand(String name, String format, String description) {
      m_name = name;
      m_format = format;
      m_description = description;
   }

   public static BookCommand getByName(String name, BookCommand defaultValue) {
      for (BookCommand command : values()) {
         if (command.getName().equalsIgnoreCase(name)) {
            return command;
         }
      }

      return defaultValue;
   }

   @Override
   public String getDescription() {
      return m_description;
   }

   @Override
   public String getFormat() {
      return m_format;
   }

   @Override
   public String getName() {
      return m_name;
   }
}
