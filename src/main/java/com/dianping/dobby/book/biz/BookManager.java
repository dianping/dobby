package com.dianping.dobby.book.biz;

import java.io.IOException;
import java.util.Collection;

import com.dianping.dobby.book.model.entity.Book;

public interface BookManager {
	
	public Book findBookById(int id);
	
	public void persistent(Book book) throws IOException;
	
	public Collection<Book> findAllBooks();

}
