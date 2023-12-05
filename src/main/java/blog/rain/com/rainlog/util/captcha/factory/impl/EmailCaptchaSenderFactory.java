package blog.rain.com.rainlog.util.captcha.factory.impl;

import blog.rain.com.rainlog.util.captcha.CaptchaSender;
import blog.rain.com.rainlog.util.captcha.factory.CaptchaSenderFactory;
import blog.rain.com.rainlog.util.captcha.impl.EmailCaptchaSender;

public class EmailCaptchaSenderFactory implements CaptchaSenderFactory {
    @Override
    public CaptchaSender createCaptchaSender() {
        return new EmailCaptchaSender();
    }
}
