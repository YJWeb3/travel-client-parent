package com.zheng.travel.client.service.advice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zheng.travel.client.entity.Advice;

import java.util.List;


public interface IAdviceService extends IService<Advice> {

    /**
     * 查询公告
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<Advice> findIndexAdvice(Integer pageNo,Integer pageSize);
}
