package gstorm.enhance
import groovy.sql.Sql
import gstorm.builders.SQLBuilderFactory
import gstorm.builders.SQLDialect
import gstorm.metadata.ClassMetaData

class ModelClassEnhancer {
    final ClassMetaData metaData
    final Sql sql

    SQLDialect dialect

    final DynamicFindEnhancer dynamicFindEnhancer = new DynamicFindEnhancer()

    ModelClassEnhancer(ClassMetaData classMetaData, Sql sql, SQLDialect dialect = SQLDialect.HSQLDB) {
        this.metaData = classMetaData
        this.sql = sql
        this.dialect = dialect
    }

    public void enhance() {
        addStaticDmlMethods()
        addDynamicFindMethods()
        addInstanceDmlMethods()
    }

    private def addStaticDmlMethods() {
        final modelMetaClass = metaData.modelClass.metaClass
        SQLBuilderFactory sqlBuilderFactory = SQLBuilderFactory.getInstance()

        def where = { Closure whereClosure ->
            def selectSqlBuilder = sqlBuilderFactory.createSelectSqlBuilder(this.dialect, metaData)
            whereClosure.delegate = selectSqlBuilder
            whereClosure.call()
            def buildResult = selectSqlBuilder.buildSqlAndValues()
            sql.rows(buildResult.sql, buildResult.values)
        }

        modelMetaClass.static.where = where
        modelMetaClass.static.findWhere = where

        modelMetaClass.static.findFirstWhere = { Closure whereClosure ->
            def selectSqlBuilder = sqlBuilderFactory.createSelectSqlBuilder(this.dialect, metaData)
            whereClosure.delegate = selectSqlBuilder
            whereClosure.call()
            def buildResult = selectSqlBuilder.buildSqlAndValues()
            sql.firstRow(buildResult.sql, buildResult.values)
        }

        def getAll = {
            def selectSqlBuilder = sqlBuilderFactory.createSelectSqlBuilder(this.dialect, metaData)
            def buildResult = selectSqlBuilder.buildSqlAndValues()
            sql.rows(buildResult.sql)
        }
        modelMetaClass.static.getAll = getAll
        modelMetaClass.static.all = getAll

        def getCount = { Closure whereClosure ->
            def countSqlBuilder = sqlBuilderFactory.createCountSqlBuilder(this.dialect, metaData)
            whereClosure.delegate = countSqlBuilder
            whereClosure.call()
            def buildResult = countSqlBuilder.buildSqlAndValues()
            sql.firstRow(buildResult.sql, buildResult.values).count
        }
        modelMetaClass.static.count = getCount
        modelMetaClass.static.getCount = getCount

        modelMetaClass.static.get = { id ->
            def selectByIdQuery = sqlBuilderFactory.createSelectSqlBuilder(this.dialect, metaData)
            def buildResult = selectByIdQuery.eq(metaData.idField.name, id).buildSqlAndValues()
            sql.firstRow(buildResult.sql, buildResult.values)
        }
    }

    private def addDynamicFindMethods() {
        final modelMetaClass = metaData.modelClass.metaClass
        modelMetaClass.static.methodMissing = { String name, args ->
            if (dynamicFindEnhancer.supports(name)) {
                return dynamicFindEnhancer.tryExecute(metaData.modelClass, name, (List) args)
            } else {
                new MissingMethodException(name, delegate, args)
            }
        }

    }

    private def addInstanceDmlMethods() {
        final modelMetaClass = metaData.modelClass.metaClass
        final fieldNames = metaData.fieldNames
        SQLBuilderFactory sqlBuilderFactory = SQLBuilderFactory.getInstance()



        final updateQuery = sqlBuilderFactory.createUpdateQueryBuilder(this.dialect, metaData).byId().build()
        final deleteQuery = sqlBuilderFactory.createDeleteQueryBuilder(this.dialect, metaData).byId().build()

        if (!metaData.idField) { // add id if not already defined
            modelMetaClass.id = null
        }

        modelMetaClass.getId$ = { -> delegate.getProperty(metaData.idFieldName ?: 'id') }
        modelMetaClass.setId$ = { value -> delegate.setProperty(metaData.idFieldName ?: 'id', value) }

        modelMetaClass.save = {
            if (delegate.id$ == null) {
                def insertSqlBuilder = sqlBuilderFactory.createInsertSqlBuilder(this.dialect, metaData, delegate)
                def buildResult = insertSqlBuilder.buildSqlAndValues()
                final generatedIds = sql.executeInsert(buildResult.sql, buildResult.values)
                delegate.id$ = generatedIds[0][0] // pretty stupid way to extract it
            } else {
                def updateSqlBuilder = sqlBuilderFactory.createUpdateSqlBuilder(this.dialect, metaData, delegate)
                def buildResult = updateSqlBuilder.idEq(delegate.id$).buildSqlAndValues()
                sql.executeUpdate(buildResult.sql, buildResult.values)
            }
            delegate
        }

        modelMetaClass.delete = {
            if (delegate.id$ != null) {
                def deleteSqlBuilder = sqlBuilderFactory.createDeleteSqlBuilder(this.dialect, metaData)
                def buildResult = deleteSqlBuilder.idEq(delegate.id$).buildSqlAndValues()
                sql.execute(buildResult.sql, buildResult.values)
            }
            delegate
        }
    }

}
