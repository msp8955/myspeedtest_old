package com.num.tasks;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import com.num.Values;
import com.num.helpers.LossHelper;
import com.num.helpers.ThreadPoolHelper;
import com.num.listeners.ResponseListener;
import com.num.models.Loss;

public class LossTask extends ServerTask{
	
	ThreadPoolHelper serverhelper;
	
	public LossTask(Context context, Map<String, String> reqParams, 
			ResponseListener listener) {
		super(context, new HashMap<String, String>(), listener);
		serverhelper = new ThreadPoolHelper(Values.THREADPOOL_MAX_SIZE,
				Values.THREADPOOL_KEEPALIVE_SEC);
	}

	@Override
	public void runTask() {
		
		try {
			
			Loss l = LossHelper.getLoss();			
			
			getResponseListener().onCompleteLoss(l);
			//serverhelper.execute(new MeasurementTask(getContext(), t, true, new FakeListener()));
			
			
		} catch (Exception e) {
			getResponseListener().onException(e);
		}
		
	}

	@Override
	public String toString() {
		return "LossTask";
	}
	
}
