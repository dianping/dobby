package com.dianping.dobby.book.biz;

import java.util.ArrayList;
import java.util.List;

import org.unidal.lookup.configuration.AbstractResourceConfigurator;
import org.unidal.lookup.configuration.Component;

import com.dianping.dobby.DobbyConstants;
import com.dianping.dobby.book.biz.BookCommandTest.MockBookMessageHandler;
import com.dianping.dobby.email.EmailChannel;
import com.dianping.dobby.email.MessageHandler;
import com.dianping.dobby.view.FreeMarkerView;

public class BookCommandTestConfigurator extends AbstractResourceConfigurator implements DobbyConstants {
	@Override
	public List<Component> defineComponents() {
		List<Component> all = new ArrayList<Component>();

		all.add(C(MessageHandler.class, ID_BOOK, MockBookMessageHandler.class) //
		      .req(BookManager.class, FreeMarkerView.class).req(EmailChannel.class, ID_BOOK));

		return all;
	}

	public static void main(String[] args) {
		generatePlexusComponentsXmlFile(new BookCommandTestConfigurator());
	}

	@Override
	protected Class<?> getTestClass() {
		return BookCommandTest.class;
	}
}
