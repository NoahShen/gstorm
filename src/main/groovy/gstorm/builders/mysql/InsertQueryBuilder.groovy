package gstorm.builders.mysql

import gstorm.builders.AbstractQueryBuilder
import gstorm.metadata.ClassMetaData

class InsertQueryBuilder extends AbstractQueryBuilder {

    InsertQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

    String build() {
        final fieldNames = classMetaData.fieldNames
        final columns = fieldNames.join ", "
        final placeholders = fieldNames.collect { "?" }.join(", ")

        "INSERT INTO ${classMetaData.tableName} (${columns}) values (${placeholders})".toString()
    }
}
