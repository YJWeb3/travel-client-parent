package com.zheng.travel.client.lock.service.redis;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zheng.travel.client.lock.model.UserReg;
import com.zheng.travel.client.lock.vo.UserRegVo;
import com.zheng.travel.client.lock.mapper.UserRegMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserRegServiceImpl extends ServiceImpl<UserRegMapper, UserReg> implements IUserRegService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 无锁
     *
     * @param userRegVo
     */
    @Override
    public void regUserNoLock(UserRegVo userRegVo) {
        // 根据用户名查询用户实体信息，如果不存在进行注册
        LambdaQueryWrapper<UserReg> userRegLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 根据用户名查询对应的用户信息条件
        userRegLambdaQueryWrapper.eq(UserReg::getUserName, userRegVo.getUserName());
        // 执行查询语句
        UserReg userReg = this.getOne(userRegLambdaQueryWrapper);

        if (null == userReg) {
            // 这里切记一定要创建一个用户
            userReg = new UserReg();
            // 使用BeanUtils.copyProperties进行两个对象相同属性的的复制和赋值，如果不同自动忽略
            BeanUtils.copyProperties(userRegVo, userReg);
            // 设置注册时间
            userReg.setCreateTime(new Date());
            // 执行保存和注册用户方法
            this.saveOrUpdate(userReg);
        } else {
            // 如果存在就返回用户信息已经存在
            throw new RuntimeException("用户信息已经注册存在了...");
        }
    }

    /**
     * redis分布式锁
     *
     * @param userRegVo
     */
    @Override
    public void regUserRedisLock(UserRegVo userRegVo) {
        // D yykk_lock 1 ?--晚于 A B C
        // 1: 设置redis的sexnx的key，考虑到幂等性，这个key有如下做法
        // 前提是：前userRegVo.getUserName()必须唯一的，
        // 为什么这样要唯一：A 注册 B也能够注册
        String key = userRegVo.getUserName() + "_lock";
        // 给每个线程线程产生不同的value,目的是：用于删除锁的判断
        String value = System.nanoTime() + "" + UUID.randomUUID();
        // 通过java代码设置setnx
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        // 获取锁
        //这里又一个隐患，设置太长吞吐量下降，太短可能会造成资源不一致。 这个时间一定要根据实际情况而定
        // setnx ===
        Boolean resLock = opsForValue.setIfAbsent(key, value, 20L, TimeUnit.SECONDS);//setnx
        if (resLock) {
            try {
                // 根据用户名查询用户实体信息，如果不存在进行注册
                LambdaQueryWrapper<UserReg> userRegLambdaQueryWrapper = new LambdaQueryWrapper<>();
                // 根据用户名查询对应的用户信息条件
                userRegLambdaQueryWrapper.eq(UserReg::getUserName, userRegVo.getUserName());
                // 执行查询语句
                UserReg userReg = this.getOne(userRegLambdaQueryWrapper);
                if (null == userReg) {
                    // 这里切记一定要创建一个用户
                    userReg = new UserReg();
                    // 使用BeanUtils.copyProperties进行两个对象相同属性的的复制和赋值，如果不同自动忽略
                    BeanUtils.copyProperties(userRegVo, userReg);
                    // 设置注册时间
                    userReg.setCreateTime(new Date());
                    // 执行保存和注册用户方法
                    this.saveOrUpdate(userReg);
                } else {
                    // 如果存在就返回用户信息已经存在
                    throw new RuntimeException("用户信息已经注册存在了...");
                }
            } catch (Exception ex) {
                throw ex;
            } finally {
                // ---------------------释放锁--不论成功还是失败都应该把锁释放掉
                // 不管发生任何情况，都一定是自己删除自己的锁 lua
                if (opsForValue.get(key).toString().equals(value)) {
                    stringRedisTemplate.delete(key);//上面的expire 20s为啥设置其实就是因为delete报错。
                }
            }
        }
    }


    // 创建zookeeper的客户端实列client
    @Autowired
    private CuratorFramework client;
    // zookeeper分布式锁的原理是由：znode的创建和删除 +watcher监听机制构成的
    // 1: 创建就就获取锁的过程，删除就是释放锁的过程
    // 2: 删除的同时，会出发watcher机制，进行临时节点的移除，从而保证我们集合的znode第一个元素永远是最小的。
    // 3: 这样就保证获取到锁.
    private static final String lockPath = "/middleware/zklock/";


    /**
     * zk分布式锁 ---写多读少的情况
     * redisson
     * redlock
     * <p>
     * 分布式事务 ---微服务
     * <p>
     * 分布式会话 ---TRAVELadmin
     *
     * @param userRegVo
     */
    @Override
    public void regUserZkLock(UserRegVo userRegVo) throws Exception {
        InterProcessMutex lock = new InterProcessMutex(client, lockPath + userRegVo.getUserName() + "-lock");
        try {
            // 本身就是排队机制，就是一个阻塞的机制，所以你发现执行的速度会非常的慢.
            boolean acquire = lock.acquire(20L, TimeUnit.SECONDS);
            if (acquire) {
                // 根据用户名查询用户实体信息，如果不存在进行注册
                LambdaQueryWrapper<UserReg> userRegLambdaQueryWrapper = new LambdaQueryWrapper<>();
                // 根据用户名查询对应的用户信息条件
                userRegLambdaQueryWrapper.eq(UserReg::getUserName, userRegVo.getUserName());
                // 执行查询语句
                UserReg userReg = this.getOne(userRegLambdaQueryWrapper);
                if (null == userReg) {
                    // 这里切记一定要创建一个用户
                    userReg = new UserReg();
                    // 使用BeanUtils.copyProperties进行两个对象相同属性的的复制和赋值，如果不同自动忽略
                    BeanUtils.copyProperties(userRegVo, userReg);
                    // 设置注册时间
                    userReg.setCreateTime(new Date());
                    // 执行保存和注册用户方法
                    this.saveOrUpdate(userReg);
                } else {
                    // 如果存在就返回用户信息已经存在
                    throw new RuntimeException("用户信息已经注册存在了...");
                }
            } else {
                log.info("1-----------没有获取到!!!!!!!");
            }
        } catch (Exception ex) {
            throw new RuntimeException("用户注册出现异常，获取zookeeper锁失败...");
        } finally {
            log.info("2-----------释放锁了!!!!!!!");
            lock.release();
        }
    }


    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void regUserRedissionLock(UserRegVo userRegVo) {
        // D yykk_lock 1 ?--晚于 A B C
        // 1: 设置redis的sexnx的key，考虑到幂等性，这个key有如下做法
        // 前提是：前userRegVo.getUserName()必须唯一的，
        // 为什么这样要唯一：A 注册 T1 T2 T3  | B也能够注册 T1 T2 T3 1W A 9999
        // A用户 t1
        String key = "redissolock_" + userRegVo.getUserName();
        // 获redisson分布式锁
        RLock lock = redissonClient.getLock(key);
        try {
            // 访问共享资源前上锁
            // 这里主要通过lock.lock方法进行上锁。
            // 上锁成功，不管何种情况下，10s后会自动释放。A 注册---100次 1 99 写少读多 --lock
            lock.lock(30, TimeUnit.SECONDS);
            // 根据用户名查询用户实体信息，如果不存在进行注册
            LambdaQueryWrapper<UserReg> userRegLambdaQueryWrapper = new LambdaQueryWrapper<>();
            // 根据用户名查询对应的用户信息条件
            userRegLambdaQueryWrapper.eq(UserReg::getUserName, userRegVo.getUserName());
            // 执行查询语句
            UserReg userReg = this.getOne(userRegLambdaQueryWrapper);
            if (null == userReg) {
                // 这里切记一定要创建一个用户
                userReg = new UserReg();
                // 使用BeanUtils.copyProperties进行两个对象相同属性的的复制和赋值，如果不同自动忽略
                BeanUtils.copyProperties(userRegVo, userReg);
                // 设置注册时间
                userReg.setCreateTime(new Date());
                // 执行保存和注册用户方法
                this.saveOrUpdate(userReg);
            } else {
                // 如果存在就返回用户信息已经存在
                throw new RuntimeException("用户信息已经注册存在了...");
            }
        } catch (Exception ex) {
            log.error("---获取Redisson的分布式锁失败!---",ex.getMessage());
            throw ex;
        } finally {
            // ---------------------释放锁--不论成功还是失败都应该把锁释放掉
            // 不管发生任何情况，都一定是自己删除自己的锁
            if (lock != null) {
                //释放锁
                log.error("---获取Redisson的分布式锁释放了---");
                lock.unlock();
                // 在一些严格的场景下，也可以调用强制释放锁
                // lock.forceUnlock();
            }
        }
    }
}
