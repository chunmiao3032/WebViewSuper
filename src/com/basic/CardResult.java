package com.basic;

import java.io.Serializable;

public class CardResult implements Serializable{
	public String ID;
	public String BANK_ID;
	public String CARD_NAME;
	public String CARD_HUMAN_AGES;// 适用年龄段
	public String CARD_DESC1;// 免息期
	public String CARD_DESC2;// 免年费政策
	public String CARD_DESC3;// 积分有效期
	public String CARD_DESC4;// 卡等级
	public String CARD_DESC5;// 卡币种
	public String CARD_DESC6;// 卡组织
	public String CARD_DESC7;// 注意事项
	public String CARD_DESC8;// 其他
	public String CARD_DESC9;// 其他
	public String CARD_DESC10;// 其他
	public String CARD_TITLE_DESC1;// 标题描述
	public String CARD_TITLE_DESC2;// 标题描述
	public String CARD_TITLE_DESC3;// 标题描述
	public String CARD_IMG_DATA;
	public String CARD_URL;
	public String APPLY_USER_COUNT;// 申请人数
	public String IS_NEW_USER; // 适合新户办卡（征信一般且没有逾期客户）
	public String IS_FAST_GET; // 白户办卡（征信好且没有信用卡客户）
	public String IS_HIGH_QUOTAR;// 高额度办卡（征信好且有信用卡客户）

	public String IS_ACTIVED;
	public String MAKE_DATE;
	public String MAKE_USER;
	public String REMARK;

}