package org.gpstable.shared;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author xielongwang
 *
 * 标注是否需要分表
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface TableSplit {
	// 是否分表
	public boolean split() default true;

	// 表名
	public String value();

	// 获取分表策略
	public String strategy();

	public String ShardColumn();


}
