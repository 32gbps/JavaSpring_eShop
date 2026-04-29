package project.Services;

import project.Models.Product.Product;
import project.Models.Product.ProductDto;
import project.Models.Product.ProductMapper;
import project.Repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Optional<ProductDto> findById(Long id){
        try{
            return Optional.of(ProductMapper.EntityToDto(productRepository.findById(id).orElseThrow()));
        }
        catch (Exception e){
            return Optional.empty();
        }
    }
    public Optional<ProductDto> findByName(String name){
        try{
            return Optional.of(ProductMapper.EntityToDto(productRepository.findByName(name).orElseThrow()));
        }
        catch (Exception e){
            return Optional.empty();
        }
    }
    public boolean isExistById(Long id){ return productRepository.existsById(id);}
    public ProductDto getProductById(Long id){
        var entity = productRepository.getReferenceById(id);
        return ProductMapper.EntityToDto(entity);
    }
    public Optional<Product> addProduct(Product product){
        try{
            return Optional.of(productRepository.save(product));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    public List<ProductDto> getFirstNElements(int page, int n) {
        // Нормализация значений
        int validPage = Math.max(0, page);
        int validSize = n > 0 ? n : 10;

        PageRequest pageRequest = PageRequest.of(validPage, validSize, Sort.by("name").ascending());
        Page<Product> productPage = productRepository.findAll(pageRequest);

        return productPage.stream()
                .map(ProductMapper::EntityToDto)  // Используем stream API
                .collect(Collectors.toList());
    }
    public Long getCount(){
        return productRepository.count();
    }
    public void ClearTable(){
        productRepository.deleteAll();
    }
    public void deleteProductById(Long id){
        productRepository.deleteById(id);
    }
}
