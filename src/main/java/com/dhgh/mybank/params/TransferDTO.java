package com.dhgh.mybank.params;

import lombok.Data;

/**
 * @Author: HZB
 * @Description:
 * @Date: Created in 16:11 2020/1/13
 */
@Data
public class TransferDTO {

    /**
     * 转账对象
     */
    private String uid;

    /**
     * 转账金额
     */
    private String amount;

    /**
     * 唯一订单号
     */
    private String tradeNo;

}
