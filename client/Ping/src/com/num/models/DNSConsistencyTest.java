package com.num.models;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.xbill.DNS.Cache;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

import android.content.Context;

public class DNSConsistencyTest implements MainModel{
	private String target;
	private String controlResolver;
	private boolean isModified;
	private List<Record> testList;
	private List<Record> controlList;
	private List<Record> intersection;
	private long ttl;
	private int type;
	
	
	public DNSConsistencyTest(String t){
		setTarget(t);
		setControlResolver("8.8.8.8");
	}
	
	public DNSConsistencyTest(String t, String r){
		setTarget(t);
		setControlResolver(r);
	}

	public String getControlResolver() {
		return controlResolver;
	}

	public void setControlResolver(String controlResolver) {
		this.controlResolver = controlResolver;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public boolean isModified() {
		return isModified;
	}

	public void setModified(boolean isModified) {
		this.isModified = isModified;
	}
	
	public JSONObject toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getIcon() {
		return 0;
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void run(){
		try {
			Lookup test = new Lookup(target,Type.A);
			Lookup cont = new Lookup(target,Type.A);
			SimpleResolver trustedResolver = new SimpleResolver(controlResolver);
			trustedResolver.setPort(50000);
			cont.setResolver(trustedResolver);
			Record[] testRecords = test.run();
			Record[] controlRecords = cont.run();
			if(test.getResult()==Lookup.SUCCESSFUL){
				testList = Arrays.asList(testRecords);
				controlList = Arrays.asList(controlRecords);
				intersection = testList;
				intersection.retainAll(controlList);
				this.isModified = !(intersection.size()>0);
				ttl = testList.get(0).getTTL();
				type = testList.get(0).getType();
			}
			else{
				this.isModified = true;
			}
		} catch (TextParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<Row> getDisplayData(Context context) {
		return null;
	}

	public String getDescription() {
		return null;
	}

	public String toString(){
		String res = new String();
		if(testList==null){
			res += "No IP address returned\n";
			return res;
		}
		res += "Target: "+ target + "\n";
		res += "Type: " + Type.string(type) +"\n";
		res += "TTL: " + ttl + "\n";
		for(Record r:testList){
			res +=  r.rdataToString()+"\n";
		}
		return res;
	}
}
