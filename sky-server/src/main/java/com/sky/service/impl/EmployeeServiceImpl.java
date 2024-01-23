package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        //md5加密
        String password = DigestUtils.md5DigestAsHex(employeeLoginDTO.getPassword().getBytes());

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对   进行md5加密，然后再进行比对
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus().equals(StatusConstant.DISABLE)) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 添加员工
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        log.info("当前拦截器线程的ID为{}", Thread.currentThread().getId());
        Employee employee = new Employee();
        //拷贝属性,从源到目的,要求两者属性名要一样
        BeanUtils.copyProperties(employeeDTO, employee);

        //密码常量类默认值123456,进行MD5加密
        employee.setPassword(DigestUtils.
                md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        //0表示禁用，1表示启用，这里用常量类
        employee.setStatus(StatusConstant.DISABLE);

        //设置保存日期
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //在本次线程中，取到本次线程独立的存储空间内的局部变量
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());
        BaseContext.removeCurrentId();

        employeeMapper.save(employee);
    }

    /**
     * 分页查询
     */
    @Override
    public PageResult page(EmployeePageQueryDTO employeePageQueryDTO) {
        //设置分页参数
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        //查询
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO.getName());

        //获取记录数和数据
        long total = page.getTotal();
        List<Employee> records = page.getResult();


        return new PageResult(total, records);
    }

    /**
     * 启用和禁用功能
     */
    @Override
    public void update(Integer status, Long id) {
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
        employeeMapper.update(employee);
    }

    /**
     * 根据id查询回显员工信息
     */
    @Override
    public Employee selectById(Long id) {
       return employeeMapper.selectById(id);
    }

    /**
     * 编辑员工信息
     */
    @Override
    public Result<Object> edit(EmployeeDTO employeeDTO) {
        //方式一：
//        Employee employee = Employee.builder()
//                .id(employeeDTO.getId())
//                .idNumber(employeeDTO.getIdNumber())
//                .name(employeeDTO.getName())
//                .phone(employeeDTO.getPhone())
//                .sex(employeeDTO.getSex())
//                .username(employeeDTO.getUsername())
//                .build();
        //方式二：
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);

        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.update(employee);
        return Result.success();
    }

}
