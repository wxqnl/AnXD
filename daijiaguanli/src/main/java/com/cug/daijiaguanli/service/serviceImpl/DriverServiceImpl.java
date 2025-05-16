package com.cug.daijiaguanli.service.Impl;


import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.driver.DriverAccount;
import com.cug.daijiaguanli.enetity.driver.DriverAccountDetail;
import com.cug.daijiaguanli.enetity.driver.DriverInfo;
import com.cug.daijiaguanli.enetity.driver.DriverLoginLog;
import com.cug.daijiaguanli.mapper.DriverMapper;
import com.cug.daijiaguanli.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverMapper driverMapper;


    @Override
    public PageBean<DriverInfo> getDriverInfo(Integer pageNum, Integer pageSize, String name, String nickname,
                                              String gender, String phone, String jobNo, String status,
                                              String authStatus, BigDecimal minScore, BigDecimal maxScore,
                                              String minOrderCount, String maxOrderCount) {
        int offset = (pageNum - 1) * pageSize;
        List<DriverInfo> list = driverMapper.selectDriverInfoWithCondition(name, nickname, gender, phone, jobNo,
                status, authStatus, minScore, maxScore, minOrderCount, maxOrderCount, offset, pageSize);
        long count = driverMapper.countDriverInfoWithCondition(name, nickname, gender, phone, jobNo,
                status, authStatus, minScore, maxScore, minOrderCount, maxOrderCount);
        return new PageBean<>(count, list);
    }

    @Override
    public DriverInfo getDriverInfoByName(String name) {
        return driverMapper.selectByName(name);
    }

    @Override
    public DriverInfo getDriverInfoByJobNo(String jobNo) {
        return driverMapper.selectByJobNo(jobNo);
    }

    @Override
    public DriverInfo addDriverInfo(DriverInfo driverInfo) {
        driverMapper.insert(driverInfo);
        return driverInfo;
    }

    @Override
    public DriverInfo updateDriverInfo(DriverInfo driverInfo) {
        driverMapper.update(driverInfo);
        return driverInfo;
    }

    @Override
    public DriverInfo removeDriverInfo(String name) {
        DriverInfo di = driverMapper.selectByName(name);
        driverMapper.deleteByName(name);
        return di;
    }

    @Override
    public DriverLoginLog getDriverLoginLog(String name) {
        return driverMapper.selectLatestByName(name);
    }

    @Override
    public PageBean<DriverAccountDetail> getDriverAccountDetail(Integer pageNum, Integer pageSize, String driverId,
                                                                String tradeNo, String tradeType, BigDecimal minAmount, BigDecimal maxAmount) {
        int offset = (pageNum - 1) * pageSize;
        List<DriverAccountDetail> list = driverMapper.selectDriverAccountDetailWithCondition(driverId, tradeNo, tradeType, minAmount, maxAmount, offset, pageSize);
        long count = driverMapper.countDriverAccountDetailWithCondition(driverId, tradeNo, tradeType, minAmount, maxAmount);
        return new PageBean<>(count, list);
    }

    @Override
    public DriverAccount getDriverAccount(String driverId) {
        return driverMapper.selectDriverAccount(driverId);
    }
}
