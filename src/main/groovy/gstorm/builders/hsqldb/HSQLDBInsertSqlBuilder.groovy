package gstorm.builders.hsqldb

import gstorm.builders.BaseInsertSqlBuilder
import gstorm.builders.BuildResult
import gstorm.metadata.ClassMetaData

class HSQLDBInsertSqlBuilder extends BaseInsertSqlBuilder {

    HSQLDBInsertSqlBuilder(ClassMetaData classMetaData, entity) {
        super(classMetaData, entity)
    }

    @Override
    BuildResult buildSqlAndValues() {
        def columnNames = []
        def valuePlaceHolders = []
        def values = []
        classMetaData.fields.each {
            columnNames << "${it.columnName}"
            valuePlaceHolders << "?"
            values << entity.getProperty(it.name)
        }
        def sql = "INSERT INTO ${classMetaData.tableName} (${columnNames.join(", ")}) VALUES (${valuePlaceHolders.join(", ")})"
        new BuildResult(sql: sql, values: values)
    }
}
