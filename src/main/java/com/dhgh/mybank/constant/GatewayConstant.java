/**
 * 
 */
package com.dhgh.mybank.constant;

/**
 * <p>注释</p>
 * @author fjl
 * @version $Id: GatewayConstant.java, v 0.1 2013-11-13 下午4:01:07 fjl Exp $
 */
public interface GatewayConstant {

    /*
     * 连接符
     */
    public static final String and               = "&";
    public static final String eq                = "=";
    public static final String empty             = "";

    /**
     * version
     */
    public static final String version           = "2.0";

    /**
     * 入款账户类型
     */
    public static final String account_type      = "BASIC";

    /**
     * 签名方式
     */
    public static final String sign_type         = "TWSIGN";

    /*
     * 字符集
     */
    public static final String charset_iso_latin = "ISO-8859-1";
    public static final String charset_utf_8     = "UTF-8";
    public static final String charset_gbk       = "GBK";
    public static final String charset_gb2312    = "GB2312";
    public static final String EXPANDED_NAME_TMP = ".TMP";
    
    /**
     * 与配置文件中keyStoreName保持一致
     */
    public static final String KEY_STORE_NAME    = "testKeyStore";

    /**
     * service
     */
    public static final String transfer          = "mybank.tc.trade.transfer";
    public static final String subaccount        = "mybank.tc.trade.remit.subaccount";

}
