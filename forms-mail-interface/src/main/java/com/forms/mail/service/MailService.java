package com.forms.mail.service;

import com.forms.mail.pojo.MailParam;

/**
 * Created by Administrator on 2016/3/21.
 */
public interface MailService {
	
	
    void threadSend(MailParam mailParam);

    /**
     * 发送邮件
     */
    void send(MailParam mailParam);
}
