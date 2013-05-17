package com.dianping.dobby.conosle.page.home;

import com.dianping.dobby.conosle.ConoslePage;
import org.unidal.web.mvc.view.BaseJspViewer;

public class JspViewer extends BaseJspViewer<ConoslePage, Action, Context, Model> {
	@Override
	protected String getJspFilePath(Context ctx, Model model) {
		Action action = model.getAction();

		switch (action) {
		case VIEW:
			return JspFile.VIEW.getPath();
		case SUMMARY:
			return JspFile.SUMMARY.getPath();
		}

		throw new RuntimeException("Unknown action: " + action);
	}
}
