package com.dianping.dobby.book.page.home;

import java.util.Map;

import org.unidal.web.mvc.ViewModel;

import com.dianping.dobby.book.BookPage;
import com.dianping.dobby.book.model.entity.Book;

public class Model extends ViewModel<BookPage, Action, Context> {

	private Map<Integer, Book> m_books;

	public Model(Context ctx) {
		super(ctx);
	}

	@Override
	public Action getDefaultAction() {
		return Action.VIEW;
	}

	public Map<Integer, Book> getBooks() {
		return m_books;
	}

	public void setBooks(Map<Integer, Book> books) {
		m_books = books;
	}
}
