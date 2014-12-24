package org.checkin.client.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2014-12-23
 */
public class ComponentAccessToken {
    @JSONField(name = "component_access_token")
    String componentAccessToken;

    @JSONField(name = "expires_in")
    Integer expiresIn;

    public String getComponentAccessToken() {
        return componentAccessToken;
    }

    public void setComponentAccessToken(String componentAccessToken) {
        this.componentAccessToken = componentAccessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String toString() {
        return "ComponentAccessToken{" +
                "componentAccessToken='" + componentAccessToken + '\'' +
                ", expiresIn=" + expiresIn +
                '}';
    }
}
