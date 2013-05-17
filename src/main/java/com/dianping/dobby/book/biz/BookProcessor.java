package com.dianping.dobby.book.biz;

import java.util.Collection;

import com.dianping.dobby.book.model.entity.Book;

public interface BookProcessor {
	
	public Collection<Book> listAllAvailbleBooks();
	
	public void process(int bookId, String by, String cmd, int expireDuration) throws Exception;
	
	public void help();

}
