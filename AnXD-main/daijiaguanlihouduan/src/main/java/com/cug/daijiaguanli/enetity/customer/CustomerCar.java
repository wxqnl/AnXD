package com.cug.daijiaguanli.enetity.customer;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "CustomerCar")
public class CustomerCar {

	private static final long serialVersionUID = 1L;

	@Schema(description = "客户ID")
	private Long customerId;

	@Schema(description = "车牌号")
	private String license;

	@Schema(description = "车型")
	private String brand;

}