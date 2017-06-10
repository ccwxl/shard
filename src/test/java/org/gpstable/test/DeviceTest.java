package org.gpstable.test;

import org.gpstable.domain.Device;
import org.gpstable.mapper.DeviceMapper;
import org.gpstable.mapper.UserMapper;
import org.gpstable.shared.DeviceHashStrategy;
import org.gpstable.utils.HashAlgorithmUtis;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class DeviceTest extends BaseTest  {

	@Autowired
	private DeviceMapper deviceMapper;
	@Autowired
	private UserMapper userMapper;

	@org.junit.Test
	public void Test1() {
		String aa = "{\"password\": \"bb\", \"userName\": \"ccccccccccc\"}";
		deviceMapper.insert2(new Device("C20C3E", aa));
	}
	

	@org.junit.Test
	public void Test2() {
		Device device = deviceMapper.queryOneById(107L, "C20C3E");
		System.out.println(device);
	}

	@org.junit.Test
	public void Test3() {
		ArrayList<String> arrayList = (ArrayList<String>) userMapper.deviceSelect();
		for (String deviceNum : arrayList) {
			String tableAlgorithm = HashAlgorithmUtis.hashTableAlgorithm("device", deviceNum);
			System.out.println(deviceNum + "存入" + tableAlgorithm);
			deviceMapper.insert(new Device(deviceNum));
			System.out.println(deviceMapper.queryByName(deviceNum));
			System.out.println(deviceMapper.queryByNames(new Device(deviceNum)));
		}
	}

	@Test
	public void Test4() {
		int last = "10000022".hashCode() % 99;
		System.out.println(last);
	}
	@Test
	public void test5(){
		System.out.println(DeviceHashStrategy.hashAlgorithm("C20C3E"));
	}
}
