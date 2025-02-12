package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {
    /**
     * 根据分类id查询菜品数量
     *
     * @param categoryId
     * @return
     */
    @Select("SELECT COUNT(*) FROM dish WHERE category_id=#{cateoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 新增菜品
     *
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 菜品分页查询
     *
     * @param dto
     * @return
     */
    Page<DishVO> page(DishPageQueryDTO dto);

    /**
     * 根据id查询菜品
     *
     * @param id
     * @return
     */
    @Select("select * from dish where id=#{id}")
    Dish getById(Long id);

    /**
     * 根据主键删除菜品数据
     *
     * @param id
     */
    @Delete("delete from dish where id=#{id}")
    void deleteById(Long id);

    /**
     * 根据集合删除菜品数据
     *
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 动态条件查询菜品数据
     *
     * @param dish
     * @return
     */

    List<Dish> list(Dish dish);

    /**
     * 更新dish数据
     *
     * @param dish
     */

    void update(Dish dish);

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     */
    @Select("SELECT * FROM dish WHERE category_id=#{categoryId}")
    List<Dish> getByCategoryId(Long categoryId);


    /**
     * 根据条件统计菜品数量
     *
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
