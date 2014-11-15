package models

import gstorm.annotation.Column

class ClassAutoTimestamp {
    Integer id
    String name
    @Column(name = "addTime")
    Date dateCreated

    @Column(name = "updateTime")
    Date lastUpdated
}
