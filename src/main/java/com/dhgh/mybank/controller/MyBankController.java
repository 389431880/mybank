package com.dhgh.mybank.controller;

import com.dhgh.mybank.params.DepositDTO;
import com.dhgh.mybank.params.PayMethodDTO;
import com.dhgh.mybank.params.TransferDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import com.dhgh.mybank.constant.GatewayConstant;
import com.dhgh.mybank.util.MagCore;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@RestController
@RequestMapping(value = "/myBank")
public class MyBankController {

	@Value("${my_bank_config.partner_id}")
	private String partner_id;

	//	@CrossOrigin(origins = "http://domain2.com")
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/register")	// 个人注册
	public Map<String,String> register(@RequestBody(required = false) Map<String, String> params) {
		Map<String, String> result = new HashMap<>();
		params = new HashMap<>();

		String gatewayUrl = "http://test.tc.mybank.cn/gop/gateway.do";
		try {
			Map<String, String> send;

			params.put("charset", "UTF-8");
			params.put("member_name", "59a2x");
			params.put("mobile", "13674401802");
			params.put("partner_id", partner_id);
			params.put("real_name", "3uu8r");
			params.put("service", "mybank.tc.user.personal.register");
			params.put("uid", "779b29f273fe4eb8b492371732322b11");
			params.put("version", "2.0");

			send = MagCore.paraFilter2(params);
			String sign = MagCore.buildRequestByTWSIGN(send,"utf-8", GatewayConstant.KEY_STORE_NAME);
			send.put("sign", sign);
			send.put("sign_type", "TWSIGN");
			String data = doHttpClientPost(gatewayUrl, send);

			JSONObject jsonData = new JSONObject(data);
			if (!StringUtils.isEmpty(jsonData.getString("is_success"))) {
				if ("T".equals(jsonData.getString("is_success"))) {
					String member_id = jsonData.getString("member_id");
					String sub_account_no = jsonData.getString("sub_account_no");

					result.put("code", "1");
					result.put("is_success", "success");
					result.put("member_id", member_id);
					result.put("sub_account_no", sub_account_no);
					result.put("main_account_no", "58453427229");
					result.put("payee_card_name", "新增企业八");
				} else {
					result.put("code", "0");
					result.put("is_success", "false");
					result.put("remark", jsonData.getString("error_message"));
				}
			} else {
				result.put("code", "0");
				result.put("is_success", "false");
				result.put("remark", data);
			}
		} catch (Exception e) {
			result.put("code", "0");
			result.put("is_success", "false");
			result.put("remark", e.getMessage());
		}

		return result;
	}

	@CrossOrigin(origins = "*")
	@PostMapping(value = "/enterpriseRegister")	// 企业注册
	public Map<String,String> enterpriseRegister(@RequestBody(required = false) Map<String, String> params) {
		Map<String, String> result = new HashMap<>();
		params = new HashMap<>();

		String gatewayUrl = "http://test.tc.mybank.cn/gop/gateway.do";
		try {
			Map<String, String> send;

			params.put("version", "2.0");
			params.put("partner_id", partner_id);
			params.put("charset", "UTF-8");
			params.put("service", "mybank.tc.user.enterprise.register");
			params.put("uid", "3e80bad757fe423492c4278074ca3b95");
			params.put("member_name", "模拟企业户");
			params.put("enterprise_name", "模拟企业户一");

			send = MagCore.paraFilter2(params);
			String sign = MagCore.buildRequestByTWSIGN(send,"utf-8", GatewayConstant.KEY_STORE_NAME);
			send.put("sign", sign);
			send.put("sign_type", "TWSIGN");
			String data = doHttpClientPost(gatewayUrl, send);

			JSONObject jsonData = new JSONObject(data);
			if (!StringUtils.isEmpty(jsonData.getString("is_success"))) {
				if ("T".equals(jsonData.getString("is_success"))) {
					String member_id = jsonData.getString("member_id");
					String sub_account_no = jsonData.getString("sub_account_no");

					result.put("code", "1");
					result.put("is_success", "success");
					result.put("member_id", member_id);
					result.put("sub_account_no", sub_account_no);
					result.put("main_account_no", "58453427229");
					result.put("payee_card_name", "新增企业八");
				} else {
					result.put("code", "0");
					result.put("is_success", "false");
					result.put("remark", jsonData.getString("error_message"));
				}
			} else {
				result.put("code", "0");
				result.put("is_success", "false");
				result.put("remark", data);
			}
		} catch (Exception e) {
			result.put("code", "0");
			result.put("is_success", "false");
			result.put("remark", e.getMessage());
		}

		return result;
	}

	@CrossOrigin(origins = "*")
	@PostMapping(value = "/transfer")	// 转账
	public Map<String,String> transfer(@RequestBody(required = false) Map<String, String> params) {
		Map<String, String> result = new HashMap<>();
		if (ObjectUtils.isEmpty(params)) {
			params = new HashMap<>();
		}

		String gatewayUrl = "http://test.tc.mybank.cn/gop/gateway.do";
		try {
			Map<String, String> send;
			params.put("charset", GatewayConstant.charset_utf_8);
			params.put("partner_id", partner_id);
			params.put("service", GatewayConstant.transfer);
			params.put("version", GatewayConstant.version);
			params.put("outer_trade_no", produceTradeNo());
			params.put("fundin_account_type", GatewayConstant.account_type);
			params.put("fundout_account_type", GatewayConstant.account_type);
			params.put("transfer_amount", "100");
			params.put("notify_url", "http://www.fyez56.com/bank/transferNotify");
//			params.put("fundin_uid", partner_id);
//			params.put("fundout_uid", "147852369@qq.com");
			System.out.println(params);
			send = MagCore.paraFilter2(params);
			String sign = MagCore.buildRequestByTWSIGN(send,"utf-8", GatewayConstant.KEY_STORE_NAME);
			send.put("sign", sign);
			send.put("sign_type", GatewayConstant.sign_type);
			String data = doHttpClientPost(gatewayUrl, send);
			
			JSONObject jsonData = new JSONObject(data);
			if (!StringUtils.isEmpty(jsonData.getString("is_success"))) {
				if ("T".equals(jsonData.getString("is_success"))) {
					String memo = jsonData.getString("memo");

					result.put("code", "1");
					result.put("is_success", "success");
					result.put("remark", memo);
				} else {
					result.put("code", "0");
					result.put("is_success", "false");
					result.put("remark", jsonData.getString("error_message"));
				}
			} else {
				result.put("code", "0");
				result.put("is_success", "false");
				result.put("remark", data);
			}
		} catch (Exception e) {
			result.put("code", "0");
			result.put("is_success", "false");
			result.put("remark", e.getMessage());
		}

		return result;
	}

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/subaccount") // 充值
    public Map<String,String> subaccount(@RequestBody(required = false) Map<String, String> params) {
		Map<String, String> result = new HashMap<>();
		if (ObjectUtils.isEmpty(params)) {
			params = new HashMap<>();
		}

		String gatewayUrl = "http://test.tc.mybank.cn/gop/gateway.do";
		try {
			Map<String, String> send;
			params.put("charset", GatewayConstant.charset_utf_8);
			params.put("partner_id", partner_id);
			params.put("service", GatewayConstant.subaccount);
			params.put("version", GatewayConstant.version);

			params.put("payer_card_no","20131105154925");
			params.put("payer_card_name","张三");
			params.put("payee_card_no","5845342722900644096");
			params.put("payee_card_name","新增企业八");
			params.put("amount", "10000");
			params.put("payer_remark","汇款");
			params.put("notify_url", "http://www.fyez56.com/fyez/bank/transferNotify");

			System.out.println(params);
			send = MagCore.paraFilter2(params);
			String sign = MagCore.buildRequestByTWSIGN(send,"utf-8", GatewayConstant.KEY_STORE_NAME);
			send.put("sign", sign);
			send.put("sign_type", GatewayConstant.sign_type);

			String data = doHttpClientPost(gatewayUrl, send);

			JSONObject jsonData = new JSONObject(data);
			if (!StringUtils.isEmpty(jsonData.getString("is_success"))) {
				if ("T".equals(jsonData.getString("is_success"))) {
					String memo = jsonData.getString("memo");

					result.put("code", "1");
					result.put("is_success", "success");
					result.put("remark", memo);
					result.put("payee_card_no", "5845342722900644096");
					result.put("payee_card_name", "新增企业八");
				} else {
					result.put("code", "0");
					result.put("is_success", "false");
					result.put("remark", jsonData.getString("error_message"));
				}
			} else {
				result.put("code", "0");
				result.put("is_success", "false");
				result.put("remark", data);
			}
		} catch (Exception e) {
			result.put("code", "0");
			result.put("is_success", "false");
			result.put("remark", e.getMessage());
		}

		return result;
    }

	@CrossOrigin(origins = "*")
	@PostMapping(value = "/bindCard") 	// 绑卡
	public Map<String,String> bindCard(@RequestBody(required = false) Map<String, String> params) {
		Map<String, String> result = new HashMap<>();
		if (ObjectUtils.isEmpty(params)) {
			params = new HashMap<>();
		}

		String gatewayUrl = "http://test.tc.mybank.cn/gop/gateway.do";
		try {
			Map<String, String> send;

			params.put("charset", "UTF-8");
			params.put("partner_id", "200002007807");
			params.put("service", "mybank.tc.user.bankcard.bind");
			params.put("account_name", "测试用户");
			params.put("bank_account_no", "6225885425699746");
			params.put("bank_branch", "浦东支行");
			params.put("bank_code", "CMB");
			params.put("bank_name", "招商银行");
//			params.put("branch_no", "123212524541");
			params.put("card_attribute", "C");
			params.put("card_type", "DC");
			params.put("city", "上海市");
			params.put("pay_attribute", "normal");
			params.put("province", "上海市");
			params.put("reserved_mobile", "13866668888");
			params.put("uid", "779b29f273fe4eb8b492371732322b11");
			params.put("version", "2.0");;
			params.put("timestamp", "20170106134055");
			params.put("version", "2.0");
			params.put("sign_type", "TWSIGN");

			send = MagCore.paraFilter2(params);
			String sign = MagCore.buildRequestByTWSIGN(send,"utf-8", GatewayConstant.KEY_STORE_NAME);
			send.put("sign", sign);
			send.put("sign_type", "TWSIGN");
			String data = doHttpClientPost(gatewayUrl, send);

			JSONObject jsonData = new JSONObject(data);
			if (!StringUtils.isEmpty(jsonData.getString("is_success"))) {
				if ("T".equals(jsonData.getString("is_success"))) {
					String bank_id = jsonData.getString("bank_id");

					result.put("code", "1");
					result.put("is_success", "success");
					result.put("bank_id", bank_id);
				} else {
					result.put("code", "0");
					result.put("is_success", "false");
					result.put("remark", jsonData.getString("error_message"));
				}
			} else {
				result.put("code", "0");
				result.put("is_success", "false");
				result.put("remark", data);
			}
		} catch (Exception e) {
			result.put("code", "0");
			result.put("is_success", "false");
			result.put("remark", e.getMessage());
		}

		return result;
	}

	@CrossOrigin(origins = "*")
	@PostMapping(value = "/payToCard") 	// 提现
	public Map<String,String> payToCard(@RequestBody(required = false) Map<String, String> params) {
		Map<String, String> result = new HashMap<>();
		if (ObjectUtils.isEmpty(params)) {
			params = new HashMap<>();
		}

		String gatewayUrl = "http://test.tc.mybank.cn/gop/gateway.do";
		try {
			Map<String, String> send;

			params.put("charset", "UTF-8");
			params.put("partner_id", "200002007807");
			params.put("service", "mybank.tc.trade.paytocard");
			params.put("timestamp", "20170106134055");
			params.put("version", "2.0");
			params.put("sign_type", "TWSIGN");

			params.put("account_type", "BASIC");
			params.put("amount", "10");
			params.put("uid", "779b29f273fe4eb8b492371732322b11");
			params.put("return_url", "http://test.tc.mybank.cn/gop-test/mag/syncNotify.htm");
			params.put("bank_account_no", "6225885425698746");
			params.put("bank_id", "14020");
			params.put("is_web_access", "Y");
			params.put("outer_inst_order_no", produceTradeNo());
			params.put("outer_trade_no", produceTradeNo());
			params.put("white_channel_code", "MYBANK00097");
			params.put("notify_url", "http://www.fyez56.com/bank/payToCardNotify");

			send = MagCore.paraFilter2(params);
			String sign = MagCore.buildRequestByTWSIGN(send,"utf-8", GatewayConstant.KEY_STORE_NAME);
			send.put("sign", sign);
			send.put("sign_type", "TWSIGN");
			String data = doHttpClientPost(gatewayUrl, send);

			JSONObject jsonData = new JSONObject(data);
			if (!StringUtils.isEmpty(jsonData.getString("is_success"))) {
				if ("T".equals(jsonData.getString("is_success"))) {
					result.put("code", "1");
					result.put("is_success", "success");
				} else {
					result.put("code", "0");
					result.put("is_success", "false");
					result.put("remark", jsonData.getString("error_message"));
				}
			} else {
				result.put("code", "0");
				result.put("is_success", "false");
				result.put("remark", data);
			}
		} catch (Exception e) {
			result.put("code", "0");
			result.put("is_success", "false");
			result.put("remark", e.getMessage());
		}

		return result;
	}

	@CrossOrigin(origins = "*")
	@PostMapping(value = "/queryTrans") // 查询交易明细
	public Map<String,String> queryTrans(@RequestBody(required = false) Map<String, String> params) {
		Map<String, String> result = new HashMap<>();
		if (ObjectUtils.isEmpty(params)) {
			params = new HashMap<>();
		}

		String gatewayUrl = "http://test.tc.mybank.cn/gop/gateway.do";
		try {
			Map<String, String> send;

			params.put("charset", "UTF-8");
			params.put("partner_id", partner_id);
			params.put("service", "mybank.tc.trade.info.query");
			params.put("version", "2.0");
			params.put("outer_trade_no", "40fecff3bf69487aa3fa7fcdeaa2e1ee");
			params.put("return_url", "http://func121admin.vfinance.cn/gop-test/mag/syncNotify.htm");

			send = MagCore.paraFilter2(params);
			String sign = MagCore.buildRequestByTWSIGN(send,"utf-8", GatewayConstant.KEY_STORE_NAME);
			send.put("sign", sign);
			send.put("sign_type", "TWSIGN");
			String data = doHttpClientPost(gatewayUrl, send);

			JSONObject jsonData = new JSONObject(data);
			if (!StringUtils.isEmpty(jsonData.getString("is_success"))) {
				if ("T".equals(jsonData.getString("is_success"))) {
					result.put("code", "1");
					result.put("is_success", "success");
					result.put("buyer_fee", jsonData.getString("buyer_fee"));
					result.put("inner_trade_no", jsonData.getString("inner_trade_no"));
					result.put("outer_inst_order_no", jsonData.getString("outer_inst_order_no"));
					result.put("outer_trade_no", jsonData.getString("outer_trade_no"));
					result.put("product_code", jsonData.getString("product_code"));
					result.put("seller_fee", jsonData.getString("seller_fee"));
					result.put("trade_amount", jsonData.getString("trade_amount"));
					result.put("trade_status", jsonData.getString("trade_status"));
					result.put("trade_time", jsonData.getString("trade_time"));
				} else {
					result.put("code", "0");
					result.put("is_success", "false");
					result.put("remark", jsonData.getString("error_message"));
				}
			} else {
				result.put("code", "0");
				result.put("is_success", "false");
				result.put("remark", data);
			}
		} catch (Exception e) {
			result.put("code", "0");
			result.put("is_success", "false");
			result.put("remark", e.getMessage());
		}

		return result;
	}

	@CrossOrigin(origins = "*")
	@PostMapping(value = "/queryBalance") // 查询余额
	public Map<String,String> queryBalance(@RequestBody(required = false) Map<String, String> params) {
		Map<String, String> result = new HashMap<>();
		if (ObjectUtils.isEmpty(params)) {
			params = new HashMap<>();
		}

		String gatewayUrl = "http://test.tc.mybank.cn/gop/gateway.do";
		try {
			Map<String, String> send;

			params.put("charset", "UTF-8");
			params.put("partner_id", "200002007807");
			params.put("service", "mybank.tc.user.account.balance");
			params.put("timestamp", "20170106134055");
			params.put("version", "2.0");
			params.put("sign_type", "TWSIGN");
			params.put("uid", "3e80bad757fe423492c4278074ca3b95");

			send = MagCore.paraFilter2(params);
			String sign = MagCore.buildRequestByTWSIGN(send,"utf-8", GatewayConstant.KEY_STORE_NAME);
			send.put("sign", sign);
			send.put("sign_type", "TWSIGN");
			String data = doHttpClientPost(gatewayUrl, send);

			JSONObject jsonData = new JSONObject(data);
			if (!StringUtils.isEmpty(jsonData.getString("is_success"))) {
				if ("T".equals(jsonData.getString("is_success"))) {
					JSONObject account_list = jsonData.getJSONArray("account_list").getJSONObject(0);
					result.put("code", "1");
					result.put("is_success", "success");
					result.put("account_id", account_list.getString("account_id"));
					result.put("account_type", account_list.getString("account_type"));
					result.put("available_balance", account_list.getString("available_balance"));
					result.put("balance", account_list.getString("balance"));
					result.put("current_balance_direction", account_list.getString("current_balance_direction"));
					result.put("sub_account_no", account_list.getString("sub_account_no"));
					result.put("transit_amount", jsonData.getString("transit_amount"));
				} else {
					result.put("code", "0");
					result.put("is_success", "false");
					result.put("remark", jsonData.getString("error_message"));
				}
			} else {
				result.put("code", "0");
				result.put("is_success", "false");
				result.put("remark", data);
			}
		} catch (Exception e) {
			result.put("code", "0");
			result.put("is_success", "false");
			result.put("remark", e.getMessage());
		}

		return result;
	}

	@CrossOrigin(origins = "*")
	@PostMapping(value = "/queryCardbin") // 查询卡BIN
	public Map<String,String> queryCardbin(@RequestBody(required = false) Map<String, String> params) {
		Map<String, String> result = new HashMap<>();
		if (ObjectUtils.isEmpty(params)) {
			params = new HashMap<>();
		}

		String gatewayUrl = "http://test.tc.mybank.cn/gop/gateway.do";
		try {
			Map<String, String> send;

			params.put("charset", "UTF-8");
			params.put("partner_id", "200002007807");
			params.put("service", "mybank.tc.user.cardbin.query");
			params.put("version", "2.0");;
			params.put("timestamp", "20170106134055");
			params.put("sign_type", "TWSIGN");
			params.put("bank_account_no", "6214835530629805");
			send = MagCore.paraFilter2(params);
			String sign = MagCore.buildRequestByTWSIGN(send,"utf-8", GatewayConstant.KEY_STORE_NAME);
			send.put("sign", sign);
			send.put("sign_type", "TWSIGN");
			String data = doHttpClientPost(gatewayUrl, send);

			JSONObject jsonData = new JSONObject(data);
			if (!StringUtils.isEmpty(jsonData.getString("is_success"))) {
				if ("T".equals(jsonData.getString("is_success"))) {
					JSONObject card_bin_info = jsonData.getJSONObject("card_bin_info");

					result.put("code", "1");
					result.put("is_success", "success");
					result.put("bank_name", card_bin_info.getString("bank_name"));
					result.put("branch_no", card_bin_info.getString("branch_no"));
					result.put("card_bin", card_bin_info.getString("card_bin"));
					result.put("card_type", card_bin_info.getString("card_type"));
				} else {
					result.put("code", "0");
					result.put("is_success", "false");
					result.put("remark", jsonData.getString("error_message"));
				}
			} else {
				result.put("code", "0");
				result.put("is_success", "false");
				result.put("remark", data);
			}
		} catch (Exception e) {
			result.put("code", "0");
			result.put("is_success", "false");
			result.put("remark", e.getMessage());
		}

		return result;
	}



	/**
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/getMember")
	public Map<String,String> getMember(@RequestBody Map<String, String> params) {
		Map<String, String> result = new HashMap<>();
		String gatewayUrl = "http://test.tc.mybank.cn/gop/gateway.do";
		try {
			Map<String, String> send;

			params.put("charset", "UTF-8");
			params.put("partner_id", "200002007807");
			params.put("service", "mybank.tc.user.personal.register");
			params.put("version", "2.0");
			params.put("sign_type", "TWSIGN");
			params.put("mobile", "13674401802");
			params.put("member_name", "59a2x");
			params.put("real_name", "3uu8r");

			send = MagCore.paraFilter2(params);
			String sign = MagCore.buildRequestByTWSIGN(send,"utf-8", GatewayConstant.KEY_STORE_NAME);
			send.put("sign", sign);
			send.put("sign_type", "TWSIGN");
			String data = doHttpClientPost(gatewayUrl, send);

			JSONObject jsonData = new JSONObject(data);
			if (!StringUtils.isEmpty(jsonData.getString("is_success"))) {
				if ("T".equals(jsonData.getString("is_success"))) {
					String member_id = jsonData.getString("member_id");
					String sub_account_no = jsonData.getString("sub_account_no");

					result.put("code", "1");
					result.put("is_success", "success");
					result.put("member_id", member_id);
					result.put("sub_account_no", sub_account_no);
				} else {
					result.put("code", "0");
					result.put("is_success", "false");
					result.put("remark", jsonData.getString("error_message"));
				}
			} else {
				result.put("code", "0");
				result.put("is_success", "false");
				result.put("remark", data);
			}
		} catch (Exception e) {
			result.put("code", "0");
			result.put("is_success", "false");
			result.put("remark", e.getMessage());
		}

		return result;
	}

	@CrossOrigin(origins = "*")
	@PostMapping(value = "/queryMember")
	public Map<String,String> queryMember(@RequestBody Map<String, String> params) {
		Map<String, String> result = new HashMap<>();
		String gatewayUrl = "http://test.tc.mybank.cn/gop/gateway.do";
		try {
			Map<String, String> send;

			params.put("charset", "UTF-8");
			params.put("partner_id", "200002007807");
			params.put("service", "mybank.tc.user.personal.info.query");
			params.put("timestamp", "20170106134055");
			params.put("version", "2.0");
			params.put("sign_type", "TWSIGN");

			send = MagCore.paraFilter2(params);
			String sign = MagCore.buildRequestByTWSIGN(send,"utf-8", GatewayConstant.KEY_STORE_NAME);
			send.put("sign", sign);
			send.put("sign_type", "TWSIGN");
			String data = doHttpClientPost(gatewayUrl, send);

			JSONObject jsonData = new JSONObject(data);
			if (!StringUtils.isEmpty(jsonData.getString("is_success"))) {
				if ("T".equals(jsonData.getString("is_success"))) {
					String member_id = jsonData.getString("member_id");

					result.put("code", "1");
					result.put("is_success", "success");
					result.put("member_id", member_id);
					result.put("member_name", jsonData.getString("member_name"));
					result.put("mobile", jsonData.getString("mobile"));
					result.put("real_name", jsonData.getString("real_name"));
					result.put("uid", jsonData.getString("uid"));
				} else {
					result.put("code", "0");
					result.put("is_success", "false");
					result.put("remark", jsonData.getString("error_message"));
				}
			} else {
				result.put("code", "0");
				result.put("is_success", "false");
				result.put("remark", data);
			}
		} catch (Exception e) {
			result.put("code", "0");
			result.put("is_success", "false");
			result.put("remark", e.getMessage());
		}

		return result;
	}


	@CrossOrigin(origins = "*")
	@PostMapping(value = "/queryBank")
	public Map<String,String> queryBank(@RequestBody Map<String, String> params) {
		Map<String, String> result = new HashMap<>();
		String gatewayUrl = "http://test.tc.mybank.cn/gop/gateway.do";
		try {
			Map<String, String> send;

			params.put("charset", "UTF-8");
			params.put("partner_id", "200002007807");
			params.put("service", "mybank.tc.user.bankcard.query");
			params.put("timestamp", "20170106134055");
			params.put("version", "2.0");
			params.put("sign_type", "TWSIGN");

			send = MagCore.paraFilter2(params);
			String sign = MagCore.buildRequestByTWSIGN(send,"utf-8", GatewayConstant.KEY_STORE_NAME);
			send.put("sign", sign);
			send.put("sign_type", "TWSIGN");
			String data = doHttpClientPost(gatewayUrl, send);

			JSONObject jsonData = new JSONObject(data);
			System.out.println(jsonData.toString());
			if (!StringUtils.isEmpty(jsonData.getString("bank_list"))) {
				if ("T".equals(jsonData.getJSONObject("bank_list").getString("is_success"))) {
					result.put("code", "1");
					result.put("is_success", "success");
					result.put("area_bank_list", jsonData.getString("area_bank_list"));
					System.out.println("111111111");
				} else {
					result.put("code", "0");
					result.put("is_success", "false");
					result.put("remark", jsonData.getString("error_message"));
					System.out.println("22222");
				}
			} else {
				result.put("code", "0");
				result.put("is_success", "false");
				result.put("remark", data);
				System.out.println("33333333");
			}
		} catch (Exception e) {
			result.put("code", "0");
			result.put("is_success", "false");
			result.put("remark", e.getMessage());
			System.out.println("4444444");
		}

		return result;
	}
    */

	private static String doHttpClientPost(String url, Map<String, String> params) {
		CloseableHttpResponse response=null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String rst = null;
		try {
		//创建一个post对象
		List<NameValuePair> ps = buildPostParams(params);
		HttpPost post = new HttpPost(url);
		post.setEntity(new UrlEncodedFormEntity(ps, "UTF-8"));
		//执行post请求
		response = httpClient.execute(post);
		if (response.getStatusLine().getStatusCode() == 200) {// 网关调用成功
			rst = inputStreamToStr(response.getEntity().getContent(), "UTF-8");
			System.out.println("=======================================");
			System.out.println(String.format("httpClient Post调用结果：%s", rst));
			System.out.println("=======================================");
		}
		} catch (Exception e) {
			System.out.println("=======================================");
			System.out.println(String.format("httpClient Post 请求失败：{}", e));
			System.out.println("=======================================");
		}finally {
		        try {
			          if (null != response) 
			        	  response.close();
			          if(null != httpClient)
			        	  httpClient.close();
			      	}
			        catch (IOException e) {
			          e.printStackTrace();
			        }
		      	}
		return rst;
	}

	private static List<NameValuePair> buildPostParams(Map<String, String> params) {
		if (params == null || params.size() == 0)
			return null;
		List<NameValuePair> results = new ArrayList<NameValuePair>();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			results.add(new BasicNameValuePair(key, value));
		}

		return results;
	}

	private static String inputStreamToStr(InputStream is, String charset) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line);
		}
		return new String(buffer.toString().getBytes("ISO-8859-1"), charset);
	}

	private static String produceTradeNo() {
		String a = UUID.randomUUID().toString();
		String b = a.replaceAll("-", "");
		System.out.println(b);
		return b;
	}

}
