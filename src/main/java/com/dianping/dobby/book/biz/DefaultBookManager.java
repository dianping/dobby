package com.dianping.dobby.book.biz;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
   public String buildCsv(boolean availableOnly) {
      List<Book> books = findAllBooks(availableOnly);
      StringBuilder sb = new StringBuilder(4096);
      char comma = ',';
      char lf = '\n';

      sb.append(
            "id,title,category,total,remaining,author,description,press,isbn,borrower,status,borrow time,return time,expired time")
            .append(lf);

      for (Book book : books) {
         sb.append(quoteString(book.getId()));
         sb.append(comma).append(quoteString(book.getTitle()));
         sb.append(comma).append(quoteString(book.getCategory()));
         sb.append(comma).append(book.getTotal());
         sb.append(comma).append(book.getRemaining());
         sb.append(comma).append(quoteString(book.getAuthor()));
         sb.append(comma).append(quoteString(book.getDescription()));
         sb.append(comma).append(quoteString(book.getPress()));
         sb.append(comma).append(quoteString(book.getIsbn()));
         sb.append(lf);

         for (Borrow borrow : book.getBorrowHistory()) {
            for (int i = 0; i < 8; i++) {
               sb.append(comma);
            }

            sb.append(comma).append(quoteString(borrow.getBorrower()));
            sb.append(comma).append(quoteString(borrow.getStatus()));
            sb.append(comma).append(quoteDate(borrow.getBorrowTime()));
            sb.append(comma).append(quoteDate(borrow.getReturnTime()));
            sb.append(comma).append(quoteDate(borrow.getExpiredTime()));
            sb.append(lf);
         }
      }

      return sb.toString();
   }

   @Override
   public List<Book> findAllBooks(boolean availableOnly) {
      List<Book> books = new ArrayList<Book>();

      for (Book book : m_model.getBooks().values()) {
         if (!availableOnly || book.getRemaining() > 0) {
            books.add(book);

            sortBorrowHistory(book);
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

   @Override
   public Book findBookById(String id) {
      return m_model.findBook(id);
   }

   @Override
   public BookModel getModel() {
      return m_model;
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

   private String quoteDate(Date date) {
      if (date == null) {
         return "";
      } else {
         String dateFormat = "\"yyyy-MM-dd HH:mm:ss\"";

         return new SimpleDateFormat(dateFormat).format(date);
      }
   }

   private String quoteString(String str) {
      int len = str == null ? 0 : str.length();

      if (len == 0) {
         return "";
      }

      StringBuilder sb = new StringBuilder(len + 8);

      sb.append('"');

      for (int i = 0; i < len; i++) {
         char ch = str.charAt(i);

         if (ch == '"') {
            sb.append(ch);
         }

         sb.append(ch);
      }

      sb.append('"');

      return sb.toString();
   }

   @Override
   public synchronized void save() {
      try {
         Files.forIO().writeTo(m_modelFile, m_model.toString());
      } catch (IOException e) {
         e.printStackTrace();
         Cat.logError(e);
      }
   }

   private void sortBorrowHistory(Book book) {
      List<Borrow> history = book.getBorrowHistory();

      Collections.sort(history, new Comparator<Borrow>() {
         @Override
         public int compare(Borrow b1, Borrow b2) {
            return -b1.getBorrowTime().compareTo(b2.getBorrowTime());
         }
      });
   }

}
