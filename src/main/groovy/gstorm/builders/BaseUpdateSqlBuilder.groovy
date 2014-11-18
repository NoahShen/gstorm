package gstorm.builders

import gstorm.metadata.ClassMetaData

abstract class BaseUpdateSqlBuilder extends BaseWhereableSqlBuilder {

    def entity

    BaseUpdateSqlBuilder(ClassMetaData classMetaData, entity) {
        super(classMetaData)
        this.entity = entity
    }
}
