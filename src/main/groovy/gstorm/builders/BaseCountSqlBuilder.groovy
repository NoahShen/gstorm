package gstorm.builders

import gstorm.metadata.ClassMetaData

abstract class BaseCountSqlBuilder extends BaseWhereableSqlBuilder {

    BaseCountSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}
