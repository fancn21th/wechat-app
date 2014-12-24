package org.checkin.web.model;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2014-12-23
 */
public class WebWechat {
    String redirectUri;
    String componentLoginPage;
    String oauth2Authorize;
    String authUri;
    Boolean devMode;

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getComponentLoginPage() {
        return componentLoginPage;
    }

    public void setComponentLoginPage(String componentLoginPage) {
        this.componentLoginPage = componentLoginPage;
    }

    public String getOauth2Authorize() {
        return oauth2Authorize;
    }

    public void setOauth2Authorize(String oauth2Authorize) {
        this.oauth2Authorize = oauth2Authorize;
    }

    public String getAuthUri() {
        return authUri;
    }

    public void setAuthUri(String authUri) {
        this.authUri = authUri;
    }

    public Boolean getDevMode() {
        return devMode;
    }

    public void setDevMode(Boolean devMode) {
        this.devMode = devMode;
    }
}
