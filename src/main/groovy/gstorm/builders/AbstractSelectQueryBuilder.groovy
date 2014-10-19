package gstorm.builders

import gstorm.metadata.ClassMetaData

class AbstractSelectQueryBuilder extends AbstractWhereableQueryBuilder {

    AbstractSelectQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
        def fields = classMetaData.fields
        def placeholders = fields.collect { "${it.columnName} as \"${it.name}\"" }.join(", ")

        this.query = new StringBuilder("SELECT ${placeholders} FROM ${classMetaData.tableName}")
    }

}
