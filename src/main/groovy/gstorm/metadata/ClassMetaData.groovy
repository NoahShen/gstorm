package gstorm.metadata

import gstorm.annotation.Id
import gstorm.annotation.Table

import java.lang.reflect.Field

class ClassMetaData {
    final Class modelClass
    final String tableName
    final FieldMetaData idField
    private final List<FieldMetaData> fields
    private final List<FieldMetaData> allFields
    private Map _fieldsCache        // just to avoid iterating over list of fields and finding by name.

    ClassMetaData(Class modelClass) {
        this.modelClass = modelClass
        this.tableName = extractTableName(modelClass)
        this.idField = getIdFieldOfClass(modelClass)
        this.fields = getOtherFieldsOfClass(modelClass)
        this.allFields = getAllFieldsOfClass(modelClass)
        this._fieldsCache = this.fields.collectEntries { fieldMetaData -> [fieldMetaData.name, fieldMetaData] }
    }

    FieldMetaData getAt(String fieldName) {
        this._fieldsCache[fieldName]
    }

    List<FieldMetaData> getFields() {
        Collections.unmodifiableList(this.fields);
    }

    List<FieldMetaData> getAllFields() {
        Collections.unmodifiableList(this.allFields);
    }

    List<String> getFieldNames() {
        Collections.unmodifiableList(this.fields*.name);
    }

    String getIdFieldName() {
        idField?.name
    }

    private String extractTableName(Class modelClass) {
        modelClass.getAnnotation(Table)?.name()?.trim() ?: modelClass.simpleName
    }


    private List<FieldMetaData> getOtherFieldsOfClass(Class modelClass) {
        fieldsDeclaredIn(modelClass)
                .findAll { !it.isAnnotationPresent(Id) }
                .collect { field -> new FieldMetaData(field) }
    }
    private List<FieldMetaData> getAllFieldsOfClass(Class modelClass) {
        fieldsDeclaredIn(modelClass).collect { field -> new FieldMetaData(field) }
    }

    private FieldMetaData getIdFieldOfClass(Class modelClass) {
        def idField = fieldsDeclaredIn(modelClass).find { it.isAnnotationPresent(Id) }
        idField ? new FieldMetaData(idField) : null
    }

    private List<Field> fieldsDeclaredIn(Class modelClass) {
        modelClass.declaredFields.findAll { !it.synthetic }
    }

}
