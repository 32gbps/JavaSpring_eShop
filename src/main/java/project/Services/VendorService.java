package project.Services;

import project.Models.User.Vendor.Vendor;
import project.Models.User.Vendor.VendorDto;
import project.Repositories.VendorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class VendorService {
    VendorRepository vendorRepository;
    public Optional<Vendor> findById(Long id){
        return vendorRepository.findById(id);
    }
    public Optional<Vendor> findByName(String name){
        return vendorRepository.findByName(name);
    }
    public Optional<Vendor> findByUsername(String name){
        return vendorRepository.findByUsername(name);
    }
    public Optional<Vendor> addCompany(Vendor vendor){
        try{
            return Optional.of(vendorRepository.save(vendor));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    public Long getCount(){
        return vendorRepository.count();
    }
    public void ClearTable(){
        vendorRepository.deleteAll();
    }
}
