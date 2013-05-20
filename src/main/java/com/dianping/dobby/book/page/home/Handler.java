package com.dianping.dobby.book.page.home;

import java.io.IOException;

import javax.servlet.ServletException;

import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.PageHandler;
import org.unidal.web.mvc.annotation.InboundActionMeta;
import org.unidal.web.mvc.annotation.OutboundActionMeta;
import org.unidal.web.mvc.annotation.PayloadMeta;

import com.dianping.dobby.book.BookPage;
import com.dianping.dobby.book.biz.BookManager;
import com.dianping.dobby.book.model.entity.Book;
import com.dianping.dobby.book.model.entity.BookModel;

public class Handler implements PageHandler<Context> {
	@Inject
	private JspViewer m_jspViewer;

	@Inject
	private BookManager m_manager;

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
		BookModel bookModel = m_manager.getModel();

		String id = ctx.getPayload().getId();
		
		if (id != null) {
			Book book = bookModel.findBook(id);
			bookModel = new BookModel();
			bookModel.addBook(book);
		}

		model.setBooks(bookModel.getBooks());
		model.setAction(Action.VIEW);
		model.setPage(BookPage.HOME);
		m_jspViewer.view(ctx, model);
	}
}
