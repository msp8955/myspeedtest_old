package com.num.tasks;

import java.util.Map;

import android.content.Context;

import com.num.listeners.ResponseListener;
import com.num.models.Measurement;
import com.num.utils.BatteryUtil;

/*
 * Measurement Task 
 * set tasks to run and give ip address to ping and more
 * 
 * Call another task to backend
 * 
 * 
 */
public class BatteryTask extends ServerTask{
	
	public BatteryTask(Context context, Map<String, String> reqParams, ResponseListener listener) {
		super(context, reqParams, listener);
	}
 
	@Override
	public void runTask() {
		Measurement m = new Measurement();
		BatteryUtil b = new BatteryUtil();
		b.getBattery(getContext(),m);
		this.getResponseListener().onCompleteBattery(m.getBattery());
		
	}

	@Override
	public String toString() {
		return "Device Task";
	}
	

}
