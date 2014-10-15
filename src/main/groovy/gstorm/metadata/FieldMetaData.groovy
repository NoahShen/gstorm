package gstorm.metadata

import gstorm.annotation.Column
import gstorm.helpers.TypeMapper

import java.lang.annotation.Annotation
import java.lang.reflect.Field

class FieldMetaData {
    def type, name, columnName, columnType

    FieldMetaData(Field field) {
        this.type = field.type
        this.name = field.name
        this.columnType = TypeMapper.instance.getSqlType(field.type)
        Annotation columnAnno = field.getAnnotation(Column)
        this.columnName = columnAnno ? columnAnno.name() : field.name
    }
}
