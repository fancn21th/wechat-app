package org.checkin.web.controller;

import org.checkin.client.client.WechatClient;
import org.checkin.client.model.UserAccessToken;
import org.checkin.client.model.UserInfo;
import org.checkin.repository.repository.Tables;
import org.checkin.repository.repository.tables.daos.CheckinPhotoDao;
import org.checkin.repository.repository.tables.daos.FitnessCheckinDao;
import org.checkin.repository.repository.tables.daos.WechatUserDao;
import org.checkin.repository.repository.tables.pojos.CheckinPhoto;
import org.checkin.repository.repository.tables.pojos.FitnessCheckin;
import org.checkin.repository.repository.tables.pojos.WechatUser;
import org.checkin.repository.repository.tables.records.FitnessCheckinRecord;
import org.checkin.repository.repository.tables.records.WechatUserRecord;
import org.checkin.web.model.WebWechat;
import org.checkin.web.vo.FitnessCheckinVo;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2014-12-16
 */
@Controller
@RequestMapping("/")
public class MainController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    Mapper mapper;

    @Autowired
    FitnessCheckinDao fitnessCheckinDao;

    @Autowired
    DSLContext dsl;

    @Autowired
    CheckinPhotoDao photoDao;

    @Autowired
    WechatUserDao wechatUserDao;

    @Autowired
    WechatClient wechatClient;

    @Autowired
    WebWechat webWechat;

    @RequestMapping(method = RequestMethod.GET)
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        Boolean devMode = webWechat.getDevMode();
        WechatUser wechatUser;
        if (devMode) {
            wechatUser = wechatUserDao.fetchOneById(1L);
            if (wechatUser == null) {
                String errorMessage = "You are under dev mode, if it is the product environment, " +
                        "please check your dev mode in web.wechat.properties, " +
                        "and promise it is web.wechat.dev.mode=false, sorry for inconvenience.";
                LOGGER.error(errorMessage);
                throw new RuntimeException(errorMessage);
            }
            LOGGER.warn("Please be noticed you are under the dev mode without the Wechat authorization");
            LOGGER.warn("Your current user is: " + wechatUser.getNickname());
        } else {
            /*String state = request.getParameter("state");
            Object contextState = request.getSession().getServletContext().getAttribute("userAuthState");
            try {
                if (!state.equals(contextState)) {
                    String authUri = webWechat.getAuthUri();
                    return "redirect:" + authUri;
                }
            } catch (RuntimeException e) {
                String authUri = webWechat.getAuthUri();
                return "redirect:" + authUri;
            }*/
            String code = request.getParameter("code");
            LOGGER.info("Get code: " + code);
            if (StringUtils.isBlank(code)) {
                response.setStatus(403);
                return "redirect:/403";
            }
            UserAccessToken userAccessToken = wechatClient.getUserAccessToken(code);
            LOGGER.info("Get access token: " + userAccessToken.toString());
            userAccessToken = wechatClient.refreshUserAccessToken(userAccessToken.getRefreshToken());
            LOGGER.info("Fresh and get access token: " + userAccessToken.toString());
            UserInfo userInfo = wechatClient.getUserInfo(userAccessToken.getAccessToken(), userAccessToken.getOpenid());
            LOGGER.info("Get userinfo: " + userInfo.toString());
            wechatUser = wechatUserDao.fetchOneByWechatUid(userInfo.getOpenid());
            if (wechatUser == null) {
                wechatUser = new WechatUser();
                wechatUser.setAvatar(userInfo.getHeadimgurl());
                wechatUser.setCity(userInfo.getCity());
                wechatUser.setCountry(userInfo.getCountry());
                wechatUser.setEnabled(true);
                wechatUser.setNickname(userInfo.getNickname());
                wechatUser.setProvince(userInfo.getProvince());
                wechatUser.setSex(userInfo.getSex());
                wechatUser.setUnionId(userInfo.getUnionid());
                wechatUser.setWechatUid(userInfo.getOpenid());
                WechatUserRecord wechatUserRecord = dsl.newRecord(Tables.WECHAT_USER);
                wechatUserRecord.setAvatar(userInfo.getHeadimgurl());
                wechatUserRecord.setCity(userInfo.getCity());
                wechatUserRecord.setCountry(userInfo.getCountry());
                wechatUserRecord.setEnabled(true);
                wechatUserRecord.setNickname(userInfo.getNickname());
                wechatUserRecord.setProvince(userInfo.getProvince());
                wechatUserRecord.setSex(userInfo.getSex());
                wechatUserRecord.setUnionId(userInfo.getUnionid());
                wechatUserRecord.setWechatUid(userInfo.getOpenid());
                wechatUserRecord.store();
                Long userId = wechatUserRecord.getId();
                wechatUser.setId(userId);
            } else {
                wechatUser.setAvatar(userInfo.getHeadimgurl());
                wechatUser.setCity(userInfo.getCity());
                wechatUser.setCountry(userInfo.getCountry());
                wechatUser.setEnabled(true);
                wechatUser.setNickname(userInfo.getNickname());
                wechatUser.setProvince(userInfo.getProvince());
                wechatUser.setSex(userInfo.getSex());
                wechatUser.setUnionId(userInfo.getUnionid());
                wechatUser.setWechatUid(userInfo.getOpenid());
                wechatUserDao.update(wechatUser);
            }
        }
        model.addAttribute("user", wechatUser);
        return "index";
    }

    @RequestMapping(value = "/uid/{uid}/checkins", method = RequestMethod.GET)
    @ResponseBody
    public List<FitnessCheckinVo> getCheckins(@PathVariable Long uid) {
        List<FitnessCheckin> checkins = fitnessCheckinDao.fetchByUid(uid);
        List<FitnessCheckinVo> checkinVos = new ArrayList<>(checkins.size());
        for (FitnessCheckin checkin : checkins) {
            FitnessCheckinVo checkinVo = mapper.map(checkin, FitnessCheckinVo.class);
            checkinVos.add(checkinVo);
            Long checkinId = checkin.getId();
            List<CheckinPhoto> photos = photoDao.fetchByFitnessCheckinId(checkinId);
            List<Long> photoIds = new ArrayList<>(photos.size());
            for (CheckinPhoto photo : photos) {
                photoIds.add(photo.getId());
            }
            checkinVo.setPhotoIds(photoIds);
        }
        return checkinVos;
    }

    @RequestMapping(value = "/checkins", method = RequestMethod.POST)
    @ResponseBody
    public FitnessCheckinVo checkin(@RequestBody FitnessCheckinVo checkinVo) {
        checkinVo.setTimestamp(new Timestamp(System.currentTimeMillis()));
        FitnessCheckinRecord fitnessCheckinRecord = dsl.newRecord(Tables.FITNESS_CHECKIN);
        fitnessCheckinRecord.setRecord(checkinVo.getRecord());
        fitnessCheckinRecord.setReviewPassed(true);
        fitnessCheckinRecord.setTimestamp(checkinVo.getTimestamp());
        fitnessCheckinRecord.setUid(checkinVo.getUid());
        fitnessCheckinRecord.store();
        Long checkinId = fitnessCheckinRecord.getId();
        List<Long> photoIds = checkinVo.getPhotoIds();
        Long[] pas = new Long[photoIds.size()];
        photoIds.toArray(pas);
        List<CheckinPhoto> photos = photoDao.fetchById(pas);
        for (CheckinPhoto photo : photos) {
            photo.setFitnessCheckinId(checkinId);
        }
        photoDao.update(photos);
        return checkinVo;
    }

}
