package org.gpstable.mapper;

import org.apache.ibatis.annotations.Param;
import org.gpstable.domain.Device;
import org.gpstable.shared.TableSplit;

@TableSplit(value="device", strategy="hash",ShardColumn="deviceNum")
public interface DeviceMapper {
	
	void insert(Device device);

	Device queryByName(String deviceNum);

	Device queryByNames(Device device);

	Device query(Long id);

	void delete(Long id);

	void update(Long id);


	Device queryOneById(@Param("id") Long id,@Param("deviceNum") String deviceNum);

	void insert2(Device device);
}
