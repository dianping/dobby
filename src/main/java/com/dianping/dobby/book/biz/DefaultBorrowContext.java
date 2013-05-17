package com.dianping.dobby.book.biz;

import com.dianping.dobby.book.model.entity.Book;

public class DefaultBorrowContext implements BorrowContext{
	
	private BorrowListener listener;
	private BorrowState state;
	private Book book;
	private Error error;

	@Override
   public BorrowListener getListener() {
	   return listener;
   }

	@Override
   public BorrowState getState() {
	   return state;
   }

   public void setState(BorrowState state) {
	   this.state = state;
   }

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public void setListener(BorrowListener listener) {
		this.listener = listener;
	}

	public Error getError() {
		return error;
	}

	public void setErrorMessage(Error error) {
		this.error = error;
	}
}
