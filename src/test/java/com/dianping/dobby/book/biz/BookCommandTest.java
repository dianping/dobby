package com.dianping.dobby.book.biz;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;
import org.unidal.lookup.ComponentTestCase;

import com.dianping.dobby.DobbyConstants;
import com.dianping.dobby.book.model.entity.Book;
import com.dianping.dobby.email.MessageHandler;
import com.dianping.dobby.email.MessagePayload;

public class BookCommandTest extends ComponentTestCase {

	private static StringBuilder s_sb = new StringBuilder();

	@Test
	public void test() throws Exception {
		MessageHandler handler = lookup(MessageHandler.class, DobbyConstants.ID_BOOK);
		String from = "yong.you@dianping.com";
		String bookId = "101";
		String unexistId = "101***";
		String returnBook = "return";
		String borrowBook = "borrow";
		
		s_sb.setLength(0);
		handler.handle(createPayload(returnBook, bookId, from));

		s_sb.setLength(0);
		handler.handle(createPayload(borrowBook, bookId, from));
		Assert.assertEquals("onBookBorrowSuccessful", s_sb.toString());

		s_sb.setLength(0);
		handler.handle(createPayload(borrowBook, bookId, from));
		Assert.assertEquals("onBookAlreadyBorrowed", s_sb.toString());

		s_sb.setLength(0);
		handler.handle(createPayload(borrowBook, unexistId, from));
		Assert.assertEquals("onNoBookToBorrow", s_sb.toString());
		
	}

	private MessagePayload createPayload(String command, String id, String from) {
		MessagePayload payload = new MessagePayload();
		payload.setCommand(command);
		payload.setCommandParams((String[]) Arrays.asList(id).toArray());
		payload.setFrom(from);

		return payload;
	}

	public static class MockBookMessageHandler extends BookMessageHandler {

		@Override
		public void onBookAlreadyBorrowed(MessagePayload payload, Book book) {
			s_sb.append("onBookAlreadyBorrowed");
		}

		@Override
		public void onBookBorrowSuccessful(MessagePayload payload, Book book) {
			s_sb.append("onBookBorrowSuccessful");
		}

		@Override
		public void onBookNotFound(MessagePayload payload, int bookId) {
			s_sb.append("onBookNotFound");
		}

		@Override
		public void onNoBookToBorrow(MessagePayload payload, Book book) {
			s_sb.append("onNoBookToBorrow");
		}
	}

}
