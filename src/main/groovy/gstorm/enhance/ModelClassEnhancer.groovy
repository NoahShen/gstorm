package gstorm.enhance

import groovy.sql.Sql
import gstorm.builders.hsqldb.CountQueryBuilder
import gstorm.builders.hsqldb.DeleteQueryBuilder
import gstorm.builders.hsqldb.InsertQueryBuilder
import gstorm.builders.hsqldb.SelectQueryBuilder
import gstorm.builders.hsqldb.UpdateQueryBuilder
import gstorm.metadata.ClassMetaData

class ModelClassEnhancer {
    final ClassMetaData metaData
    final Sql sql

    final DynamicFindEnhancer dynamicFindEnhancer = new DynamicFindEnhancer()

    ModelClassEnhancer(ClassMetaData classMetaData, Sql sql) {
        this.metaData = classMetaData
        this.sql = sql
    }

    public void enhance() {
        addStaticDmlMethods()
        addDynamicFindMethods()
        addInstanceDmlMethods()
    }

    private def addStaticDmlMethods() {
        final modelMetaClass = metaData.modelClass.metaClass

        modelMetaClass.static.where = { String clause ->
            sql.rows(new SelectQueryBuilder(metaData).where(clause).build())
        }
        modelMetaClass.static.findWhere = { String clause ->
            sql.rows(new SelectQueryBuilder(metaData).where(clause).build())
        }

        modelMetaClass.static.findFirstWhere = { String clause ->
            def result = sql.firstRow(new SelectQueryBuilder(metaData).where(clause).build())
            result
        }

        def selectAllQuery = new SelectQueryBuilder(metaData).build()
        def getAll = {
            sql.rows(selectAllQuery)
        }
        modelMetaClass.static.getAll = getAll
        modelMetaClass.static.all = getAll

        def getCount = { String optional_clause = null ->
            def query = new CountQueryBuilder(metaData)
            if (optional_clause) query.where(optional_clause)
            sql.firstRow(query.build()).count
        }
        modelMetaClass.static.count = getCount
        modelMetaClass.static.getCount = getCount


        def selectByIdQuery = new SelectQueryBuilder(metaData).byId().build()
        modelMetaClass.static.get = { id ->
            final result = sql.rows(selectByIdQuery, [id])
            (result) ? result.first() : null
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

        final insertQuery = new InsertQueryBuilder(metaData).build()

        final updateQuery = new UpdateQueryBuilder(metaData).byId().build()
        final deleteQuery = new DeleteQueryBuilder(metaData).byId().build()

        if (!metaData.idField) { // add id if not already defined
            modelMetaClass.id = null
        }

        modelMetaClass.getId$ = { -> delegate.getProperty(metaData.idFieldName ?: 'id') }
        modelMetaClass.setId$ = { value -> delegate.setProperty(metaData.idFieldName ?: 'id', value) }

        modelMetaClass.save = {
            if (delegate.id$ == null) {
                final values = fieldNames.collect { delegate.getProperty(it) }
                final generatedIds = sql.executeInsert(insertQuery, values)
                delegate.id$ = generatedIds[0][0] // pretty stupid way to extract it
            } else {
                final values = fieldNames.collect { delegate.getProperty(it) } << delegate.id$
                sql.executeUpdate(updateQuery, values)
            }
            delegate
        }

        modelMetaClass.delete = {
            if (delegate.id$ != null) {
                sql.execute(deleteQuery, [delegate.id$])
            }
            delegate
        }
    }

}
