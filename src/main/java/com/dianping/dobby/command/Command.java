package com.dianping.dobby.command;

public interface Command<T extends CommandContext> {
   public String getName();

   public String getFormat();

   public String getDescription();

   public void execute(T ctx) throws CommandException;
}
