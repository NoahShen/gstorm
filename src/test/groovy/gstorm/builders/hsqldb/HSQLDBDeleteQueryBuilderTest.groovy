package gstorm.builders.hsqldb

import gstorm.metadata.ClassMetaData

class HSQLDBDeleteQueryBuilderTest extends GroovyTestCase {

    class Person {
        def name
        int age
    }

    def builder
    def classMetaData

    void setUp() {
        classMetaData = new ClassMetaData(Person.class)
        builder = new HSQLDBDeleteSqlBuilder(classMetaData)
    }

    void "test if builder is created" () {
        assertNotNull builder
    }

    void "test the generated delete query" () {
        assert builder.build().toLowerCase() == "delete from person"
    }

    void "test the generated delete query with where clause" () {
        assert builder.where("id = ?").build().toLowerCase() == "delete from person where id = ?"
    }

}

