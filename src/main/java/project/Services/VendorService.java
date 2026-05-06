package project.Services;

import project.Models.User.Vendor.Vendor;
import project.Models.User.Vendor.VendorDto;
import project.Repositories.VendorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class VendorService {
    VendorRepository vendorRepository;
    public Optional<VendorDto> findById(UUID id){
        try{
            return Optional.of(vendorRepository.findById(id).orElseThrow().ToDTO());
        }
        catch (Exception e){
            return Optional.empty();
        }
    }
    public Optional<VendorDto> findByName(String name){
        try{
            return Optional.of(vendorRepository.findByVendorName(name).orElseThrow().ToDTO());
        }
        catch (Exception e){
            return Optional.empty();
        }
    }
    public Optional<VendorDto> findByUsername(String name){
        try{
            return Optional.of(vendorRepository.findByUsername(name).orElseThrow().ToDTO());
        }
        catch (Exception e){
            return Optional.empty();
        }
    }
    public Optional<Vendor> addVendor(Vendor vendor){
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
