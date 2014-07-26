package com.num.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.num.database.DatabaseOutput;
import com.num.database.datasource.LatencyDataSource;
import com.num.database.datasource.ThroughputDataSource;
import com.num.graph.PingGraph;
import com.num.utils.DeviceUtil;
import com.num.utils.SHA1Util;
import com.num.R;

import android.content.Context;
import android.util.Log;

public class Measurement implements MainModel{


	ArrayList<Ping> pings; 
	ArrayList<LastMile> lastMiles;
	Device device; 
	Network network; 
	Sim sim; 
	Throughput throughput;
	Loss loss;
	Ipdv ipdv;
	WarmupExperiment warmupExperiment;
	public boolean isComplete = false;
	ArrayList<Screen> screens = new ArrayList<Screen>();
	boolean isManual = false;
	


	private static String DESCRIPTION = "Details of delay in milliseconds experienced on the network for the different destination servers";

	public ArrayList<LastMile> getLastMiles() {
		return lastMiles;
	}

	public void setLastMiles(ArrayList<LastMile> lastMiles) {
		this.lastMiles = lastMiles;
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public boolean isManual() {
		return isManual;
	}

	public void setManual(boolean isManual) {
		this.isManual = isManual;
	}

	public ArrayList<Screen> getScreens() {
		return screens;
	}

	public void setScreens(ArrayList<Screen> screens) {
		this.screens = screens;
	}

	GPS gps;
	State state;
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	String time;
	String localTime;
	public String getLocalTime() {
		return localTime;
	}

	public void setLocalTime(String localTime) {
		this.localTime = localTime;
	}

	String deviceId;
	Usage usage;
	Battery battery;
	Wifi wifi;

	public Wifi getWifi() {
		return wifi;
	}

	public void setWifi(Wifi wifi) {
		this.wifi = wifi;
	}

	public Battery getBattery() {
		return battery;
	}

	public void setBattery(Battery battery) {
		this.battery = battery;
	}

	public Usage getUsage() {
		return usage;
	}

	public void setUsage(Usage usage) {
		this.usage = usage;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public Sim getSim() {
		return sim;
	}

	public void setSim(Sim sim) {
		this.sim = sim;
	}

	public Throughput getThroughput() {
		return throughput;
	}

	public void setThroughput(Throughput throughput) {
		this.throughput = throughput;
	}
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public WarmupExperiment getWarmupExperiment() {
		return warmupExperiment;
	}

	public void setWarmupExperiment(WarmupExperiment warmupExperiment) {
		this.warmupExperiment = warmupExperiment;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public ArrayList<Ping> getPings() {
		return pings;
	}

	public void setPings(ArrayList<Ping> pings) {
		this.pings = pings;
	}

	public Measurement() {
		throughput = new Throughput();
		device = new Device();
		gps = new GPS();
	}

	public GPS getGps() {
		return gps;
	}

	public void setGps(GPS gps) {
		this.gps = gps;
	}
	
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	static {
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

/*	public JSONObject toMobiperfJSON() {
		/* Example of the mobiperf ping command 
		 * [{
		    "timestamp": 1382419261277000,
		    "values": {
		        "target_ip": "\"74.125.140.103\"",
		        "max_rtt_ms": "69.6",
		        "mean_rtt_ms": "46.559999999999995",
		        "filtered_mean_rtt_ms": "44.0",
		        "packet_loss": "0.0",
		        "ping_method": "\"ping_cmd\"",
		        "packets_sent": "10",
		        "min_rtt_ms": "40.6",
		        "stddev_rtt_ms": "8.054092127608175"
		    },
		    "task_key": null,
		    "device_id": "99000342808854",
		    "properties": {
		        "cell_info": null,
		        "location_type": "network",
		        "network_type": "WIFI",
		        "location": {
		            "longitude": -84.38707806563191,
		            "latitude": 33.81981981981982
		        },
		        "is_battery_charging": true,
		        "battery_level": 45,
		        "app_version": "v2.1",
		        "device_id": "99000342808854",
		        "dn_resolvability": "IPv4 only",
		        "os_version": "INCREMENTAL:I545VRUAME7, RELEASE:4.2.2, SDK_INT:17",
		        "timestamp": 1382419261254000,
		        "ip_connectivity": "IPv4 only",
		        "carrier": "Verizon Wireless",
		        "rssi": 17
		    },
		    "parameters": {
		        "interval_sec": 5,
		        "count": 1,
		        "ping_exe": "\/system\/bin\/ping",
		        "end_time": "2013-10-23T05:20:53.288Z",
		        "priority": -2147483648,
		        "target": "www.google.com",
		        "start_time": "2013-10-22T05:20:53.286Z",
		        "parameters": null,
		        "type": "ping",
		        "key": null,
		        "packet_size_byte": 56,
		        "ping_timeout_sec": 10
		    },
		    "type": "ping",
		    "success": true
		}]
		
		  loss. ping[s]. warmup, state, network, throughput, lastmile, battery, device, sim, usage, 
		  seperate these into individual tasks
		  - Loss 
		  - Ping (Many seperate pings)
		  - Warmup (try to group them into one)
		  - State (include network, battery, device, sim) 
		  - Usage (Have one usage data for each ping)
		  - Throughput
		  - Lastmile (Find out what this is exactly) 

		JSONObject obj = new JSONObject();
		try {
			JSONObject property = new JSONObject();
			JSONObject value = new JSONObject();
			JSONObject parameter = new JSONObject();
			try {
				// Property
				putSafe(property, "cell_info", network.cellType);
				putSafe(property, "location_type", "network");
				putSafe(property, "network_type", state.networkType);
				JSONObject location = new JSONObject();
				putSafe(location, "longitude", "-84.38707806563191");
				putSafe(location, "latitude", "33.81981981981982");
				putSafe(property, "location", location);
				putSafe(property, "is_battery_charging", (battery.plugged > 0) ? "true" : "false");
				putSafe(property, "battery_level", battery.level);
				putSafe(property, "app_version", "MySpeedTest");
				putSafe(property, "device_id", deviceId);
				putSafe(property, "dn_resolvability", "NA");
				//        "os_version": "INCREMENTAL:I545VRUAME7, RELEASE:4.2.2, SDK_INT:17",
				putSafe(property, "os_version", "INCREMENTAL:I545VRUAME7, RELEASE:4.2.2, SDK_INT:17");
				putSafe(property, "timestamp", time);
				putSafe(property, "ip_connectivity", "NA");
				putSafe(property, "carrier", device.networkName);
				putSafe(property, "rssi", network.signalStrength);
				
				// Value
				putSafe(value, "testing", "myspeedtest");
				
			} catch (Exception e) {
				System.out.println(e.getLocalizedMessage());
			}
			
			putSafe(obj,"timestamp", time);
			putSafe(obj,"values", value);
			putSafe(obj,"task_key","null");
			putSafe(obj,"devide_id", deviceId);
			putSafe(obj,"properties", property);
			putSafe(obj,"parameters", parameter);
			putSafe(obj,"type", "ping");
			putSafe(obj,"success", "true");

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}*/

	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		try {
			putSafe(obj,"time", time);	
			putSafe(obj,"localtime",localTime);
			putSafe(obj,"deviceid", SHA1Util.SHA1(deviceId));
			JSONArray array = new JSONArray();
			try{
				for(Ping p: pings){
					array.put(p.toJSON());
				}
			}
			catch(Exception e){
			}
			putSafe(obj,"pings", array);
			JSONArray tmparray = new JSONArray();
			try{
				for(LastMile p: lastMiles){
					tmparray.put(p.toJSON());
				}
			}
			catch(Exception e) {
			}
			putSafe(obj,"lastmiles", tmparray);
			JSONArray array2 = new JSONArray();
			for(Screen s: screens){
				array2.put(s.toJSON());
			}
			if (screens != null) 
				putSafe(obj,"screens", array2);
			if (device != null)
				putSafe(obj,"device",device.toJSON());
			if (throughput != null)
				putSafe(obj,"throughput",throughput.toJSON());
			if (gps != null)
				putSafe(obj,"gps",gps.toJSON());
			if (battery != null)
				putSafe(obj,"battery", battery.toJSON());
			if (usage != null) 
				putSafe(obj,"usage",usage.toJSON());
			if (network != null)		
				putSafe(obj,"network",network.toJSON());
			if (warmupExperiment != null) 
				putSafe(obj,"warmup_experiment",warmupExperiment.toJSON());
			if (sim != null) 
				putSafe(obj,"sim",sim.toJSON());
			if(wifi!=null)
				putSafe(obj,"wifi", wifi.toJSON());
			if (state != null) 
				putSafe(obj,"state",state.toJSON());
			if(isManual)
				putSafe(obj, "isManual", 1);
			else
				putSafe(obj, "isManual", 0);
			if(loss!=null)
				putSafe(obj, "loss", loss.toJSON());
			if(ipdv!=null)
				putSafe(obj, "delay_variation", ipdv.toJSON());
		} catch (Exception e) {
			e.printStackTrace();
		}
		printJSON(obj.toString());
		return obj;
	}

	public void putSafe(JSONObject obj,String key,Object text){
		try {
			obj.put(key,text);
		} catch (JSONException e) {
			
		}
	}

	public String getTitle() {

		return "Latency";
	}

	public ArrayList<Row> getDisplayData(Context context){
		ArrayList<Row> data = new ArrayList<Row>();
		try{
		data.add(new Row("ROUND TRIP"));
		
		int pingMax = 1;
		for(Ping p: pings) pingMax = Math.max((int)p.measure.getAverage(), pingMax);
		pingMax*=1.2;
		
		for(Ping p: pings){
			ArrayList<String> str = new ArrayList<String>();
			if (p != null) {
				if (p.measure != null) {
					if(p.getDst().getTagname().equals("localhost")) continue;					
					try {						
						data.add(new Row(new PingGraph(p, pingMax)));						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		} catch (Exception e) {
			ClientLog.log(context, e, "Measurement Display");
		}
		/*data.add(new Row("FIRST HOP"));
		for(LastMile p: lastMiles){
			ArrayList<String> str = new ArrayList<String>();
			if (p != null) {
				if (p.measure != null) {
					try {
						if(p.getDst().getTagname().equals("localhost")) continue;
						data.add(new Row(p.getDst().getTagname(),(int)p.measure.getAverage()*100/pingMax,((int)p.measure.getAverage()) +" ms"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		if(isComplete) {
			data.add(new Row("GRAPHS"));
			LatencyDataSource dataSource = new LatencyDataSource(context);
			String connection = DeviceUtil.getNetworkInfo(context);	
			HashMap<String,ArrayList<GraphPoint>> graphPoints = dataSource.getGraphData();
			GraphData graphdata = new GraphData(graphPoints.get("ping"));
			graphdata.setxAxisTitle("Historical trend of Roundtrip tests for " + connection);				
			data.add(new Row(graphdata));
			
			//GraphData graphdata2 = new GraphData(graphPoints.get("firsthop"));
			//graphdata2.setxAxisTitle("Historical trend of FirstHop tests for " + connection);				
			//data.add(new Row(graphdata2));
		}*/
		return data;
	}

	public int getIcon() {

		return R.drawable.png;
	}

	public void setLoss(Loss loss) {
		this.loss = loss;
		this.ipdv = loss.ipdv;
		
	}

	public static void printJSON(String str){
		if(str.length()>4000){
			Log.d("Debug", str.substring(0, 4000));
			printJSON(str.substring(4000));
		}
		else
			Log.d("Debug", str);
	}

}
