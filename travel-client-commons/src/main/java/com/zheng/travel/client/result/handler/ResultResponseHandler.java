package com.zheng.travel.client.result.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zheng.travel.client.result.ErrorHandler;
import com.zheng.travel.client.result.R;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * bug: (basePackages = "com.zheng")建议扫包
 * 为什么？
 * 如果你项目中没有使用Swagger，你可以扫包也可以不扫。都是正常的。
 * 但是如果你项目使用了Swagger，因为Swagger本身也是一个springmvc的项目，他里面也是一个个http请求
 * 这个请求的时候如果你项目中配置了拦截器，或者一些通知类xxxAdvice，那么就会把Swagger都会进行拦截。
 * 就会造成Swagger失效。
 */
@RestControllerAdvice(basePackages = "com.zheng")
public class ResultResponseHandler implements ResponseBodyAdvice<Object> {
    /**
     * 是否支持advice功能，true是支持 false是不支持
     *
     * @param methodParameter
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    // 参数o 代表其实就是springmvc的请求的方法的结果
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        // 对请求的结果在这里统一返回和处理
        if (o instanceof ErrorHandler) {
            // 1、如果返回的结果是一个异常的结果，就把异常返回的结构数据倒腾到R.fail里面即可
            ErrorHandler errorHandler = (ErrorHandler) o;
            return R.fail(errorHandler.getStatus(), errorHandler.getMessage());
        } else if (o instanceof String) {
            try {
                // 2、因为springmvc数据转换器对String是有特殊处理 StringHttpMessageConverter
                ObjectMapper objectMapper = new ObjectMapper();
                R r = R.success(o);
                return objectMapper.writeValueAsString(r);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return R.success(o);
    }
}