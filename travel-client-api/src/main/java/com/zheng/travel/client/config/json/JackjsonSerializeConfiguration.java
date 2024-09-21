package com.zheng.travel.client.config.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.SimpleTimeZone;

@JsonComponent
public class JackjsonSerializeConfiguration {

    @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        //忽略value为null 时 key的输出
        // JsonInclude.Include.ALWAYS 所以属性都必须在
        // JsonInclude.Include.NON_NULL 如果你属性是null，就会自动剔除
        // JsonInclude.Include.NON_EMPTY 如果你属性是""，就会自动剔除
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.setTimeZone(SimpleTimeZone.getDefault());
        objectMapper.setLocale(new Locale("zh_CN"));
        SimpleModule module = new SimpleModule();
        // 对long单独处理
        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(module);
        return objectMapper;
    }
}

