package com.dhgh.mybank.params;

import lombok.Data;

@Data
public class DepositDTO {

    /**
     * 充值金额
     */
    private String amount;

    /**
     * 合作方业务平台订单号
     */
    private String outerTradeNo;

    /**
     * 上送支付渠道的支付订单号
     */
    private String outerInstOrderNo;

    /**
     * 支付方法
     */
    private PayMethodDTO payMethod;

}
