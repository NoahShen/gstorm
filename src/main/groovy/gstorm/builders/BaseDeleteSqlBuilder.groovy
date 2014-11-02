package gstorm.builders

import gstorm.metadata.ClassMetaData

class BaseDeleteSqlBuilder extends BaseWhereableSqlBuilder {

    BaseDeleteSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
        this.query = new StringBuilder("DELETE FROM ${classMetaData.tableName}")
    }

}
