package models

import gstorm.annotation.Id

class ClassWithIdAnnotation {
    @Id
    Integer uid
    String name
}
