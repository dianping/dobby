package com.dianping.tool.dobby.mail;


import java.net.MalformedURLException;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
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

	private String name ;

	private String password;

	public static void main(String[] args) throws Exception {
		BlockingQueue<Payload> queue = new LinkedBlockingQueue<Payload>();
		new GmailService(queue);

		while (true) {
			try {
				Payload message = queue.poll(5, TimeUnit.SECONDS);
				if (message != null) {
					System.out.println(message);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// GmailServiceImpl s = new GmailServiceImpl(new LinkedBlockingQueue<Payload>());
		// s.sendMail("testJava", "testContent",
		// Arrays.asList(new String[] { "yong.you@dianping.com", "jinhua.liang@dianping.com" }),
		// Arrays.asList("youyong205@126.com"));
		//
		// s.sendHtmlMail("testJava", "testContent",
		// Arrays.asList(new String[] { "yong.you@dianping.com", "jinhua.liang@dianping.com" }),
		// Arrays.asList("youyong205@126.com"));
	}

	public GmailService(final BlockingQueue<Payload> queue) {
		this(queue, "ticketmatetest@gmail.com", "xgeskoauugnqddyf");
	}

	public GmailService(final BlockingQueue<Payload> queue, String userName, String password) {
		this.name = userName;
		this.password = password;
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						List<Payload> payloads = getUnreadMessages();
						queue.addAll(payloads);

						markAllMessagesAsRead(payloads);
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						TimeUnit.SECONDS.sleep(10);
					} catch (InterruptedException e) {

					}
				}
			}
		});
		t.setDaemon(true);
		t.start();
	}

	public void sendHtmlMail(String subject, String content, List<String> tos, List<String> ccs) throws EmailException,
	      MalformedURLException {
		HtmlEmail email = new HtmlEmail();
		email.setHostName("smtp.gmail.com");
		email.setSmtpPort(465);
		email.setAuthenticator(new DefaultAuthenticator(name, password));
		email.setSSL(true);
		email.setFrom(name);
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
		email.setAuthenticator(new DefaultAuthenticator(name, password));
		email.setSSL(true);
		email.setFrom(name);
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

	private void markAllMessagesAsRead(final List<Payload> payloadList) throws Exception {

		connect(new BizLogic() {

			@Override
			public void doBiz(Store store) {
				Folder inbox = null;
				try {
					inbox = store.getFolder("INBOX");// 收件箱
					inbox.open(Folder.READ_WRITE);
					for (Payload payload : payloadList) {
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

						}
					}
				}
			}
		});
	}

	private List<Payload> getUnreadMessages() throws Exception {
		final List<Payload> unReadMails = new ArrayList<Payload>();

		connect(new BizLogic() {

			@Override
			public void doBiz(Store store) {
				Folder inbox = null;
				try {
					inbox = store.getFolder("INBOX");// 收件箱
					inbox.open(Folder.READ_ONLY);

					for (final Message msg : inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false))) {
						unReadMails.add(Payload.valueOf(msg));
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (inbox != null) {
						try {
							inbox.close(false);
						} catch (Exception e) {

						}
					}
				}
			}
		});

		return unReadMails;

	}

	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

	private static Properties getProperties() {
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.host", "smtp.gmail.com");
		// Gmail提供的POP3和SMTP是使用安全套接字层SSL的
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

		props.put("mail.smtp.auth", "true");
		return props;
	}

	private void connect(BizLogic bizLogic) throws Exception {
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		Session session = Session.getDefaultInstance(getProperties(), null);
		// 用pop3协议：new URLName("pop3", "pop.gmail.com", 995, null,"[邮箱帐号]", "[邮箱密码]");
		// 用IMAP协议
		URLName urln = new URLName("imap", "imap.gmail.com", 995, null, name, password);
		Store store = null;
		try {
			// Store用来收信,Store类实现特定邮件协议上的读、写、监视、查找等操作。
			store = session.getStore(urln);
			store.connect();
			bizLogic.doBiz(store);
		} finally {
			if (store != null) {
				try {
					store.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public interface BizLogic {
		public void doBiz(Store store);
	}
}
