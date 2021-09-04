package com.swust.system.controller.admin;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping(value = "/admin/kaptcha")
public class KaptchaController {

    private final static Logger LOG = LoggerFactory.getLogger(KaptchaController.class);

    public static final String BUSINESS_NAME = "图片验证码";

    // 注入kaptcha属性
    @Qualifier("getDefaultKaptcha")
    @Autowired
    DefaultKaptcha defaultKaptcha;

    @Resource
    public RedisTemplate redisTemplate;

//    http://localhost:9000/system/admin/kaptcha/imagecode/11

    @GetMapping(value = "/imagecode/{imageCodeToken}")
    public void imageCode(
            @PathVariable(value = "imageCodeToken") String imageCodeToken,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            // 生成验证码字符串
            String kaptchaText = defaultKaptcha.createText();
            // 将生成的验证码放入会话缓存
            LOG.info("sessionId is " + request.getSession().getId());
            request.getSession().setAttribute(imageCodeToken,kaptchaText);
            redisTemplate.opsForValue().set(imageCodeToken,kaptchaText,300, TimeUnit.SECONDS);

            // 使用验证码字符串生成验证码图片
            BufferedImage image = defaultKaptcha.createImage(kaptchaText);
            ImageIO.write(image,"jpg",outputStream);

        }catch (Exception e){
            LOG.error("请求出错 ==>{}",e.getMessage());
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 使用response输出流输出图片信息
        byte[] captchaAsJpeg = outputStream.toByteArray();
        response.setHeader("Cache-Control","no-store");
        response.setHeader("Pragma","no-cache");
        response.setDateHeader("Expires",0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(captchaAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
