package org.gpstable.shared.converter;

import net.sf.jsqlparser.statement.Statement;

/**
 * @author xielongwang
 * @create 2017-03-22 上午9:26
 * @email xielong.wang@nvr-china.com
 */
public interface SqlConverter {
    String convert(Statement var1,String baseTableName,String convertTableName );
}
