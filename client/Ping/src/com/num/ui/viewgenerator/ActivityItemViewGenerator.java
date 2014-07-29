package com.num.ui.viewgenerator;

import com.num.models.ActivityItem;
import com.num.models.Row;
import com.num.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;

public class ActivityItemViewGenerator extends ViewGenerator{

	ViewHolder holder;
	ActivityItem activityItem;

	public ActivityItemViewGenerator(ActivityItem item) {
		super(R.layout.cell_view_activityitem);
		this.activityItem = item;
		holder = new ViewHolder();
	}

	@Override
	public ViewHolder fillViewHolder(View view, LayoutInflater inflater) {
		
		holder.first =  (TextView) view.findViewById(R.id.start_title);
		holder.second =  (TextView) view.findViewById(R.id.description);		
		holder.imageview = (ImageView) view.findViewById(R.id.icon);		
		holder.linear = (LinearLayout) view.findViewById(R.id.view);
		return holder;
	}
	@Override
	public void populateView(Row item,final Context context,View view) {
				
		holder.first.setText(activityItem.getTitle());		
		holder.second.setText(activityItem.getDescription());				
		holder.imageview.setImageResource(activityItem.getImageResource());
		
		holder.linear.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if(activityItem.getMessage()!=null)
					activityItem.getHandle().sendMessage(activityItem.getMessage());
				else
					activityItem.getHandle().sendEmptyMessage(0);
			}
		});
		
	}
	
}
