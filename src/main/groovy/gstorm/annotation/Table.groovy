package gstorm.annotation
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Retention (RetentionPolicy.RUNTIME)
@interface Table {

    String name()
}
