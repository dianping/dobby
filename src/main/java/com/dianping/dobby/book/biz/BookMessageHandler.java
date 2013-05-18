package com.dianping.dobby.book.biz;

import java.util.Collection;

import javax.mail.internet.InternetAddress;

import org.unidal.lookup.annotation.Inject;

import com.dianping.dobby.DobbyConstants;
import com.dianping.dobby.book.model.entity.Book;
import com.dianping.dobby.email.EmailChannel;
import com.dianping.dobby.email.MessageHandler;
import com.dianping.dobby.email.MessagePayload;
import com.dianping.dobby.view.FreeMarkerView;

public class BookMessageHandler implements MessageHandler, DobbyConstants {
	public static final String ID = ID_BOOK;

	@Inject(ID_BOOK)
	private EmailChannel m_service;

	@Inject
	private BookManager m_manager;

	@Inject
	private FreeMarkerView m_view;

	@Override
	public String getId() {
		return ID;
	}

	public BookManager getManager() {
		return m_manager;
	}

	@Override
	public void handle(MessagePayload payload) throws Exception {
		BookCommand cmd = BookCommand.getByName(payload.getCommand(), BookCommand.HELP);
		BookCommandContext ctx = new BookCommandContext(this, payload);

		cmd.execute(ctx);
	}

	public void onBookAlreadyBorrowed(MessagePayload payload, Book book) {
		String htmlContent = m_view.render("book/book_already_borrowed.ftl", "book", book);

		sendHtmlEmail(payload, htmlContent);
	}

	public void onBookBorrowSuccessful(MessagePayload payload, Book book) {
		String htmlContent = m_view.render("book/book_borrow_successful.ftl", "book", book);

		sendHtmlEmail(payload, htmlContent);
	}

	public void onBookNotFound(MessagePayload payload, int bookId) {
		String htmlContent = m_view.render("book/book_not_found.ftl", "bookId", bookId);

		sendHtmlEmail(payload, htmlContent);
	}

	public void onNoBookToBorrow(MessagePayload payload, Book book) {
		String htmlContent = m_view.render("book/no_book_to_borrow.ftl", "book", book);

		sendHtmlEmail(payload, htmlContent);
	}

	public void onBookReturnSuccessful(MessagePayload payload, Book book) {
		String htmlContent = m_view.render("book/book_return_successful.ftl", "book", book);

		sendHtmlEmail(payload, htmlContent);
	}

	public void onNoBookToReturn(MessagePayload payload, Book book) {
		String htmlContent = m_view.render("book/no_book_to_return.ftl", "book", book);

		sendHtmlEmail(payload, htmlContent);
	}

	public void onReturnBookNotBorrowed(MessagePayload payload, Book book) {
		String htmlContent = m_view.render("book/return_book_not_borrowed.ftl", "book", book);

		sendHtmlEmail(payload, htmlContent);
	}
	
	public void onShowAllAvailableBookList(MessagePayload payload, Collection<Book> availableBooks){
		//TODO
	}

	private void sendHtmlEmail(MessagePayload payload, String htmlContent) {
		try {
			m_service.send(InternetAddress.parse(payload.getFrom()), InternetAddress.parse(m_service.getAddress()),
			      payload.getSubject(), null, htmlContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
