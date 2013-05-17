package com.dianping.dobby.ticket.page.home;

import com.dianping.dobby.ticket.TicketPage;

import org.unidal.web.mvc.view.BaseJspViewer;

public class JspViewer extends BaseJspViewer<TicketPage, Action, Context, Model> {
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
