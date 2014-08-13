package com.num.database.datasource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.http.cookie.SetCookie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.num.database.DatabaseOutput;
import com.num.database.mapping.BaseMapping;
import com.num.database.mapping.ThroughputMapping;
import com.num.helpers.GAnalytics;
import com.num.models.GraphData;
import com.num.models.GraphPoint;
import com.num.models.Link;
import com.num.models.MainModel;
import com.num.models.Model;
import com.num.models.Throughput;
import com.num.utils.DeviceUtil;

public class ThroughputDataSource extends DataSource {
	
	private final String GRAPH_TYPE = "area";
	private final String Y_AXIS_UNITS = "kbps";
	private final boolean IS_PURGE_ALLOWED = true;
		
	private final String[] MODES = {"normal","aggregate"};
	
	public ThroughputDataSource(Context context) {
		super(context);
		setDBHelper(new ThroughputMapping(context));
	}
	

	public boolean isPurgeAllowed() {
		return IS_PURGE_ALLOWED;
	}
	
	public void addRow(Link l, String type, String connectionType) {
		ContentValues value = new ContentValues();		
		
		value.put(ThroughputMapping.COLUMN_TIME, getTime());
		value.put(ThroughputMapping.COLUMN_SPEED, "" + l.speedInBits());
		value.put(ThroughputMapping.COLUMN_TYPE, type);
		value.put(ThroughputMapping.COLUMN_CONNECTION,connectionType);
		
		if(!database.isOpen()) {
			database = dbHelper.getWritableDatabase();
		}
		try {
			database.insert(dbHelper.getTableName(), null, value);
		} catch (Exception e) {
			Log.d("db", e.getLocalizedMessage());
		}
		
	}
	
	protected void insertModel(Model model) {
		try {
			String currentConnectionType = DeviceUtil.getNetworkInfo(context);
			addRow(((Throughput)model).getDownLink(), "downlink", currentConnectionType);
			addRow(((Throughput)model).getUpLink(), "uplink", currentConnectionType);
		} catch (Exception e) {
			GAnalytics.log(GAnalytics.DATABASE, "Insert Fail " + dbHelper.getDBName(),e.getMessage());
		}
	}
	
	public DatabaseOutput getOutput(String currentConnectionType) {
		
		List<Map<String,String>> allData = getDataStores();
		
		int totalUpload = 0;
		int totalDownload = 0;
		int countUpload = 0;
		int countDownload = 0;		
		
		for (Map<String,String> data : allData) {
			
			if(!data.get(ThroughputMapping.COLUMN_CONNECTION).equals(currentConnectionType)) {
				continue;
			}
			
			if(data.get(ThroughputMapping.COLUMN_TYPE).equals("uplink")){
				try {
					totalUpload+=(int)Double.parseDouble(data.get(ThroughputMapping.COLUMN_SPEED));
					countUpload++;
				} catch (Exception e) {
					continue;
				}				
			} else if(data.get(ThroughputMapping.COLUMN_TYPE).equals("downlink")){
				try {
					totalDownload+=(int)Double.parseDouble(data.get(ThroughputMapping.COLUMN_SPEED));
					countDownload++;
				} catch (Exception e) {
					continue;
				}				
			}						
		}
		
		if(countDownload==0) countDownload++;
		if(countUpload==0) countUpload++;
		
		DatabaseOutput output = new DatabaseOutput();
				
		output.add("avg_download", String.format("%.3f", (double)totalDownload/countDownload/1000));
		output.add("avg_upload", String.format("%.3f", (double)totalUpload/countUpload/1000));

		return output;
	}
	
	public DatabaseOutput getOutput() {
		return getOutput(DeviceUtil.getNetworkInfo(context));
	}
	
	public HashMap<String, ArrayList<GraphPoint>> getGraphData() {
		return getGraphData(DeviceUtil.getNetworkInfo(context));
	}
	
	public HashMap<String, ArrayList<GraphPoint>> getGraphData(String currentConnectionType) {

		List<Map<String,String>> allData = getDataStores();
		
		ArrayList<GraphPoint> downloadPoints = new ArrayList<GraphPoint>();
		ArrayList<GraphPoint> uploadPoints = new ArrayList<GraphPoint>();
		
		for (Map<String,String> data : allData) {			
			
			if(!data.get(ThroughputMapping.COLUMN_CONNECTION).equals(currentConnectionType)) {
				continue;
			}
			
			if(data.get(ThroughputMapping.COLUMN_TYPE).equals("uplink")){
				try {
					uploadPoints.add(new GraphPoint(uploadPoints.size(),extractValue(data)/1000,extractTime(data)));
				} catch (Exception e) {
					continue;
				}				
			} else if(data.get(ThroughputMapping.COLUMN_TYPE).equals("downlink")){
				try {
					downloadPoints.add(new GraphPoint(downloadPoints.size(),extractValue(data)/1000,extractTime(data)));
				} catch (Exception e) {
					continue;
				}				
			}						
		}
		
		HashMap<String,ArrayList<GraphPoint>> collection = new HashMap<String,ArrayList<GraphPoint>>();
		
		collection.put("uplink", uploadPoints);
		collection.put("downlink", downloadPoints);

		return collection;
		
	}

	@Override
	public int extractValue(Map<String, String> data) {
		return (int)Double.parseDouble(data.get(ThroughputMapping.COLUMN_SPEED));
	}

	@Override
	public Date extractTime(Map<String, String> data) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		String dateString =  data.get(ThroughputMapping.COLUMN_TIME);
		try {
			return df.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}
	
	@Override
	public String getGraphType() {
		return GRAPH_TYPE;
	}

	@Override
	public String getYAxisLabel() {
		return Y_AXIS_UNITS;
	}

	@Override
	public void aggregatePoints(GraphPoint oldP, GraphPoint newP) {
		oldP.incrementCount();
		oldP.y+=newP.y;
	}
	
	@Override
	public String[] getModes() {
		return MODES;
	}

}
