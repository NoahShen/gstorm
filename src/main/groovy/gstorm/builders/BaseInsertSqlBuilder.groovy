package gstorm.builders

import gstorm.metadata.ClassMetaData

abstract class BaseInsertSqlBuilder extends BaseSqlBuilder {

    BaseInsertSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

    String build() {
        final fieldNames = classMetaData.fieldNames
        final columns = fieldNames.join ", "
        final placeholders = fieldNames.collect { "?" }.join(", ")

        "INSERT INTO ${classMetaData.tableName} (${columns}) values (${placeholders})".toString()
    }
}
