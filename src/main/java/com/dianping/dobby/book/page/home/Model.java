package com.dianping.dobby.book.page.home;

import com.dianping.dobby.book.BookPage;
import org.unidal.web.mvc.ViewModel;

public class Model extends ViewModel<BookPage, Action, Context> {
	public Model(Context ctx) {
		super(ctx);
	}

	@Override
	public Action getDefaultAction() {
		return Action.VIEW;
	}
}
