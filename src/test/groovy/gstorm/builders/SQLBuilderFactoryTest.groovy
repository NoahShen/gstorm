package gstorm.builders

import gstorm.builders.hsqldb.*
import gstorm.builders.mysql.*
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
        def builder = factory.createCreateTableBuilder(SQLDialect.HSQLDB, classMetaData)
        assert builder instanceof HSQLDBCreateTableQueryBuilder

        def mysqlBuilder = factory.createCreateTableBuilder(SQLDialect.MYSQL, classMetaData)
        assert mysqlBuilder instanceof MySqlCreateTableQueryBuilder
    }

    void "test create CountQueryBuilder"() {
        SQLBuilderFactory factory = SQLBuilderFactory.getInstance()
        def builder = factory.createCountQueryBuilder(SQLDialect.HSQLDB, classMetaData)
        assert builder instanceof HSQLDBCountQueryBuilder

        def mysqlBuilder = factory.createCountQueryBuilder(SQLDialect.MYSQL, classMetaData)
        assert mysqlBuilder instanceof MySqlCountQueryBuilder
    }

    void "test create DeleteQueryBuilder"() {
        SQLBuilderFactory factory = SQLBuilderFactory.getInstance()
        def builder = factory.createDeleteQueryBuilder(SQLDialect.HSQLDB, classMetaData)
        assert builder instanceof HSQLDBDeleteQueryBuilder

        def mysqlBuilder = factory.createDeleteQueryBuilder(SQLDialect.MYSQL, classMetaData)
        assert mysqlBuilder instanceof MySqlDeleteQueryBuilder
    }

    void "test create InsertQueryBuilder"() {
        SQLBuilderFactory factory = SQLBuilderFactory.getInstance()
        def builder = factory.createInsertQueryBuilder(SQLDialect.HSQLDB, classMetaData)
        assert builder instanceof HSQLDBInsertQueryBuilder

        def mysqlBuilder = factory.createInsertQueryBuilder(SQLDialect.MYSQL, classMetaData)
        assert mysqlBuilder instanceof MySqlInsertQueryBuilder
    }

    void "test create SelectQueryBuilder"() {
        SQLBuilderFactory factory = SQLBuilderFactory.getInstance()
        def builder = factory.createSelectQueryBuilder(SQLDialect.HSQLDB, classMetaData)
        assert builder instanceof HSQLDBSelectQueryBuilder

        def mysqlBuilder = factory.createSelectQueryBuilder(SQLDialect.MYSQL, classMetaData)
        assert mysqlBuilder instanceof MySqlSelectQueryBuilder
    }

    void "test create UpdateQueryBuilder"() {
        SQLBuilderFactory factory = SQLBuilderFactory.getInstance()
        def builder = factory.createUpdateQueryBuilder(SQLDialect.HSQLDB, classMetaData)
        assert builder instanceof HSQLDBUpdateQueryBuilder

        def mysqlBuilder = factory.createUpdateQueryBuilder(SQLDialect.MYSQL, classMetaData)
        assert mysqlBuilder instanceof MySqlUpdateQueryBuilder
    }
}
