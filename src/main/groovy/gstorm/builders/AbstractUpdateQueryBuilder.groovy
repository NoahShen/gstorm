package gstorm.builders

import gstorm.metadata.ClassMetaData

class AbstractUpdateQueryBuilder extends AbstractWhereableQueryBuilder {

    AbstractUpdateQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
        final fieldNames = classMetaData.fieldNames
        final placeholders = fieldNames.collect { "${it} = ?" }.join(", ")

        this.query = new StringBuilder("UPDATE ${classMetaData.tableName} SET ${placeholders}")
    }
}
