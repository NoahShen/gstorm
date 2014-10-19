package gstorm.builders

import gstorm.metadata.ClassMetaData

abstract class AbstractCreateTableQueryBuilder extends AbstractQueryBuilder {

    AbstractCreateTableQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}