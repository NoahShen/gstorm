package gstorm
import models.Person

/**
 * Created by noahshen on 14-11-18.
 */
class PersonService {

    List<Person> findPerson(Integer startAge) {
        List ages = [21,22,28]
        Person.find {
            age in ages
        }
    }
}
