package gstorm.builders.mysql

import gstorm.builders.BaseSelectSqlBuilder
import gstorm.metadata.ClassMetaData

class MySqlSelectSqlBuilder extends BaseSelectSqlBuilder {

    MySqlSelectSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

    @Override
    String build() {
        def fields = classMetaData.getAllFields()
        def projections = fields.collect { "${it.columnName} as \"${it.name}\"" }.join(", ")

//        StringBuilder query = new StringBuilder("SELECT ${projections} FROM ${classMetaData.tableName}")

        def values = []
        def conditionSql = queryCondition.conditions.collect {
            MySqlConditions.generateConditionSql(it, classMetaData, values)
        }.join(" AND ")
    }

}
