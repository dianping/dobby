package com.dianping.dobby;

import org.unidal.helper.Threads;
import org.unidal.initialization.AbstractModule;
import org.unidal.initialization.Module;
import org.unidal.initialization.ModuleContext;

import com.dianping.dobby.email.EmailDispatcher;
import com.dianping.dobby.email.EmailChannel;

public class DobbyModule extends AbstractModule implements DobbyConstants {
   public static final String ID = ID_DOBBY;

   @Override
   public Module[] getDependencies(ModuleContext ctx) {
      return null;
   }

   @Override
   protected void execute(ModuleContext ctx) throws Exception {
      // force initialized
      ctx.lookup(EmailChannel.class, ID_TICKET);
      ctx.lookup(EmailChannel.class, ID_BOOK);

      Threads.forGroup(DobbyConstants.NAME_DOBBY).start(ctx.lookup(EmailDispatcher.class, ID_TICKET));
      Threads.forGroup(DobbyConstants.NAME_DOBBY).start(ctx.lookup(EmailDispatcher.class, ID_BOOK));
   }
}
