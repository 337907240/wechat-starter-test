package com.herui.wechatstarttest.controller;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import me.chanjar.weixin.cp.bean.WxCpMessageSendResult;
import me.chanjar.weixin.cp.bean.WxCpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TestController {
    @Autowired
    private WxCpService wxCpService;

    @RequestMapping("test")
    public String test() {
        if (simpleNotifyWx("HeRui", "wechat-start-test-message")) {
            return "send success";
        } else {
            return "send failed";
        }
    }

    /**
     * 微信通知
     *
     * @param wxId    企业微信id
     * @param content 内容
     */
    private Boolean simpleNotifyWx(String wxId, String content) {
        if (StringUtils.isEmpty(wxId)) {
            return false;
        }
        WxCpMessage message = new WxCpMessage();
        message.setToUser(wxId);
        message.setMsgType("text");
        message.setContent(content);
        try {
            WxCpUser wxCpUser = wxCpService.getUserService().getById(wxId);
            if (wxCpUser == null) {
                return false;
            }
            WxCpMessageSendResult result = wxCpService.messageSend(message);
            if (StringUtils.isNotEmpty(result.getInvalidUser())) {
                System.out.println("WxErrorException: 用户信息错误");
                return false;
            }
            System.out.println(result);
            return true;
        } catch (WxErrorException e) {
            System.out.println("WxErrorException:" + e.getError().getJson());
            return false;
        }
    }
}
