package gstorm.builders.query.condition

/**
 * Created by noahshen on 14-10-28.
 */
class PropertyComparisonCondition implements Condition {

    String otherProperty;

    PropertyComparisonCondition(String otherProperty) {
        this.otherProperty = otherProperty
    }

    protected void setOtherProperty(String otherProperty) {
        this.otherProperty = otherProperty
    }
}
