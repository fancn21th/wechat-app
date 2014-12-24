package org.checkin.web.controller;

import org.checkin.repository.repository.Tables;
import org.checkin.repository.repository.tables.daos.CheckinPhotoDao;
import org.checkin.repository.repository.tables.pojos.CheckinPhoto;
import org.checkin.repository.repository.tables.records.CheckinPhotoRecord;
import org.checkin.web.vo.FileUploadVo;
import org.checkin.web.vo.FileVo;
import org.apache.commons.io.IOUtils;
import org.dozer.Mapper;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * File upload.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2014-12-21
 */
@Controller
@RequestMapping("/checkin")
@PropertySources({
        @PropertySource("classpath:/file.properties")
})
public class FileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    @Autowired
    Environment env;

    @Autowired
    CheckinPhotoDao photoDao;

    @Autowired
    DSLContext dsl;

    @Autowired
    Mapper mapper;

    @RequestMapping(value = "/photos/{photoId}", method = RequestMethod.GET)
    @ResponseBody
    public void getPhoto(@PathVariable Long photoId, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        CheckinPhoto photo = photoDao.fetchOneById(photoId);
        String location = env.getRequiredProperty("file.location");
        String uploadRealPath = request.getSession().getServletContext().getRealPath(location);
        File file = new File(uploadRealPath, photo.getStorageName());
        if (file != null) {
            BufferedInputStream inputStream = null;
            ServletOutputStream outputStream = null;
            try {
                response.setContentType(photo.getType());
                response.setContentLength(photo.getSize());
                response.setHeader("content-Disposition", "attachment; filename=" + photo.getLabel());
                // copy it to response's OutputStream
                inputStream = new BufferedInputStream(new FileInputStream(file));
                outputStream = response.getOutputStream();
                IOUtils.copyLarge(inputStream, outputStream);
            } catch (IOException ex) {
                LOGGER.info("Error writing file to output stream. Filename was '" + photo.getLabel() + "'");
                throw new RuntimeException("IOError writing file to output stream");
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public FileUploadVo multipleSave(@RequestParam("file") MultipartFile[] files
            , HttpServletRequest request) {
        String location = env.getRequiredProperty("file.location");
        Integer bufferSize = env.getRequiredProperty("file.bufferSize", Integer.class);
        String uploadRealPath = request.getSession().getServletContext().getRealPath(location);
        File uploadFolder = new File(uploadRealPath);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdir();
        }
        String fileName;
        String msg = "";
        FileUploadVo fileUploadVo = new FileUploadVo();
        Map<String, String> requestHeaders = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            requestHeaders.put(header, request.getHeader(header));
        }
        fileUploadVo.setRequestHeaders(requestHeaders);
        List<FileVo> fileVos = new ArrayList<>();
        List<CheckinPhoto> photos = new ArrayList<>();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                BufferedOutputStream outputStream = null;
                InputStream inputStream = null;
                FileVo fileVo = new FileVo();
                CheckinPhoto photo = new CheckinPhoto();
                try {
                    fileName = files[i].getOriginalFilename();
                    fileVo.setFieldName(fileName);
                    fileVo.setName(fileName);
                    photo.setLabel(fileName);
                    photo.setType(files[i].getContentType());
                    String uploadFileName = getUploadFileName(fileName);
                    photo.setStorageName(uploadFileName);
                    photo.setFitnessCheckinId(-1L);
                    inputStream = new BufferedInputStream(files[i].getInputStream());
                    outputStream =
                            new BufferedOutputStream(new FileOutputStream(new File(uploadRealPath, uploadFileName)));
                    int size = read(inputStream, outputStream, bufferSize);
                    fileVo.setSize(size);
                    photo.setSize(size);
                    msg += "You have successfully uploaded " + fileName;
                    LOGGER.info(msg);
                    fileVos.add(fileVo);
                    photos.add(photo);
                } catch (Exception e) {
                    LOGGER.error("", e);
                    throw new RuntimeException(e);
                } finally {
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            LOGGER.error("", e);
                            throw new RuntimeException(e);
                        }
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            LOGGER.error("", e);
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            fileUploadVo.setResult(fileVos);
            List<Long> ids = savePhotos(photos);
            fileUploadVo.setPhotoIds(ids);
            return fileUploadVo;
        } else {
            msg = "Unable to upload. File is empty.";
            LOGGER.warn(msg);
            throw new RuntimeException(msg);
        }
    }

    private List<Long> savePhotos(List<CheckinPhoto> photos) {
        List<Long> ids = new ArrayList<>(photos.size());
        for (CheckinPhoto photo : photos) {
            CheckinPhotoRecord photoRecord = dsl.newRecord(Tables.CHECKIN_PHOTO);
            photoRecord.setFitnessCheckinId(photo.getFitnessCheckinId());
            photoRecord.setLabel(photo.getLabel());
            photoRecord.setStorageName(photo.getStorageName());
            photoRecord.setType(photo.getType());
            photoRecord.setSize(photo.getSize());
            photoRecord.store();
            photo.setId(photoRecord.getId());
            ids.add(photo.getId());
        }
        return ids;
    }

    private String getUploadFileName(String fileName) {
        String ts = String.valueOf(System.currentTimeMillis());
        return ts + "_" + fileName;
    }

    private int read(InputStream in, OutputStream out, Integer bufferSize) {
        int length = 0;
        try {
            byte[] buffer = new byte[bufferSize];
            int size;
            while ((size = in.read(buffer)) != -1) {
                out.write(buffer, 0, size);
                out.flush();
                length += size;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return length;
    }
}
