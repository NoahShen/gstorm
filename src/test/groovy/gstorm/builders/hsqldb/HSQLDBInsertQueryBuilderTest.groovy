package gstorm.builders.hsqldb

import gstorm.metadata.ClassMetaData

class HSQLDBInsertQueryBuilderTest extends GroovyTestCase {

    class Person {
        def name
        int age
    }

    def builder
    def classMetaData

    void setUp() {
        classMetaData = new ClassMetaData(Person.class)
        builder = new HSQLDBInsertSqlBuilder(classMetaData)
    }

    void "test if builder is created" () {
        assertNotNull builder
    }

    void "test the generated insert query" () {
        assert builder.build().toLowerCase() == "insert into person (name, age) values (?, ?)"
    }

}

