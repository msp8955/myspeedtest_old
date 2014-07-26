package com.num.listeners;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONObject;

import com.num.models.Battery;
import com.num.models.Censorship;
import com.num.models.Device;
import com.num.models.GPS;
import com.num.models.LastMile;
import com.num.models.Link;
import com.num.models.Loss;
import com.num.models.Measurement;
import com.num.models.Network;
import com.num.models.Ping;
import com.num.models.Sim;
import com.num.models.Throughput;
import com.num.models.Traceroute;
import com.num.models.TracerouteEntry;
import com.num.models.Usage;
import com.num.models.WarmupExperiment;
import com.num.models.Wifi;


public interface ResponseListener {

    public void onComplete(String response);
    
    public void onCompletePing(Ping response);
    
    public void onCompleteMeasurement(Measurement response);
    
    public void onCompleteDevice(Device response);
    
    public void onIOException(IOException e);


    public void onFileNotFoundException(FileNotFoundException e);

    public void onCompleteBattery(Battery response);
    
    public void onException(Exception e);
    
    public void onUpdateProgress(int val);

	public void onCompleteGPS(GPS gps);
	
	public void onCompleteUsage(Usage usage);
	
	public void onCompleteThroughput(Throughput throughput);
	
	public void onCompleteLoss(Loss loss);

	public void makeToast(String text);

	public void onCompleteSignal(String signalStrength);

	public void onCompleteWifi(Wifi wifiList);

	public void onCompleteNetwork(Network network);

	public void onCompleteSIM(Sim sim);
	
	public void onFail(String response);
	
	public void onCompleteSummary(JSONObject Object);

	public void onCompleteLastMile(LastMile lastMile);
	
	public void onUpdateUpLink(Link link);
	
	public void onUpdateThroughput(Throughput throughput);
	
	public void onUpdateDownLink(Link link);
	
	public void onCompleteTraceroute(Traceroute traceroute);
	
	public void onCompleteWarmupExperiment(WarmupExperiment experiment);

	public void onCompleteTracerouteHop(TracerouteEntry traceroute);

	public void onCompleteCensorship(Censorship censorship);

}