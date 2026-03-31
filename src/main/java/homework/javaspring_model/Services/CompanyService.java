package homework.javaspring_model.Services;

import homework.javaspring_model.Models.User.Company.Company;
import homework.javaspring_model.Repositories.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class CompanyService {
    CompanyRepository companyRepository;
    public Optional<Company> findById(Long id){
        return companyRepository.findById(id);
    }
    public Optional<Company> findByName(String name){
        return companyRepository.findByName(name);
    }
    public Optional<Company> findByUsername(String name){
        return companyRepository.findByUsername(name);
    }
    public Optional<Company> addCompany(Company company){
        try{
            return Optional.of(companyRepository.save(company));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    public Long getCount(){
        return companyRepository.count();
    }
    public void ClearTable(){
        companyRepository.deleteAll();
    }
}
