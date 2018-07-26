//package com.forms.mail.service.impl;
//
//import javax.mail.Message;
//import javax.mail.Session;
//import javax.print.attribute.standard.Destination;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jms.core.JmsTemplate;
//import org.springframework.jms.listener.SessionAwareMessageListener;
//import org.springframework.stereotype.Component;
//
//import com.alibaba.dubbo.common.json.JSONObject;
//import com.forms.mail.pojo.MailParam;
//import com.forms.mail.service.MailService;
//
//@Component
//public class ConsumerSessionAwareMessageListener implements SessionAwareMessageListener<Message> {
//
//	private static final Log log = LogFactory.getLog(ConsumerSessionAwareMessageListener.class);
//
//	@Autowired
//	private JmsTemplate activeMqJmsTemplate;
//	@Autowired
//	private Destination sessionAwareQueue;
//	@Autowired
//	private MailService mailService;
//
//	public synchronized void onMessage(Message message, Session session) {
//		try {
//			ActiveMQTextMessage msg = (ActiveMQTextMessage) message;
//			final String ms = msg.getText();
//			log.info("==>receive message:" + ms);
//			MailParam mailParam = JSONObject.parseObject(ms, MailParam.class);// 转换成相应的对象
//			if (mailParam == null) {
//				return;
//			}
//
//			try {
//				mailService.threadSend(mailParam);
//			} catch (Exception e) {
//				// 发送异常，重新放回队列
////				activeMqJmsTemplate.send(sessionAwareQueue, new MessageCreator() {
////					public Message createMessage(Session session) throws JMSException {
////						return session.createTextMessage(ms);
////					}
////				});
//				log.error("==>MailException:", e);
//			}
//		} catch (Exception e) {
//			log.error("==>", e);
//		}
//	}
//}
