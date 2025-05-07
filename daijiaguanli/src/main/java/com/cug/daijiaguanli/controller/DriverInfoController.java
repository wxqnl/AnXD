package com.cug.daijiaguanli.controller;

import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.driver.DriverAccount;
import com.cug.daijiaguanli.enetity.driver.DriverAccountDetail;
import com.cug.daijiaguanli.enetity.driver.DriverInfo;
import com.cug.daijiaguanli.enetity.driver.DriverLoginLog;
import com.cug.daijiaguanli.result.Result;
import com.cug.daijiaguanli.service.DriverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Slf4j

@RestController
@RequestMapping(value="/driver")
@SuppressWarnings({"unchecked", "rawtypes"})
public class DriverInfoController {

    @Autowired
    public DriverService diverService;
    //分页条件查询司机
    @GetMapping("/getDriverInfo")
    public Result<PageBean<DriverInfo>> getDriverInfo(Integer pageNum,
                                                      Integer pageSize,
                                                      @RequestParam(required = false) String name,
                                                      @RequestParam(required = false) String nickname,
                                                      @RequestParam(required = false) String gender,
                                                      @RequestParam(required = false) String phone,
                                                      @RequestParam(required = false) String jobNo,
                                                      @RequestParam(required = false) String status,
                                                      @RequestParam(required = false) String authStatus,
                                                      @RequestParam(required = false) BigDecimal minScore,
                                                      @RequestParam(required = false) BigDecimal maxScore,
                                                      @RequestParam(required = false) String minOrderCount,
                                                      @RequestParam(required = false) String maxOrderCount,
                                                      @RequestParam(required = false) String driverLicenseClazz
    ){
        PageBean<DriverInfo> di=diverService.getDriverInfo(pageNum, pageSize, name, nickname, gender, phone, jobNo, status, authStatus, minScore, maxScore, minOrderCount, maxOrderCount);
        System.out.println("di="+di);
        return Result.ok(di);
    }

    //查询指定用户名司机
    @GetMapping("/getDriverInfoByName")
    public Result<DriverInfo> getDriverInfoByName(String name){
        DriverInfo di=diverService.getDriverInfoByName(name);
        return Result.ok(di);
    }

    //查询指定工号司机
    @GetMapping("/getDriverInfoByJobNo")
    public Result<DriverInfo> getDriverInfoByJobNo(String jobNo){
        DriverInfo di=diverService.getDriverInfoByJobNo(jobNo);
        return Result.ok(di);
    }

    //添加司机
    @GetMapping("/addDriverInfo")
    public Result<DriverInfo> addDriverInfo(DriverInfo driverInfo){
        DriverInfo di=diverService.addDriverInfo(driverInfo);
        return Result.ok(di);
    }

    //修改司机信息
    @GetMapping("/updateDriverInfo")
    public Result<DriverInfo> updateDriverInfo(DriverInfo driverInfo){
        DriverInfo di=diverService.updateDriverInfo(driverInfo);
        return Result.ok(di);
    }

    //删除司机
    @GetMapping("/removeDriverInfo")
    public Result<DriverInfo> removeDriverInfo(String name){
        DriverInfo di=diverService.removeDriverInfo(name);
        return Result.ok(di);
    }


    //查询司机登录日志
    @GetMapping("/getDriverLoginLog")
    public Result<DriverLoginLog> getDriverLoginLog(String name){
        DriverLoginLog dll=diverService.getDriverLoginLog(name);
        return Result.ok(dll);
    }

    //查询司机账单信息
    @GetMapping("/getDriverAccountDetail")
    public Result<PageBean<DriverAccountDetail>> getDriverAccountDetail(Integer pageNum,
                                                                        Integer pageSize,
                                                                        @RequestParam(required = false) String dirverId,
                                                                        @RequestParam(required = false) String tradeNo,
                                                                        @RequestParam(required = false) String tradeType,
                                                                        @RequestParam(required = false) BigDecimal minAmount,
                                                                        @RequestParam(required = false)BigDecimal maxAmount
    )
    {
        PageBean<DriverAccountDetail>dad=diverService.getDriverAccountDetail(pageNum, pageSize, dirverId, tradeNo, tradeType, minAmount, maxAmount);
        return Result.ok(dad);
    }

    //查询司机总收入等
    @GetMapping("/getDriverAccount")
    public Result<DriverAccount> getDriverAccount(String driverId){
        DriverAccount da=diverService.getDriverAccount(driverId);
        return Result.ok(da);
    }




}

