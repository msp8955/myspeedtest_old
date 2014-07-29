package com.num.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.apps.analytics.easytracking.TrackedActivity;
import com.num.Values;
import com.num.models.MainModel;
import com.num.models.Row;
import com.num.ui.UIUtil;
import com.num.ui.adapter.ItemAdapter;
import com.num.R;

public class DisplayActivity extends TrackedActivity {
	
	Values session;
	TextView title;
	ListView listview;
	//ImageView imageview;
	TextView description;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_view);
		session = (Values) this.getApplicationContext();
		Bundle extras = getIntent().getExtras();
		String key = extras.getString("model_key");
		
		MainModel item = session.getModel(key);
		
		
		title =  (TextView) findViewById(R.id.start_title);
		listview = (ListView) findViewById(R.id.main_list_view);
		//imageview = (ImageView) findViewById(R.id.image);
		description = (TextView) findViewById(R.id.description);
		
		//note.setVisibility(View.GONE);
		title.setText(item.getTitle().toUpperCase());
		description.setText(item.getDescription());
		
		ArrayList<Row> cells = item.getDisplayData(this);

		if(cells.size()!=0){
			ItemAdapter itemadapter = new ItemAdapter(this,cells);
			for(Row cell: cells)
				itemadapter.add(cell);
			listview.setAdapter(itemadapter);


			itemadapter.notifyDataSetChanged();
			UIUtil.setListViewHeightBasedOnChildren(listview,itemadapter);
		}

	}
}