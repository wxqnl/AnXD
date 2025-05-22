package com.cug.daijiaguanli.service;

import com.cug.daijiaguanli.enetity.PageBean;
import com.cug.daijiaguanli.enetity.customer.CustomerInfo;
import com.cug.daijiaguanli.enetity.customer.CustomerLoginLog;

public interface CustomerService {
    CustomerInfo getCustomerInfo(String username);

    boolean remove(String username);

    boolean update(CustomerInfo customerInfo);

    PageBean<CustomerInfo> getAllCustomerInfo(int pageNum, int pageSize, String nickname, Byte gender, String phone, String status);

    CustomerLoginLog CustomerLoginLog(Long id);
}