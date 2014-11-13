package gstorm.builders.mysql
import gstorm.builders.BaseCreateTableSqlBuilder
import gstorm.builders.BuildResult
import gstorm.metadata.ClassMetaData

class MySqlCreateTableSqlBuilder extends BaseCreateTableSqlBuilder {

    MySqlCreateTableSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

    @Override
    BuildResult buildSqlAndValues() {
        def tableName = classMetaData.tableName
        def columnDefs = classMetaData.fields.collect { field -> "`${field.columnName}` ${field.columnType}" }

        columnDefs.add(0, "${classMetaData.idFieldName ?: '`ID`'} INT(11) NOT NULL AUTO_INCREMENT")
        columnDefs.add("PRIMARY KEY (${classMetaData.idFieldName ?: '`ID`'})")

        def sql = "CREATE TABLE IF NOT EXISTS `${tableName}` (${columnDefs.join(', ')})"
        new BuildResult(sql: sql, values: null)
    }
}