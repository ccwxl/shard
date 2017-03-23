package org.gpstable.shared.converter;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;

/**
 * @author xielongwang
 * @create 2017-03-22 上午9:27
 * @email xielong.wang@nvr-china.com
 */
public class DeleteSqlConverter extends AbstractSqlConverter {

    public DeleteSqlConverter() {
    }

    protected String  doConvert(Statement statement,String baseTablName,String convertTableName) {
        if(!(statement instanceof Delete)) {
            throw new IllegalArgumentException("The argument statement must is instance of Delete.");
        } else {
            Delete delete = (Delete)statement;
            delete.getTable().setName(convertTableName);
            return delete.toString();
        }
    }

}
