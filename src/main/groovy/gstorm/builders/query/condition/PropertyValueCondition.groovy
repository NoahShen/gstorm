package gstorm.builders.query.condition

/**
 * Created by noahshen on 14-10-28.
 */
class PropertyValueCondition extends PropertyNameCondition {

    Object value;

    PropertyValueCondition(String propertyName, Object value) {
        super(propertyName)
        this.value = value
    }

    protected void setValue(Object value) {
        this.value = value
    }
}
