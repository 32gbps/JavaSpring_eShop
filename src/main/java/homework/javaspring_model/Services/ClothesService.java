package homework.javaspring_model.Services;

import homework.javaspring_model.Models.Clothes;
import homework.javaspring_model.Repositories.ClothesRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClothesService {
    private final ClothesRepository clothesRepository;

    public ClothesService(ClothesRepository clothesRepository) {
        this.clothesRepository = clothesRepository;
    }

    // UPDATE
    public Clothes updateClothes(Long id, Clothes updatedClothes) {
        var isPresent = clothesRepository.existsById(id);
        if(!isPresent)
            throw new RuntimeException("Not found");
        else
            updatedClothes.setId(id);
        return clothesRepository.save(updatedClothes);
    }
    public Clothes addClothes(Clothes clothes) {
        var isPresent = clothesRepository.findByParametersIgnoreCase(clothes.getName(),
                clothes.getType(),
                clothes.getSize(),
                clothes.getColor(),
                clothes.getBrand()).isPresent();
        if(isPresent)
            throw new RuntimeException("Already exist");

        IO.println("isPresent: " + isPresent);

        clothes.setId(null);
        IO.println("clothes: " + clothes.getId());
         var result = clothesRepository.insertClothes(clothes.getName(),
                clothes.getType(),
                clothes.getSize(),
                clothes.getColor(),
                clothes.getBrand(),
                clothes.getPrice());
        IO.println("result: " + result);
        return clothes;
    }
    public Clothes getClothesById(long id) {
        return clothesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Already exist"));
    }
    public void deleteClothes(Long id) {
        clothesRepository.deleteById(id);
    }

    public List<Clothes> getFirstNElements(long n) {
        return clothesRepository.findAll(PageRequest.of(0, (int) n, Sort.by("name").ascending())).stream().toList();
    }
    public List<Clothes> findClothesByName(String name, int limit) {
        return clothesRepository.findByNameContainingIgnoreCaseOrderByName(
                name,
                PageRequest.of(0, limit)
        );
    }
    public List<Clothes> getAll(){
        return clothesRepository.findAll();
    }
}


