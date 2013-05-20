package com.dianping.dobby.book.biz;

import java.util.Collection;

import com.dianping.dobby.book.model.entity.Book;
import com.dianping.dobby.book.model.entity.BookModel;

public interface BookManager {
   public Book findBookById(String id);

   public BookModel getModel();

   public void save(Book book);
   
   public void rmBook(String id);

   public Collection<Book> findAllBooks();
}
