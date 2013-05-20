package com.dianping.dobby.book;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.unidal.lookup.ContainerLoader;

import com.dianping.dobby.book.biz.BookManager;
import com.dianping.dobby.book.model.entity.Book;
import com.dianping.dobby.book.model.entity.Borrow;

public class DataImporterTest {

   @SuppressWarnings("deprecation")
   public static void main(String[] args) throws Exception {
      List<String> lines = FileUtils.readLines(new File("src/test/resources/com/dianping/dobby/book/books.txt"));

      BookManager manager = ContainerLoader.getDefaultContainer().lookup(BookManager.class);
      DateFormat format = new SimpleDateFormat("yyyy.MM.dd");

      for (String line : lines) {
         String[] words = StringUtils.split(line, ";");
         Book book = new Book();
         book.setId(words[0]);
         book.setTitle(words[1]);
         book.setIsbn("9784072344072");
         book.setCreatedDate(new Date(2013, 1, 15, 18, 14, 15));
         book.setDescription(words[4]);
         book.setAuthor(words[5]);
         book.setPress(words[6]);
         book.setCategory(words[7]);
         book.setTotal(Integer.parseInt(words[8]));
         book.setRemaining(Integer.parseInt(words[9]));

         manager.save(book);
      }

      List<String> historys = FileUtils.readLines(new File(
            "src/test/resources/com/dianping/dobby/book/history.txt"));
      for (String line : historys) {
         String[] words = StringUtils.split(line, ";");
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
            borrow.setStatus(words[4]);
         }
         if (!ex) {
            borrow.setStatus(words[5]);
         }

         Book book = manager.findBookById(id);
         book.addBorrow(borrow);
         manager.save(book);
      }

      for (Book bk : manager.findAllAvaliableBooks()) {
         System.out.println(bk);
      }
   }
}
