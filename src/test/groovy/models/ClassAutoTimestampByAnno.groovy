package models

import gstorm.annotation.AutoDateCreated
import gstorm.annotation.AutoLastUpdated

class ClassAutoTimestampByAnno {
    Integer id
    String name

    @AutoDateCreated
    Date addTime

    @AutoLastUpdated
    Date updateTime
}
