package com.dianping.dobby.book.page.home;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;

import org.unidal.helper.Files;
import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.PageHandler;
import org.unidal.web.mvc.annotation.InboundActionMeta;
import org.unidal.web.mvc.annotation.OutboundActionMeta;
import org.unidal.web.mvc.annotation.PayloadMeta;
import org.xml.sax.SAXException;

import com.dianping.dobby.book.BookPage;
import com.dianping.dobby.book.model.entity.Book;
import com.dianping.dobby.book.model.entity.BookModel;
import com.dianping.dobby.book.model.transform.DefaultSaxParser;

public class Handler implements PageHandler<Context> {
	@Inject
	private JspViewer m_jspViewer;

	@Override
	@PayloadMeta(Payload.class)
	@InboundActionMeta(name = "home")
	public void handleInbound(Context ctx) throws ServletException, IOException {
		// display only, no action here
	}

	@Override
	@OutboundActionMeta(name = "home")
	public void handleOutbound(Context ctx) throws ServletException, IOException {
		Model model = new Model(ctx);

		BookModel books = new BookModel();

		InputStream in = getClass().getResourceAsStream("book.xml");

		String xml = Files.forIO().readFrom(in, "utf-8");
		try {
			books = DefaultSaxParser.parse(xml);
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		int id = ctx.getPayload().getId();
		if(id != 0){
			Book book = books.findBook(id);
			books = new BookModel();
			books.addBook(book);
		}

		model.setBooks(books.getBooks());
		model.setAction(Action.VIEW);
		model.setPage(BookPage.HOME);
		m_jspViewer.view(ctx, model);
	}
}
