package com.cug.daijiaguanli.enetity.driver;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "DriverLoginLog")
@TableName("driver_login_log")
public class DriverLoginLog {

	private static final long serialVersionUID = 1L;

    @Schema(description = "司机id")
	@TableField("driver_id")
	private Long driverId;

    @Schema(description = "登录IP地址")
	@TableField("ipaddr")
	private String ipaddr;

    @Schema(description = "登录状态（0成功 1失败）")
	@TableField("status")
	private Boolean status;

    @Schema(description = "提示信息")
	@TableField("msg")
	private String msg;

	private Date createTime;

}