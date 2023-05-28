package cn.td.aiot.common.utils;

import cn.td.aiot.common.domain.TiduConstant;
import cn.td.aiot.common.exception.RedisConnectException;
import cn.td.aiot.common.service.RedisService;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

/**
 * 验证码工具类，重写 {@link com.wf.captcha.utils.CaptchaUtil}
 * 因为它没有提供修改验证码类型方法
 */
@Slf4j
public class CaptchaUtil {


    private static final RedisService redisService = SpringContextUtil.getBean(RedisService.class);

    /**
     * GIF 格式验证码类型
     */
    private static final int GIF_TYPE = 1;
    /**
     * PNG格式验证码类型
     */
    private static final int PNG_TYPE = 0;

    /**
     * 验证码图片默认高度
     */
    private static final int DEFAULT_HEIGHT = 48;
    /**
     * 验证码图片默认宽度
     */
    private static final int DEFAULT_WIDTH = 130;
    /**
     * 验证码图片默认位数
     */
    private static final int DEFAULT_LEN = 5;


    /**
     * @param width     验证码宽度
     * @param height    验证码高度
     * @param len       验证码的位数
     * @param vType     验证码的类型
     * @param timestamp 生成验证码的时间戳
     * @throws IOException IO异常
     * @author YeYouGui
     */
    public static void outPng(int width, int height, int len, Integer vType, String timestamp, HttpServletResponse response) throws IOException {
        outCaptcha(width, height, len, null, PNG_TYPE, vType, timestamp, response);
    }

    public static boolean verify(String code, String timestamp) {
        String key = TiduConstant.CODE_PREFIX + timestamp;

        String redisCode = "";
        try {
            redisCode = redisService.get(key);
        } catch (RedisConnectException e) {
            log.error("验证码获取错误");
        }
        return StringUtils.equalsIgnoreCase(code, redisCode);
    }

    /**
     * 生成验证码
     *
     * @param width     验证码宽度
     * @param height    验证码高度
     * @param len       验证码的位数
     * @param vType     验证码的类型
     * @param font      验证码的字体
     * @param timestamp 生成验证码的时间戳
     * @throws IOException IO异常
     * @author YeYouGui
     */
    private static void outCaptcha(int width, int height, int len, Font font, int cType, Integer vType, String timestamp, HttpServletResponse response) throws IOException {
        setHeader(response, cType);
        Captcha captcha = null;
        if (cType == GIF_TYPE) {
            captcha = new GifCaptcha(width, height, len);
        } else {
            captcha = new SpecCaptcha(width, height, len);
        }
        if (font != null) {
            captcha.setFont(font);
        }
        if (vType != null) {
            captcha.setCharType(vType);
        }
        String code = captcha.text().toLowerCase();
        String key = TiduConstant.CODE_PREFIX + timestamp;
        try {
            redisService.set(key, code, 60000L);
        } catch (RedisConnectException e) {
            log.error("保存验证码异常", e);
        }

        captcha.out(response.getOutputStream());
    }

    public static void setHeader(HttpServletResponse response, int cType) {
        if (cType == GIF_TYPE) {
            response.setContentType(MediaType.IMAGE_GIF_VALUE);
        } else {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
        }

        response.setHeader(HttpHeaders.PRAGMA, "No-cache");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "No-cache");
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);
    }

}
