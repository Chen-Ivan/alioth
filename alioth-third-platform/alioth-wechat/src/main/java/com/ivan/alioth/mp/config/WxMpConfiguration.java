package com.ivan.alioth.mp.config;


import com.ivan.alioth.cache.redis.RedisUtil;
import com.ivan.alioth.mp.handler.*;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.redis.JedisWxRedisOps;
import me.chanjar.weixin.common.redis.RedisTemplateWxRedisOps;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import me.chanjar.weixin.mp.config.impl.WxMpRedisConfigImpl;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.stream.Collectors;

import static me.chanjar.weixin.common.api.WxConsts.EventType;
import static me.chanjar.weixin.common.api.WxConsts.EventType.SUBSCRIBE;
import static me.chanjar.weixin.common.api.WxConsts.EventType.UNSUBSCRIBE;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.EVENT;
import static me.chanjar.weixin.mp.constant.WxMpEventConstants.CustomerService.*;
import static me.chanjar.weixin.mp.constant.WxMpEventConstants.POI_CHECK_NOTIFY;

/**
 * wechat mp configuration
 *
 * @author yifan
 */
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties(WxMpProperties.class)
public class WxMpConfiguration {
    private final LogHandler logHandler;
    private final NullHandler nullHandler;
    private final KfSessionHandler kfSessionHandler;
    private final StoreCheckNotifyHandler storeCheckNotifyHandler;
    private final LocationHandler locationHandler;
    private final MenuHandler menuHandler;
    private final MsgHandler msgHandler;
    private final UnsubscribeHandler unsubscribeHandler;
    private final SubscribeHandler subscribeHandler;
    private final ScanHandler scanHandler;
    private final WxMpProperties properties;
    private final StringRedisTemplate redisTemplate;


    @Bean
    public WxMpService wxMpService() {
        // ????????? getConfigs()???????????????????????????????????????????????????????????????IDE????????????lombok??????????????????
        final List<WxMpProperties.MpConfig> configs = this.properties.getConfigs();
        if (configs == null) {
            throw new RuntimeException("????????????????????????????????????????????????readme?????????????????????????????????????????????????????????");
        }

        WxMpService service = new WxMpServiceImpl();
        service.setMultiConfigStorages(configs
            .stream().map(wxConfig -> {
                WxMpDefaultConfigImpl configStorage;
                if (this.properties.isUseRedis()) {
                    configStorage = new WxMpRedisConfigImpl(new RedisTemplateWxRedisOps(redisTemplate), wxConfig.getAppId());
                } else {
                    configStorage = new WxMpDefaultConfigImpl();
                }

                configStorage.setAppId(wxConfig.getAppId());
                configStorage.setSecret(wxConfig.getSecret());
                configStorage.setToken(wxConfig.getToken());
                configStorage.setAesKey(wxConfig.getAesKey());
                return configStorage;
            }).collect(Collectors.toMap(WxMpDefaultConfigImpl::getAppId, wxConfig -> wxConfig, (o, n) -> o)));
        return service;
    }

    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

        // ??????????????????????????? ??????????????????
        newRouter.rule().handler(this.logHandler).next();

        // ??????????????????????????????
        newRouter.rule().async(false).msgType(EVENT).event(KF_CREATE_SESSION)
            .handler(this.kfSessionHandler).end();
        newRouter.rule().async(false).msgType(EVENT).event(KF_CLOSE_SESSION)
            .handler(this.kfSessionHandler).end();
        newRouter.rule().async(false).msgType(EVENT).event(KF_SWITCH_SESSION)
            .handler(this.kfSessionHandler).end();

        // ??????????????????
        newRouter.rule().async(false).msgType(EVENT).event(POI_CHECK_NOTIFY).handler(this.storeCheckNotifyHandler).end();

        // ?????????????????????
        newRouter.rule().async(false).msgType(EVENT).event(EventType.CLICK).handler(this.menuHandler).end();

        // ????????????????????????
        newRouter.rule().async(false).msgType(EVENT).event(EventType.VIEW).handler(this.nullHandler).end();

        // ????????????
        newRouter.rule().async(false).msgType(EVENT).event(SUBSCRIBE).handler(this.subscribeHandler).end();

        // ??????????????????
        newRouter.rule().async(false).msgType(EVENT).event(UNSUBSCRIBE).handler(this.unsubscribeHandler).end();

        // ????????????????????????
        newRouter.rule().async(false).msgType(EVENT).event(EventType.LOCATION).handler(this.locationHandler).end();

        // ????????????????????????
        newRouter.rule().async(false).msgType(XmlMsgType.LOCATION).handler(this.locationHandler).end();

        // ????????????
        newRouter.rule().async(false).msgType(EVENT).event(EventType.SCAN).handler(this.scanHandler).end();

        // ??????
        newRouter.rule().async(false).handler(this.msgHandler).end();

        return newRouter;
    }

}
