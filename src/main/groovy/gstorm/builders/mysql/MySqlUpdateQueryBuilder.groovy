package gstorm.builders.mysql

import gstorm.builders.AbstractUpdateQueryBuilder
import gstorm.metadata.ClassMetaData

class MySqlUpdateQueryBuilder extends AbstractUpdateQueryBuilder {

    MySqlUpdateQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}
