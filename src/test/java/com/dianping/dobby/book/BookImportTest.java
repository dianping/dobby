package com.dianping.dobby.book;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.unidal.helper.Splitters;
import org.unidal.lookup.ComponentTestCase;

import com.dianping.dobby.book.biz.BookManager;
import com.dianping.dobby.book.model.entity.Book;
import com.dianping.dobby.book.model.entity.Borrow;

public class BookImportTest extends ComponentTestCase {

   @Test
   public void testImport() throws Exception {
      BookManager manager = lookup(BookManager.class);
      DateFormat format = new SimpleDateFormat("yyyy.MM.dd");
      List<String> lines = FileUtils.readLines(new File("src/test/resources/com/dianping/dobby/book/books.txt"));

      for (String line : lines) {
         String[] words = Splitters.by(';').trim().split(line).toArray(new String[0]);
         String id = words[0];
         Book book = manager.findBookById(id);

         if (book == null) {
            book = new Book(id);

            book.setTotal(1);
            book.setRemaining(0);

            manager.getModel().addBook(book);
         }

         book.setTitle(words[1]);
         book.setCategory(words[2]);
         book.setCreatedDate(new Date());
      }

      manager.save();

      List<String> historys = FileUtils.readLines(new File("src/test/resources/com/dianping/dobby/book/history.txt"));

      for (String line : historys) {
         String[] words = Splitters.by(';').trim().split(line).toArray(new String[0]);
         Borrow borrow = new Borrow();
         String id = words[0];

         borrow.setBorrower(words[1]);
         borrow.setBorrowTime(format.parse(words[2]));
         borrow.setExpiredTime(format.parse(words[3]));
         
         boolean ex = false;
         try {
            borrow.setReturnTime(format.parse(words[4]));
         } catch (Exception e) {
            ex = true;
            borrow.setStatus(words[5]);
         }
         
         if (!ex) {
            borrow.setStatus(words[5]);
         }

         System.out.println(id);

         Book book = manager.findBookById(id);
         
         book.addBorrow(borrow);
      }

      manager.save();

      for (Book bk : manager.findAllAvaliableBooks()) {
         System.out.println(bk);
      }
   }
}
