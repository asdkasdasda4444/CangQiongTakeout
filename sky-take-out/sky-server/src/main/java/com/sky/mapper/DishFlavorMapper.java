package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 新增口味表
     * @param flavorList
     */

    void insertBatch(List<DishFlavor> flavorList);

    /**
     * 根据id删除菜品口味
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id=#{dishId}")
    void deleteByDishId(Long dishId);

    /**
     * 根据集合删除菜品口味
     * @param dish_ids
     */
    void deleteByDishIds(List<Long> dish_ids);
    /**
     * 根据菜品id查询口味信息
     * @return
     */
    @Select("select * from dish_flavor where dish_id=#{dishId} ")
    List<DishFlavor> getByDishId(Long dishId);

}
