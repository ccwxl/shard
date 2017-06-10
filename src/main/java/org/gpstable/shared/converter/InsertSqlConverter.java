package org.gpstable.shared.converter;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;

/**
 * @author xielongwang
 * @create 2017-03-22 上午9:28
 * @email xielong.wang@nvr-china.com
 */
@Deprecated
public class InsertSqlConverter extends AbstractSqlConverter {
    public InsertSqlConverter() {
    }

    @Override
    protected String doConvert(Statement statement,String baseTablName,String convertTableName) {
        if(!(statement instanceof Insert)) {
            throw new IllegalArgumentException("The argument statement must is instance of Insert.");
        } else {
            Insert insert = (Insert)statement;
            insert.getTable().setName(convertTableName);
            return insert.toString();
        }
    }

}
