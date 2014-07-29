package com.num.activities;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.apps.analytics.easytracking.TrackedActivity;
import com.num.R;
import com.num.Values;
import com.num.database.DatabasePicker;
import com.num.graph.ChartView;
import com.num.graph.TimelineView;
import com.num.models.Application;
import com.num.models.Row;
import com.num.ui.UIUtil;
import com.num.ui.adapter.ItemAdapter;

public class IndividualAppActivity extends TrackedActivity{

	Values values;
	DatabasePicker picker;

	private Bundle bundle;
	private TextView app_name;
	private TextView app_package_name;
	private ProgressBar progress;
	private ImageView imageview;
	private TextView app_data_used;
	private TextView total_data_used;
	private TextView total_app_data_used;
	private TextView sent_data;
	private TextView recv_data;
	private TextView percent_data;
	
	private ListView listview;
	private ChartView chart;
	private ProgressBar load;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_individual_app);
		
		bundle = getIntent().getExtras();
        
		values = (Values) this.getApplicationContext();
		picker = values.getPicker();
		picker.setGraphUpdateHandler(updateGraphHandler);

	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		TextView title = (TextView) this.findViewById(R.id.individual_app_page_title);
		
		app_name = (TextView) this.findViewById(R.id.individual_app_page_name);
		app_package_name = (TextView) this.findViewById(R.id.individual_app_page_description);
		progress =  (ProgressBar) this.findViewById(R.id.individual_app_page_value);
		imageview = (ImageView) this.findViewById(R.id.individual_app_page_icon);
		app_data_used =  (TextView) this.findViewById(R.id.individual_app_page_app_usage);
		total_data_used =  (TextView) this.findViewById(R.id.individual_app_page_total_usage);
		total_app_data_used =  (TextView) this.findViewById(R.id.individual_app_page_total_data_used_by_app_value);
		sent_data = (TextView) this.findViewById(R.id.individual_app_page_sent_data_used_by_app_value);
		recv_data = (TextView) this.findViewById(R.id.individual_app_page_recv_data_used_by_app_value);
		percent_data = (TextView) this.findViewById(R.id.individual_app_page_percentage_used_by_app_value);
		
		app_name.setText(bundle.getString("app_name"));
		app_package_name.setText(bundle.getString("app_package_name"));
		
		progress.setProgress(bundle.getInt("app_progress"));
		byte[] b = bundle.getByteArray("app_icon");
		Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
		imageview.setImageBitmap(bmp);
		app_data_used.setText("App Usage: " + bundle.getString("app_data_used"));
		total_data_used.setText("Total Used: " + bundle.getString("total_data_used"));
		total_app_data_used.setText(bundle.getString("app_data_used"));
		sent_data.setText(bundle.getString("sent_data"));
		recv_data.setText(bundle.getString("recv_data"));
		percent_data.setText(bundle.getInt("app_progress") + "%");
		
		if (picker.getChartType().equals("area"))
			chart = (TimelineView) this.findViewById(R.id.individual_app_page_timeline);
		else if (picker.getChartType().equals("bar")){
			chart = (ChartView) this.findViewById(R.id.individual_app_page_barchart);
		}
		load = (ProgressBar) this.findViewById(R.id.individual_app_page_load);
		listview = (ListView) findViewById(R.id.individual_app_page_list_view);
		chart.setPicker(picker);
		title.setText(picker.getTitle());

		chart.constructGraph();
		updateGraphHandler.sendEmptyMessage(0);

		listview.setVisibility(View.INVISIBLE);
		populatePicker();
		
	}


	public Handler updateGraphHandler = new Handler() {

		public void handleMessage(Message msg) {
			loadStart.sendEmptyMessage(0);
			chart.updateGraph();
			loadEnd.sendEmptyMessage(0);

		}

	};
	
	public Handler loadStart = new Handler() {

		public void handleMessage(Message msg) {
			load.setVisibility(View.VISIBLE);
			
		}

	};
	
	public Handler loadEnd = new Handler() {

		public void handleMessage(Message msg) {
			load.setVisibility(View.INVISIBLE);
			
		}

	};
	
	private void populatePicker() {

		ArrayList<Row> cells = picker.getRows();

		if (cells.size() != 0) {
			ItemAdapter itemadapter = new ItemAdapter(this, cells);
			for (Row cell : cells)
				itemadapter.add(cell);
			listview.setAdapter(itemadapter);

			itemadapter.notifyDataSetChanged();
			UIUtil.setListViewHeightBasedOnChildren(listview, itemadapter);
		}

	}
	
}
