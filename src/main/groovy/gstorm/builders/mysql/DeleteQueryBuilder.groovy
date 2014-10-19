package gstorm.builders.mysql

import gstorm.builders.AbstractWhereableQueryBuilder
import gstorm.metadata.ClassMetaData

class DeleteQueryBuilder extends AbstractWhereableQueryBuilder {

    DeleteQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
        this.query = new StringBuilder("DELETE FROM ${classMetaData.tableName}")
    }

}
