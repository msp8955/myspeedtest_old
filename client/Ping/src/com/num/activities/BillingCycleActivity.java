package com.num.activities;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import com.num.R;
import com.num.helpers.UserDataHelper;
import com.num.utils.PreferencesUtil;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BillingCycleActivity extends Activity {
	private UserDataHelper userhelp;
	private Activity activity;
	private final String[] dates = {"Don't know", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
			, "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25"
			, "26", "27", "28", "29", "30", "31"};
	private WheelView wheel;
	private boolean force;
	private Button saveButton, skipButton;
	private int previous;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billing_cycle);
		activity = this;
		Bundle extras = getIntent().getExtras();
		try{
			force = extras.getBoolean("force");
		}
		catch (Exception e){
			force = false;
		}
		
		//Skip if data is already present
		if(!force && PreferencesUtil.contains("billingCycle", this)){
			finish();
			Intent myIntent = null;
			if(!PreferencesUtil.contains("billingCost",activity) && userhelp.getDataCap() == UserDataHelper.PREPAID){
				myIntent = new Intent(activity, PrepaidActivity.class);
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
		
		if(!force){
			TextView titleText = (TextView) findViewById(R.id.configuration);
			titleText.setText(titleText.getText() + " [Step: 2 of 3]");
		}
		
		userhelp = new UserDataHelper(this);
		previous = userhelp.getBillingCycle();
		wheel = (WheelView) findViewById(R.id.billing_cycle_wheel);
		saveButton = (Button) findViewById(R.id.billing_cycle_save_button);
		skipButton = (Button) findViewById(R.id.billing_cycle_skip_button);
		ArrayWheelAdapter<String> adapter =
	            new ArrayWheelAdapter<String>(this, dates);
		adapter.setTextSize(48);
		wheel.setVisibleItems(32);
		wheel.setViewAdapter(adapter);
		wheel.setCurrentItem(previous);
		saveButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				int billingCycle;
				if(wheel.getCurrentItem()==0)
					billingCycle = 0;
				else
					billingCycle = Integer.parseInt(dates[wheel.getCurrentItem()]);
				userhelp.setBillingCycle(billingCycle);
				finish();
				Intent myIntent;
				if(force){
					finishActivity(0);
					return;					
				}
				else{
					myIntent = new Intent(v.getContext(), BillingCostActivity.class);
				}
				myIntent.putExtra("force", force);
				startActivity(myIntent);
			}
			
		});
		skipButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(!PreferencesUtil.contains("billingCycle", BillingCycleActivity.this)){
					userhelp.setBillingCycle(1);
				}
				finish();
				if(force){
					finishActivity(0);
					return;		
				}
				else{
					Intent myIntent = null;
					if(!PreferencesUtil.contains("emailData", activity)){
						myIntent = new Intent(v.getContext(), EmailActivity.class);
					}
					else {
						myIntent = new Intent(v.getContext(), MainActivity.class);
					}
					myIntent.putExtra("force", force);
					startActivity(myIntent);
				}
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.billing_cycle, menu);
		return true;
	}

}
