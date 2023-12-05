package blog.rain.com.rainlog.util.captcha;

import blog.rain.com.rainlog.util.captcha.impl.EmailCaptchaSender;

public class Test {
    public static void main(String[] args) {
        EmailCaptchaSender emailCaptchaSender = new EmailCaptchaSender();
        // 经测试 qq邮箱 和 163邮箱 可以正常发送
        //emailCaptchaSender.sendCaptcha("csetxh@163.com");
        emailCaptchaSender.sendCaptcha("2472047312@qq.com");
    }
}
