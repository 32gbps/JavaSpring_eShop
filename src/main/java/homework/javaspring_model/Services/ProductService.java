package homework.javaspring_model.Services;

import homework.javaspring_model.Models.Clothes;
import homework.javaspring_model.Models.Product.ProductDto;
import homework.javaspring_model.Models.Product.ProductEntity;
import homework.javaspring_model.Repositories.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public boolean isExistById(Long id){ return productRepository.existsById(id);}
    public ProductDto getProductById(Long id){
        var entity = productRepository.getReferenceById(id);
        return new ProductDto(entity);
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
}
