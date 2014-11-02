package gstorm.builders.hsqldb

import gstorm.builders.BaseSelectSqlBuilder
import gstorm.metadata.ClassMetaData

class HSQLDBSelectSqlBuilder extends BaseSelectSqlBuilder {

    HSQLDBSelectSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}
