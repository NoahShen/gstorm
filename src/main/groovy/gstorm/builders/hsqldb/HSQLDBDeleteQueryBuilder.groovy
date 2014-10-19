package gstorm.builders.hsqldb

import gstorm.builders.AbstractDeleteQueryBuilder
import gstorm.metadata.ClassMetaData

class HSQLDBDeleteQueryBuilder extends AbstractDeleteQueryBuilder {

    HSQLDBDeleteQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}
