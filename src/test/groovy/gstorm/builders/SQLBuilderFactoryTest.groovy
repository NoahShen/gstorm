package gstorm.builders

import gstorm.metadata.ClassMetaData

/**
 * Created by noahshen on 14-10-19.
 */
class SQLBuilderFactoryTest extends GroovyTestCase {

    class Person {
        def name
        int age
    }

    ClassMetaData classMetaData

    void setUp() {
        classMetaData = new ClassMetaData(Person)
    }

    void "test create instance"() {
        SQLBuilderFactory factory = SQLBuilderFactory.getInstance()
        assert factory

        def factory2 = SQLBuilderFactory.getInstance()
        assert factory.is(factory2)

    }

    void "test create CreateTableBuilder"() {
        SQLBuilderFactory factory = SQLBuilderFactory.getInstance()
        factory.createCreateTableBuilder(SQLDialect.HSQLDB, classMetaData)

    }
}
