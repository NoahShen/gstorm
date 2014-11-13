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
        def conditionSql = queryCondition.conditions.collect {
            HSQLDBConditions.generateConditionSql(it, classMetaData, values)
        }.join(" AND ")

        def sql = "SELECT COUNT(1) as \"count\" FROM ${classMetaData.tableName} WHERE ${conditionSql}"
        new BuildResult(sql: sql, values: values)
    }
}
