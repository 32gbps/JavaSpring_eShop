package homework.javaspring_model.Controllers;

import homework.javaspring_model.Models.Clothes;
import homework.javaspring_model.Models.ClothesType;
import homework.javaspring_model.Services.ClothesService;
import net.datafaker.Faker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@RestController
@RequestMapping("/api/clothes")
public class ApiController {
    private final ClothesService Service;
    private final Faker faker;
    public ApiController(ClothesService clothesService){
        var l = new Locale.Builder().setLanguage("ru").build();
        faker = new Faker(l);
        this.Service = clothesService;
    }
    @GetMapping()
    public String getMainPage(){
        return "http://localhost:8080/api/clothes/list?count={кол-во вещей для генерации}";
    }
    //http://localhost:8080/api/clothes/list?count={кол-во вещей}
    @GetMapping("list{count}")
    public List<Clothes> getList(int count) {
        final int MAXCOUNT = 1000;
        if(count > MAXCOUNT)
            count = MAXCOUNT;

        return Service.getFirstNElements(0, count);
    }
    @GetMapping("/generate{count}")
    public List<Clothes> generateRandClothes(int count) {
        Random r = new Random();
        String[] size = new String[]{"XS", "S", "M", "L", "XL", "XXL"};
        List<Clothes> ClothesList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ClothesList.add(new Clothes(
                    faker.commerce().productName(),
                    ClothesType.values()[r.nextInt(ClothesType.values().length)].getDisplayName(),
                    size[r.nextInt(size.length)],
                    faker.color().name(),
                    faker.commerce().brand(),
                    Double.parseDouble(faker.commerce().price(10.0, 3000.0).replace(',','.'))
            ));
        }

        return ClothesList;
    }
    @GetMapping("/list")
    public List<Clothes> getListByProperty(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {

        final int MAXCOUNT = 1000;
        if(limit > MAXCOUNT)
            limit = MAXCOUNT;

        return Service.findClothesByName(name, limit);
    }
    @GetMapping("/filter/name/{name}")
    public List<Clothes> findByName(String name){ return Service.findClothesByNameIsLikeIgnoreCase(name); }
    @GetMapping("/filter/type/{type}")
    public List<Clothes> findByType(String type){ return Service.findClothesByType(type); }
    @GetMapping("/filter/size/{size}")
    public List<Clothes> findBySize(String size){ return Service.findClothesBySize(size); }
    @GetMapping("/filter/brand/{brand}")
    public List<Clothes> findByBrand(String brand){ return Service.findClothesByBrand(brand); }
    @GetMapping("/filter/color/{color}")
    public List<Clothes> findByColor(String color){ return Service.findClothesByColor(color); }
    @GetMapping("/filter/price/{min}{max}")
    public List<Clothes> findByPriceBetween(Double min, Double max){ return Service.findClothesByPriceBetween(min, max); }
}
