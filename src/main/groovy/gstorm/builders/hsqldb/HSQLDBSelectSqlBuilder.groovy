package gstorm.builders.hsqldb
import gstorm.builders.BaseSelectSqlBuilder
import gstorm.builders.BuildResult
import gstorm.metadata.ClassMetaData

class HSQLDBSelectSqlBuilder extends BaseSelectSqlBuilder {

    HSQLDBSelectSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }


    @Override
    BuildResult buildSqlAndValues() {
        def fields = classMetaData.getAllFields()
        def projections = fields.collect { "${it.columnName} as \"${it.name}\"" }.join(", ")

        def values = []
        def where = ""
        if (queryCondition.conditions) {
            def conditionSql = queryCondition.conditions.collect {
                HSQLDBConditions.generateConditionSql(it, classMetaData, values)
            }.join(" AND ")
            where = " WHERE ${conditionSql}"
        }

        def sql = "SELECT ${projections} FROM ${classMetaData.tableName}${where}"
        new BuildResult(sql: sql, values: values)
    }
}
