package com.zheng.travel.client.mapper.userorder;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zheng.travel.client.entity.UserOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserOrderMapper extends BaseMapper<UserOrder> {

    void updateNoPayOrderStatus();

    /**
     * 查询订单状态的统计信息
     * @return
     */
    List<Map<String,Object>> selectOrderStatusInfo(@Param("userId")Long userId);
}
