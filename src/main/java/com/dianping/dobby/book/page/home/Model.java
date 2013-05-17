package com.dianping.dobby.book.page.home;

import org.unidal.web.mvc.ViewModel;

import com.dianping.dobby.book.BookPage;
import com.dianping.dobby.book.model.entity.BookModel;

public class Model extends ViewModel<BookPage, Action, Context> {
	
	private BookModel m_books;
	
	public Model(Context ctx) {
		super(ctx);
	}

	@Override
	public Action getDefaultAction() {
		return Action.VIEW;
	}

	public BookModel getBooks() {
   	return m_books;
   }

	public void setBooks(BookModel books) {
   	m_books = books;
   }
}
