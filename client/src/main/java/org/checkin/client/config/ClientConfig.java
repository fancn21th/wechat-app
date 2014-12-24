package org.checkin.client.config;

import org.checkin.client.model.Wechat;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

/**
 * Client Configuration.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2014-12-23
 */
@Configuration
@ComponentScan(basePackages = {"org.checkin.client.client"})
@PropertySources({
        @PropertySource("classpath:/client.properties"),
        @PropertySource("classpath:/wechat.properties")
})
public class ClientConfig {
    @Autowired
    private Environment env;

    @Bean
    public HttpClient httpClient() {
        Integer requestTimeout = env.getRequiredProperty("client.requestTimeout", Integer.class);
        Integer connectTimeout = env.getRequiredProperty("client.connectTimeout", Integer.class);
        Integer socketTimeout = env.getRequiredProperty("client.socketTimeout", Integer.class);

        RequestConfig requestConfig = RequestConfig.custom().
                setConnectionRequestTimeout(requestTimeout)
                .setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();

        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig);
        return builder.build();
    }

    @Bean
    public Wechat wechatConfig() {
        String appid = env.getRequiredProperty("wechat.appid");
        String secret = env.getRequiredProperty("wechat.secret");
        String apiCreatePreauthcode = env.getRequiredProperty("wechat.api.create.preauthcode");
        String apiComponentToken = env.getRequiredProperty("wechat.api.component.token");
        String apiOauth2AccessToken = env.getRequiredProperty("wechat.api.oauth2.access.token");
        String apiUserinfo = env.getRequiredProperty("wechat.api.userinfo");
        String apiOauth2RefreshToken = env.getRequiredProperty("wechat.api.oauth2.refresh.token");
        Wechat wechat = new Wechat();
        wechat.setAppid(appid);
        wechat.setSecret(secret);
        wechat.setApiCreatePreauthcode(apiCreatePreauthcode);
        wechat.setApiComponentToken(apiComponentToken);
        wechat.setApiOauth2AccessToken(apiOauth2AccessToken);
        wechat.setApiUserinfo(apiUserinfo);
        wechat.setApiOauth2RefreshToken(apiOauth2RefreshToken);
        return wechat;
    }
}
