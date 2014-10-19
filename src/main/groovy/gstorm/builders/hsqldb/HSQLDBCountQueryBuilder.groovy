package gstorm.builders.hsqldb

import gstorm.builders.AbstractCountQueryBuilder
import gstorm.metadata.ClassMetaData

class HSQLDBCountQueryBuilder extends AbstractCountQueryBuilder {

    HSQLDBCountQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}
