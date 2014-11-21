package models

import gstorm.annotation.Entity

/**
 * simplest model, just for sanity checks
 */
@Entity
class Person {
    def firstName
    def lastName
    int age
}
