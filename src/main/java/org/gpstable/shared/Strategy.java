package org.gpstable.shared;

/**
 * 
 * @author xielong.wang
 * 分表接口
 */
public interface Strategy {
     /**
     * 传入原sql 返回分表后的sql
     * @param tableName    基本表名
     * @param sharedColumn 分表key的值
     * @return
     */
    String  convert2( String tableName, Object sharedColumn);
}  
