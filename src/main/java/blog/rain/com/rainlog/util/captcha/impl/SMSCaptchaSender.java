package blog.rain.com.rainlog.util.captcha.impl;

import blog.rain.com.rainlog.util.captcha.CaptchaSender;

public class SMSCaptchaSender implements CaptchaSender {
    @Override
    public String sendCaptcha(String toAddress) {
        String captcha = generateCaptcha();



        return captcha;
    }
}
