package org.checkin.web.vo;

import java.util.List;
import java.util.Map;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2014-12-21
 */
public class FileUploadVo {
    List<FileVo> result;
    List<Long> photoIds;
    Map<String, String> requestHeaders;

    public List<FileVo> getResult() {
        return result;
    }

    public void setResult(List<FileVo> result) {
        this.result = result;
    }

    public List<Long> getPhotoIds() {
        return photoIds;
    }

    public void setPhotoIds(List<Long> photoIds) {
        this.photoIds = photoIds;
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(Map<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }
}
