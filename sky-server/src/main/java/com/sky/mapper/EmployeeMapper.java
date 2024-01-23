package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 员工映射器
 *
 * @author 周简coding~~~
 * @date 2024/01/23
 */
@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 添加员工
     */
    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into employee (name, username, password, phone, " +
            "sex, id_number, status, create_time, update_time, create_user, update_user) " +
            "values " +
            "(#{name}, #{username}, #{password}," +
            " #{phone}, #{sex}, #{idNumber},#{status}," +
            "#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void save(Employee employee);

    /**
     * 分页查询
     */
    Page<Employee> pageQuery(String name);

    /**
     * 启用和禁用功能  以及编辑员工功能
     */
    @AutoFill(OperationType.UPDATE)
    void update(Employee employee);

    /**
     * 根据id查询回显员工信息
     */
    Employee selectById(Long id);
}
