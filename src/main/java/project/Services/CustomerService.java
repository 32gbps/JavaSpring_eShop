package project.Services;

import project.Models.Product.ProductDto;
import project.Models.User.Customer.Customer;
import project.Models.User.Customer.CustomerDto;
import project.Repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class CustomerService {
    CustomerRepository customerRepository;

    public Optional<CustomerDto> findById(UUID id){
        try{
            return Optional.of(customerRepository
                    .findById(id)
                    .orElseThrow().ToDTO());
        } catch (Exception e) {
            IO.println(e.getMessage());
            return Optional.empty();
        }
    }
    public Optional<CustomerDto> findByName(String name){
        try{
            return Optional.of(customerRepository
                    .findByName(name)
                    .orElseThrow().ToDTO());
        } catch (Exception e) {
            IO.println(e.getMessage());
            return Optional.empty();
        }
    }
    public Optional<CustomerDto> findByUsername(String username){
        try{
            return Optional.of(customerRepository
                    .findByUsername(username)
                    .orElseThrow().ToDTO());
        } catch (Exception e) {
            IO.println(e.getMessage());
            return Optional.empty();
        }
    }
    public Optional<Customer> addCustomer(Customer customer){
        try{
            return Optional.of(customerRepository.save(customer));
        } catch (Exception e) {
            IO.println(e.getMessage());
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
