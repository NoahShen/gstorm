package gstorm.builders.mysql

import gstorm.builders.AbstractInsertQueryBuilder
import gstorm.metadata.ClassMetaData

class MySqlInsertQueryBuilder extends AbstractInsertQueryBuilder {

    MySqlInsertQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }
}
