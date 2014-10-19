package gstorm.builders

import gstorm.metadata.ClassMetaData

class AbstractDeleteQueryBuilder extends AbstractWhereableQueryBuilder {

    AbstractDeleteQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
        this.query = new StringBuilder("DELETE FROM ${classMetaData.tableName}")
    }

}
