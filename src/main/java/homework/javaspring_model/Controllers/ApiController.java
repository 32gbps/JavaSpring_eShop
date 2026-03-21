package homework.javaspring_model.Controllers;

import homework.javaspring_model.Models.ApiResponse;
import homework.javaspring_model.Models.Clothes;
import homework.javaspring_model.Models.ClothesType;
import homework.javaspring_model.Models.DTOClothes;
import homework.javaspring_model.Services.ClothesService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import net.datafaker.Faker;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping(value = "/getClothesById", params = {"id"})
    public ResponseEntity<?> getClotheById(@RequestParam Integer id) {
        try {
            String status = "";
            String message = "";

            if(Service.isExistById(id.longValue())) {
                status = "success";
                message = "";
                var dto = new DTOClothes(Service.getClothesById(id.longValue()));
                return ResponseEntity.ok(new ApiResponse(status, message, dto));
            }
            else{
                status = "fail";
                message = "Товар с данным ID не найден!";
                return ResponseEntity.ok(new ApiResponse(status, message));
            }
        } catch (Exception e) {
            // Ответ с ошибкой
            ApiResponse errorResponse = new ApiResponse("error", "Ошибка: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @PostMapping(value = "/deleteClothesById", params = {"id"})
    public ResponseEntity<?> deleteClotheById(@RequestParam Integer id) {
        try {
            String status = "";
            String message = "";
            ApiResponse response;
            if(!Service.isExistById(id.longValue())) {
                status = "fail";
                message = "Товар с данным ID не найден!";
            }
            else{
                status = "success";
                message = "Предмет успешно удалён!";
            }
            // Успешный ответ с сообщением
            response = new ApiResponse(status, message);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Ответ с ошибкой
            ApiResponse errorResponse = new ApiResponse("error", "Ошибка при удалении: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @PostMapping(value = "/editClothesById")
    public ResponseEntity<?> editClotheById(@RequestBody DTOClothes clothesDto) {
        try {
            IO.println("Что пришло: " + clothesDto);
            String status;
            String message;
            // Проверяем существование товара
            if (!Service.isExistById(clothesDto.getId())) {
                status = "fail";
                message = "Товар с данным ID не найден!";
            } else {
                // Обновляем товар
                Service.updateClothes(clothesDto.getId(), clothesDto.toEntity());
                status = "success";
                message = "Товар успешно обновлён!";
            }

            var response = new ApiResponse(status, message);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ApiResponse errorResponse = new ApiResponse("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @PostMapping(value = "/addClothes")
    public ResponseEntity<?> addClothes(@RequestBody DTOClothes clothesDto) {
        try {
            String status;
            String message;

            // Проверяем существование товара
            if (Service.isExistByName(clothesDto.getName())) {
                status = "fail";
                message = "Товар с данным названием уже существует!";

                return ResponseEntity.ok(new ApiResponse(status, message));
            }
            var res = Service.addClothes(clothesDto.toEntity());
            if (res == null){
                status = "fail";
                message = "Ошибка при добавлении товара";
                return ResponseEntity.ok(new ApiResponse(status, message));
            }
            status = "success";
            message = "Товар успешно добавлен! ID:" + res.getId();
            var response = new ApiResponse(status, message);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ApiResponse errorResponse = new ApiResponse("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
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
