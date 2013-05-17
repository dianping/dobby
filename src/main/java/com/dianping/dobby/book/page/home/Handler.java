package com.dianping.dobby.book.page.home;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;

import org.unidal.helper.Files;
import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.PageHandler;
import org.unidal.web.mvc.annotation.InboundActionMeta;
import org.unidal.web.mvc.annotation.OutboundActionMeta;
import org.unidal.web.mvc.annotation.PayloadMeta;
import org.xml.sax.SAXException;

import com.dianping.dobby.book.BookPage;
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

		File m_modelFile = new File("book.xml").getCanonicalFile();

		if (m_modelFile.canRead()) {
			String xml = Files.forIO().readFrom(m_modelFile, "utf-8");
			System.out.println(xml);
			try {
				books = DefaultSaxParser.parse(xml);
				System.out.println(books);
			} catch (SAXException e) {
				e.printStackTrace();
			}
		}
		

		model.setBooks(books);
		model.setAction(Action.VIEW);
		model.setPage(BookPage.HOME);
		m_jspViewer.view(ctx, model);
	}
}
