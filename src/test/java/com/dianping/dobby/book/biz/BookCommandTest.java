package com.dianping.dobby.book.biz;

import java.util.Arrays;

import org.junit.Test;
import org.unidal.lookup.ComponentTestCase;

import com.dianping.dobby.DobbyConstants;
import com.dianping.dobby.book.model.entity.Book;
import com.dianping.dobby.email.MessageHandler;
import com.dianping.dobby.email.MessagePayload;

public class BookCommandTest extends ComponentTestCase{
	
	@Test
	public void test() throws Exception{
		MessageHandler handler = lookup(MessageHandler.class, DobbyConstants.ID_BOOK);
		
		MessagePayload payload = new MessagePayload();
		payload.setCommand("borrow");
		payload.setCommandParams((String[])Arrays.asList("101").toArray());
		payload.setFrom("yong.you@dianping.com");
		handler.handle(payload);
	}
	
	public static class MockBookMessageHandler extends BookMessageHandler{

		@Override
      public void onBookAlreadyBorrowed(MessagePayload payload, Book book) {
			System.out.println(1);
      }

		@Override
      public void onBookBorrowSuccessful(MessagePayload payload, Book book) {
			System.out.println(1);
      }

		@Override
      public void onBookNotFound(MessagePayload payload, int bookId) {
			System.out.println(1);
      }

		@Override
      public void onNoBookToBorrow(MessagePayload payload, Book book) {
			System.out.println(1);
      }

		
		
	}
}
