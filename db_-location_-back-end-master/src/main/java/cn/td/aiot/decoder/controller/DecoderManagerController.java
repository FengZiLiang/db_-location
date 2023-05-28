package cn.td.aiot.decoder.controller;

import cn.td.aiot.common.exception.TdException;
import cn.td.aiot.decoder.vo.DecoderVo;
import cn.td.aiot.decoder.service.DecoderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 解码器controller.
 */
@Validated
@RestController
@RequestMapping("decoder/manager")
@Slf4j
public class DecoderManagerController {

    @Autowired
    DecoderService decoderService;
    /**
     * 停止解码.
     * @return 结果
     */
    @RequiresPermissions("camera:view")
    @RequestMapping(value = "transcode/{id}", method = RequestMethod.DELETE)
    public String stopTranscode(@PathVariable(value = "id")String id) throws TdException {
        boolean res = false;
        log.info(id);

        if (StringUtils.isNotBlank(id)) {
            //停止一个
            res = decoderService.stopTranscoding(id);
        }else {
            //停止全部
            res = decoderService.stopTranscoding();
        }
        if (res) {
            return "停止解码成功";
        } else {
            throw new TdException("停止解码失败");
        }
    }

    /**
     * 开始解码.
     * @param decoderVo 摄像头信息
     * @return 结果
     */
    @RequiresPermissions("camera:view")
    @RequestMapping(value = "transcode", method = RequestMethod.POST)
    public String transcode(@RequestBody DecoderVo decoderVo) throws TdException, InterruptedException, IOException, TimeoutException {
        String id = decoderService.startTranscoding(decoderVo);
        if (StringUtils.isNotBlank(id)) {
            //前端获取实际的地址+ID
            return id;
        } else {
            throw new TdException("解码失败");
        }
    }
    @RequestMapping(value = "hello", method = RequestMethod.GET)
    public String hello(HttpServletRequest httpServletRequest){
        return httpServletRequest.getRequestURL().toString();

    }


}
