package com.hotdms.proxy.init;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts({
    @Signature(type= StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
    })
public class MyBatisInterceptor implements Interceptor {

	private static Logger logger = LoggerFactory.getLogger(MyBatisInterceptor.class);
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		 if (invocation.getTarget() instanceof StatementHandler) {
			 addUserOrgParameter(invocation);
		 }
		 return invocation.proceed();
	}

	private void addUserOrgParameter(Invocation invocation) {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget(); 
        MetaObject metaObject = MetaObject.forObject(
                statementHandler,
                SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY,
                new DefaultReflectorFactory());
        
        // 获取成员变量mappedStatement
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        
        BoundSql boundSql = statementHandler.getBoundSql();

        String sql = boundSql.getSql();
        // logger.info("RUN SQL: " + sql);
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties arg0) {
		// TODO Auto-generated method stub
		
	}

}
