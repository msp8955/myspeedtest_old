package com.num.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.num.models.ClientLog;

import android.util.Log;

public class CommandLineUtil {
	
	/**
	 * Run command method that runs a command and returns its output as string
	 * @param cmd - Name of the command
	 * @param input - Input for the command
	 * @param options - Commands for the command
	 * @return
	 */
	public String runCommand(String cmd, String input, String options) {
		
		String runtimeInput = "/system/bin/" + cmd + " " + options + " " + input;
		return runCommand(runtimeInput);
	}
	
	public String runCommand(String cmd) {
		Process process;
		String line 		= "";
		String message 		= "";
		String runtimeInput = cmd;
		
		try {
			process = Runtime.getRuntime().exec(runtimeInput);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			message += "";
			while ((line = bufferedReader.readLine()) != null) {
				message += line + "\n";
			}
			int status = process.waitFor();
		} catch (Exception e) {								
		}
		return message;
	}
	
}