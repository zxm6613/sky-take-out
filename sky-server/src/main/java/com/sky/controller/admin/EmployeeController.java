package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * 员工控制器
 *
 * @author 周简coding~~~
 * @date 2024/01/23
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     */

    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     */
    @ApiOperation(("员工退出"))
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }


    /**
     * 添加员工
     */
    @PostMapping
    @ApiOperation("添加员工")
    public Result<Object> save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("当前controller线程的ID为{}", Thread.currentThread().getId());
        log.info("员工{}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("分页参数为{}", employeePageQueryDTO);
        PageResult pageResult = employeeService.page(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用和禁用功能
     */
    @PostMapping("status/{status}")
    @ApiOperation("启用和禁用功能")
    public Result<Object> update(@PathVariable Integer status, Long id) {
        log.info("状态是{}，id是{}", status, id);
        employeeService.update(status, id);
        return Result.success();
    }

    /**
     * 根据id查询回显员工信息
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询回显员工信息")
    public Result<Employee> selectById(@PathVariable Long id){
        log.info("员工id为{}",id);
        Employee employee = employeeService.selectById(id);
        return Result.success(employee);
    }

    /**
     * 编辑员工信息
     */
    @PutMapping
    @ApiOperation("编辑员工信息")
    public Result<Object> edit(@RequestBody EmployeeDTO employeeDTO){
        log.info("员工信息为：{}",employeeDTO);
        return employeeService.edit(employeeDTO);
    }
}
