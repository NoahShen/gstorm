package gstorm.builders.mysql

import gstorm.builders.BaseUpdateSqlBuilder
import gstorm.metadata.ClassMetaData

class MySqlUpdateSqlBuilder extends BaseUpdateSqlBuilder {

    MySqlUpdateSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}
