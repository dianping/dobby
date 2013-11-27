package com.dianping.dobby;

import java.io.File;

import org.apache.log4j.Logger;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

public class DobbyServer {
   private final static Logger logger = Logger.getLogger(DobbyServer.class);

   public static void main(String[] args) throws Exception {
      int index = 0;
      File warRoot = new File(args.length > index ? args[index++] : "dobby");
      int port = args.length > index ? Integer.parseInt(args[index++]) : 3622;
      String contextPath = args.length > index ? args[index++] : "/dobby";

      logger.info(String.format("starting jetty@%d, contextPath %s, warRoot %s", port, contextPath, warRoot
            .getAbsoluteFile().getAbsolutePath()));

      Server server = new Server(port);
      addTerminateSingalHandler(server);
      WebAppContext context = new WebAppContext();

      context.setContextPath(contextPath);
      context.setDescriptor(new File(warRoot, "WEB-INF/web.xml").getPath());
      context.setResourceBase(warRoot.getPath());

      server.setHandler(context);
      server.start();
   }

   public static void addTerminateSingalHandler(final Server server) {
      // not officially supported API
      sun.misc.Signal.handle(new sun.misc.Signal("TERM"), new sun.misc.SignalHandler() {
         @Override
         public void handle(sun.misc.Signal signal) {
            logger.info(String.format("%s signal received, try to stop jetty server", signal));
            try {
               server.stop();
            } catch (Exception e) {
               logger.error("error stop jetty server", e);
            }
         }
      });
   }
}
