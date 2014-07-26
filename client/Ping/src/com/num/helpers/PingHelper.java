package com.num.helpers;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.util.Log;

import com.num.models.Address;
import com.num.models.LastMile;
import com.num.models.Measure;
import com.num.models.Ping;
import com.num.models.WarmupExperiment;
import com.num.utils.CommandLineUtil;
import com.num.utils.ParseUtil;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class PingHelper {

	public static CommandLineUtil cmdUtil;

	public static LastMile firstHopHelp(Address address, int count) {


		LastMile p 			= null;
		int ttl 		= 1;
		String ipDst 	= address.getIp();
		String cmd 		= "ping";
		String options 	= "-n -s 56 -c 1 -t " + ttl;
		String output 	= "";

		cmdUtil = new CommandLineUtil();
		int hopCount = 1;
		output = cmdUtil.runCommand(cmd, ipDst, options);

		try{

			if (!output.contains("ttl")) {
				if (!output.contains("From")){
					while(!output.contains("From")) {
						options 	= "-n -s 56 -c 1 -t " + ++ttl;
						output = cmdUtil.runCommand(cmd, ipDst, options);
						if (ttl > 50) {
							break;
						}
					}			
				}
				ipDst = output.substring(output.indexOf("From") + 4, output.indexOf("icmp")).trim();
				options 	= "-c 5";
				output = cmdUtil.runCommand(cmd, ipDst, options);
			}		
			Measure ping_measurement = ParseUtil.PingParser(output);

			Socket conn;
			String ipSrc = "";
			try {
				conn = new Socket("www.google.com", 80);
				ipSrc = conn.getLocalAddress().toString(); 
				conn.close();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			p=new LastMile(ipSrc, address ,ping_measurement, ttl, ipDst);
		}
		catch(Exception e){
			p=new LastMile("", address ,new Measure(-1, -1, -1, -1), ttl, ipDst);
		}
		

		return p;
	}

	/**
	 * Pinghelp helps run ping command by creating cmd and inputs
	 * @return
	 */
	public static Ping pingHelp(Address address, int count) {
		Ping p 			= null;
		String ipDst 	= address.getIp();
		String cmd 		= "ping";
		double timegap = 0.5;
		String options 	= "-c " + count +" -i "+timegap;
		String output 	= "";

		cmdUtil = new CommandLineUtil();

		output = cmdUtil.runCommand(cmd, ipDst, options);
//		D("Output: " + output);
		Measure ping_measurement = ParseUtil.PingParser(output);
//		D("Measure: " + ping_measurement.toString());
		
		/* try TCP Ping if Runtime Ping does not work */
		if (ping_measurement.isDeadPing())
			return	TCPPingHelper(address, count);
		
		Socket conn;
		String ipSrc = "";
		try { // ???? what's the purpose of this socket ?????? 
			conn = new Socket("www.google.com", 80);
			ipSrc = conn.getLocalAddress().toString();
			conn.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return new Ping(ipSrc, address ,ping_measurement);
	}
	
	public static Ping TCPPingHelper(Address addr, int count) {
		T("TCPPingHelper " + addr.getIp());
		String ipDst = addr.getIp();
		Socket socket = null;
		String hostAddr = "";
		String ipSrc = "";
		SummaryStatistics ss = new SummaryStatistics();
		try {
			hostAddr = InetAddress.getByName(ipDst).getHostAddress();
			ipSrc = InetAddress.getLocalHost().toString();
			socket = new Socket(ipDst, 80);
			if (socket.isConnected()) {
				int total = 0;
				int times = 15; // let's do 15 times for now :0
				while (total < times) {
					total++;
					double start = System.currentTimeMillis();
					try {
					SocketAddress sockAddr = new InetSocketAddress(hostAddr, 80);
					socket = new Socket();
					socket.connect(sockAddr, 1000);
					} catch(SocketTimeoutException e) {
						T("Socket Request["+total+"]: Connection timed out.");
						continue;
					} catch(UnknownHostException e) {
					} catch(IOException e) {
					}
					double end = System.currentTimeMillis();
					socket.close();
					double totaltime = (end - start);
					ss.addValue(totaltime);
				}
				T("TCP Result:  " + ipDst + " (max/min/avg/std) "+ ss.getMax() 
						+ " " + ss.getMin() +" " + ss.getSum()/15 + " "
						+ ss.getStandardDeviation());
				return new Ping(ipSrc, addr ,new Measure(ss.getMax(), ss.getMin(), 
						ss.getSum()/15.0, (double) ss.getStandardDeviation()));
			} else
				T("Socket is NOT connected");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Ping(ipSrc, addr, new Measure(-1, -1, -1, -1));
	}
	
	public static void D(String x){
		Log.d("PingHelper", x);
	}
	
	public static void T(String x){
		Log.v("TCPPingHelper", x);
	}
	
	public static void warmupSequenceHelp(WarmupExperiment experiment) {
		Ping p 			= null;
		String ipDst 	= experiment.getAddress().getIp();
		String cmd 		= "ping";
		String options 	= "-c " + experiment.getTotal_count() + " -i " + experiment.getTime_gap();
		String output 	= "";

		cmdUtil = new CommandLineUtil();

		output = cmdUtil.runCommand(cmd, ipDst, options);
		
		ParseUtil.warmupParser(output,experiment);
 	}



}