package com.dianping.dobby;

import org.unidal.helper.Threads;
import org.unidal.initialization.AbstractModule;
import org.unidal.initialization.Module;
import org.unidal.initialization.ModuleContext;

public class TicketModule extends AbstractModule {
	public static final String ID = "ticket";

	@Override
	public Module[] getDependencies(ModuleContext ctx) {
		return null;
	}

	@Override
	protected void execute(ModuleContext ctx) throws Exception {
		TicketTask task = ctx.lookup(TicketTask.class);

		Threads.forGroup("Ticket").start(task);
	}
}
