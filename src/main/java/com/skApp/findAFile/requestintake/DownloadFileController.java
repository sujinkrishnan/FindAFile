package com.skApp.findAFile.requestintake;

import com.skApp.findAFile.shared.properties.LuceneProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Controller
public class DownloadFileController {

    private final LuceneProperties luceneProperties;

    public DownloadFileController(LuceneProperties luceneProperties) {
        this.luceneProperties = luceneProperties;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/download")
    public void download(@RequestParam("downloadBtn") String fileName, HttpServletResponse response)
            throws ServletException, IOException {

        log.info("Going to download file " + fileName);

        if (fileName != null) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            FileInputStream fileInputStream = new FileInputStream(
                    luceneProperties.getUploaderDirectoryPath() + "\\" + fileName);

            int i;
            while ((i = fileInputStream.read()) != -1) {
                out.write(i);
            }

            fileInputStream.close();
            out.close();
        }
    }

}
