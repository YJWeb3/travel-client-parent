package com.zheng.travel.client.task.xxljob;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyXXLJob {

    @XxlJob("myXXLJobHandler")
    public ReturnT<String> myXXLJobHandler(String... params) throws Exception {

        log.info("MyXXLJob execute.................");
        XxlJobHelper.log("XXL-JOB,User createSuccess userid  ");
//        // 分片的序号
//        int shardIndex = XxlJobHelper.getShardIndex();
//        // 分片的总数
//        int shardTotal = XxlJobHelper.getShardTotal();
//
//        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
//        for (Integer integer : list) {
//            if (integer % shardTotal == shardIndex) {
//                log.info("MyXXLJob execute.................,shareIndex:{},shardTotal:{},{}", shardIndex, shardTotal,integer);
//                XxlJobHelper.log("XXL-JOB,User createSuccess userid : " + integer + ".");
//            }
//        }

        return ReturnT.SUCCESS;
    }
}
