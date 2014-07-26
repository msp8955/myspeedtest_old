package com.num.models;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;

import com.num.R;
import com.num.Values;
import com.num.database.DatabaseOutput;
import com.num.database.datasource.ThroughputDataSource;
import com.num.graph.LinkGraph;
import com.num.utils.DeviceUtil;

public class Throughput implements MainModel{

	public Link downLink;
	public Link upLink;
	public boolean isComplete = false;


	private static String DESCRIPTION = "Download and Upload speeds";

	public String getDescription() {
		return DESCRIPTION;
	}
	public Link getDownLink() {
		return downLink;
	}
	public void setDownLink(Link downLink) {
		this.downLink = downLink;
	}
	public Link getUpLink() {
		return upLink;
	}
	public void setUpLink(Link upLink) {
		this.upLink = upLink;
	}

	public Throughput() {
		super();
		Link downLink = new Link();
		Link upLink = new Link();

	}

	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		try {			

			obj.put("downLink", downLink.toJSON());
			obj.put("upLink", upLink.toJSON());

		} catch (Exception e) {
			obj = new JSONObject();
		}

		return obj;
	}

	public String getTitle() {

		return "Throughput";
	}

	public ArrayList<Row> getDisplayData(Context context){
		ArrayList<Row> data = new ArrayList<Row>();
		int downL = -1;
		int upL = -1;
		if (downLink == null) {
			downL = -1;
		} else {
			downL = (int)downLink.speedInBits();
		}
		if (upLink == null) {
			upL = -1;
		} else {
			upL = (int)upLink.speedInBits();
		}
		
		if (downL>0) {
			data.add(new Row(new LinkGraph(downLink, Values.DOWNLINK_DURATION, "Download", "downlink")));
		}
		
		if (upL>0) {
			data.add(new Row(new LinkGraph(upLink, Values.UPLINK_DURATION, "Upload", "uplink")));
		}

		if(isComplete) {
			
			ThroughputDataSource dataSource = new ThroughputDataSource(context);
			
			DatabaseOutput output = dataSource.getOutput();
			HashMap<String,ArrayList<GraphPoint>> graphPoints = dataSource.getGraphData();
			if (output.getDouble("avg_download")>0) {
				String connection = DeviceUtil.getNetworkInfo(context);
				
				data.add(new Row("GRAPHS"));
				data.add(new Row("Avg Download",output.getDouble("avg_download") + " Mbps"));
				GraphData graphdata = new GraphData(graphPoints.get("downlink"));
				graphdata.setxAxisTitle("Historical trend of Download tests for " + connection);				
				data.add(new Row(graphdata));
				
				if (output.getDouble("avg_upload")>0) {					
					data.add(new Row("Avg Upload",output.getDouble("avg_upload") + " Mbps"));
					GraphData graphdata2 = new GraphData(graphPoints.get("uplink"));
					graphdata2.setxAxisTitle("Historical trend of Upload tests for " + connection);
					data.add(new Row(graphdata2));
				}
			
			}
			
		} else {
			data.add(new Row("In progress ..."));
		}


		return data;
	}

	public int getIcon() {

		return R.drawable.throughput;
	}



}
