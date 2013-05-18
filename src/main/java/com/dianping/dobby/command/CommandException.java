package com.dianping.dobby.command;

public class CommandException extends Exception {
   private static final long serialVersionUID = 1L;

   public CommandException(String message) {
      super(message);
   }

   public CommandException(String message, Throwable cause) {
      super(message, cause);
   }
}
