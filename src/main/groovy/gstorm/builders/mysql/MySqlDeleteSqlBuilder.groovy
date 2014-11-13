package gstorm.builders.mysql

import gstorm.builders.BaseDeleteSqlBuilder
import gstorm.builders.BuildResult
import gstorm.metadata.ClassMetaData

class MySqlDeleteSqlBuilder extends BaseDeleteSqlBuilder {

    MySqlDeleteSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

    @Override
    BuildResult buildSqlAndValues() {
        def values = []
        def where = ""
        if (queryCondition.conditions) {
            def conditionSql = queryCondition.conditions.collect {
                MySqlConditions.generateConditionSql(it, classMetaData, values)
            }.join(" AND ")
            where = " WHERE ${conditionSql}"
        }

        def sql = "DELETE FROM `${classMetaData.tableName}`${where}"
        new BuildResult(sql: sql, values: values)
    }
}
