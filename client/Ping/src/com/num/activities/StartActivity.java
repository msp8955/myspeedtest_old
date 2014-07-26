package com.num.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;

import com.google.android.apps.analytics.easytracking.TrackedActivity;
import com.num.R;
import com.num.helpers.UserDataHelper;
import com.num.utils.PreferencesUtil;

public class StartActivity extends TrackedActivity 
{
	private Activity activity;
	private TextView title;
	private UserDataHelper userhelp;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		activity = this;
		setContentView(R.layout.activity_start);
		title = (TextView) findViewById(R.id.start_title);
		final Animation in = new AlphaAnimation(0.0f, 1.0f);
		title.setAnimation(in);
		userhelp = new UserDataHelper(activity);
		
		in.setAnimationListener(new AnimationListener() {

			public void onAnimationEnd(Animation animation) {
				finish();
				Intent myIntent = null;
				if(!PreferencesUtil.isAccepted(activity)){
					myIntent = new Intent(activity, PrivacyActivity.class);
				}
				else if(!PreferencesUtil.contains("dataLimit",activity)){
					myIntent = new Intent(activity, DataCapActivity.class);
				}
				else if(!PreferencesUtil.contains("emailData", activity)){
					myIntent = new Intent(activity, EmailActivity.class);
				}
				/* Only collecting data cap */
//				else if(!PreferencesUtil.contains("billingCost",activity) && userhelp.getDataCap() == UserDataHelper.PREPAID){
//					myIntent = new Intent(activity, PrepaidActivity.class);
//				}
//				else if(!PreferencesUtil.contains("billingCycle",activity) && userhelp.getDataCap()!=UserDataHelper.NONE &&
//						userhelp.getDataCap() != UserDataHelper.PREPAID){
//					myIntent = new Intent(activity, BillingCycleActivity.class);
//				}
//				else if(!PreferencesUtil.contains("billingCost",activity) && userhelp.getDataCap()!=UserDataHelper.NONE){
//					myIntent = new Intent(activity, BillingCostActivity.class);
//				}
				else {
					myIntent = new Intent(activity, MainActivity.class);
				}
				startActivity(myIntent);

			}

			public void onAnimationRepeat(Animation arg0) {
				return;
			}

			public void onAnimationStart(Animation arg0) {
				return;
			}
		});


	}	



}