package com.num.tasks;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import com.num.Values;
import com.num.database.datasource.ThroughputDataSource;
import com.num.helpers.ThreadPoolHelper;
import com.num.helpers.ThroughputHelper;
import com.num.listeners.FakeListener;
import com.num.listeners.ResponseListener;
import com.num.models.Throughput;

public class ThroughputTask extends ServerTask{
	
	ThreadPoolHelper serverhelper;
	
	public ThroughputTask(Context context, Map<String, String> reqParams, ResponseListener listener) {
		super(context, new HashMap<String, String>(), listener);
		serverhelper = new ThreadPoolHelper(Values.THREADPOOL_MAX_SIZE, Values.THREADPOOL_KEEPALIVE_SEC);
	}

	@Override
	public void runTask() {
		try {
			Throughput t = ThroughputHelper.getThroughput(getContext(),getResponseListener());			
			//String connection = DeviceUtil.getNetworkInfo(getContext());			
			ThroughputDataSource datasource = new ThroughputDataSource(getContext());			
			datasource.insert(t);
			getResponseListener().onCompleteThroughput(t);
			serverhelper.execute(new MeasurementTask(getContext(), t, true, new FakeListener()));			
		} catch (Exception e) {
			getResponseListener().onException(e);
		}
		
	}

	@Override
	public String toString() {
		return "ThroughputTask";
	}
	
}
