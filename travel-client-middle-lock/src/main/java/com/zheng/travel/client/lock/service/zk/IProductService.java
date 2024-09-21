package com.zheng.travel.client.lock.service.zk;

import com.zheng.travel.client.lock.vo.ProductVo;

public interface IProductService {
    /**
     * 不加锁的商品抢购
     * @param productVo
     */
    void purchaseProductNoLock(ProductVo productVo);
    /**
     * 使用分布式锁解决商品的抢购超卖的问题
     * @param productVo
     */
    void purchaseProductZKLock(ProductVo productVo);

    /***
     * 获取redssion锁解决商品抢购超卖的问题
     * @param productVo
     */
    void purchaseProductRedissonLock(ProductVo productVo);
}
