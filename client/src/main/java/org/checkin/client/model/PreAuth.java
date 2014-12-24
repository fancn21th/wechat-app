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
public class PreAuth {
    @JSONField(name = "pre_auth_code")
    String preAuthCode;

    @JSONField(name = "expires_in")
    int expiresIn;

    public String getPreAuthCode() {
        return preAuthCode;
    }

    public void setPreAuthCode(String preAuthCode) {
        this.preAuthCode = preAuthCode;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
