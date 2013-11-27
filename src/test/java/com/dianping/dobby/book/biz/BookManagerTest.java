package com.dianping.dobby.book.biz;

import java.io.File;

import org.junit.Test;
import org.unidal.helper.Files;
import org.unidal.lookup.ComponentTestCase;

public class BookManagerTest extends ComponentTestCase {
   @Test
   public void testCsv() throws Exception {
      BookManager manager = lookup(BookManager.class);
      String csv = manager.buildCsv(false);

      Files.forIO().writeTo(new File("book.csv"), csv, "GBK");
      System.out.println(csv);
   }
}
