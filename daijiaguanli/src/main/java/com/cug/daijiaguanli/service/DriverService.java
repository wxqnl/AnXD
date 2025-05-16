package com.cug.daijiaguanli.service;

import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.driver.DriverAccount;
import com.cug.daijiaguanli.enetity.driver.DriverAccountDetail;
import com.cug.daijiaguanli.enetity.driver.DriverInfo;
import com.cug.daijiaguanli.enetity.driver.DriverLoginLog;

import java.math.BigDecimal;

public interface DriverService {
    PageBean<DriverInfo> getDriverInfo(Integer pageNum, Integer pageSize, String name, String nickname, String gender,
                                       String phone, String jobNo, String status, String authStatus,
                                       BigDecimal minScore, BigDecimal maxScore, String minOrderCount, String maxOrderCount);

    DriverInfo getDriverInfoByName(String name);


    DriverInfo getDriverInfoByJobNo(String jobNo);

    DriverInfo addDriverInfo(DriverInfo driverInfo);

    DriverInfo updateDriverInfo(DriverInfo driverInfo);

    DriverInfo removeDriverInfo(String name);

    DriverLoginLog getDriverLoginLog(String name);

    PageBean<DriverAccountDetail> getDriverAccountDetail(Integer pageNum, Integer pageSize, String driverId,
                                                         String tradeNo, String tradeType, BigDecimal minAmount, BigDecimal maxAmount);

    DriverAccount getDriverAccount(String driverId);
}
