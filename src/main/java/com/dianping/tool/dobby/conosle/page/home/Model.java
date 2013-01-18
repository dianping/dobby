package com.dianping.tool.dobby.conosle.page.home;

import com.dianping.tool.dobby.conosle.ConoslePage;
import org.unidal.web.mvc.ViewModel;

public class Model extends ViewModel<ConoslePage, Action, Context> {
	public Model(Context ctx) {
		super(ctx);
	}

	@Override
	public Action getDefaultAction() {
		return Action.VIEW;
	}
}
