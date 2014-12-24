package org.checkin.client.core;

import com.alibaba.fastjson.JSON;
import org.checkin.client.model.Error;
import org.checkin.client.model.Wechat;
import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2014-12-23
 */
public abstract class BaseClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseClient.class);

    private static final String USER_AGENT = "Mozilla/5.0";

    private static final String CONTENT_TYPE = "application/json";

    protected abstract HttpClient getClient();

    protected abstract Wechat getWechat();

    public <T> T get(String url, Class<T> outType) {
        HttpGet request = new HttpGet(url);
        request.setHeader("User-Agent", USER_AGENT);
        request.setHeader("accept", CONTENT_TYPE);

        return service(request, outType);
    }

    public <T> T post(String url, String jsonIn, Class<T> outType) {
        HttpPost request = new HttpPost(url);

        // add header
        request.setHeader("User-Agent", USER_AGENT);
        request.setHeader("content-type", CONTENT_TYPE);
        StringEntity stringEntity = new StringEntity(jsonIn, Consts.UTF_8);
        stringEntity.setContentType(CONTENT_TYPE);
        request.setEntity(stringEntity);

        return service(request, outType);
    }

    private <T> T service(HttpRequestBase request, Class<T> outType) {
        HttpResponse response;
        try {
            response = getClient().execute(request);
        } catch (IOException e) {
            LOGGER.error("", e);
            throw new RuntimeException(e);
        }
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            InputStream input = null;
            try {
                input = response.getEntity().getContent();
                String content = IOUtils.toString(input, Consts.UTF_8);
                Error error = JSON.parseObject(content, Error.class);
                if (error.getErrcode() == null || error.getErrcode() == 0) {
                    T jsonOut = JSON.parseObject(content, outType);
                    LOGGER.info(jsonOut.toString());
                    return jsonOut;
                }
                LOGGER.error(error.toString());
                throw new RuntimeException(error.getErrcode() + ": " + error.getErrmsg());
            } catch (IOException e) {
                LOGGER.error("", e);
                throw new RuntimeException(e);
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        LOGGER.error("", e);
                        throw new RuntimeException(e);
                    }
                }
            }
        } else {
            throw new RuntimeException(String.format("Error status code %d", statusCode));
        }
    }

}
