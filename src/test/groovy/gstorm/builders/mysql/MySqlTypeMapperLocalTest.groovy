package gstorm.builders.mysql

import groovy.sql.Sql
import gstorm.Gstorm
import gstorm.builders.SQLDialect
import models.Item

import java.util.logging.Level

/**
 * Created by noahshen on 14-11-14.
 */
class MySqlTypeMapperLocalTest extends GroovyTestCase {
    Gstorm gstorm
    Sql sql


    void setUp() {
        MySqlTypeMapper.instance.reset()

        MySqlTypeMapper.instance.setDefaultType("VARCHAR(64)")
        MySqlTypeMapper.instance.setType((java.lang.String), "VARCHAR(16)")
        sql = Sql.newInstance("jdbc:MySQL://localhost:3306/test", "user", "123", "com.mysql.jdbc.Driver")

//        sql = Sql.newInstance("jdbc:MySQL://192.168.7.104:3306/DianPingBA_FSSettle", "aspnet_dianping", "dp!@OpQW34bn", "com.mysql.jdbc.Driver")
        gstorm = new Gstorm(sql, SQLDialect.MYSQL)
        gstorm.enableQueryLogging(Level.INFO)

        gstorm.stormify(Item, true)

    }

    void tearDown() {
        sql.execute("DROP TABLE Item")
        sql.close()

        MySqlTypeMapper.instance.reset()
    }

    void "test should customize the types"() {
        println MySqlTypeMapper.instance.defaultType
        def mysqlMappings = sql
                .rows("show columns from Item")
                .collectEntries { [it.field, it.type] }

        assert mysqlMappings["name"] == "varchar(16)"
        assert mysqlMappings["description"] == "varchar(64)"
    }
}
