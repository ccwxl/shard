package org.gpstable.shared.converter;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.update.Update;

/**
 * @author xielongwang
 * @create 2017-03-22 上午9:30
 * @email xielong.wang@nvr-china.com
 */
public class UpdateSqlConverter extends AbstractSqlConverter {
    public UpdateSqlConverter() {}

    @Override
    protected String doConvert(Statement statement,String baseTablName,String convertTableName) {
        if(!(statement instanceof Update)) {
            throw new IllegalArgumentException("The argument statement must is instance of Update.");
        } else {
            Update update = (Update)statement;

            update.getTable().setName(convertTableName);
            return update.toString();
        }
    }

}
