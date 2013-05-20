package com.dianping.dobby.book.biz;

import java.util.Collection;

import com.dianping.dobby.book.model.entity.Book;
import com.dianping.dobby.command.CommandContext;
import com.dianping.dobby.email.MessagePayload;

public class BookCommandContext implements CommandContext {
   private MessagePayload m_payload;

   private BookMessageHandler m_handler;

   public BookCommandContext(BookMessageHandler handler, MessagePayload payload) {
      m_handler = handler;
      m_payload = payload;
   }

   public int getArgInt(int index, int defaultValue) {
      try {
         return Integer.parseInt(getArgString(index, null));
      } catch (Exception e) {
         // ignore it
      }

      return defaultValue;
   }

   public String getArgString(int index, String defaultValue) {
      String[] params = m_payload.getCommandParams();
      String value = index < params.length ? params[index] : null;

      if (value != null) {
         return value;
      } else {
         return defaultValue;
      }
   }

   public String getFrom() {
      return m_payload.getFrom();
   }

   public BookManager getManager() {
      return m_handler.getManager();
   }

   public MessagePayload getPayload() {
      return m_payload;
   }

   @SuppressWarnings("unchecked")
   public void notify(BookMessageId id, Object... args) {
      m_handler.log(String.format("Notify BookMessageId: %s.", id));

      switch (id) {
      case BOOK_NOT_FOUND:
         m_handler.onBookNotFound(m_payload, (Integer) args[0]);
         break;
      case BORROW_SAME_BOOK_ALREADY_BORROWED:
         m_handler.onBookAlreadyBorrowed(m_payload, (Book) args[0]);
         break;
      case NO_BOOK_TO_BORROW:
         m_handler.onNoBookToBorrow(m_payload, (Book) args[0]);
         break;
      case BORROW_SUCCESSFUL:
         m_handler.onBookBorrowSuccessful(m_payload, (Book) args[0]);
         break;
      case NO_BOOK_TO_RETURN:
         m_handler.onNoBookToReturn(m_payload, (Book) args[0]);
         break;
      case RETURN_BOOK_NOT_BORROWED:
         m_handler.onReturnBookNotBorrowed(m_payload, (Book) args[0]);
         break;
      case RETURN_SUCCESSFUL:
         m_handler.onBookReturnSuccessful(m_payload, (Book) args[0]);
         break;
      case SHOW_ALL_AVAILABLE_BOOK_LIST:
         m_handler.onShowAllAvailableBookList(m_payload, (Collection<Book>) args[0]);
         break;
      }
   }
}
