package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    /**
     * 新增订单数据
     *
     * @param orders
     */
    void insert(Orders orders);


    /**
     * 更新orders表数据
     * @param orders
     */
    void update(Orders orders);

    /**
     * 处理订单超时
     *
     * @param status
     * @param orderTime
     */
    //select * from orders where status = ? and order_time<(当前时间-15分钟)
    @Select("select * from orders where status=#{status} and order_time<#{orderTime}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);
    /**
     * 营业额统计
     * @param map
     * @return
     */
    Double sumByMap(Map map);


    /**
     * 订单统计
     * @param map
     * @return
     */
    Integer countByMap(Map map);


    /**
     * 排名统计
     * @param begin
     * @param end
     * @return
     */
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime begin,LocalDateTime end);

    /**
     * 获取所有符合条件的订单
     * @param ordersPageQueryDTO
     */
    List<Orders> getAllOrders(OrdersPageQueryDTO ordersPageQueryDTO);
    /**
     * 订单搜索
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);
    /**
     * 分页条件查询并按下单时间排序
     * @param ordersPageQueryDTO
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据状态统计订单数量
     * @param status
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);
}
