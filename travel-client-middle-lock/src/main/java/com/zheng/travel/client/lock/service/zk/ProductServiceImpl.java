package com.zheng.travel.client.lock.service.zk;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zheng.travel.client.lock.model.ProductRecords;
import com.zheng.travel.client.lock.model.ProductStock;
import com.zheng.travel.client.lock.queue.QueuePublisher;
import com.zheng.travel.client.lock.vo.ProductVo;
import com.zheng.travel.client.lock.mapper.ProductRecordsMapper;
import com.zheng.travel.client.lock.mapper.ProductStockMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductStockMapper productStockMapper;
    @Autowired
    private ProductRecordsMapper productRecordsMapper;
    @Autowired
    private QueuePublisher queuePublisher;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void purchaseProductNoLock(ProductVo productVo) {

        // 统计每个用户是否已经抢购该商品了
        Long total = this.countByProductNoUserId(productVo.getUserId(), productVo.getProductNo());
        if (null != total && total > 0) {
            throw new RuntimeException("你已经抢过了!!!");
        }

        // 根据商品编号查询商品库存的信息
        ProductStock productStock = this.selectProductStock(productVo.getProductNo());
        // 判断当前用户是否已经抢购和库存是否充足
        if (null != productStock && productStock.getStock() > 0 && total <= 0) {
            log.info("-----当前商品编号{} ,库存是：{}，抢购用户是：{}", productStock.getProductNo(), productStock.getStock(), productVo.getUserId());
            // 抢购成功，进行更新库存
            // 同时把抢购的商品和用户记录保存到ProductRecords
            int updateResponse = this.updateProductStock(productVo.getProductNo());
            // 更新库存成功，需要添加用户的抢购记录,防止用户重复抢购
            if (updateResponse > 0) {
                // 创建抢购记录用户实体对象
                ProductRecords productRecords = new ProductRecords();
                productRecords.setProductNo(productVo.getProductNo());
                productRecords.setUserId(productVo.getUserId());
                productRecords.setCreateTime(new Date());
                // 保存用户抢购记录
                productRecordsMapper.insert(productRecords);
            }
        } else {
            log.info("-----当前商品编号{} ,库存是：{}，库存不足", productStock.getProductNo(), productStock.getStock(), productVo.getUserId());
            throw new RuntimeException("商品库存不足!!!");
        }
    }


    @Autowired
    private CuratorFramework client;

    // 临时节点 后面这个/记得加上去. 和下面商品编号和用户链接在一起了
    // /TRAVEL_middle_lock/middleware/zklock1000101-lock
    // /TRAVEL_middle_lock/middleware/zklock/1000101-lock000001
    private static final String PRODUCT_TEMP_PATH = "/middleware/zklock/";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void purchaseProductZKLock(ProductVo productVo) {
        // 创建zookeper的互斥锁，A /TRAVEL_middle_lock/middleware/zklock/1000101-lock t1 t2 t3 --- 30s
        // 创建zookeper的互斥锁，B /TRAVEL_middle_lock/middleware/zklock/1000102-lock t1 t2 t3 --- 30s
        InterProcessMutex mutex = new InterProcessMutex(client, PRODUCT_TEMP_PATH
                + productVo.getProductNo() + productVo.getUserId() + "-lock");
        try {
            //A t1 t2 t3 ---排队取号
            //B t1 t2 t3 ---排队取号 10 500
            if (mutex.acquire(30, TimeUnit.SECONDS)) {
                Long total = this.countByProductNoUserId(productVo.getUserId(), productVo.getProductNo());
                if (null != total && total > 0) {
                    throw new RuntimeException("你已经抢过了!!!");
                }

                // 根据商品编号查询商品库存的信息
                ProductStock productStock = this.selectProductStock(productVo.getProductNo());
                // 判断当前用户是否已经抢购和库存是否充足
                if (null != productStock && productStock.getStock() > 0 && total <= 0) {
                    log.info("-----当前商品编号{} ,库存是：{}，抢购用户是：{}", productStock.getProductNo(), productStock.getStock(), productVo.getUserId());
                    // 抢购成功，进行更新库存
                    // 同时把抢购的商品和用户记录保存到ProductRecords
                    int updateResponse = this.updateProductStock(productVo.getProductNo());
                    // 更新库存成功，需要添加用户的抢购记录,防止用户重复抢购
                    if (updateResponse > 0) {
                        // 创建抢购记录用户实体对象
                        ProductRecords productRecords = new ProductRecords();
                        productRecords.setProductNo(productVo.getProductNo());
                        productRecords.setUserId(productVo.getUserId());
                        productRecords.setCreateTime(new Date());
                        // 保存用户抢购记录
                        productRecordsMapper.insert(productRecords);
                    }

                    // 下单发消息 MQ / redis queue /delay queue--webscoket/轮询

                } else {
                    log.info("-----当前商品编号{} ,库存是：{}，库存不足", productStock.getProductNo(), productStock.getStock(), productVo.getUserId());
                    throw new RuntimeException("商品库存不足!!!");
                }
            } else {
                log.info("zookeeper 获取锁失败!!!，如果进来的了，说明就不具备可重入性");
                throw new RuntimeException("zookeeper 获取锁失败!!!");
            }
        } catch (Exception ex) {
            throw new RuntimeException("抢购失败..." + ex.getMessage());
        } finally {
            try {
                //进行锁释放
                mutex.release();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    @Autowired
    private RedissonClient redissonClient;

    @Override
    @Transactional(rollbackFor = Exception.class)//aop
    public void purchaseProductRedissonLock(ProductVo productVo) {
        String key = "redislock_ " + productVo.getProductNo() + productVo.getUserId();
        RLock lock = redissonClient.getLock(key);
        try {
            // param1 :100 是阻塞线程的执行时间上限
            // param2 :10 当前线程的执行时间
            // param3 : 执行时间单位
            // A t1 t2 t3 --timertask--定时器 ---重入性---自旋锁
            // 重入性 判断某个线程获取锁成功的 tryLock=true 并且这个线程还在waitTime时间内的线程继续处理。 处理是时间最大leaseTime
            // while(true) t2==true
            boolean tryLock = lock.tryLock(100, 10L, TimeUnit.SECONDS);
            log.info("1--------->用户尝试获取锁:{}", tryLock);
            if (tryLock) {
                Long total = this.countByProductNoUserId(productVo.getUserId(), productVo.getProductNo());
                if (null != total && total > 0) {
                    throw new RuntimeException("你已经抢过了!!!");
                }
                // 根据商品编号查询商品库存的信息
                ProductStock productStock = this.selectProductStock(productVo.getProductNo());
                // 判断当前用户是否已经抢购和库存是否充足
                if (null != productStock && productStock.getStock() > 0 && total <= 0) {
                    log.info("2-----当前商品编号{} ,库存是：{}，抢购用户是：{}", productStock.getProductNo(), productStock.getStock(), productVo.getUserId());
                    // 抢购成功，进行更新库存
                    // 同时把抢购的商品和用户记录保存到ProductRecords
                    int updateResponse = this.updateProductStock(productVo.getProductNo());
                    // 更新库存成功，需要添加用户的抢购记录,防止用户重复抢购
                    if (updateResponse > 0) {
                        // 创建抢购记录用户实体对象
                        ProductRecords productRecords = new ProductRecords();
                        productRecords.setProductNo(productVo.getProductNo());
                        productRecords.setUserId(productVo.getUserId());
                        productRecords.setCreateTime(new Date());
                        // 保存用户抢购记录
                        productRecordsMapper.insert(productRecords);
                        try {
                            // 发送消息
                            queuePublisher.sendMessage("用户：" + productVo.getUserId() + ",抢购商品是：" + productVo.getProductNo());
                        }catch (Exception ex){
                            log.error("发送消息失败");
                        }
                    }
                } else {
                    log.info("-----当前商品编号{} ,库存是：{}，库存不足", productStock.getProductNo(), productStock.getStock(), productVo.getUserId());
                    throw new RuntimeException("商品库存不足!!!");
                }
            } else {
                log.info("redisson 获取锁失败!!!，如果进来的了");
                throw new RuntimeException("redisson 获取锁失败!!!");
            }

        } catch (Exception ex) {
            throw new RuntimeException("抢购失败..." + ex.getMessage());
        } finally {
            try {
                log.info("3------------redisson锁释放了");
                //进行锁释放
                if (lock != null) {
                    lock.unlock();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    /**
     * 根据商品编号查询商品的库存信息
     *
     * @param productNo
     * @return
     */
    private ProductStock selectProductStock(String productNo) {
        LambdaQueryWrapper<ProductStock> productStockLambdaQueryWrapper
                = new LambdaQueryWrapper<>();
        productStockLambdaQueryWrapper.eq(ProductStock::getProductNo, productNo);
        //1：根据商品编号查询库存信息
        ProductStock productStock = productStockMapper.selectOne(productStockLambdaQueryWrapper);
        return productStock;
    }

    /**
     * 根据商品编号和抢购用户信息，判断当前用户是否已经抢购商品
     *
     * @param productNo
     * @return result > 1 ? 已经抢购 :  没有抢购
     */
    private Long countByProductNoUserId(Integer userId, String productNo) {
        LambdaQueryWrapper<ProductRecords> productRecordsLambdaQueryWrapper
                = new LambdaQueryWrapper<>();
        productRecordsLambdaQueryWrapper.eq(ProductRecords::getUserId, userId);
        productRecordsLambdaQueryWrapper.eq(ProductRecords::getProductNo, productNo);
        return this.productRecordsMapper.selectCount(productRecordsLambdaQueryWrapper);
    }


    /**
     * 更新商品的库存-1
     *
     * @return
     */
    private int updateProductStock(String productNo) {
        // 设置更新的值
        UpdateWrapper<ProductStock> updateWrapper = new UpdateWrapper<>();
        // 修改的列
        updateWrapper.setSql("stock = stock-1");
        // 条件
        updateWrapper.eq("product_no", productNo);
        updateWrapper.eq("is_active", 1);
        // 要修改的列
        ProductStock productStock = new ProductStock();
        // 执行更新
        return this.productStockMapper.update(productStock, updateWrapper);
    }


}
