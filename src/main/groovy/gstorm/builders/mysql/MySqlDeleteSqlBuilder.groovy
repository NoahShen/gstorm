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
        def where = MySqlStatementBuilders.generateQuerySql(queryCondition, classMetaData, values)
        def sql = "DELETE FROM `${classMetaData.tableName}`${where}"
        new BuildResult(sql: sql, values: values)
    }
}
