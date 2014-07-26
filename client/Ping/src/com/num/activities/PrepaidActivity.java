package com.num.activities;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import com.num.R;
import com.num.helpers.UserDataHelper;
import com.num.utils.PreferencesUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class PrepaidActivity extends Activity{
	private boolean force;
	private final String currency[] = {"USD","CNY","EUR","GBP","INR","JPY","KRW","RUB","TND","ZAR"};
	private WheelView wheel;
	private Button saveButton, skipButton;
	private EditText costInput, dataInput;
	private UserDataHelper userhelp;
	private Activity activity;
	private RadioGroup radioGroup; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prepaid);
		activity = this;
		userhelp = new UserDataHelper(this);
		Bundle extras = getIntent().getExtras();
		try{
			force = extras.getBoolean("force");
		}
		catch (Exception e){
			force = false;
		}
		//Skip if data is already present
		if(!force && PreferencesUtil.contains("prepaidData", this) && PreferencesUtil.contains("billingCost", this)){
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
		
		if(!force){
			TextView titleText = (TextView) findViewById(R.id.configuration);
			titleText.setText(titleText.getText() + " [Step: 3 of 3]");
		}
		
		saveButton = (Button) findViewById(R.id.prepaid_cost_save_button);
		skipButton = (Button) findViewById(R.id.prepaid_cost_skip_button);
		wheel = (WheelView) findViewById(R.id.prepaid_cost_wheel);
		costInput = (EditText) findViewById(R.id.prepaid_cost_input);
		dataInput = (EditText) findViewById(R.id.prepaid_data_input);
		radioGroup = (RadioGroup) findViewById(R.id.prepaid_radio_group);
		ArrayWheelAdapter<String> adapter =
	            new ArrayWheelAdapter<String>(this, currency);
		adapter.setTextSize(48);
		wheel.setVisibleItems(10);
		wheel.setViewAdapter(adapter);
		
		saveButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				String costString = costInput.getText().toString();
				String dataString = dataInput.getText().toString();
				if(costString.length()==0 || dataString.length()==0) return;
				int cost = Integer.parseInt(costString);
				float data = Float.parseFloat(dataString);
				if(radioGroup.getCheckedRadioButtonId()==1){
					data = data*1000;
				}
				String curr = currency[wheel.getCurrentItem()];
				userhelp.setCurrency(curr);
				userhelp.setBillingCost(cost);
				userhelp.setPrepaidData((float) data);
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
		skipButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(!PreferencesUtil.contains("currency", PrepaidActivity.this))
					userhelp.setCurrency("USD");
				if(!PreferencesUtil.contains("billingCost", PrepaidActivity.this))
					userhelp.setBillingCost(-1);
				if(!PreferencesUtil.contains("prepaidData", PrepaidActivity.this))
					userhelp.setPrepaidData(0f);
				finish();
				if(force){
					finishActivity(0);
					return;		
				}
				else{
					Intent myIntent = new Intent(v.getContext(), DataFormActivity.class);
					myIntent.putExtra("force", force);
					startActivity(myIntent);
				}
			}
		});
	}
	
	
}
