package gstorm.builders.mysql

import gstorm.builders.AbstractSelectQueryBuilder
import gstorm.metadata.ClassMetaData

class MySqlSelectQueryBuilder extends AbstractSelectQueryBuilder {

    MySqlSelectQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}
