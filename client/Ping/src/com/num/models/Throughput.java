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
		
		if(isComplete) {
			data.add(new Row("TEST RESULT"));
		}else {
			data.add(new Row("TEST IN PROGRESS ..."));
		}
		
		//Display download throughput
		if (downL>0) {
			data.add(new Row(new LinkGraph(downLink, Values.DOWNLINK_DURATION, "Download", "downlink")));
		}
		
		//Display upload throughput
		if (upL>0) {
			data.add(new Row(new LinkGraph(upLink, Values.UPLINK_DURATION, "Upload", "uplink")));
		}

		return data;
	}

	public int getIcon() {

		return R.drawable.throughput;
	}



}
