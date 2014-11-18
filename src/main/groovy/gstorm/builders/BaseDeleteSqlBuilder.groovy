package gstorm.builders

import gstorm.metadata.ClassMetaData

abstract class BaseDeleteSqlBuilder extends BaseWhereableSqlBuilder {

    BaseDeleteSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }
}
