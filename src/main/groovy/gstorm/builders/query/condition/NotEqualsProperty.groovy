package gstorm.builders.query.condition

/**
 * Created by noahshen on 14-10-29.
 */
class NotEqualsProperty extends PropertyComparisonCondition {
    NotEqualsProperty(String propertyName, String otherProperty) {
        super(propertyName, otherProperty)
    }
}
