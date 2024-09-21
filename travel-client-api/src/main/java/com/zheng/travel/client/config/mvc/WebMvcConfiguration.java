package com.zheng.travel.client.config.mvc;

import com.zheng.travel.client.config.mvc.interceptor.PassportInterceptor;
import com.zheng.travel.client.config.mvc.interceptor.PassportTokenInterceptor;
import com.zheng.travel.client.config.mvc.interceptor.RefererInterceptor;
import com.zheng.travel.client.config.mvc.interceptor.RepeatSubmitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    /**
     * 请求限流拦截
     */
    @Autowired
    public PassportTokenInterceptor passportTokenInterceptor;

    /**
     * 表单重复提交
     */
    @Autowired
    private RepeatSubmitInterceptor repeatSubmitInterceptor;

    @Autowired
    private RefererInterceptor refererInterceptor;

    @Autowired
    private PassportInterceptor passportInterceptor;

    /**
     * 注册拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 验证码拦截
        registry.addInterceptor(passportInterceptor).addPathPatterns("/api/passport/smssend/**");        //registry.addInterceptor(refererInterceptor).addPathPatterns("/api/**");
        // token 拦截
        registry.addInterceptor(passportTokenInterceptor)
                // 统一拦截需要鉴权的接口
                .addPathPatterns("/api/**")
                // 把登录，退出，短信发送进行排除
                // 特别注意: 支付的回调地址一定不能进行拦截。
                .excludePathPatterns("/api/passport/**","/api/pay/wechat-pay/notifyUrl");

    }

    /**
     * 解决跨域问题
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                // 允许跨域的域名
                .allowedOriginPatterns("*") // 允许所有域
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedHeaders("*") // 允许任何请求头
                .allowCredentials(true) // 允许证书、cookie
                .maxAge(3600L); // maxAge(3600)表明在3600秒内，不需要再发送预检验请求，可以缓存该结果
    }
}
