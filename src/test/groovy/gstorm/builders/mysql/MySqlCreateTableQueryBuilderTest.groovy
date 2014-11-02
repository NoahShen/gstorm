package gstorm.builders.mysql

import gstorm.metadata.ClassMetaData
/**
 * Created by noahshen on 14-10-19.
 */
class MySqlCreateTableQueryBuilderTest extends GroovyTestCase {

    class Person {
        def name
        int age
    }

    MySqlCreateTableSqlBuilder builder
    def classMetaData

    void setUp() {
        classMetaData = new ClassMetaData(Person)
        builder = new MySqlCreateTableSqlBuilder(classMetaData)
    }

    void "test generate mysql create sql"() {
        def createSql =  builder.build().toLowerCase()
        assert createSql == "create table if not exists person (id int(11) not null auto_increment, name varchar(255), age numeric, primary key (id))"
    }
}
