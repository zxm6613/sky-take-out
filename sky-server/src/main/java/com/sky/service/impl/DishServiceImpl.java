package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 碟品服务实现
 *
 * @author 周简coding~~~
 * @date 2024/01/25
 */
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    /**
     * 插入菜品和菜品口味
     *
     * @param dishDTO 菜 dto
     */
    @Override
    @Transactional
    public void addDish(DishDTO dishDTO) {
        //先插入菜品数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.addDish(dish);
        //再插入菜品口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {  //口味不一定传过去
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
            dishMapper.addDishFlavors(flavors);
        }
    }

    /**
     * 分页
     *
     * @param dishPageQueryDTO dish 页面查询 dto
     * @return 页面结果
     */
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Dish dish = Dish
                .builder()
                .name(dishPageQueryDTO.getName())
                .categoryId(dishPageQueryDTO.getCategoryId())
                .status(dishPageQueryDTO.getStatus())
                .build();
        Page<DishVO> dishes = dishMapper.page(dish);
        long total = dishes.getTotal();
        List<DishVO> records = dishes.getResult();
        return new PageResult(total, records);
    }

    /**
     * 删除批处理
     *
     * @param ids IDS
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        ids.forEach(id -> {
            DishVO dishVO = this.selectById(id);
            //起售状态的菜品不能删除
            if (dishVO.getStatus() == 1) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        });
        //套餐关联的菜品不能删除
        int count = dishMapper.dishSetmealRelationship(ids);
        if (count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //删除菜品，删除后记得口味也得删除(先删除口味)
        dishMapper.deleteFavory(ids);
        dishMapper.deleteDishById(ids);
    }

    /**
     * 按 ID 查询
     *
     * @param id 编号
     * @return 菜 VO
     */
    @Override
    public DishVO selectById(Long id) {
        DishVO dishVO = dishMapper.selectById(id);
        List<DishFlavor> dishFlavor = dishMapper.selectFlavors(id);
        dishVO.setFlavors(dishFlavor);
        return dishVO;
    }

    /**
     * 按类别 ID 查询
     *
     * @param categoryId 类别 ID
     * @return 菜
     */
    @Override
    public List<Dish> selectByCategoryId(Long categoryId) {
        Dish dish = Dish.builder().categoryId(categoryId).build();
        return dishMapper.selectByCategoryId(dish);
    }

    /**
     * 更新菜品的数据
     *
     * @param dishDTO 菜 dto
     */
    @Override
    @Transactional
    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        List<DishFlavor> flavors = dishDTO.getFlavors();
//        List<Long> ids = new ArrayList<>();
        if (flavors != null) {
            //修改口味表
            flavors.forEach(flavor -> {
                flavor.setDishId(dish.getId());
//                ids.add(flavor.getDishId());
            });
            dishMapper.deleteOneFavory(dish.getId());
            flavors.forEach(flavor -> {
                dishMapper.addDishFlavor(flavor);
            });
//            dishMapper.addDishFlavors(flavors);
        }
        dishMapper.update(dish);
    }

    /**
     * 菜是否启用
     *
     * @param status 地位
     */
    @Override
    public void dishEnableOrNot(Integer status, Long id) {
        Dish dish = Dish.builder().status(status).id(id).build();
        dishMapper.update(dish);
    }


    /**
     * 根据分类id查询菜品
     *
     * @param dish 菜
     * @return 列表<菜 VO>
     */
    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.selectByCategoryId(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishMapper.selectFlavors(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
