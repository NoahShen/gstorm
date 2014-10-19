package gstorm.builders.mysql

import gstorm.builders.AbstractCountQueryBuilder
import gstorm.metadata.ClassMetaData

class MySqlCountQueryBuilder extends AbstractCountQueryBuilder {

    MySqlCountQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}
