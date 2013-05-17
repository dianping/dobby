package com.dianping.dobby.mail;

import java.security.Security;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.unidal.helper.Threads;
import org.unidal.helper.Threads.Task;
import org.unidal.lookup.annotation.Inject;

public class GmailMailService implements MailService, Initializable {
	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

	@Inject
	private MessageParser m_parser;

	@Inject
	private MessageQueue m_queue;

	@Inject
	private String m_name;

	@Inject
	private String m_password;

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

	private Store getStore() {
		try {
			Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

			Session session = Session.getDefaultInstance(getProperties(), null);
			URLName urln = new URLName("imap", "imap.gmail.com", 995, null, m_name, m_password);
			// Store��������,Store��ʵ���ض��ʼ�Э���ϵĶ���д�����ӡ����ҵȲ�����
			Store store = session.getStore(urln);

			return store;
		} catch (Exception e) {
			throw new RuntimeException("Error when setting up Gmail store!", e);
		}
	}

	@Override
	public void initialize() throws InitializationException {
		try {
			m_authenticator = new DefaultAuthenticator(m_name, m_password);
			m_store = getStore();
			m_store.connect();
			m_inbox = m_store.getFolder("INBOX");
			m_inbox.open(Folder.READ_ONLY);
		} catch (Exception e) {
			throw new InitializationException(String.format("Error when connecting Gmail(%s)!", m_name), e);
		}

		Threads.forGroup("Dobby").start(new GmailPoller());
	}

	@Override
	public void markRead(int messageId) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void send(Address[] tos, Address[] ccs, String subject, String content, String htmlContent) throws Exception {
		HtmlEmail email = new HtmlEmail();

		email.setHostName("smtp.gmail.com");
		email.setSmtpPort(465);
		email.setAuthenticator(m_authenticator);
		email.setSSL(true);
		email.setFrom(m_name);
		email.setSubject(subject);

		if (content != null) {
			email.setMsg(content);
		}

		if (htmlContent != null) {
			email.setHtmlMsg(htmlContent);
		}

		if (tos != null) {
			for (Address to : tos) {
				if (to instanceof InternetAddress) {
					InternetAddress address = (InternetAddress) to;

					if (address.getPersonal() != null) {
						email.addTo(address.getAddress(), address.getPersonal());
					} else {
						email.addTo(address.getAddress());
					}
				}
			}
		}

		if (ccs != null) {
			for (Address cc : ccs) {
				if (cc instanceof InternetAddress) {
					InternetAddress address = (InternetAddress) cc;

					if (address.getPersonal() != null) {
						email.addCc(address.getAddress(), address.getPersonal());
					} else {
						email.addCc(address.getAddress());
					}
				}
			}
		}

		email.send();
	}

	public void setName(String name) {
		m_name = name;
	}

	public void setPassword(String password) {
		m_password = password;
	}

	class GmailPoller implements Task {
		@Override
		public String getName() {
			return getClass().getSimpleName();
		}

		@Override
		public void run() {
			try {
				Message[] result = m_inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

				for (Message msg : result) {
					MessagePayload payload = m_parser.parse(msg);

					m_queue.offer(payload);
				}
			} catch (MessagingException e) {
				e.printStackTrace();
			} finally {
			}
		}

		@Override
		public void shutdown() {
		}
	}
}
