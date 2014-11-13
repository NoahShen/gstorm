package gstorm.builders.hsqldb

import gstorm.annotation.Column
import gstorm.annotation.Id
import gstorm.metadata.ClassMetaData

class HSQLDBUpdateSqlBuilderTest extends GroovyTestCase {

    class Person {
        @Id
        @Column(name = "PersonID")
        Integer id

        @Column(name = "PersonName")
        def name

        @Column(name = "PersonAge")
        int age
    }

    def builder
    def classMetaData

    void setUp() {
        classMetaData = new ClassMetaData(Person.class)
        builder = new HSQLDBUpdateSqlBuilder(classMetaData, new Person(name: "Noah", age: 17))
    }

    void "test if builder is created" () {
        assertNotNull builder
    }

    void "test the generated update query with where clause"() {
        builder.eq("id", 8787)
        def result = builder.buildSqlAndValues()

        assert result.sql == "UPDATE Person SET PersonName = ?, PersonAge = ? WHERE PersonID = ?"
        assert result.values.size() == 3
        assert result.values[0] == "Noah"
        assert result.values[1] == 17
        assert result.values[2] == 8787
    }


}

