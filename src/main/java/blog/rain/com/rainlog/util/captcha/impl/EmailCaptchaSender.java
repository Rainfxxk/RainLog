package blog.rain.com.rainlog.util.captcha.impl;

import blog.rain.com.rainlog.util.captcha.CaptchaSender;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.*;
import java.util.Properties;

public class EmailCaptchaSender implements CaptchaSender {
    private static String template = null;

    static {
        if (template == null) {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                String buffer;

                InputStream inputStream = EmailCaptchaSender.class.getClassLoader().getResourceAsStream("captcha/template/email.txt");
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                while ( (buffer = bufferedReader.readLine()) != null) {
                    stringBuilder.append(buffer);
                    stringBuilder.append('\n');
                }

                template = stringBuilder.toString();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String sendCaptcha(String toAddress) {
        String captcha = generateCaptcha();

        try {
            InputStream resource = getClass().getClassLoader().getResourceAsStream("captcha/property/email.properties");
            Properties properties = new Properties();
            properties.load(resource);

            String host = properties.getProperty("mail.host");
            String user = properties.getProperty("mail.user");
            String password = properties.getProperty("mail.password");
            String subject = properties.getProperty("mail.subject");

            //创建一个session对象
            Session session = Session.getDefaultInstance(properties, null);
            //获取连接对象
            Transport transport = session.getTransport();
            //连接服务器
            transport.connect(host, user, password);
            //创建邮件对象
            MimeMessage mimeMessage = new MimeMessage(session);
            //邮件发送人
            mimeMessage.setFrom(new InternetAddress(user));
            //邮件接收人
            mimeMessage.setRecipient(Message.RecipientType.TO,new InternetAddress(toAddress));
            //邮件标题
            mimeMessage.setSubject(subject);
            //邮件内容
            mimeMessage.setContent(template.replace("xxxxxx", captcha),"text/html;charset=UTF-8");
            //发送邮件
            transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
            //关闭连接
            transport.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return captcha;
    }
}
