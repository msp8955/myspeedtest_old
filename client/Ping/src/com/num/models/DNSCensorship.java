package com.num.models;

import java.util.HashMap;

public class DNSCensorship {
	private String[] targets;
	private String controlResolver;
	private HashMap<String,Boolean> results;
	
	public DNSCensorship(String[] t){
		setTargets(t);
		setControlResolver("8.8.8.8");
	}
	
	public DNSCensorship(String[] t, String r){
		setTargets(t);
		setControlResolver(r);
	}

	public String getControlResolver() {
		return controlResolver;
	}

	public void setControlResolver(String controlResolver) {
		this.controlResolver = controlResolver;
	}

	public String[] getTargets() {
		return targets;
	}

	public void setTargets(String[] targets) {
		this.targets = targets;
	}

	public HashMap<String,Boolean> getResults() {
		return results;
	}

	public void setResults(HashMap<String,Boolean> results) {
		this.results = results;
	}
}
