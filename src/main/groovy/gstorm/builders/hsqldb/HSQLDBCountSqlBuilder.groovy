package gstorm.builders.hsqldb

import gstorm.builders.BaseCountSqlBuilder
import gstorm.metadata.ClassMetaData

class HSQLDBCountSqlBuilder extends BaseCountSqlBuilder {

    HSQLDBCountSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}
