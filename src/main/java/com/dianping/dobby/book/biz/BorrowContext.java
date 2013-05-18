package com.dianping.dobby.book.biz;

import com.dianping.dobby.book.model.entity.Book;


public interface BorrowContext {
	
	public BorrowListener getListener();

	public BorrowState getState();
	
	public Book getBook();
	
	public BookMessageId getError();
}
