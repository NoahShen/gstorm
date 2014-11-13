package gstorm.builders.hsqldb
import gstorm.builders.BaseCountSqlBuilder
import gstorm.builders.BuildResult
import gstorm.metadata.ClassMetaData

class HSQLDBCountSqlBuilder extends BaseCountSqlBuilder {

    HSQLDBCountSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

    @Override
    BuildResult buildSqlAndValues() {
        def values = []
        def where = ""
        if (queryCondition.conditions) {
            def conditionSql = queryCondition.conditions.collect {
                HSQLDBConditions.generateConditionSql(it, classMetaData, values)
            }.join(" AND ")
            where = " WHERE ${conditionSql}"
        }

        def sql = "SELECT COUNT(1) as \"count\" FROM ${classMetaData.tableName}${where}"
        new BuildResult(sql: sql, values: values)
    }
}
