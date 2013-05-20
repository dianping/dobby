package com.dianping.dobby.email;

import java.security.Security;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.unidal.helper.Threads;
import org.unidal.helper.Threads.Task;
import org.unidal.lookup.annotation.Inject;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;

public class GmailEmailChannel implements EmailChannel, Initializable {
   private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

   @Inject
   private MessageParser m_parser;

   @Inject
   private MessageQueue m_queue;

   @Inject
   private String m_name;

   @Inject
   private String m_password;

   @Inject
   private int m_checkInterval = 10; // 10 seconds

   private Authenticator m_authenticator;

   private Store m_store;

   private Folder m_inbox;

   @Override
   public void close() {
      if (m_inbox != null) {
         try {
            m_inbox.close(false);
         } catch (Exception e) {
            // ignore it
         }
      }

      if (m_store != null) {
         try {
            m_store.close();
         } catch (Exception e) {
            // ignore it
         }
      }
   }

   private HtmlEmail createHtmlEmail() throws EmailException {
      HtmlEmail email = new HtmlEmail();

      email.setHostName("smtp.gmail.com");
      email.setSmtpPort(465);
      email.setAuthenticator(m_authenticator);
      email.setSSL(true);
      email.setFrom(m_name);
      email.setCharset("utf-8");

      return email;
   }

   private Store createStore() throws InitializationException {
      Session session = Session.getDefaultInstance(getProperties(), null);
      URLName urln = new URLName("imap", "imap.gmail.com", 995, null, m_name, m_password);

      try {
         return session.getStore(urln);
      } catch (NoSuchProviderException e) {
         Cat.logError(e);
         throw new InitializationException("Error when setting up Gmail!", e);
      }
   }

   @Override
   public String getAddress() {
      return m_name;
   }

   private Properties getProperties() {
      Properties props = System.getProperties();

      props.setProperty("mail.smtp.host", "smtp.gmail.com");

      props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
      props.setProperty("mail.smtp.socketFactory.fallback", "false");
      props.setProperty("mail.smtp.port", "465");
      props.setProperty("mail.smtp.socketFactory.port", "465");

      props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
      props.setProperty("mail.imap.socketFactory.fallback", "false");
      props.setProperty("mail.imap.port", "993");
      props.setProperty("mail.imap.socketFactory.port", "993");

      props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
      props.setProperty("mail.pop3.socketFactory.fallback", "false");
      props.setProperty("mail.pop3.port", "995");
      props.setProperty("mail.pop3.socketFactory.port", "995");

      props.setProperty("mail.smtp.auth", "true");
      return props;
   }

   @Override
   public void initialize() throws InitializationException {
      Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

      m_authenticator = new DefaultAuthenticator(m_name, m_password);
      m_store = createStore();

      Threads.forGroup("Dobby").start(new InboxPoller());
   }

   @Override
   public void markRead(int... messageIds) throws Exception {
      Transaction t = Cat.newTransaction(GmailEmailChannel.class.getSimpleName(), getClass().getSimpleName());
      Folder inbox = null;

      try {
         if (!m_store.isConnected()) {
            m_store.connect();
         }

         inbox = m_store.getFolder("INBOX");
         inbox.open(Folder.READ_WRITE);

         for (int messageId : messageIds) {
            Message message = inbox.getMessage(messageId);

            if (!message.isSet(Flags.Flag.SEEN)) {
               message.setFlag(Flags.Flag.SEEN, true);
            }
         }

         t.setStatus(Transaction.SUCCESS);
      } catch (Exception e) {
         t.setStatus(e);
         Cat.logError(e);
         throw e;
      } finally {
         if (inbox != null) {
            try {
               inbox.close(false);
            } catch (Exception e) {
               Cat.logError(e);
            }
         }

         t.complete();
      }
   }

   @Override
   public void pollUnread() throws Exception {
      Transaction t = Cat.newTransaction(GmailEmailChannel.class.getSimpleName(), getClass().getSimpleName());
      Folder inbox = null;

      try {
         if (!m_store.isConnected()) {
            m_store.connect();
         }

         inbox = m_store.getFolder("INBOX");
         inbox.open(Folder.READ_ONLY);

         Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

         for (Message message : messages) {
            MessagePayload payload = m_parser.parse(message);

            m_queue.offer(payload);
         }

         t.setStatus(Transaction.SUCCESS);
      } catch (Exception e) {
         t.setStatus(e);
         Cat.logError(e);
         throw e;
      } finally {
         if (inbox != null) {
            try {
               inbox.close(false);
            } catch (Exception e) {
               Cat.logError(e);
            }
         }

         t.complete();
      }
   }

   @Override
   public void send(InternetAddress[] tos, InternetAddress[] ccs, String originalMessageId, String subject,
         String content, String htmlContent) throws Exception {
      HtmlEmail email = createHtmlEmail();

      email.setSubject(subject == null ? "No Subject" : subject);

      if (originalMessageId != null) {
         email.addHeader("In-Reply-To", originalMessageId);
      }

      if (content != null) {
         email.setMsg(content);
      }

      if (htmlContent != null) {
         email.setHtmlMsg(htmlContent);
      }

      if (tos != null) {
         for (InternetAddress to : tos) {
            if (to.getPersonal() != null) {
               email.addTo(to.getAddress(), to.getPersonal());
            } else {
               email.addTo(to.getAddress());
            }
         }
      }

      if (ccs != null) {
         for (InternetAddress cc : ccs) {
            if (cc.getPersonal() != null) {
               email.addCc(cc.getAddress(), cc.getPersonal());
            } else {
               email.addCc(cc.getAddress());
            }
         }
      }

      email.send();
   }

   public void setCheckInterval(int checkInterval) {
      m_checkInterval = checkInterval;
   }

   public void setName(String name) {
      m_name = name;
   }

   public void setPassword(String password) {
      m_password = password;
   }

   class InboxPoller implements Task {
      @Override
      public String getName() {
         return getClass().getSimpleName();
      }

      @Override
      public void run() {
         while (true) {
            try {
               pollUnread();
            } catch (Exception e) {
               // ignore it
            }

            if (m_checkInterval > 0) {
               try {
                  TimeUnit.SECONDS.sleep(m_checkInterval);
               } catch (InterruptedException e) {
                  break;
               }
            }
         }
      }

      @Override
      public void shutdown() {
      }
   }
}
