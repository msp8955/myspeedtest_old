package com.num.activities;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.google.android.apps.analytics.easytracking.TrackedActivity;
import com.num.helpers.AppRater;

import com.num.R;



public class AboutUsActivity extends TrackedActivity
{
	private Activity activity;
	private Button rateButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.about_us);

		activity = this;			
		rateButton = (Button) this.findViewById(R.id.back);		

		rateButton.setOnClickListener(new OnClickListener()  {
			public void onClick(View v) {	
				AppRater.startIntent(activity);
				finish();

			}
		});


	}	

}