package com.edward.ES;

public interface SensorChecker {

	/**
	 * Checks if the device that is currently running the application has a hardware gyroscope built into it.
	 * 
	 * @return True, if a gyroscope is available. False otherwise.
	 */
	public boolean IsGyroscopeAvailable();
}
