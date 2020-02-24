package com.jtfu.util;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class Test {
    public static void main(String[] args) {

        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        /*for(FileItemIterator iterator = upload.getItemIterator(request); iterator.hasNext(); fileStream = null) {
            fileStream = iterator.next();
            if (!fileStream.isFormField()) {
                break;
            }
        }*/
    }
}
