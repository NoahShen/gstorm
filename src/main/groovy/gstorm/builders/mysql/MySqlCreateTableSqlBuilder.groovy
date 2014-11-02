package gstorm.builders.mysql
import gstorm.builders.BaseCreateTableSqlBuilder
import gstorm.metadata.ClassMetaData

class MySqlCreateTableSqlBuilder extends BaseCreateTableSqlBuilder {

    MySqlCreateTableSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

    String build() {
        def tableName = classMetaData.tableName
        def columnDefs = classMetaData.fields.collect { field -> "${field.name} ${field.columnType}" }

        columnDefs.add(0, "${classMetaData.idFieldName ?: 'ID'} int(11) NOT NULL AUTO_INCREMENT")
        columnDefs.add("PRIMARY KEY (${classMetaData.idFieldName ?: 'ID'})")

        new StringBuilder("CREATE").append(SPACE)
                .append('TABLE').append(SPACE)
                .append("IF NOT EXISTS").append(SPACE)
                .append(tableName).append(SPACE)
                .append("(${ columnDefs.join(', ') })")
                .toString()
    }
}