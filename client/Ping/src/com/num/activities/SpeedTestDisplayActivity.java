package com.num.activities;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.apps.analytics.easytracking.TrackedActivity;
import com.num.Values;
import com.num.helpers.GAnalytics;
import com.num.helpers.TaskHelper;
import com.num.helpers.ThreadPoolHelper;
import com.num.listeners.BaseResponseListener;
import com.num.models.Battery;
import com.num.models.Censorship;
import com.num.models.Device;
import com.num.models.GPS;
import com.num.models.LastMile;
import com.num.models.Link;
import com.num.models.Loss;
import com.num.models.MainModel;
import com.num.models.Measurement;
import com.num.models.Network;
import com.num.models.Ping;
import com.num.models.Row;
import com.num.models.Sim;
import com.num.models.Throughput;
import com.num.models.Traceroute;
import com.num.models.TracerouteEntry;
import com.num.models.Usage;
import com.num.models.WarmupExperiment;
import com.num.models.Wifi;
import com.num.ui.UIUtil;
import com.num.ui.adapter.ItemAdapter;
import com.num.R;

public class SpeedTestDisplayActivity extends TrackedActivity {

	private Values session;
	private TextView title;
	private ListView listview;
	private Button startTestBtn;
	
	//ImageView imageview;
	private TextView description;
	private Activity activity;
	
	private ThreadPoolHelper serverhelper;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		session = (Values) this.getApplicationContext();
		showDisplayPage();
		
		final Bundle extras = getIntent().getExtras();
		final String key = extras.getString("model_key");
		String desc = extras.getString("model_description");
		
		title.setText(key.toUpperCase());
		description.setText(desc);
		startTestBtn.setOnClickListener(new OnClickListener() {
 
			public void onClick(View arg0) {
				showLoadPage();
				serverhelper = new ThreadPoolHelper(5,10);
				serverhelper.execute(TaskHelper.getTask(key, activity, new MeasurementListener()));
				GAnalytics.log(GAnalytics.ACTION, "Click",key);
			}
 
		});
		
	}
	
	public void showLoadPage() {
//		setContentView(R.layout.load_screen);
		startTestBtn.setText("Running " + title.getText().toString().toLowerCase() + " test...");
		startTestBtn.setClickable(false);
	}
	
	public void showDisplayPage() {
		setContentView(R.layout.item_view_start_button);
		title =  (TextView) findViewById(R.id.start_title);
		listview = (ListView) findViewById(R.id.main_list_view);	
		description = (TextView) findViewById(R.id.description);
		startTestBtn = (Button) findViewById(R.id.start_test);
	}

	@Override
	public void finish(){
		super.finish();
	
		try{
		serverhelper.shutdown();
		
		} catch (Exception e) {	}
	}

	public class MeasurementListener extends BaseResponseListener{

		public void onCompletePing(Ping response) {

			//onCompleteOutput(response);
		}

		public void onCompleteDevice(Device response) {

			onCompleteOutput(response);
		}

		public void onCompleteMeasurement(Measurement response) {
			//LoadBarHandler.sendEmptyMessage(0);
			onCompleteOutput(response);
		}

		public void onCompleteOutput(MainModel model){

			Message msg2=Message.obtain(UIHandler, 0, model);
			UIHandler.sendMessage(msg2);
		}

		public void onComplete(String response) {

		}

		public void onUpdateProgress(int val){
			//Message msg=Message.obtain(progressBarHandler, 0, val);
			//progressBarHandler.sendMessage(msg);
		}

		public void onCompleteGPS(GPS response) {
			onCompleteOutput(response);

		}

		public void makeToast(String text) {
			//Message msg=Message.obtain(toastHandler, 0, text);
			//toastHandler.sendMessage(msg);

		}

		public void onCompleteSignal(String signalStrength) {

		}
		public void onCompleteUsage(Usage response) {
			System.out.println("usage completed");
			onCompleteOutput(response);

		}

		public void onCompleteThroughput(Throughput response) {
			onCompleteOutput(response);

		}

		public void onCompleteWifi(Wifi response) {
			onCompleteOutput(response);

		}

		public void onCompleteBattery(Battery response) {
			onCompleteOutput(response);

		}

		public void onCompleteNetwork(Network response) {
			onCompleteOutput(response);

		}

		public void onCompleteSIM(Sim response) {
			//onCompleteOutput(response);

		}
		
		public void onCompleteCensorship(Censorship response){
			onCompleteOutput(response);
		}

		public void onCompleteSummary(JSONObject Object) {
			// TODO Auto-generated method stub

		}

		public void onFail(String response) {
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

			onCompleteOutput(throughput);
			
			
		}

		public void onCompleteTraceroute(Traceroute traceroute) {
			
			onCompleteOutput(traceroute);
			
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
	}


	private Handler UIHandler = new Handler(){
		public void  handleMessage(Message msg) {
			
			MainModel item = (MainModel)msg.obj;
			
			startTestBtn.setText("Re-run test");
			startTestBtn.setClickable(true);
			
			ArrayList<Row> cells = item.getDisplayData(activity);

			if(cells.size()!=0){
				ItemAdapter itemadapter = new ItemAdapter(activity,cells);
				for(Row cell: cells)
					itemadapter.add(cell);
				listview.setAdapter(itemadapter);


				itemadapter.notifyDataSetChanged();
				UIUtil.setListViewHeightBasedOnChildren(listview,itemadapter);
			}

		}
	};
}