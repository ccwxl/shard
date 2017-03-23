package org.gpstable.shared;

/**
 * 
 * @author xielong.wang
 *	
 * 具体的分表策略
 */
public class DeviceHashStrategy  implements Strategy{

	/**
	 * 根据分表键和算法返回相应的表名
	 *
	 * @param baseTableName 	原表名
	 * @param sharedKeyValue	分表键的值
	 * @return	转换后的表名
	 */
	@Override
	public String convert2(String baseTableName, Object sharedKeyValue) {
		//生成表名
		return hashTableAlgorithm(baseTableName,sharedKeyValue);
	}

	private String hashTableAlgorithm(String baseTableName, Object sharedKeyValue) {

		String hashKey = hashAlgorithm((String) sharedKeyValue);

		if (hashKey!=null){
			return  baseTableName+hashKey;
		}
		return  baseTableName;
	}


	public static String hashAlgorithm(String sharedKeyValue) {

		int last= sharedKeyValue.hashCode()%5;

		return String.valueOf(last);
	}


}
