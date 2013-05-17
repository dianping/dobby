package com.dianping.dobby.email;

import java.net.MalformedURLException;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.search.FlagTerm;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

public class GmailService {
   private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

   private String m_name;

   private String m_password;

   private static Properties getProperties() {
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

   public GmailService(final BlockingQueue<Payload> queue) {
      this(queue, "ticketmatetest@gmail.com", "xgeskoauugnqddyf");
   }

   public GmailService(final BlockingQueue<Payload> queue, String userName, String password) {
      m_name = userName;
      m_password = password;

      Thread t = new Thread(new Runnable() {
         @Override
         public void run() {
            try {
               while (true) {
                  try {
                     List<Payload> payloads = getUnreadMessages();
                     queue.addAll(payloads);

                     markAllMessagesAsRead(payloads);
                  } catch (Exception e) {
                     e.printStackTrace();
                  }

                  TimeUnit.SECONDS.sleep(10);
               }
            } catch (InterruptedException e) {
            }
         }
      });

      t.setDaemon(true);
      t.start();
   }

   private void connect(Processor processor) throws Exception {
      Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
      Session session = Session.getDefaultInstance(getProperties(), null);
      
      // URLName urln = new URLName("pop3", "pop.gmail.com", 995, null,m_name, m_password);
      URLName urln = new URLName("imap", "imap.gmail.com", 995, null, m_name, m_password);
      Store store = null;
      
      try {
         store = session.getStore(urln);
         store.connect();
         processor.process(store);
      } finally {
         if (store != null) {
            try {
               store.close();
            } catch (Exception e) {
            }
         }
      }
   }

   private List<Payload> getUnreadMessages() throws Exception {
      final List<Payload> unReadMails = new ArrayList<Payload>();

      connect(new UnreadProcessor(unReadMails));

      return unReadMails;

   }

   private void markAllMessagesAsRead(final List<Payload> payloadList) throws Exception {
      connect(new MarkAsReadProcessor(payloadList));
   }

   public void sendHtmlMail(String subject, String content, List<String> tos, List<String> ccs) throws EmailException,
         MalformedURLException {
      HtmlEmail email = new HtmlEmail();
      email.setHostName("smtp.gmail.com");
      email.setSmtpPort(465);
      email.setAuthenticator(new DefaultAuthenticator(m_name, m_password));
      email.setSSL(true);
      email.setFrom(m_name);
      email.setSubject(subject);
      email.setHtmlMsg(content);

      if (tos != null) {
         for (String to : tos) {
            email.addTo(to);
         }
      }

      if (ccs != null) {
         for (String cc : ccs) {
            email.addCc(cc);
         }
      }
      email.send();
   }

   public void sendMail(String subject, String content, List<String> tos, List<String> ccs) throws EmailException {
      Email email = new SimpleEmail();
      email.setHostName("smtp.gmail.com");
      email.setSmtpPort(465);
      email.setAuthenticator(new DefaultAuthenticator(m_name, m_password));
      email.setSSL(true);
      email.setFrom(m_name);
      email.setSubject(subject);
      email.setMsg(content);
      if (tos != null) {
         for (String to : tos) {
            email.addTo(to);
         }
      }

      if (ccs != null) {
         for (String cc : ccs) {
            email.addCc(cc);
         }
      }
      email.send();
   }

   private final class MarkAsReadProcessor implements Processor {
      private List<Payload> m_payloadList;

      private MarkAsReadProcessor(List<Payload> payloadList) {
         m_payloadList = payloadList;
      }

      @Override
      public void process(Store store) {
         Folder inbox = null;

         try {
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            for (Payload payload : m_payloadList) {
               Message message = inbox.getMessage(payload.getNum());
               if (!message.isSet(Flags.Flag.SEEN)) {
                  message.setFlag(Flags.Flag.SEEN, true);
               }
            }
         } catch (Exception e) {
            e.printStackTrace();
         } finally {
            if (inbox != null) {
               try {
                  inbox.close(false);
               } catch (Exception e) {
                  // ignore it
               }
            }
         }
      }
   }

   private final class UnreadProcessor implements Processor {
      private final List<Payload> m_unreadMails;

      private UnreadProcessor(List<Payload> unreadMails) {
         m_unreadMails = unreadMails;
      }

      @Override
      public void process(Store store) {
         Folder inbox = null;

         try {
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            for (final Message msg : inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false))) {
               m_unreadMails.add(Payload.valueOf(msg));
            }
         } catch (Exception e) {
            e.printStackTrace();
         } finally {
            if (inbox != null) {
               try {
                  inbox.close(false);
               } catch (Exception e) {
                  // ignore it
               }
            }
         }
      }
   }

   public interface Processor {
      public void process(Store store);
   }
}
