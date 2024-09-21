package com.zheng.travel.client.service.advice;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zheng.travel.client.entity.Advice;
import com.zheng.travel.client.mapper.advice.AdviceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdviceServiceImpl extends ServiceImpl<AdviceMapper, Advice> implements IAdviceService {

    /**
     * 查询公告
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public List<Advice> findIndexAdvice(Integer pageNo, Integer pageSize) {
        // 1: 设置分页
        Page<Advice> page = new Page<>(pageNo, pageSize);
        // 2 :设置条件
        LambdaQueryWrapper<Advice> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Advice::getStatus, 1);
        lambdaQueryWrapper.orderByAsc(Advice::getSorted);

        Page<Advice> advicePage = this.page(page, lambdaQueryWrapper);
        List<Advice> records = advicePage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new ArrayList<>();
        }
        return records;
    }

}
