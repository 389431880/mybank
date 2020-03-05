package com.dhgh.mybank.params;

import lombok.Data;

@Data
public class PayMethodDTO {

    /**
     * 充值金额
     */
    private String payMethod;

    /**
     * 充值金额
     */
    private String amount;

    /**
     * 充值金额
     */
    private String memo;

    /**
     * 充值金额
     */
    private String extension;

}
