package blog.rain.com.rainlog.util.captcha.impl;

import blog.rain.com.rainlog.util.captcha.CaptchaSender;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SMSCaptchaSender implements CaptchaSender {
    @Override
    public String sendCaptcha(String toAddress) {
        String captcha = generateCaptcha();

        try {
            InputStream resource = getClass().getClassLoader().getResourceAsStream("captcha/property/sms.properties");
            Properties properties = new Properties();
            properties.load(resource);

            String accessKeyId = properties.getProperty("sms.accessKeyId");
            String accessKeySecret = properties.getProperty("sms.accessKeySecret");
            String regionId = properties.getProperty("sms.regionId");
            String endpoint = properties.getProperty("sms.endpoint");
            String signName = properties.getProperty("sms.signName");
            String templateCode = properties.getProperty("sms.templateCode");

            StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                    .accessKeyId(accessKeyId)
                    .accessKeySecret(accessKeySecret)
                    .build());

            AsyncClient client = AsyncClient.builder()
                    .region(regionId)
                    //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
                    .credentialsProvider(provider)
                    //.serviceConfiguration(Configuration.create()) // Service-level configuration
                    .overrideConfiguration(
                            ClientOverrideConfiguration.create()
                                    .setEndpointOverride(endpoint)
                            //.setConnectTimeout(Duration.ofSeconds(30))
                    )
                    .build();

            SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                    .signName(signName)
                    .templateCode(templateCode)
                    .phoneNumbers(toAddress) //TODO:Check if the phone number is valid
                    .templateParam("{\"code\":\""+captcha+"\"}")
                    // Request-level configuration rewrite, can set Http request parameters, etc.
                    // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
                    .build();
            // Asynchronously get the return value of the API request
            CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
            // Synchronously get the return value of the API request
            SendSmsResponse resp = response.get();
            System.out.println(new Gson().toJson(resp));
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        return captcha;
    }
}
