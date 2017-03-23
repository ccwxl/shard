package org.gpstable.utils;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xielongwang
 * @create 2017-03-22 下午4:02
 * @email xielong.wang@nvr-china.com
 */
@MappedJdbcTypes(JdbcType.OTHER) // 可有可无
@MappedTypes(Object.class)
public class JSONTypeHandler extends BaseTypeHandler<Object> {
    private static final PGobject jsonObject = new PGobject();
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        jsonObject.setType("jsonb");
        jsonObject.setValue(parameter.toString());
        ps.setObject(i, jsonObject);
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getString(columnIndex);
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }

}
