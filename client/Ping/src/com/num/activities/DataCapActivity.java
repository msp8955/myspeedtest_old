package com.num.activities;

import com.num.R;
import com.num.helpers.UserDataHelper;
import com.num.utils.PreferencesUtil;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class DataCapActivity extends Activity {

	private Activity activity;
	private RadioGroup radioGroup;
	private int[] limit_val= {-1,0,-2,250,500,750,1000,2000,9999};
	private String[] limit_text = {"Don't have one","Don't know","Prepaid","250 MB","500 MB","750 MB","1 GB","2 GB","More than 2GB"};
	private boolean force = false;
	private UserDataHelper userhelp;
	private Button saveButton, skipButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		userhelp = new UserDataHelper(this);
		activity = this;
		try{
			force = extras.getBoolean("force");
		}
		catch (Exception e){
			force = false;
		}
		
		//Skip if data is already present
		/* Commenting these out; only collecting data cap and email address for now */
//		if(!force && PreferencesUtil.contains("dataLimit", this)){
//			finish();
//			Intent myIntent = null;
//			if(!PreferencesUtil.contains("billingCost",activity) && userhelp.getDataCap() == UserDataHelper.PREPAID){
//				myIntent = new Intent(activity, PrepaidActivity.class);
//			}
//			else if(!PreferencesUtil.contains("billingCycle",activity) && userhelp.getDataCap()!=UserDataHelper.NONE){
//				myIntent = new Intent(activity, BillingCycleActivity.class);
//			}
//			else if(!PreferencesUtil.contains("billingCost",activity) && userhelp.getDataCap()!=UserDataHelper.NONE){
//				myIntent = new Intent(activity, BillingCostActivity.class);
//			}
//			else if(!PreferencesUtil.contains("emailData", activity)){
//				myIntent = new Intent(activity, EmailActivity.class);
//			}
//			else {
//				myIntent = new Intent(activity, MainActivity.class);
//			}
//			startActivity(myIntent);
//		}
		if(!force && PreferencesUtil.contains("dataLimit", this)){
			finish();
			Intent myIntent = null;
			if(!PreferencesUtil.contains("emailData", activity)){
				myIntent = new Intent(activity, EmailActivity.class);
			}
			else {
				myIntent = new Intent(activity, MainActivity.class);
			}
			startActivity(myIntent);
		}
		
		setContentView(R.layout.activity_data_cap);
		
		int cap = userhelp.getDataCap();
		radioGroup = (RadioGroup) findViewById(R.id.data_cap_radio_group);
		LinearLayout.LayoutParams rg = new RadioGroup.LayoutParams(
				RadioGroup.LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		for(int i=0; i<limit_text.length; i++){
			RadioButton radiobutton = new RadioButton(this);
			radiobutton.setText(limit_text[i]);
			radiobutton.setId(i);
			radiobutton.setTextColor(Color.GRAY);
			if(cap==limit_val[i])
				radiobutton.setChecked(true);
			radioGroup.addView(radiobutton, rg);
		}
		saveButton = (Button) findViewById(R.id.data_cap_save_button);
		skipButton = (Button) findViewById(R.id.data_cap_skip_button);
		saveButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				int checkedRadioButton = radioGroup.getCheckedRadioButtonId();
				if(checkedRadioButton<0) return;
				userhelp.setDataCap(limit_val[checkedRadioButton]);
				finish();
				Intent myIntent;
				if(force){
					finishActivity(0);
					return;			
				}
				else{
//					if(limit_val[checkedRadioButton]==UserDataHelper.PREPAID)
//						myIntent = new Intent(v.getContext(), PrepaidActivity.class);
//					else if(limit_val[checkedRadioButton]==UserDataHelper.NONE){
//						userhelp.setBillingCost(-1);
//						myIntent = new Intent(v.getContext(), DataFormActivity.class);
//					}
//					else
//						myIntent = new Intent(v.getContext(), BillingCycleActivity.class);
					if(!PreferencesUtil.contains("emailData", activity)){
						myIntent = new Intent(v.getContext(), EmailActivity.class);
					}
					else {
						myIntent = new Intent(v.getContext(), MainActivity.class);
					}
				}
				myIntent.putExtra("force", force);
				startActivity(myIntent);
			}
			
		});
		skipButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(!PreferencesUtil.contains("dataLimit", DataCapActivity.this)){
					userhelp.setDataCap(limit_val[0]); //default to "don't know"
//					userhelp.setBillingCost(-1);
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
		getMenuInflater().inflate(R.menu.data_cap, menu);
		return true;
	}

}
