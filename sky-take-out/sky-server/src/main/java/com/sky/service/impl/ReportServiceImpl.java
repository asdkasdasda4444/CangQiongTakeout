package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {


    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;


    /**
     * 营业额统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        //开始日期到结束日期的每天的日期的集合
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        //将日期放入集合
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        //将日期对应的营业额放入集合
        List<Double> turnOverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            //查询营业额--->相对应日期并且状态是已完成（5）的金额合计
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            //select sum（amount） from orders where order_time>? and oder_time<? and status=5
            Map map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);
            //存放每天的营业额
            Double turnOver = orderMapper.sumByMap(map);
            //不能为空
            turnOver = turnOver == null ? 0.0 : turnOver;
            turnOverList.add(turnOver);

        }
        //封装返回结果
        String turnOverString = StringUtils.join(turnOverList, ",");
        String dateString = StringUtils.join(dateList, ",");
        TurnoverReportVO build = TurnoverReportVO.builder().dateList(dateString).turnoverList(turnOverString).build();

        return build;
    }

    /**
     * 用户统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {

        //开始日期到结束日期的每天的日期的集合
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        //将日期放入集合
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        //存放每天新用户数量
        List<Integer> newUserList = new ArrayList<>();
        //select count(id) from user where create_time<? and create_time>?

        //存放每天总用户数量
        List<Integer> totalUserList = new ArrayList<>();
        //select count(id) from user where create_time<?


        for (LocalDate date : dateList) {

            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap<>();

            //总用户数量
            map.put("end", endTime);
            Integer totalUser = userMapper.countByMap(map);

            //新增用户数量
            map.put("begin", beginTime);
            Integer newUser = userMapper.countByMap(map);
            //将每一天所对应的数量添加进List
            totalUserList.add(totalUser);
            newUserList.add(newUser);

        }
        //转成需要的字符串
        String dateString = StringUtils.join(dateList, ",");
        String totalString = StringUtils.join(totalUserList, ",");
        String newString = StringUtils.join(newUserList, ",");

        //封装结果数据
        UserReportVO build = UserReportVO.builder()
                .dateList(dateString)
                .totalUserList(totalString)
                .newUserList(newString)
                .build();


        return build;
    }


    /**
     * 订单统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end) {

        log.info("开始进行订单统计");

        //开始日期到结束日期的每天的日期的集合
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        //将日期放入集合
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        List<Integer> totalOrderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        //查询每一天的有效订单数和订单总数
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            //查每天的订单总数
            //select count(id) from orders where order_time>? and order_time<?
            Integer totalCount = getOrderCount(beginTime, endTime, null);
            totalOrderCountList.add(totalCount);
            //查每天的有效订单数
            //select count(id) from orders where order_time>? and order_time<? and status=5
            Integer validCount = getOrderCount(beginTime, endTime, Orders.COMPLETED);
            validOrderCountList.add(validCount);

        }

        //计算时间区间内的订单总数
        Integer totalOrderCount = totalOrderCountList.stream().reduce(Integer::sum).get();

        //计算时间区间内的有效订单总数
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();

        //计算时间区间内的订单完成率
        Double rate = 0.0;
        if (totalOrderCount!=0) {
//        TODO:    rate=(double) (validOrderCount / totalOrderCount);
//         不行的原因是int/int除完之后才强转成double类型
            rate=validOrderCount.doubleValue() / totalOrderCount;

        }

        //转成需要的字符串
        String dateString = StringUtils.join(dateList, ",");
        String totalString = StringUtils.join(totalOrderCountList, ",");
        String validString = StringUtils.join(validOrderCountList, ",");

        //封装返回值对象
        OrderReportVO build = OrderReportVO.builder()
                .dateList(dateString)
                .validOrderCountList(validString)
                .orderCountList(totalString)
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(rate)
                .build();

        //返回对象
        return build;


    }


    /**
     * 排名统计
     * @param begin
     * @param end
     * @return
     */

    @Override
    public SalesTop10ReportVO getTop10Statistics(LocalDate begin, LocalDate end) {
        //转换时间端值
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop10(beginTime, endTime);

        //获取所有前十的名字及个数
        List<String> names = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numbers = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        //将列表转换成想要的字符串
        String nameList=StringUtils.join(names,",");
        String numberList = StringUtils.join(numbers, ",");

        SalesTop10ReportVO build = SalesTop10ReportVO.builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();

        return build;
    }


    /**
     * 根据条件统计订单数量
     *
     * @param begin
     * @param end
     * @param status
     * @return
     */
    private Integer getOrderCount(LocalDateTime begin, LocalDateTime end, Integer status) {
        Map map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        map.put("status", status);
        return orderMapper.countByMap(map);
    }
}
