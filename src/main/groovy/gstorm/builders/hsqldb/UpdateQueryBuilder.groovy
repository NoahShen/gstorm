package gstorm.builders.hsqldb

import gstorm.builders.AbstractWhereableQueryBuilder
import gstorm.metadata.ClassMetaData

class UpdateQueryBuilder extends AbstractWhereableQueryBuilder {

    UpdateQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
        final fieldNames = classMetaData.fieldNames
        final placeholders = fieldNames.collect { "${it} = ?" }.join(", ")

        this.query = new StringBuilder("UPDATE ${classMetaData.tableName} SET ${placeholders}")
    }

}
