package project.Services;

import project.Models.User.Role;
import project.Repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Optional<Role> findById(Long id){
        return roleRepository.findById(id);
    }
    public Optional<Role> findByName(String name){
        return roleRepository.findByName(name);
    }
    public Optional<Role> addRole(Role role){
        try{
            return Optional.of(roleRepository.save(role));
        }
        catch (Exception e)
        {
            return Optional.empty();
        }
    }
    public Long getCount(){
        return roleRepository.count();
    }
    public void ClearTable(){
        roleRepository.deleteAll();
    }
}
