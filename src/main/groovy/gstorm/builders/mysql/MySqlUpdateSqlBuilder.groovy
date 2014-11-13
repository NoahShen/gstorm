package gstorm.builders.mysql

import gstorm.builders.BaseUpdateSqlBuilder
import gstorm.builders.BuildResult
import gstorm.metadata.ClassMetaData

class MySqlUpdateSqlBuilder extends BaseUpdateSqlBuilder {

    MySqlUpdateSqlBuilder(ClassMetaData classMetaData, entity) {
        super(classMetaData, entity)
    }

    @Override
    BuildResult buildSqlAndValues() {

        def updateFieldPlaceHolder = []
        def values = []
        classMetaData.fields.each {
            updateFieldPlaceHolder << "`${it.columnName}` = ?"
            values << entity.getProperty(it.name)
        }
        def where = ""
        if (queryCondition.conditions) {
            def conditionSql = queryCondition.conditions.collect {
                MySqlConditions.generateConditionSql(it, classMetaData, values)
            }.join(" AND ")
            where = " WHERE ${conditionSql}"
        }

        def sql = "UPDATE `${classMetaData.tableName}` SET ${updateFieldPlaceHolder.join(", ")}${where}"
        new BuildResult(sql: sql, values: values)
    }
}
