package com.learnbatch.segmentcodebatch;

import java.nio.charset.Charset;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

@Component
public class SqlUtil {
    @Autowired private ApplicationContext context;
    @Autowired private DataSource datasource;

    // SQLFileName could be your schema-all.sql
    // inject this class somewhere and call sqlUtil.runSqlFile("schema-all.sql");
    public void runSqlFile(String SQLFileName) {
        Resource resource = context.getResource(SQLFileName);
        EncodedResource encodedResource = new EncodedResource(resource, Charset.forName("UTF-8"));
        try {
            ScriptUtils.executeSqlScript(datasource.getConnection(), encodedResource);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}