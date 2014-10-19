package gstorm.builders.hsqldb

import gstorm.builders.AbstractCreateTableQueryBuilder
import gstorm.metadata.ClassMetaData

class HSQLDBCreateTableQueryBuilder extends AbstractCreateTableQueryBuilder {

    HSQLDBCreateTableQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

    String build() {
        def tableName = classMetaData.tableName
        def columnDefs = classMetaData.fields.collect { field -> "${field.name} ${field.columnType}" }

        columnDefs.add(0, "${classMetaData.idFieldName ?: 'ID'} NUMERIC GENERATED ALWAYS AS IDENTITY PRIMARY KEY")

        new StringBuilder("CREATE").append(SPACE)
                .append('TABLE').append(SPACE)
                .append("IF NOT EXISTS").append(SPACE)
                .append(tableName).append(SPACE)
                .append("(${ columnDefs.join(', ') })")
                .toString()
    }
}