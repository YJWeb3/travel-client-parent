package com.zheng.travel.client.lock.service.db; /**
 * Created by Administrator on 2019/4/17.
 */

import com.zheng.travel.client.lock.dto.UserAccountDto;
import com.zheng.travel.client.lock.model.UserAccount;
import com.zheng.travel.client.lock.model.UserAccountRecord;
import com.zheng.travel.client.lock.mapper.UserAccountMapper;
import com.zheng.travel.client.lock.mapper.UserAccountRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
@Slf4j
public class TravelNoLockUserService {
    //定义“用户账户余额实体”Mapper操作接口
    @Autowired
    private UserAccountMapper userAccountMapper;
    //定义“用户成功申请提现时金额记录”Mapper操作接口
    @Autowired
    private UserAccountRecordMapper userAccountRecordMapper;

    /**
     * 用户账户提取金额处理 ----------无锁
     * UserAccount 余额表
     * UserAccountRecord 余额记录表
     *
     * @param dto
     * @throws Exception
     * A 能解决  B 不能
     * 锁升级
     */
    @Transactional
    public  void takeMoney(UserAccountDto dto) throws Exception {
        // 根据用户查询用户的实际余额
        UserAccount userAccount = userAccountMapper.selectByUserId(dto.getUserId());
        // 判断当前用户是否存在
        if (null == userAccount) {
            throw new RuntimeException("账号不存在!!!");
        }

        // 同时判断用户余额释放大于提现的金额
        if (userAccount.getAmount().doubleValue() - dto.getAmount() > 0) {

            // 如果余额足够，就开始执行体现操作，也就执行更新操作，把现有账户余额进行更新
            userAccountMapper.updateAmount(dto.getAmount(), userAccount.getId());

            // 同时记录体现表进行增加记录
            UserAccountRecord userAccountRecord = new UserAccountRecord();
            // 设置体现的时间
            userAccountRecord.setCreateTime(new Date());
            // 设置体现的用户
            userAccountRecord.setAccountId(userAccount.getId());
            // 设置成功体现的金额
            userAccountRecord.setMoney(BigDecimal.valueOf(dto.getAmount()));
            // 保存体现记录
            userAccountRecordMapper.insert(userAccountRecord);

            log.info("当前体现的金额是：{}, 用户的余额是：{}", dto.getAmount(), userAccount.getAmount());
        } else {
            throw new RuntimeException("账户余额不足!!!");
        }
    }


    /**
     * 用户账户提取金额处理 -----------乐观锁
     * UserAccount 余额表
     * UserAccountRecord 余额记录表
     *
     * @param dto
     * @throws Exception
     */
    @Transactional
    public void takeMoneyLock(UserAccountDto dto) throws Exception {
        // 根据用户查询用户的实际余额
        UserAccount userAccount = userAccountMapper.selectByUserId(dto.getUserId()); //数据量小：悲观锁
        // 判断当前用户是否存在
        if (null == userAccount) {
            throw new RuntimeException("账号不存在!!!");
        }
        // 同时判断用户余额释放大于提现的金额
        if (userAccount.getAmount().doubleValue() - dto.getAmount() > 0) {
            // 如果余额足够，就开始执行体现操作，也就执行更新操作，把现有账户余额进行更新
            //MYSQL---
            // A线程 version=0  B线程 version=0 C线程 version=0 --JVM线程 ----------MYSQL内存---三个update---这里出现先后
            int res = userAccountMapper.updateByPKVersion(dto.getAmount(), userAccount.getId(), userAccount.getVersion());
            // 这里记得一定是获取到锁的记录并且余额是充足，并更新状态是大于 0 ---一句话就体现成功的记录
            if (res > 0) {
                // 同时记录体现表进行增加记录
                UserAccountRecord userAccountRecord = new UserAccountRecord();
                // 设置体现的时间
                userAccountRecord.setCreateTime(new Date());
                // 设置体现的用户
                userAccountRecord.setAccountId(userAccount.getId());
                // 设置成功体现的金额
                userAccountRecord.setMoney(BigDecimal.valueOf(dto.getAmount()));
                // 保存体现记录
                userAccountRecordMapper.insert(userAccountRecord);
                log.info("当前体现的金额是：{}, 用户的余额是：{}", dto.getAmount(), userAccount.getAmount());
            }
        } else {
            throw new RuntimeException("账户余额不足!!!");
        }
    }


    /**
     * 用户账户提取金额处理 -----------悲观锁
     * UserAccount 余额表
     * UserAccountRecord 余额记录表
     *
     * @param dto
     * @throws Exception
     */
    @Transactional
    public void takeMoneyPessLock(UserAccountDto dto) throws Exception {
        // 根据用户查询用户的实际余额 A , B ,C
        UserAccount userAccount = userAccountMapper.selectByUserIdLock(dto.getUserId()); //数据量小：悲观锁 等价于：lock.lock()
        //B线程  select * from user_account where user_id = 10010 lock  --- MYSQL线程栈（队列）----yes
        //A线程  select * from user_account where user_id = 10010 lock 阻塞 --- MYSQL线程栈（队列）
        //C线程  select * from user_account where user_id = 10010 lock 阻塞 --- MYSQL线程栈（队列）
        // 判断当前用户是否存在
        if (null == userAccount) {
            throw new RuntimeException("账号不存在!!!");
        }
        // 同时判断用户余额释放大于提现的金额
        if (userAccount.getAmount().doubleValue() - dto.getAmount() > 0) {
            // 如果余额足够，就开始执行体现操作，也就执行更新操作，把现有账户余额进行更新
            int res = userAccountMapper.updateAmountNoVerionLock(dto.getAmount(), userAccount.getId());
            // 这里记得一定是获取到锁的记录并且余额是充足，并更新状态是大于 0 ---一句话就体现成功的记录
            if (res > 0) {
                // 同时记录体现表进行增加记录
                UserAccountRecord userAccountRecord = new UserAccountRecord();
                // 设置体现的时间
                userAccountRecord.setCreateTime(new Date());
                // 设置体现的用户
                userAccountRecord.setAccountId(userAccount.getId());
                // 设置成功体现的金额
                userAccountRecord.setMoney(BigDecimal.valueOf(dto.getAmount()));
                // 保存体现记录
                userAccountRecordMapper.insert(userAccountRecord);
                log.info("当前体现的金额是：{}, 用户的余额是：{}", dto.getAmount(), userAccount.getAmount());
            }
        } else {
            throw new RuntimeException("账户余额不足!!!");
        }
    }
}
































