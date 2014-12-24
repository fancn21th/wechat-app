package org.checkin.client.model;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2014-12-23
 */
public class Wechat {
    String appid;
    String secret;
    String apiCreatePreauthcode;
    String apiComponentToken;
    String apiOauth2AccessToken;
    String apiUserinfo;
    String apiOauth2RefreshToken;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getApiCreatePreauthcode() {
        return apiCreatePreauthcode;
    }

    public void setApiCreatePreauthcode(String apiCreatePreauthcode) {
        this.apiCreatePreauthcode = apiCreatePreauthcode;
    }

    public String getApiComponentToken() {
        return apiComponentToken;
    }

    public void setApiComponentToken(String apiComponentToken) {
        this.apiComponentToken = apiComponentToken;
    }

    public String getApiOauth2AccessToken() {
        return apiOauth2AccessToken;
    }

    public void setApiOauth2AccessToken(String apiOauth2AccessToken) {
        this.apiOauth2AccessToken = apiOauth2AccessToken;
    }

    public String getApiUserinfo() {
        return apiUserinfo;
    }

    public void setApiUserinfo(String apiUserinfo) {
        this.apiUserinfo = apiUserinfo;
    }

    public String getApiOauth2RefreshToken() {
        return apiOauth2RefreshToken;
    }

    public void setApiOauth2RefreshToken(String apiOauth2RefreshToken) {
        this.apiOauth2RefreshToken = apiOauth2RefreshToken;
    }
}
