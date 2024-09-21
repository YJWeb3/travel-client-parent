package com.zheng.travel.client.result.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zheng.travel.client.result.ErrorHandler;
import com.zheng.travel.client.result.ResultEnum;
import com.zheng.travel.client.result.ex.TravelBusinessException;
import com.zheng.travel.client.result.ex.TravelValidationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalRestExceptionHandler {

    /**
     * 对服务器端出现500异常进行统一处理
     * 缺点：不明确
     * 场景：
     */
    @ExceptionHandler(Throwable.class)
    public ErrorHandler makeExcepton(Throwable e, HttpServletRequest request) {
        e.printStackTrace();
        ErrorHandler errorHandler = ErrorHandler.fail(ResultEnum.SERVER_ERROR, e);
        return errorHandler;
    }


    /**
     * 对服务器出现RuntimeException进行捕获
     * 缺点：不明确
     * 场景：
     */
    @ExceptionHandler(RuntimeException.class)
    public ErrorHandler makeException(RuntimeException e, HttpServletRequest request) {
        e.printStackTrace();
        ErrorHandler errorHandler = ErrorHandler.fail(ResultEnum.SERVER_ERROR, e, e.getMessage());
        return errorHandler;
    }

    /**
     * 对自定义异常的统一处理
     * 缺点：明确异常信息
     */
    @ExceptionHandler(TravelValidationException.class)
    public ErrorHandler makeValidationException(TravelValidationException e, HttpServletRequest request) {
        e.printStackTrace();
        ErrorHandler errorHandler = new ErrorHandler();
        errorHandler.setMessage(e.getMsg());
        errorHandler.setStatus(e.getCode());
        return errorHandler;
    }

    /**
     * 对服务器端出现500异常进行统一处理
     * 缺点：明确异常信息
     */
    @ExceptionHandler(TravelBusinessException.class)
    public ErrorHandler makeOrderException(TravelBusinessException e, HttpServletRequest request) {
        e.printStackTrace();
        ErrorHandler errorHandler = new ErrorHandler();
        errorHandler.setMessage(e.getMsg());
        errorHandler.setStatus(e.getCode());
        return errorHandler;
    }


    /**
     * 对验证的统一异常进行统一处理
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorHandler handlerValidator(MethodArgumentNotValidException e, HttpServletRequest request) throws JsonProcessingException {
        e.printStackTrace();
        // 1: 从MethodArgumentNotValidException提取验证失败的所有的信息。返回一个List<FieldError>
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        // 2: 把fieldErrors中，需要的部分提出出来进行返回
        Map<String, Object> map = getValidatorErrors(e.getBindingResult());
        // 3: 把需要的异常信息转换成json进行返回
        ErrorHandler errorHandler = ErrorHandler.fail(ResultEnum.SERVER_ERROR, e, map);
        return errorHandler;
    }

    /**
     * 对验证异常进行统一处理提取需要的部分
     *
     * @param bindingResult
     * @return
     */
    private Map<String, Object> getValidatorErrors(BindingResult bindingResult) {
        Map<String, Object> mapList = new HashMap<>();
        // 循环提取
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            mapList.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return mapList;
    }


    /**
     * 对验证异常进行统一处理提取需要的部分
     *
     * @param fieldErrorList
     * @return
     */
    private List<Map<String, String>> toValidatorMsg(List<FieldError> fieldErrorList) {
        List<Map<String, String>> mapList = new ArrayList<>();
        // 循环提取
        for (FieldError fieldError : fieldErrorList) {
            Map<String, String> map = new HashMap<>();
            // 获取验证失败的属性
            map.put("field", fieldError.getField());
            // 获取验证失败的的提示信息
            map.put("msg", fieldError.getDefaultMessage());

            mapList.add(map);
        }
        return mapList;
    }


}