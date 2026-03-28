package homework.javaspring_model.Services;

import homework.javaspring_model.Models.Product.Product;
import homework.javaspring_model.Models.Product.ProductDto;
import homework.javaspring_model.Repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Optional<Product> findById(Long id){
        return productRepository.findById(id);
    }
    public Optional<Product> findByName(String name){
        return productRepository.findByName(name);
    }
    public boolean isExistById(Long id){ return productRepository.existsById(id);}
    public ProductDto getProductById(Long id){
        var entity = productRepository.getReferenceById(id);
        return new ProductDto(entity);
    }
    public Optional<Product> addProduct(Product product){
        try{
            return Optional.of(productRepository.save(product));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    public List<ProductDto> getFirstNElements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int n) {

        var pEntityList = productRepository.findAll(
                PageRequest.of(page, n, Sort.by("name").ascending())
        ).stream().toList();
        List<ProductDto> pDtoList = new ArrayList<>();
        pEntityList.forEach(p->{
            pDtoList.add(new ProductDto(p));
        });
        return pDtoList;
    }
    public Long getCount(){
        return productRepository.count();
    }
    public void ClearTable(){
        productRepository.deleteAll();
    }
    public void deleteById(Long id){
        productRepository.deleteById(id);
    }
    public void deleteProduct(Product product){
        productRepository.delete(product);
    }
}
