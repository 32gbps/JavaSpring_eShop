package homework.javaspring_model.Controllers;

import homework.javaspring_model.Models.Clothes;
import homework.javaspring_model.Services.ClothesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clothes")
public class ApiController {
    private final ClothesService Service;
    public ApiController(ClothesService clothesService){
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

        return Service.getFirstNElements(count);
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
    @GetMapping("/add")
    public String getListByProperty() {

        return "LoadForm";
    }
}
