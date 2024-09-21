package com.zheng.travel.client.config.middle.redis;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

@Configuration
public class LuaConfiguration {


    /**
     * 将lua脚本的内容加载出来放入到DefaultRedisScript
     * @return
     */
    @Bean
    public DefaultRedisScript<Boolean> ipLimitLua() {
        DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/iplimiter.lua")));
        defaultRedisScript.setResultType(Boolean.class);
        return defaultRedisScript;
    }

    /**
     * 将lua脚本的内容加载出来放入到DefaultRedisScript
     * @return
     */
    @Bean
    public DefaultRedisScript<Boolean> ipLimiterLuaScript() {
        DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/iplimiter2.lua")));
        defaultRedisScript.setResultType(Boolean.class);
        return defaultRedisScript;
    }

}