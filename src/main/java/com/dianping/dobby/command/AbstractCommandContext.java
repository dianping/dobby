package com.dianping.dobby.command;

import com.dianping.dobby.email.EmailService;

public abstract class AbstractCommandContext implements CommandContext {
   private EmailService m_service;

   public AbstractCommandContext(EmailService service) {
      m_service = service;
   }

   public EmailService getEmailService() {
      return m_service; 
   }
}
