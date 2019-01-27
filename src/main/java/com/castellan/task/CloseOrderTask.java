package com.castellan.task;


import com.castellan.service.IOrderService;
import com.castellan.service.impl.OrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    private IOrderService iOrderService;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void test(){
        log.info("任务开启了");
        iOrderService.closeOrder(1);
        log.info("任务结束了");
    }
}
