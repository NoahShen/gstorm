package gstorm.builders.hsqldb

import gstorm.builders.BaseUpdateSqlBuilder
import gstorm.metadata.ClassMetaData

class HSQLDBUpdateSqlBuilder extends BaseUpdateSqlBuilder {

    HSQLDBUpdateSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}
