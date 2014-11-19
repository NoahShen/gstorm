package gstorm
import models.Person

/**
 * Created by noahshen on 14-11-18.
 */
class PersonService {

    List<Person> findPerson(Integer startAge) {
        Person.find {
            age > 1
        }
    }
}
