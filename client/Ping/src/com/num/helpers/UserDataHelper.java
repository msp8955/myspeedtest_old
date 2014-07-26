package com.num.helpers;

import android.content.Context;
import com.num.utils.PreferencesUtil;

public class UserDataHelper {
	
	
	Context context;
	public final static int NONE = -1;
	public final static int PREPAID = -2;
	
	public UserDataHelper(Context context){
		this.context = context;
	}
	
	public int getDataCap() {
		return PreferencesUtil.getDataInt("dataLimit", context);
	}
	public void setDataCap(int dataCap) {
		PreferencesUtil.setDataInt("dataLimit", dataCap, context);
	}
	
	public int getBillingCost() {
		return PreferencesUtil.getDataInt("billingCost", context);
	}
	public void setBillingCost(int cost) {
		PreferencesUtil.setDataInt("billingCost", cost, context);
	}
	
	public String getCurrency(){
		return PreferencesUtil.getDataString("currency", context);
	}
	public void setCurrency(String c){
		PreferencesUtil.setDataString("currency", c, context);
	}
	
	public int getDataEnable() {
		return PreferencesUtil.getDataInt("dataenable", context);
	}
	public void setDataEnable(int val) {
		PreferencesUtil.setDataInt("dataenable", val, context);
	}
	public float getPrepaidData() {
		return PreferencesUtil.getDataFloat("prepaidData", context);
	}
	public void setPrepaidData(float data) {
		PreferencesUtil.setDataFloat("prepaidData", data, context);
	}
	public String getEmailData() {
		return PreferencesUtil.getDataString("emailData", context);
	}
	public void setEmailData(String email) {
		PreferencesUtil.setDataString("emailData", email, context);
	}
	
	public boolean isFilled(){		
		return PreferencesUtil.contains("dataLimit", context) && 
				PreferencesUtil.contains("billingCost", context) &&
				PreferencesUtil.contains("currency", context);
	}

}
