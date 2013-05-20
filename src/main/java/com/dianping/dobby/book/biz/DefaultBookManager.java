package com.dianping.dobby.book.biz;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.unidal.helper.Files;

import com.dianping.cat.Cat;
import com.dianping.dobby.book.model.entity.Book;
import com.dianping.dobby.book.model.entity.BookModel;
import com.dianping.dobby.book.model.entity.Borrow;
import com.dianping.dobby.book.model.transform.DefaultSaxParser;

public class DefaultBookManager implements BookManager, Initializable {
   private File m_modelFile;

   private BookModel m_model;

   @Override
   public Book findBookById(String id) {
      return m_model.findBook(id);
   }

   @Override
   public void initialize() throws InitializationException {
      try {
         m_modelFile = new File("book.xml").getCanonicalFile();

         if (m_modelFile.canRead()) {
            String xml = Files.forIO().readFrom(m_modelFile, "utf-8");

            m_model = DefaultSaxParser.parse(xml);
         } else {
            m_model = new BookModel();
         }
      } catch (Exception e) {
         Cat.logError(e);

         throw new InitializationException(String.format("Unable to load books from %s!", m_modelFile), e);
      }
   }

   @Override
   public synchronized void save() {
      try {
         Files.forIO().writeTo(m_modelFile, m_model.toString());
      } catch (IOException e) {
         Cat.logError(e);
      }
   }

   @Override
   public BookModel getModel() {
      return m_model;
   }

   @Override
   public List<Book> findAllAvaliableBooks() {
      List<Book> books = new ArrayList<Book>();

      for (Book book : m_model.getBooks().values()) {
         if (book.getRemaining() > 0) {
            books.add(book);
         }
      }

      return books;
   }

   @Override
   public List<Book> findAllBorrowedBooksBy(String borrower) {
      List<Book> books = new ArrayList<Book>();

      for (Book book : m_model.getBooks().values()) {
         for (Borrow borrow : book.getBorrowHistory()) {
            if (borrow.getReturnTime() == null && borrow.getBorrower().equals(borrower)) {
               books.add(book);
            }
         }
      }

      return books;
   }
}
