package com.zheng.travel.client.service;

import com.zheng.travel.client.dto.UserOrderDto;

public interface IUserOrderService {
    boolean updateUserOrderStatus(UserOrderDto userOrderDto);
}
