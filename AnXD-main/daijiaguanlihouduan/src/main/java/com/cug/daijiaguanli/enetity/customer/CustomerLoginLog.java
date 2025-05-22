package com.cug.daijiaguanli.enetity.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "CustomerLoginLog")
public class CustomerLoginLog {

	private static final long serialVersionUID = 1L;

	@Schema(description = "客户id")
	private Long customerId;

	@Schema(description = "登录IP地址")
	private String ipaddr;

	@Schema(description = "登录状态（0成功 1失败）")
	private Boolean status;

	@Schema(description = "提示信息")
	private String msg;

	private Date createTime;
}