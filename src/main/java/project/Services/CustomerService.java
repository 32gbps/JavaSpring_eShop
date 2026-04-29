package project.Services;

import project.Models.User.Person.Customer;
import project.Repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class CustomerService {
    CustomerRepository customerRepository;

    public Optional<Customer> findById(Long id){
        return customerRepository.findById(id);
    }
    public Optional<Customer> findByName(String name){
        return customerRepository.findByName(name);
    }
    public Optional<Customer> findByUsername(String username){
        return customerRepository.findByUsername(username);
    }
    public Optional<Customer> addPerson(Customer customer){
        try{
            return Optional.of(customerRepository.save(customer));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    public Long getCount(){
        return customerRepository.count();
    }
    public void ClearTable(){
        customerRepository.deleteAll();
    }
}
