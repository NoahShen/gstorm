package gstorm.builders

import gstorm.metadata.ClassMetaData

abstract class BaseCreateTableSqlBuilder extends BaseSqlBuilder {

    BaseCreateTableSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}