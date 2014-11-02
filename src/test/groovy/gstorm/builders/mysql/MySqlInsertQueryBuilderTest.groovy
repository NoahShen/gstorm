package gstorm.builders.mysql

import gstorm.metadata.ClassMetaData

class MySqlInsertQueryBuilderTest extends GroovyTestCase {

    class Person {
        def name
        int age
    }

    def builder
    def classMetaData

    void setUp() {
        classMetaData = new ClassMetaData(Person.class)
        builder = new MySqlInsertSqlBuilder(classMetaData)
    }

    void "test if builder is created" () {
        assertNotNull builder
    }

    void "test the generated insert query" () {
        assert builder.build().toLowerCase() == "insert into person (name, age) values (?, ?)"
    }

}

