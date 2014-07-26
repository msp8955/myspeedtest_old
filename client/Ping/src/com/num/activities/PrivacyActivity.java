package com.num.activities;


import java.util.HashMap;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.apps.analytics.easytracking.TrackedActivity;
import com.num.R;
import com.num.Values;
import com.num.helpers.ThreadPoolHelper;
import com.num.helpers.UserDataHelper;
import com.num.listeners.BaseResponseListener;
import com.num.models.Battery;
import com.num.models.Censorship;
import com.num.models.Device;
import com.num.models.GPS;
import com.num.models.LastMile;
import com.num.models.Link;
import com.num.models.Loss;
import com.num.models.Measurement;
import com.num.models.Network;
import com.num.models.Ping;
import com.num.models.Sim;
import com.num.models.Throughput;
import com.num.models.Traceroute;
import com.num.models.TracerouteEntry;
import com.num.models.Usage;
import com.num.models.WarmupExperiment;
import com.num.models.Wifi;
import com.num.tasks.UrlTask;
import com.num.utils.PreferencesUtil;


public class PrivacyActivity extends TrackedActivity 
{

	private Activity activity;
	private ThreadPoolHelper serverhelper;
	final String mimeType = "text/html";
	private Button acceptButton;
	private Button rejectButton;
	public String serviceTag = "PerformanceService";
	
	public static final String SETTINGS_FILE_NAME = "PingSettings";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		
		Values session = (Values) this.getApplicationContext();
		UserDataHelper userhelp = new UserDataHelper(this);
		
		/* Skip if setting fields are filled */
		if(!session.DEBUG&&PreferencesUtil.isAccepted(this)){
			finish();
			Intent myIntent = null;
			if(!PreferencesUtil.contains("dataLimit",activity)){
				myIntent = new Intent(activity, DataCapActivity.class);
			}
			else if(!PreferencesUtil.contains("billingCost",activity) && userhelp.getDataCap() == UserDataHelper.PREPAID){
				myIntent = new Intent(activity, PrepaidActivity.class);
			}
			else if(!PreferencesUtil.contains("billingCycle",activity) && userhelp.getDataCap()!=UserDataHelper.NONE){
				myIntent = new Intent(activity, BillingCycleActivity.class);
			}
			else if(!PreferencesUtil.contains("billingCost",activity) && userhelp.getDataCap()!=UserDataHelper.NONE){
				myIntent = new Intent(activity, BillingCostActivity.class);
			}
			else if(!PreferencesUtil.contains("emailData", activity)){
				myIntent = new Intent(activity, EmailActivity.class);
			}
			else {
				myIntent = new Intent(activity, MainActivity.class);
			}
			startActivity(myIntent);
		}
		
		setContentView(R.layout.activity_privacy);
		
		activity = this;
				
		serverhelper = new ThreadPoolHelper(5,10);
		serverhelper.execute(new UrlTask(activity,new HashMap<String,String>(), "http://ruggles.gtnoise.net/static/Conditions_of_Use.html", new UrlListener()));

		acceptButton = (Button) findViewById(R.id.privacy_accept_button);
		rejectButton = (Button) findViewById(R.id.privacy_decline_button);
		
		rejectButton.setOnClickListener(new OnClickListener()  {
			public void onClick(View v) {	
				finish();
			}
		});
		
		acceptButton.setOnClickListener(new OnClickListener()  {
			public void onClick(View v) {	
				finish();
				PreferencesUtil.acceptConditions(activity);
				
				Intent myIntent = new Intent(v.getContext(), DataCapActivity.class);
                startActivity(myIntent);
				
			}
		});
		

	}	
	
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent!=null){
	        intent.getExtras();
        }
    }
	

	@SuppressLint("HandlerLeak")
	private Handler UrlHandler = new Handler() {
		public void  handleMessage(Message msg) {
			try {
				String data = (String)msg.obj;
				ProgressBar pb = (ProgressBar) findViewById(R.id.privacy_progress);
				
				pb.setVisibility(View.INVISIBLE);
				WebView webview = (WebView) findViewById(R.id.privacy_text);
				webview.loadData(data,mimeType,null);
				webview.setVisibility(View.VISIBLE);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private class UrlListener extends BaseResponseListener{

		public void onComplete(String response) {
			Message msg = new Message();
			msg.obj = response;
			UrlHandler.sendMessage(msg);			
		}

		public void onCompletePing(Ping response) {
			// TODO Auto-generated method stub
			
		}

		public void onCompleteMeasurement(Measurement response) {
			// TODO Auto-generated method stub
			
		}

		public void onCompleteDevice(Device response) {
			// TODO Auto-generated method stub
			
		}

		public void onCompleteBattery(Battery response) {
			// TODO Auto-generated method stub
			
		}

		public void onUpdateProgress(int val) {
			// TODO Auto-generated method stub
			
		}

		public void onCompleteGPS(GPS gps) {
			// TODO Auto-generated method stub
			
		}

		public void onCompleteUsage(Usage usage) {
			// TODO Auto-generated method stub
			
		}

		public void onCompleteThroughput(Throughput throughput) {
			// TODO Auto-generated method stub
			
		}

		public void makeToast(String text) {
			// TODO Auto-generated method stub
			
		}

		public void onCompleteSignal(String signalStrength) {
			// TODO Auto-generated method stub
			
		}

		public void onCompleteWifi(Wifi wifiList) {
			// TODO Auto-generated method stub
			
		}

		public void onCompleteNetwork(Network network) {
			// TODO Auto-generated method stub
			
		}

		public void onCompleteSIM(Sim sim) {
			// TODO Auto-generated method stub
			
		}

		public void onFail(String response) {
			// TODO Auto-generated method stub
			
		}

		public void onCompleteSummary(JSONObject Object) {
			// TODO Auto-generated method stub
			
		}

		public void onCompleteLastMile(LastMile lastMile) {
			// TODO Auto-generated method stub
			
		}

		public void onUpdateUpLink(Link link) {
			// TODO Auto-generated method stub
			
		}

		public void onUpdateDownLink(Link link) {
			// TODO Auto-generated method stub
			
		}

		public void onUpdateThroughput(Throughput throughput) {
			// TODO Auto-generated method stub
			
		}

		public void onCompleteTraceroute(Traceroute traceroute) {
			// TODO Auto-generated method stub
			
		}

		public void onCompleteTracerouteHop(TracerouteEntry traceroute) {
			// TODO Auto-generated method stub
			
		}

		public void onCompleteWarmupExperiment(WarmupExperiment experiment) {
			// TODO Auto-generated method stub
			
		}

		public void onCompleteLoss(Loss loss) {
			// TODO Auto-generated method stub
			
		}

		public void onCompleteCensorship(Censorship censorshipe) {
			// TODO Auto-generated method stub
			
		} 
	}
}