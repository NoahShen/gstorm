package models

import gstorm.annotation.Table

@Table(name = "TestTable")
class ClassWithTable {
    String name
}
