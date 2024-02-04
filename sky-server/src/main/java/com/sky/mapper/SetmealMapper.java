package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 套餐映射器
 *
 * @author 周简coding~~~
 * @date 2024/01/23
 */
@Mapper
public interface SetmealMapper {

    /**
     * 按类别 ID 查询
     *
     * @param id 编号
     * @return int
     */
    @Select("select COUNT(*) from setmeal where category_id = #{id}")
    int selectCountByCategoryId(Long id);

    /**
     * 插入 套餐
     *
     * @param setmeal setmeal
     */
    @AutoFill(OperationType.INSERT)
    void insertSetmeal(Setmeal setmeal);

    /**
     * 插入两者关系
     *
     * @param list 列表
     */
    void insertRelationship(@Param("list") List<SetmealDish> list);

    /**
     * 分页
     *
     * @param  setmeal 页面查询 dto
     * @return 页面<setmeal vo>
     */
    Page<SetmealVO> page(Setmeal setmeal);


    /**
     * 查询状态
     *
     * @param id 编号
     * @return int
     */
    @Select("select COUNT(*) from setmeal where id = #{id} and status = 1")
    int selectStatus(Long id);

    /**
     * 选择类别
     *
     * @param id 编号
     * @return int
     */
    int selectCategory(Long id);

    /**
     * 删除套餐
     *
     * @param ids IDS
     */
    void delete(List<Long> ids);

    /**
     * 删除关系
     *
     * @param ids IDS
     */
    void deleteRelationship(List<Long> ids);

    /**
     * 按 ID 查询
     *
     * @param id 编号
     * @return Setmeal DTO
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal selectById(Long id);

    /**
     * 查询关系
     *
     * @param id 编号
     * @return 清单<套餐>
     */
    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> selectRelationship(Long id);

    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 动态条件查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询菜品选项
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);

    /**
     * 根据条件统计套餐数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
