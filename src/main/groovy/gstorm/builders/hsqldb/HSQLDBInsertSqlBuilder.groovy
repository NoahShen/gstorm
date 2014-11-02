package gstorm.builders.hsqldb

import gstorm.builders.BaseInsertSqlBuilder
import gstorm.metadata.ClassMetaData

class HSQLDBInsertSqlBuilder extends BaseInsertSqlBuilder {

    HSQLDBInsertSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}
