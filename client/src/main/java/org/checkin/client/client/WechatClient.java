package org.checkin.client.client;

import org.checkin.client.core.BaseClient;
import org.apache.http.client.HttpClient;
import org.checkin.client.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2014-12-23
 */
@Service
public class WechatClient extends BaseClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(WechatClient.class);

    @Autowired
    HttpClient client;

    @Autowired
    Wechat wechat;

    @Override
    protected HttpClient getClient() {
        return client;
    }

    @Override
    protected Wechat getWechat() {
        return wechat;
    }

    public ComponentAccessToken getComponentAccessToken(String componentVerifyTicket) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{").append("\"component_appid\":").append("\"").append(wechat.getAppid()).append("\"");
        jsonBuilder.append(",").append("\"component_appsecret\":").append("\"").append(wechat.getSecret()).append("\"");
        jsonBuilder.append(",").append("\"component_verify_ticket\":").append("\"").append(componentVerifyTicket)
                .append("\"}");
        String jsonString = jsonBuilder.toString();
        LOGGER.info("Posting json: " + jsonString);
        return post(wechat.getApiComponentToken(), jsonString, ComponentAccessToken.class);
    }

    public PreAuth getPreAuth(String componentAccessToken) {
        String apiCreatePreauthcode = wechat.getApiCreatePreauthcode();
        String url = apiCreatePreauthcode.replace("#component_access_token", componentAccessToken);
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{").append("\"component_appid\":").append("\"").append(wechat.getAppid()).append("\"}");
        String jsonString = jsonBuilder.toString();
        LOGGER.info("Posting json: " + jsonString);
        return post(url, jsonString, PreAuth.class);
    }

    public UserAccessToken getUserAccessToken(String code) {
        String apiOauth2AccessTokenTemplate = getWechat().getApiOauth2AccessToken();
        String uri = apiOauth2AccessTokenTemplate.replace("#appid", getWechat().getAppid())
                .replace("#secret", getWechat().getSecret())
                .replace("#code", code);
        LOGGER.info("Get uri: " + uri);
        return get(uri, UserAccessToken.class);
    }

    public UserInfo getUserInfo(String accessToken, String openid) {
        String apiUserinfoTemplate = getWechat().getApiUserinfo();
        String uri = apiUserinfoTemplate.replace("#access_token", accessToken)
                .replace("#openid", openid);
        return get(uri, UserInfo.class);
    }

    public UserAccessToken refreshUserAccessToken(String refreshToken) {
        String apiOauth2RefreshTokenTemplate = getWechat().getApiOauth2RefreshToken();
        String uri = apiOauth2RefreshTokenTemplate.replace("#appid", getWechat().getAppid())
                .replace("#refresh_token", refreshToken);
        return get(uri, UserAccessToken.class);
    }

}
