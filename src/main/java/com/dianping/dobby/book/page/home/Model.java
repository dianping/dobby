package com.dianping.dobby.book.page.home;

import java.util.List;

import org.unidal.web.mvc.ViewModel;

import com.dianping.dobby.book.BookPage;
import com.dianping.dobby.book.model.entity.Book;

public class Model extends ViewModel<BookPage, Action, Context> {
   private List<Book> m_books;

   public Model(Context ctx) {
      super(ctx);
   }

   @Override
   public Action getDefaultAction() {
      return Action.VIEW;
   }

   public List<Book> getBooks() {
      return m_books;
   }

   public void setBooks(List<Book> books) {
      m_books = books;
   }
}
