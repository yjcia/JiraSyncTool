package com.kewill.jira.util;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * Created by YanJun on 2016/7/11.
 */
public class ServletUtil {

    public static void doDownload(File file, HttpServletResponse response)
            throws IOException {
        if (file.exists()) {
            String filename = URLEncoder.encode(file.getName(), "utf-8");
            response.reset();
            response.setContentType("application/x-msdowmload");
            response.addHeader("Content-Disposition", "attachment;filename="+filename);
            FileInputStream  fis = new FileInputStream(file);
            byte[] byteArr = new byte[fis.available()];
            fis.read(byteArr);
            ServletOutputStream out = response.getOutputStream();
            out.write(byteArr);
            out.flush();
            out.close();
        }
    }
}
