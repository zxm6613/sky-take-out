package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
    void update(Employee employee);
}
