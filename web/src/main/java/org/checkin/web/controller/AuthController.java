package org.checkin.web.controller;

import org.checkin.client.client.WechatClient;
import org.checkin.client.model.ComponentAccessToken;
import org.checkin.client.model.PreAuth;
import org.checkin.client.model.Wechat;
import org.checkin.web.model.WebWechat;
import org.checkin.web.util.HttpUtils;
import org.checkin.web.vo.TicketVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Login for wechat.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2014-12-23
 */
@Controller
@RequestMapping("/")
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    WechatClient wechatClient;

    @Autowired
    WebWechat webWechat;

    @Autowired
    Wechat wechat;

    @RequestMapping(value = "/user/auth", method = RequestMethod.GET)
    public void userAuth(HttpServletRequest request, HttpServletResponse response) {
        String oauth2AuthorizeTemplate = webWechat.getOauth2Authorize();
        String ts = String.valueOf(System.currentTimeMillis());
        String oauth2AuthorizeUri =
                oauth2AuthorizeTemplate.replace("#appid", wechat.getAppid())
                        .replace("#redirect_uri", getRedirectUri())
                        .replace("#state", ts);
        request.getSession().getServletContext().setAttribute("userAuthState", ts);
        response.setHeader("Location", oauth2AuthorizeUri);
    }

    @RequestMapping(value = "/component/auth", method = RequestMethod.GET)
    public void componentAuth(HttpServletRequest request, HttpServletResponse response) {
        Object ticketObject = request.getSession().getServletContext().getAttribute("component_verify_ticket");
        if (ticketObject == null) {
            throw new RuntimeException("No ticket to authorize");
        }
        TicketVo ticket = (TicketVo) ticketObject;
        ComponentAccessToken accessToken = wechatClient.getComponentAccessToken(ticket.getComponentVerifyTicket());
        PreAuth preAuth = wechatClient.getPreAuth(accessToken.getComponentAccessToken());

        String componentLoginPageTemplate = webWechat.getComponentLoginPage();
        String uri = componentLoginPageTemplate.replace("#component_appid", wechat.getAppid())
                .replace("#pre_auth_code", preAuth.getPreAuthCode())
                .replace("#redirect_uri", getRedirectUri());
        response.setHeader("Location", uri);
    }

    private String getRedirectUri() {
        String redirectUri = webWechat.getRedirectUri();
        redirectUri = HttpUtils.encodeURIComponent(redirectUri);
        LOGGER.info("Redirect uri: " + redirectUri);
        return redirectUri;
    }
}
