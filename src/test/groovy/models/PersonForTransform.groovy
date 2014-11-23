package models

import gstorm.annotation.Entity

/**
 * Created by noahshen on 14-11-22.
 */
@Entity
class PersonForTransform {
    def firstName
    def lastName
    int age
}
