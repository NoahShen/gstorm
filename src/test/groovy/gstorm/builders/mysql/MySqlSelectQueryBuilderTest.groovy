package gstorm.builders.mysql

import gstorm.metadata.ClassMetaData

class MySqlSelectQueryBuilderTest extends GroovyTestCase {

    class Person {
        Integer id
        def name
        int age
    }

    MySqlSelectQueryBuilder builder
    ClassMetaData classMetaData

    void setUp() {
        classMetaData = new ClassMetaData(Person.class)
        builder = new MySqlSelectQueryBuilder(classMetaData)
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

}

