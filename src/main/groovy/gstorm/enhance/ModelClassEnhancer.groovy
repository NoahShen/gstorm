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

    ModelClassEnhancer(ClassMetaData classMetaData, Sql sql, SQLDialect dialect) {
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
            if (!whereClosure) {
                throw new IllegalArgumentException("no whereClosure")
            }
            def selectSqlBuilder = sqlBuilderFactory.createSelectSqlBuilder(this.dialect, metaData)
            whereClosure.delegate = selectSqlBuilder
            whereClosure.call()
            def buildResult = selectSqlBuilder.buildSqlAndValues()
            sql.rows(buildResult.sql, buildResult.values)
        }

        modelMetaClass.static.where = where
        modelMetaClass.static.findWhere = where

        modelMetaClass.static.findFirstWhere = { Closure whereClosure ->
            if (!whereClosure) {
                throw new IllegalArgumentException("no whereClosure")
            }
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
            if (whereClosure) {
                whereClosure.delegate = countSqlBuilder
                whereClosure.call()
            }
            def buildResult = countSqlBuilder.buildSqlAndValues()
            sql.firstRow(buildResult.sql, buildResult.values).count
        }
        modelMetaClass.static.count = getCount
        modelMetaClass.static.getCount = getCount

        modelMetaClass.static.get = { id ->
            def selectByIdQuery = sqlBuilderFactory.createSelectSqlBuilder(this.dialect, metaData)
            def buildResult = selectByIdQuery.idEq(id).buildSqlAndValues()
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
        SQLBuilderFactory sqlBuilderFactory = SQLBuilderFactory.getInstance()

        String idFieldName = metaData.idFieldName;
        modelMetaClass.save = {
            if (delegate."${idFieldName}" == null) {
                autoTimestampWhenInsert(delegate, metaData)
                def insertSqlBuilder = sqlBuilderFactory.createInsertSqlBuilder(this.dialect, metaData, delegate)
                def buildResult = insertSqlBuilder.buildSqlAndValues()
                final generatedIds = sql.executeInsert(buildResult.sql, buildResult.values)
                def keyId = generatedIds[0][0] // pretty stupid way to extract it
                delegate."${idFieldName}" = keyId
            } else {
                autoTimestampWhenUpdate(delegate, metaData)
                def updateSqlBuilder = sqlBuilderFactory.createUpdateSqlBuilder(this.dialect, metaData, delegate)
                def buildResult = updateSqlBuilder.idEq(delegate."${idFieldName}").buildSqlAndValues()
                sql.executeUpdate(buildResult.sql, buildResult.values)
            }
            delegate
        }

        modelMetaClass.delete = {
            if (delegate."${idFieldName}" != null) {
                def deleteSqlBuilder = sqlBuilderFactory.createDeleteSqlBuilder(this.dialect, metaData)
                def buildResult = deleteSqlBuilder.idEq(delegate."${idFieldName}").buildSqlAndValues()
                sql.execute(buildResult.sql, buildResult.values)
            }
            delegate
        }
    }

    private void autoTimestampWhenInsert(def entity, ClassMetaData classMetaData) {
        if (classMetaData.dateCreatedField) {
            entity."${classMetaData.dateCreatedField.name}" = new Date()
        }
        if (classMetaData.lastUpdatedField) {
            entity."${classMetaData.lastUpdatedField.name}" = new Date()
        }
    }

    private void autoTimestampWhenUpdate(def entity, ClassMetaData classMetaData) {
        if (classMetaData.lastUpdatedField) {
            entity."${classMetaData.lastUpdatedField.name}" = new Date()
        }
    }
}
