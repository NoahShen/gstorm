package gstorm.builders.mysql

import gstorm.builders.AbstractDeleteQueryBuilder
import gstorm.metadata.ClassMetaData

class MySqlDeleteQueryBuilder extends AbstractDeleteQueryBuilder {

    MySqlDeleteQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}
