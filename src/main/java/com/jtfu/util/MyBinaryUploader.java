package com.jtfu.util;

import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.FileType;
import com.baidu.ueditor.define.State;
import com.baidu.ueditor.upload.StorageManager;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MyBinaryUploader {

    public static final State save(HttpServletRequest request, Map<String, Object> conf, MultipartFile upfile) {
        FileItemStream fileStream = null;
        boolean isAjaxUpload = request.getHeader("X_Requested_With") != null;
        if (!ServletFileUpload.isMultipartContent(request)) {
            return new BaseState(false, 5);
        } else {
            ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
            if (isAjaxUpload) {
                upload.setHeaderEncoding("UTF-8");
            }

            try {
            /*    for(FileItemIterator iterator = upload.getItemIterator(request); iterator.hasNext(); fileStream = null) {
                    fileStream = iterator.next();
                    if (!fileStream.isFormField()) {
                        break;
                    }
                }*/

                String savePath = (String)conf.get("savePath");
                String originFileName = "";
                String suffix = FileType.getSuffixByFilename(upfile.getOriginalFilename());
                //originFileName = originFileName.substring(0, originFileName.length() - suffix.length());
                savePath = savePath + suffix;
                long maxSize = (Long)conf.get("maxSize");
                if (!validType(suffix, (String[])conf.get("allowFiles"))) {
                    return new BaseState(false, 8);
                } else {
                    savePath = PathFormat.parse(savePath, originFileName);
                    String physicalPath = (String)conf.get("rootPath") + savePath;
                    InputStream is = upfile.getInputStream();
                    State storageState = StorageManager.saveFileByInputStream(is, physicalPath, maxSize);
                    is.close();
                    if (storageState.isSuccess()) {
                        storageState.putInfo("url", PathFormat.format(savePath));
                        storageState.putInfo("type", suffix);
                        storageState.putInfo("original", "");
                    }

                    return storageState;
                }
              /*  if (fileStream == null) {
                    return new BaseState(false, 7);
                } else {
                    String savePath = (String)conf.get("savePath");
                    String originFileName = fileStream.getName();
                    String suffix = FileType.getSuffixByFilename(originFileName);
                    originFileName = originFileName.substring(0, originFileName.length() - suffix.length());
                    savePath = savePath + suffix;
                    long maxSize = (Long)conf.get("maxSize");
                    if (!validType(suffix, (String[])conf.get("allowFiles"))) {
                        return new BaseState(false, 8);
                    } else {
                        savePath = PathFormat.parse(savePath, originFileName);
                        String physicalPath = (String)conf.get("rootPath") + savePath;
                        InputStream is = fileStream.openStream();
                        State storageState = StorageManager.saveFileByInputStream(is, physicalPath, maxSize);
                        is.close();
                        if (storageState.isSuccess()) {
                            storageState.putInfo("url", PathFormat.format(savePath));
                            storageState.putInfo("type", suffix);
                            storageState.putInfo("original", originFileName + suffix);
                        }

                        return storageState;
                    }
                }*/
            } catch (IOException var15) {
                return new BaseState(false, 4);
            }
        }
    }

    private static boolean validType(String type, String[] allowTypes) {
        List<String> list = Arrays.asList(allowTypes);
        return list.contains(type);
    }
}
