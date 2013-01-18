package com.dianping.tool.dobby.ticket;

public interface TicketProcessor {
	/**
	 * Create a ticket.
	 * 
	 * @param id
	 *           ticket id.
	 * @param subject
	 *           ticket subject.
	 * @param content
	 *           ticket content.
	 * @param createdBy
	 *           created by.
	 */
	public void createTicket(String id, String subject, String content, String createdBy) throws Exception;

	/**
	 * Process a ticket.
	 * 
	 * @param id
	 *           message id.
	 * @param by
	 *           who sent the message.
	 * @param comment
	 *           comment. optional.
	 * @param cmd
	 *           command name optional.
	 * @param args
	 *           arguments of command. optional.
	 */
	public void processTicket(String id, String by, String comment, String cmd, String... args) throws Exception;
}
