package com.num.models;

import java.util.Date;

public class GraphPoint implements Comparable<GraphPoint> {
	public double x;
	public double y;
	public Date datetime;
	public String string;
	public int count;
	public boolean sortByDate = false;
	
	public void sortByDate(boolean valid) {
		sortByDate = valid;
	}
	
	public GraphPoint(double x,double y,Date date) {
		this.x = x;
		this.y = y;
		this.datetime = date;
		count = 1;
	}
	
	public void setString(String str) {
		string = str;
	}

	public int compareTo(GraphPoint another) {
		
		if(sortByDate) {
			if(this.datetime.after(another.datetime)) {
				return 1;
			}
			return -1;
		}
		
		
		if(this.y>another.y) {
			return 1;
		}
		return -1;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public void incrementCount() {
		count++;
	}
	
	public double getValue() {
		return y/count;
	}
}