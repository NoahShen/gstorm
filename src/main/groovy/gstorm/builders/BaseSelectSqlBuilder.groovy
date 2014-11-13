package gstorm.builders

import gstorm.metadata.ClassMetaData

abstract class BaseSelectSqlBuilder extends BaseWhereableSqlBuilder {

    BaseSelectSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}
