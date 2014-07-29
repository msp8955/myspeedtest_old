package com.num.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import com.num.Values;
import com.num.helpers.ThreadPoolHelper;
import com.num.listeners.ResponseListener;
import com.num.models.Censorship;
import com.num.models.DNSConsistencyTest;

public class CensorshipTask extends ServerTask{
	
	ThreadPoolHelper serverhelper;
	private String[] targets = {"www.google.com","www.facebook.com","www.twitter.com"};
	public CensorshipTask(Context context, Map<String, String> reqParams, 
			ResponseListener listener) {
		super(context, new HashMap<String, String>(), listener);
		serverhelper = new ThreadPoolHelper(Values.THREADPOOL_MAX_SIZE,
				Values.THREADPOOL_KEEPALIVE_SEC);
	}

	@Override
	public void runTask() {
		Censorship c = new Censorship(this.getContext());
		ArrayList<DNSConsistencyTest> DNSTests = new ArrayList<DNSConsistencyTest>();
		for(String t:targets){
			DNSConsistencyTest test = new DNSConsistencyTest(t);
			test.run();
			DNSTests.add(test);
		}
		c.submitResults(DNSTests);
		getResponseListener().onCompleteCensorship(c);
	}

	@Override
	public String toString() {
		return "CensorshipTask";
	}
	
}
