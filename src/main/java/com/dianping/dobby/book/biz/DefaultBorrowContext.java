package com.dianping.dobby.book.biz;

import org.unidal.lookup.annotation.Inject;

import com.dianping.dobby.book.model.entity.Book;

public class DefaultBorrowContext implements BorrowContext {
   @Inject
   private BorrowListener m_listener;

   private BorrowState m_state;

   private Book m_book;

   private Error m_error;

   @Override
   public BorrowListener getListener() {
      return m_listener;
   }

   @Override
   public BorrowState getState() {
      return m_state;
   }

   public void setState(BorrowState state) {
      m_state = state;
   }

   public Book getBook() {
      return m_book;
   }

   public void setBook(Book book) {
      m_book = book;
   }

   public void setListener(BorrowListener listener) {
      m_listener = listener;
   }

   public Error getError() {
      return m_error;
   }

   public void setErrorMessage(Error error) {
      m_error = error;
   }
}
