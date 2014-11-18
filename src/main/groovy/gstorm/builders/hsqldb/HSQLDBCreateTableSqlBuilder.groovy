package gstorm.builders.hsqldb
import gstorm.builders.BaseCreateTableSqlBuilder
import gstorm.builders.BuildResult
import gstorm.metadata.ClassMetaData

class HSQLDBCreateTableSqlBuilder extends BaseCreateTableSqlBuilder {

    HSQLDBCreateTableSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }


    @Override
    BuildResult buildSqlAndValues() {
        def tableName = classMetaData.tableName
        def columnDefs = classMetaData.fields.collect { field -> "${field.columnName} ${HSQLDBTypeMapper.instance.getSqlType(field.clazz)}" }

        columnDefs.add(0, "${classMetaData.idField.columnName} ${HSQLDBTypeMapper.instance.getSqlType(classMetaData.idField.clazz)} GENERATED ALWAYS AS IDENTITY PRIMARY KEY")
        def sql = "CREATE TABLE IF NOT EXISTS ${tableName} (${columnDefs.join(', ')})"
        new BuildResult(sql: sql, values: null)
    }
}