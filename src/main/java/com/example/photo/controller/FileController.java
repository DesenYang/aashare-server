package com.example.photo.controller;

import com.example.photo.base.RestResult;
import com.example.photo.utils.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

@RestController
@RequestMapping("/file")
public class FileController {
    /**
     * 下载图片
     * @param response
     * @param type
     * @param id
     * @param name
     * @return
     */
    @GetMapping(value = "/download/{type}/{id}/{name}")
    RestResult<String> download(
            HttpServletResponse response,
            @PathVariable String type,
            @PathVariable Long id,
            @PathVariable String name
    ) {
        try{
            File file = FileUtils.getFile(type + "/" + id + "/" + name);
            if (!file.exists()) {
                return RestResult.notFound();
            }
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Type","application/stream");
            response.addHeader("Content-Disposition","attachment;fileName=" + file.getName());
            response.addHeader("Content-Length",String.valueOf(file.length()));
            if (FileUtils.WriteTo(new FileInputStream(file),response.getOutputStream())) {
                return null;
            }else {
                return RestResult.failed();
            }
        }catch (Exception e) {
            e.printStackTrace();
            return RestResult.failed(e.getMessage());
        }
    }

    /**
     * 读取图片
     * @param response
     * @param type
     * @param id
     * @param name
     * @return
     */
    @GetMapping(value = "/image/{type}/{id}/{name}",produces = MediaType.ALL_VALUE)
    @ResponseBody
    BufferedImage image(
            HttpServletResponse response,
            @PathVariable String type,
            @PathVariable Long id,
            @PathVariable String name
    ) {
        File file = FileUtils.getFile(type + "/" + id + "/" + name);
        if (!file.exists()) {
            return null;
        }
        try {
            return ImageIO.read(new FileInputStream(file));
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
