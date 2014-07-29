package com.num.models;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.num.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

public class Censorship implements MainModel {
	private static String DESCRIPTION = "Detect Internet censorship";
	private Context context;
	private ArrayList<DNSConsistencyTest> DNSTests;
	
	public Censorship(Context context){
		this.context = context;
	}
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		return obj;
	}

	public int getIcon() {
		return R.drawable.censorship;
	}

	public String getTitle() {
		return "Censorship";
	}

	public ArrayList<Row> getDisplayData(Context context) {
		ArrayList<Row> data = new ArrayList<Row>();
		data.add(new Row("DNS Censorship"));
		DataHandler dh = new DataHandler();
		for(DNSConsistencyTest t:DNSTests){
			Message msg = Message.obtain();
			msg.obj = t;
			if(!t.isModified()){
				data.add(new Row(new ActivityItem(t.getTarget(),
						"No DNS modification detected",dh,R.drawable.check,msg)));
			}
			else{
				data.add(new Row(new ActivityItem(t.getTarget(),
						"DNS Modification detected",dh, R.drawable.delete,msg)));
			}
		}
		return data;
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public void submitResults(ArrayList<DNSConsistencyTest> at){
		this.DNSTests = at;
	}

	private class DataHandler extends Handler{
		public void handleMessage(Message msg){
			AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
			builder1.setMessage(msg.obj.toString());
	        builder1.setCancelable(false);
	        builder1.setPositiveButton("Close",
	                new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	                dialog.dismiss();
	            }
	        });
	        AlertDialog alert11 = builder1.create();
	        alert11.show();			
		}
	}
}
