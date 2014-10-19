package gstorm.builders

import gstorm.metadata.ClassMetaData

abstract class AbstractInsertQueryBuilder extends AbstractQueryBuilder {

    AbstractInsertQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

    String build() {
        final fieldNames = classMetaData.fieldNames
        final columns = fieldNames.join ", "
        final placeholders = fieldNames.collect { "?" }.join(", ")

        "INSERT INTO ${classMetaData.tableName} (${columns}) values (${placeholders})".toString()
    }
}
