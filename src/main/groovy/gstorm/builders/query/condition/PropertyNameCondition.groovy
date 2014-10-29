package gstorm.builders.query.condition

/**
 * Created by noahshen on 14-10-28.
 */
class PropertyNameCondition implements Condition {

    String propertyName;

    PropertyNameCondition(String propertyName) {
        this.propertyName = propertyName
    }
w
    protected void setPropertyName(String propertyName) {
        this.propertyName = propertyName
    }
}
