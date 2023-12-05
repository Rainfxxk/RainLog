package blog.rain.com.rainlog.util.captcha;

import java.util.Random;

public interface CaptchaSender {

    default String generateCaptcha() {
        StringBuilder captcha = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            captcha.append(random.nextInt(10));
        }

        return captcha.toString();
    }

    String sendCaptcha(String toAddress);
}
