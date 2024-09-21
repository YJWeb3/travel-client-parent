package com.zheng.travel.client.dao;

import com.zheng.travel.client.entity.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserOrderDao extends JpaRepository<UserOrder,Long> {


//    @Query("update UserOrder set status = -1 where  id = :id")
//    void updateOrderStatus(@Param("id")Long  id);

}
