package com.cug.daijiaguanli.service.Impl;

import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.customer.CustomerInfo;
import com.cug.daijiaguanli.enetity.customer.CustomerLoginLog;
import com.cug.daijiaguanli.mapper.CustomerMapper;
import com.cug.daijiaguanli.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public CustomerInfo getCustomerInfo(String username) {
        return customerMapper.selectByUsername(username);
    }

    @Override
    public boolean remove(String username) {
        return customerMapper.deleteByUsername(username) > 0;
    }

    @Override
    public boolean update(CustomerInfo customerInfo) {
        return customerMapper.updateCustomerInfo(customerInfo) > 0;
    }

    @Override
    public PageBean<CustomerInfo> getAllCustomerInfo(int pageNum, int pageSize, String nickname, Byte gender, String phone, String status) {
        int start = (pageNum - 1) * pageSize;
        List<CustomerInfo> items = customerMapper.selectAllWithCondition(nickname, gender, phone, status, start, pageSize);
        Long total = customerMapper.countAllWithCondition(nickname, gender, phone, status);
        return new PageBean<>(total, items);
    }

    @Override
    public CustomerLoginLog CustomerLoginLog(Long id) {
        return customerMapper.selectLatestLogByCustomerId(id);
    }
}
