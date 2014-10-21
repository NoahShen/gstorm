package gstorm.annotation

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.Target

import static java.lang.annotation.RetentionPolicy.RUNTIME

/**
 * Created by noahshen on 14-10-11.
 */

@Retention(RUNTIME)
@Target([ElementType.FIELD])
public @interface Column {

    String name()

}