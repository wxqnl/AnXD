package com.cug.daijiaguanli.controller;


import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.customer.CustomerInfo;
import com.cug.daijiaguanli.enetity.customer.CustomerLoginLog;
import com.cug.daijiaguanli.result.Result;
import com.cug.daijiaguanli.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/customer")
@SuppressWarnings({"unchecked", "rawtypes"})
public class CustomerInfoController {

    @Autowired
    private CustomerService customerService;
    //获取指定用户名的用户信息
    @PostMapping("/getCustomerInfo")
    public Result<CustomerInfo> getCustomerInfo(String username) {
        CustomerInfo cus=customerService.getCustomerInfo(username);
        return Result.ok(cus);
    }

    //删除指定用户名用户
    @PostMapping("/remove")
    public Result remove(String username) {
        Boolean bol=customerService.remove(username);
        if (bol){
            return Result.ok();
        }
        return Result.fail();
    }

    //修改指定用户名用户信息
    @PostMapping("/update")
    public Result update(CustomerInfo customerInfo) {
        Boolean bol=customerService.update(customerInfo);
        if (bol){
            return Result.ok();
        }
        return Result.fail();
    }

    //分页获取条件查询所有用户信息
    @PostMapping("/getAllCustomerInfo")
    public Result<PageBean<CustomerInfo>>getAllCustomerInfo
    (
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) Byte gender,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String status
    )
    {
        PageBean<CustomerInfo> page = customerService.getAllCustomerInfo(pageNum, pageSize, nickname, gender, phone, status);
        System.out.println("page="+page);
        return Result.ok(page);
    }

    //获取用户日志
    @PostMapping("/getCustomerLoginLog")
    public Result<CustomerLoginLog>getCustomerLoginLog(String username)
    {
        Long id=customerService.getCustomerInfo(username).getId();
        CustomerLoginLog cusInfo=customerService.CustomerLoginLog(id);
        return Result.ok(cusInfo);
    }




}

