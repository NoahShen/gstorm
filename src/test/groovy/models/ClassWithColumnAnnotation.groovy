package models

import gstorm.annotation.Column
import gstorm.annotation.Id

class ClassWithColumnAnnotation {
    @Id
    Integer uid

    @Column(name = "fullName")
    String name
}
