package com.dianping.dobby.book.biz;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.unidal.helper.Files;

import com.dianping.dobby.book.model.entity.Book;
import com.dianping.dobby.book.model.entity.BookModel;
import com.dianping.dobby.book.model.transform.DefaultSaxParser;

public class DefaultBookManager implements BookManager, Initializable{

	private File bookModelFile;
	private BookModel bookModel;

	@Override
	public Book findBookById(int id) {
		return bookModel.findBook(id);
	}

	@Override
	public void initialize() throws InitializationException {
		try {
			bookModelFile = new File("book.xml").getCanonicalFile();

			if (bookModelFile.canRead()) {
				String xml = Files.forIO().readFrom(bookModelFile, "utf-8");

				bookModel = DefaultSaxParser.parse(xml);
			} else {
				bookModel = new BookModel();
			}
		} catch (Exception e) {
			throw new InitializationException("Unable to load book.xml!", e);
		}	   
	}

	@Override
	public void persistent(Book book) throws IOException {
		Files.forIO().writeTo(bookModelFile, book.toString());
	}

	@Override
   public Collection<Book> findAllBooks() {
	   return bookModel.getBooks().values();
	   
   }

}
