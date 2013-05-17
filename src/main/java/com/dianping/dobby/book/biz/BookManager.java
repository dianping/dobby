package com.dianping.dobby.book.biz;

import java.util.Collection;

import com.dianping.dobby.book.model.entity.Book;

public interface BookManager {
   public Book findBookById(int id);

   public void save(Book book);

   public Collection<Book> findAllBooks();
}
