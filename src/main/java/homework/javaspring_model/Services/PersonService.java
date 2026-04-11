package homework.javaspring_model.Services;

import homework.javaspring_model.Models.User.Person.Person;
import homework.javaspring_model.Repositories.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class PersonService {
    PersonRepository personRepository;

    public Optional<Person> findById(Long id){
        return personRepository.findById(id);
    }
    public Optional<Person> findByName(String name){
        return personRepository.findByName(name);
    }
    public Optional<Person> findByUsername(String username){
        return personRepository.findByUsername(username);
    }
    public Optional<Person> addPerson(Person person){
        try{
            return Optional.of(personRepository.save(person));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    public Long getCount(){
        return personRepository.count();
    }
    public void ClearTable(){
        personRepository.deleteAll();
    }
}
