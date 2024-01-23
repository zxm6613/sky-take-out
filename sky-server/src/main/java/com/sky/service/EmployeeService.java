package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;

/**
 * 员工逻辑接口
 *
 * @author 周简coding~~~
 * @date 2024/01/23
 */
public interface EmployeeService {

    /**
     * 员工登录
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 添加员工
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 分页查询
     */
    PageResult page(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用和禁用功能
     */
    void update(Integer status, Long id);

    /**
     * 根据id查询回显员工信息
     */
    Employee selectById(Long id);

    /**
     * 编辑员工信息
     */
    Result<Object> edit(EmployeeDTO employeeDTO);
}
