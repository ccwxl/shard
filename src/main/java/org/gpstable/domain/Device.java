package org.gpstable.domain;


public class Device {
	
	private Long id;
	
	private String deviceNum;

	private Object sensorData;

	public Device(Long id, String deviceNum) {
		this.id=id;
		this.deviceNum=deviceNum;
	}

	public Device(String deviceNum) {
		this.deviceNum=deviceNum;
	}

	public Device(String deviceNum,String sensorData) {
		this.deviceNum=deviceNum;
		this.sensorData=sensorData;
	}

	public Device(Long id,String deviceNum,Object sensorData) {
		this.id=id;
		this.deviceNum=deviceNum;
		this.sensorData= sensorData;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceNum() {
		return deviceNum;
	}

	public void setDeviceNum(String deviceNum) {
		this.deviceNum = deviceNum;
	}

	public Object getSensorData() {
		return sensorData;
	}

	public void setSensorData(Object sensorData) {
		this.sensorData = sensorData;
	}

}
