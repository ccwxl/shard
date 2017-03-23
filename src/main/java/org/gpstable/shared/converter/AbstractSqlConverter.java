package org.gpstable.shared.converter;

import net.sf.jsqlparser.statement.Statement;

/**
 * @author xielongwang
 * @create 2017-03-22 上午9:22
 * @email xielong.wang@nvr-china.com
 */
public abstract class AbstractSqlConverter implements SqlConverter {
    public AbstractSqlConverter() {
    }

    @Override
    public String convert(Statement statement,String baseTablName,String convertTableName) {
        return this.doConvert(statement, baseTablName,convertTableName);
    }


    protected abstract String doConvert(Statement var1,String baseTablName,String convertTableName);


}