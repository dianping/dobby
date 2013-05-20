package com.dianping.dobby.book;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.junit.Test;
import org.unidal.lookup.ContainerLoader;

import com.dianping.dobby.book.biz.BookManager;
import com.dianping.dobby.book.model.entity.Book;
import com.dianping.dobby.book.model.entity.Borrow;

public class DataImporterTest {

	@Test
	public void testImporter() throws IOException, ComponentLookupException, ParseException {
		List<String> lines = FileUtils.readLines(new File("src/test/resources/com/dianping/dobby/book/books.txt"));

		BookManager manager = ContainerLoader.getDefaultContainer().lookup(BookManager.class);
		DateFormat format = new SimpleDateFormat("yyyy.MM.dd");

		for (String line : lines) {
			String[] words = StringUtils.split(line, ";");

			if(manager.findBookById(words[0]) != null){
				manager.rmBook(words[0]);
			}
			
			if (manager.findBookById(words[0]) == null) {
				Book book = new Book();
				book.setId(words[0]);
				book.setTitle(words[1]);
				book.setCategory(words[2]);

				manager.save(book);
			}
		}

		List<String> historys = FileUtils.readLines(new File("src/test/resources/com/dianping/dobby/book/history.txt"));
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

		for (Book bk : manager.findAllBooks()) {
			System.out.println(bk);
		}
	}
}
