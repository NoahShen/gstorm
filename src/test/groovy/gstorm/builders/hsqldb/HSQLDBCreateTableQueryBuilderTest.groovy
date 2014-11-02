package gstorm.builders.hsqldb

import gstorm.metadata.ClassMetaData
/**
 * Created by noahshen on 14-10-19.
 */
class HSQLDBCreateTableQueryBuilderTest extends GroovyTestCase {

    class Person {
        def name
        int age
    }

    HSQLDBCreateTableSqlBuilder builder
    def classMetaData

    void setUp() {
        classMetaData = new ClassMetaData(Person)
        builder = new HSQLDBCreateTableSqlBuilder(classMetaData)
    }

    void "test generate mysql create sql"() {
        def createSql =  builder.build().toLowerCase()
        assert createSql == "create table if not exists person (id numeric generated always as identity primary key, name varchar(255), age numeric)"
    }
}
