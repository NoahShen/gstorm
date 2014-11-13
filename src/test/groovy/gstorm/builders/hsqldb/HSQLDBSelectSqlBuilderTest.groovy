package gstorm.builders.hsqldb
import gstorm.annotation.Column
import gstorm.annotation.Id
import gstorm.metadata.ClassMetaData

class HSQLDBSelectSqlBuilderTest extends GroovyTestCase {

    class Person {
        @Id
        @Column(name = "PersonID")
        Integer id

        @Column(name = "PersonName")
        def name

        @Column(name = "PersonAge")
        int age
    }

    HSQLDBSelectSqlBuilder builder
    ClassMetaData classMetaData

    void setUp() {
        classMetaData = new ClassMetaData(Person.class)
        builder = new HSQLDBSelectSqlBuilder(classMetaData)
    }

    void "test if builder is created"() {
        assertNotNull builder
    }

    void "test buildSqlAndValues"() {
        builder.eq("name", "Noah").gt("age", 10)
        def result = builder.buildSqlAndValues()
        assert result.sql == "SELECT PersonID as \"id\", PersonName as \"name\", PersonAge as \"age\" FROM Person WHERE PersonName = ? AND PersonAge > ?"
        assert result.values.size() == 2
        assert result.values[0] == "Noah"
        assert result.values[1] == 10
    }

    void "test buildSqlAndValues no where"() {
        def result = builder.buildSqlAndValues()
        assert result.sql == "SELECT PersonID as \"id\", PersonName as \"name\", PersonAge as \"age\" FROM Person"
        assert !result.values
    }


    void "test select by id"() {
        builder.idEq(123)
        def result = builder.buildSqlAndValues()
        assert result.sql == "SELECT PersonID as \"id\", PersonName as \"name\", PersonAge as \"age\" FROM Person WHERE PersonID = ?"
        assert result.values.size() == 1
        assert result.values[0] == 123
    }
}

