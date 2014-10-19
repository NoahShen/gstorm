package gstorm.builders.mysql

import gstorm.builders.AbstractWhereableQueryBuilder
import gstorm.metadata.ClassMetaData

class SelectQueryBuilder extends AbstractWhereableQueryBuilder {

    SelectQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
        this.query = new StringBuilder("SELECT * FROM ${classMetaData.tableName}")
    }

}
