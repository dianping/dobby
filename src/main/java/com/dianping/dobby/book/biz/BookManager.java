package com.dianping.dobby.book.biz;

import java.util.List;

import com.dianping.dobby.book.model.entity.Book;
import com.dianping.dobby.book.model.entity.BookModel;

public interface BookManager {
   public Book findBookById(String id);

   public List<Book> findAllBooks(boolean availableOnly);

   public List<Book> findAllBorrowedBooksBy(String borrower);

   public BookModel getModel();

   public void save();

   public String buildCsv(boolean availableOnly);
}
