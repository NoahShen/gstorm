package gstorm.builders.mysql

import gstorm.annotation.Column
import gstorm.metadata.ClassMetaData

class MySqlSelectQueryBuilderTest extends GroovyTestCase {

    class Person {
        @Column( name = "PersonID")
        Integer id

        @Column(name = "PersonName")
        def name

        @Column(name = "PersonAge")
        int age
    }

    MySqlSelectSqlBuilder builder
    ClassMetaData classMetaData

    void setUp() {
        classMetaData = new ClassMetaData(Person.class)
        builder = new MySqlSelectSqlBuilder(classMetaData)
    }

    void "test if builder is created" () {
        assertNotNull builder
    }

    void "test builds default query if nothing else is provided"() {
        assert builder.build().toLowerCase() == "select id as \"id\", name as \"name\", age as \"age\" from person"
    }

    void "test builds query with given where clause"() {
        assert builder.where("name = 'test'").build().toLowerCase() == "select id as \"id\", name as \"name\", age as \"age\" from person where name = 'test'"
    }

    void "test builds query by id"() {
        assert builder.byId().build().toLowerCase() == "select id as \"id\", name as \"name\", age as \"age\" from person where id = ?"
    }


    void "test buildSqlAndValues"() {
        builder.eq("name", "Noah").gt("age", 10)
        def result = builder.buildSqlAndValues()
        assert result.sql == "SELECT `PersonID` as \"id\", `PersonName` as \"name\", `PersonAge` as \"age\" FROM Person WHERE PersonName = ? AND PersonAge > ?"
        assert result.values.size() == 2
        assert result.values[0] == "Noah"
        assert result.values[1] == 10
    }

}

