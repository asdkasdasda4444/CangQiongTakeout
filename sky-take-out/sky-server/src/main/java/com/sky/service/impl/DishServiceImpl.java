package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    /**
     * 新增菜品
     * @param dishDTO
     */
    @Transactional
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        //向菜品表插入一条数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.insert(dish);
        /*
        useGeneratedKeys="true" keyProperty="id"
        必须返回主键值
         */
        Long id = dish.getId();
        //向口味表添加N条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        List<DishFlavor> flavorList = flavors.stream().peek((item) -> {
            item.setDishId(id);
        }).collect(Collectors.toList());
        dishFlavorMapper.insertBatch(flavorList);

    }

    /**
     * 菜品分页查询
     * @param dto
     * @return
     */
    @Override
    public PageResult page(DishPageQueryDTO dto) {

        PageHelper.startPage(dto.getPage(),dto.getPageSize());
        Page<DishVO> page=dishMapper.page(dto);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 菜品批量删除
     * @param ids
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //可以删除或多个菜品
        //启售中的菜品不能删除（status=1）


        for (Long id : ids) {
            Dish dish=dishMapper.getById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                throw  new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }

        }
        //被套餐关联的菜品不能删除
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealIds != null && setmealIds.size() > 0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //删除菜品后套餐中的口味也要删除
//        for (Long id : ids) {
//            dishMapper.deleteById(id);
//            dishFlavorMapper.deleteByDishId(id);
//        }
        //优化-->只发两条sql
        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteByDishIds(ids);


    }

    /**
     * 根据id查询菜品和口味信息
     * @param id
     * @return
     */
    @Override
    public DishVO getByIdWithFlavor(Long id) {
        DishVO dishVO = new DishVO();
        //根据id查询菜品数据
        Dish dish = dishMapper.getById(id);
        //根据菜品id查询口味数据
        List<DishFlavor> flavor=dishFlavorMapper.getByDishId(id);
        //将数据封装到Vo
        BeanUtils.copyProperties(dish,dishVO,"flavors");

        dishVO.setFlavors(flavor);

        return dishVO;
    }
    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

    /**
     * 修改菜品
     * @param dishVO
     *
     */
    @Override
    public void updateWithFlavor(DishVO dishVO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishVO,dish,"flavors");
        dishMapper.update(dish);
//        TODO:这里必须判断
//                      1.之前的口味(byDishId)是否为空
//                      2.传过来的口味(flavors)是否为空
//                      否则-->导致删除数据为空或者传入数据为空->sql的values空值页面报错
//                      还有就是!byDishId.isEmpty()和byDishId！=null他们俩的值不相同----需要解决

        List<DishFlavor> byDishId = dishFlavorMapper.getByDishId(dish.getId());
        if(!byDishId.isEmpty()){
            dishFlavorMapper.deleteByDishId(dish.getId());
        }

        List<DishFlavor> flavors = dishVO.getFlavors();
        if(!flavors.isEmpty()){
            dishFlavorMapper.insertBatch(flavors);
        }

    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> getByCategoryId(Long categoryId) {

        List<Dish> dishList = dishMapper.getByCategoryId(categoryId);

        return dishList;
    }
}

