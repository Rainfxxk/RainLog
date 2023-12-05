package blog.rain.com.rainlog.util.captcha.factory;

import blog.rain.com.rainlog.util.captcha.CaptchaSender;
import blog.rain.com.rainlog.util.captcha.factory.impl.EmailCaptchaSenderFactory;
import blog.rain.com.rainlog.util.captcha.factory.impl.SMSCaptchaSenderFactory;

public interface CaptchaSenderFactory {
    public static CaptchaSenderFactory newInstance(String captchaClass) {
        switch (captchaClass) {
            case "email":
                return new EmailCaptchaSenderFactory();
            case "sms":
                return new SMSCaptchaSenderFactory();
            default:
                return null;
        }
    }
    public CaptchaSender createCaptchaSender();
}
