package org.checkin.client.client;

import org.checkin.client.config.ClientConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {ClientConfig.class})
public class WechatClientTest {
    @Autowired
    WechatClient wechatClient;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetComponentAccessToken() throws Exception {
        wechatClient.getComponentAccessToken("ticketunknown");
    }

    @Test
    public void testGetPreAuth() throws Exception {
        wechatClient.getPreAuth("tokenunknown");
    }

    @Test
    public void testGetUserAccessToken() throws Exception {
        String code = "4u75ehsejshesjesjjsa3";
        wechatClient.getUserAccessToken(code);
    }

    @Test
    public void testGetUserInfo() throws Exception {

    }

    @Test
    public void testRefreshUserAccessToken() throws Exception {

    }
}