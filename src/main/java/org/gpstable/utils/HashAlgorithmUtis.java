package org.gpstable.utils;

/**
 * @author xielongwang
 * @create 2017-03-22 下午1:43
 * @email xielong.wang@nvr-china.com
 */
public class HashAlgorithmUtis {

    public static  String hashTableAlgorithm(String baseTableName, Object sharedKeyValue) {

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
