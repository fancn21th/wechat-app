package org.checkin.web.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2014-12-23
 */
@XmlRootElement(name = "xml")
public class TicketVo {
    String appId;
    Long createTime;
    String infoType;
    String componentVerifyTicket;

    public String getAppId() {
        return appId;
    }

    @XmlElement(name = "AppId")
    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    @XmlElement(name = "CreateTime")
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getInfoType() {
        return infoType;
    }

    @XmlElement(name = "InfoType")
    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getComponentVerifyTicket() {
        return componentVerifyTicket;
    }

    @XmlElement(name = "ComponentVerifyTicket")
    public void setComponentVerifyTicket(String componentVerifyTicket) {
        this.componentVerifyTicket = componentVerifyTicket;
    }
}
