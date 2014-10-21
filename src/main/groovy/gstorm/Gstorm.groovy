package gstorm
import groovy.sql.Sql
import groovy.util.logging.Log
import gstorm.builders.SQLBuilderFactory
import gstorm.builders.SQLDialect
import gstorm.enhance.ModelClassEnhancer
import gstorm.helpers.SqlObjectFactory
import gstorm.metadata.ClassMetaData

import java.sql.Connection
import java.util.logging.Level

@Log
class Gstorm {

    Sql sql

    SQLDialect dialect

    /**
     * Constructs Gstorm using in-memory hsqldb database
     */
    Gstorm(){
        this(SqlObjectFactory.memoryDB())
    }

    /**
     * Constructs Gstorm using disk based (persistent) hsqldb database
     *
     * @param dbPath the path of the database
     */
    Gstorm(String dbPath){
        this(SqlObjectFactory.fileDB(dbPath))
    }

    /**
     * Constructs Gstorm using provided Connection
     *
     * @param connection instance of java.sql.Connection
     */
    Gstorm(Connection connection){
        this(new Sql(connection))
    }

    /**
     * Constructs Gstorm using provided Sql instance
     *
     * @param connection instance of groovy.sql.Sql
     */
    Gstorm(Sql sql, SQLDialect dialect = SQLDialect.HSQLDB) {
        this.sql = sql
        this.dialect = dialect
    }

    /**
     * Adds CRUD methods to the modelClass. Also creates table for class if does not exist already.
     *
     * @param modelClass
     */
    def stormify(Class modelClass, Boolean createTable = false) {
        ClassMetaData classMetaData = new ClassMetaData(modelClass)
        if (createTable) {
            createTableFor(classMetaData)
        }
        new ModelClassEnhancer(classMetaData, sql, dialect).enhance()
        return this
    }

    private def createTableFor(ClassMetaData metaData) {
        SQLBuilderFactory sqlBuilderFactory = SQLBuilderFactory.getInstance()
        sql.execute(sqlBuilderFactory.createCreateTableBuilder(dialect, metaData).build())
    }

    def enableQueryLogging(level = Level.FINE) {
        def sqlMetaClass = Sql.class.metaClass

        sqlMetaClass.invokeMethod = { String name, args ->
            if (args) log.log(level, args.first()) // so far the first arg has been the query.
            sqlMetaClass.getMetaMethod(name, args).invoke(delegate, args)
        }
    }
}
