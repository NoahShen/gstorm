package gstorm.builders.hsqldb

import gstorm.builders.BaseDeleteSqlBuilder
import gstorm.metadata.ClassMetaData

class HSQLDBDeleteSqlBuilder extends BaseDeleteSqlBuilder {

    HSQLDBDeleteSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}
