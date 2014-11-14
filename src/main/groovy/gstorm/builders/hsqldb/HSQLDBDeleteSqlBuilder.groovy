package gstorm.builders.hsqldb
import gstorm.builders.BaseDeleteSqlBuilder
import gstorm.builders.BuildResult
import gstorm.metadata.ClassMetaData

class HSQLDBDeleteSqlBuilder extends BaseDeleteSqlBuilder {

    HSQLDBDeleteSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

    @Override
    BuildResult buildSqlAndValues() {
        def values = []
        def where = HSQLDBStatementBuilders.generateQuerySql(queryCondition, classMetaData, values)
        def sql = "DELETE FROM ${classMetaData.tableName}${where}"
        new BuildResult(sql: sql, values: values)
    }
}
