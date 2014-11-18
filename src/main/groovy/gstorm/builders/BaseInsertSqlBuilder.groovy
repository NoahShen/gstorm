package gstorm.builders

import gstorm.metadata.ClassMetaData

abstract class BaseInsertSqlBuilder extends BaseSqlBuilder {

    def entity

    BaseInsertSqlBuilder(ClassMetaData classMetaData, entity) {
        super(classMetaData)
        this.entity = entity
    }
}
