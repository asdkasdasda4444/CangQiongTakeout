package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


//暂时关闭功能注解---方便测试


/**
 * 定时任务类--定时处理订单的方法
 */
// 在线Cron表达式生成器 https://cron.qqe2.com/
@Slf4j
@Component
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单的方法
     */
//    @Scheduled(cron = "1/5 * * * * ?")测试用--每五秒执行一次
//    @Scheduled(cron = "0 * * * * ?")//每分钟触发一次
    public void processTimeOrder() {
        log.info("处理超时订单：{}", LocalDateTime.now());
        //获取从开始订单到的超时时间
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        //select * from orders where status = ? and order_time<(当前时间-15分钟)
        //获取超时订单列表
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);
        //判断是否有数据
        if (!ordersList.isEmpty()) {

            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }

    }

    /**
     * 处理派送中状态的订单
     */
//    @Scheduled(cron = "1/5 * * * * ?")测试用--每五秒执行一次
//    @Scheduled(cron = "0 0 1 * * ?")//每天凌晨一点触发一次
    public void processDeliveryOrder() {
        log.info("定时处理派送中的订单：{}", LocalDateTime.now());
        //超时时间
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        //查找派送中并且超时的订单
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);

        if (!ordersList.isEmpty()) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }


}
