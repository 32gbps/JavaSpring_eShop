package homework.javaspring_model.Config;

import homework.javaspring_model.Models.Product.Product;
import homework.javaspring_model.Models.Product.ProductDto;
import homework.javaspring_model.Models.Product.ProductMapper;
import homework.javaspring_model.Models.User.Company.Company;
import homework.javaspring_model.Models.User.Person.Person;
import homework.javaspring_model.Models.User.Role;
import homework.javaspring_model.Models.User.User;
import homework.javaspring_model.Services.*;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;           // @Component
import org.slf4j.Logger;                                  // Logger
import org.slf4j.LoggerFactory;                           // LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder; // PasswordEncoder

import java.time.LocalDate;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

// User

@Component
@AllArgsConstructor
public class DatabaseInitializer {

    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

    private final RoleService roleService;
    private final UserService userService;
    private final PersonService personService;
    private final CompanyService companyService;
    private final ProductService productService;

    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initTestData() {
        //ClearAllTables();
        printCountAll();
        // Администратор
        log.info("Добавление администратора");
        var roleAdmin = addRole("ADMIN");
        addUserWithRole("admin", "admin123", "admin@example.com", roleAdmin);
        // Модератор
        log.info("Добавление модератора");
        var roleModerator = addRole("MODERATOR");
        addUserWithRole("moderator", "moderator123", "moderator@example.com", roleModerator);
        // Обычный пользователь
        log.info("Добавление обычного пользователя");
        var roleUser = addRole("USER");
        var userPerson = addUserWithRole("user", "user123", "user@example.com", roleUser);
        var person = addPersonWitchUserAndRoleInfo("John", "Dow", LocalDate.of(1970,1,1), userPerson);
        // Компания
        log.info("Добавление компании");
        var roleVendor = addRole("VENDOR");
        var userVendor = addUserWithRole("vendor", "vendor123", "vendor@example.com", roleVendor);
        var vendor = addCompanyWitchUserAndRoleInfo("ООО \"Рога и копыта\"", "1234-5678-9abc-defg", userVendor);


        log.info("Добавление товаров");

        String s0 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla mauris felis, facilisis ut turpis eu, tempor venenatis ipsum. Donec aliquet mattis venenatis. Phasellus id sodales ligula, nec pretium quam. Integer eget gravida erat. Vivamus efficitur eu lacus eu rutrum. Sed iaculis consequat erat, et dapibus arcu consequat at. Proin ullamcorper, felis non euismod porta, eros nulla tempus nunc, et condimentum urna mi eget magna. Integer bibendum pharetra pulvinar. Etiam nec lectus placerat, finibus orci id, laoreet quam. Quisque non augue vitae nulla fermentum maximus. Curabitur ut libero lorem. Nullam ultricies tellus quis tincidunt semper. Nulla ultrices, nulla vitae cursus finibus, urna urna lobortis metus, nec gravida enim odio sit amet arcu. Sed eu mattis sem, et vehicula eros.";
        String s1 = "Nunc viverra libero non turpis euismod, imperdiet accumsan diam dignissim. Proin nec tincidunt nunc. Phasellus tristique erat ut leo volutpat dapibus eu in tortor. Morbi cursus ultricies nunc non laoreet. Ut fringilla vulputate risus, ut rhoncus lacus eleifend in. Nunc iaculis ornare est vel pulvinar. Vivamus ac ante commodo, malesuada augue pulvinar, eleifend odio. Vivamus eleifend dolor a erat varius, id porttitor nunc posuere. Nullam malesuada porttitor dolor vel sagittis. ";
        String s2 = "Mauris feugiat eu arcu blandit lobortis. Cras efficitur viverra lacus, id elementum nulla. Cras lobortis purus congue sapien consequat placerat. Ut in arcu eget purus ullamcorper eleifend vitae vitae nunc. Pellentesque interdum dolor nibh, vitae efficitur dolor ullamcorper a. Mauris erat lacus, tristique ac dapibus nec, vehicula eu nunc. Cras vel hendrerit risus, vitae iaculis quam. Pellentesque cursus dui felis, eu scelerisque turpis feugiat a. Nam tincidunt velit at consectetur ornare. Etiam ultricies lacus at aliquet tempor. Suspendisse semper cursus orci id molestie. Aliquam id mauris nisi. Nullam lorem est, ullamcorper ac lectus vel, dapibus blandit tortor. Pellentesque a sollicitudin metus, ut dapibus libero.";
        String s3 = "Morbi iaculis, ligula in accumsan ultrices, augue diam blandit augue, vitae aliquam justo sem in est. Mauris porta, mi eu porttitor aliquet, nisi velit feugiat diam, ut aliquam dui ante at mauris. Nullam id tortor metus. Suspendisse nec nisl et elit interdum iaculis non a nunc. Donec imperdiet dolor augue, ac sollicitudin risus fermentum id. Ut in purus convallis, luctus eros imperdiet, cursus tellus. Praesent eget gravida magna. In dui elit, sagittis at mollis eu, interdum eget purus. Fusce nunc risus, gravida nec massa quis, fermentum porttitor felis. Sed eleifend pretium cursus. Nullam vulputate tortor in lectus dapibus, eget feugiat felis volutpat. Phasellus dignissim feugiat porta. Nunc sed quam non turpis condimentum aliquam. Duis quam lacus, laoreet eu eros et, tristique tempor sem.";
        String s4 = "Sed commodo mattis est, et semper libero dapibus id. Mauris vulputate augue lobortis lectus malesuada finibus. Nullam tempor tempor risus, et venenatis elit aliquet eget. Maecenas a scelerisque velit. Nulla rhoncus nunc sed sem feugiat faucibus. Maecenas non augue lorem. Nulla in magna vitae turpis porttitor laoreet. Ut ullamcorper in velit in laoreet. Fusce eleifend accumsan maximus. Integer vel est eros. Pellentesque mattis libero nunc, non posuere libero rhoncus a. Etiam malesuada enim et diam mattis malesuada vel quis quam. Vivamus feugiat maximus posuere. Vivamus molestie mauris mauris, a sodales nisi imperdiet nec.";

        Map<String, String> attributes = new HashMap<>();
        attributes.put("Цвет", "Белый");
        attributes.put("Вес", "200гр.");
        attributes.put("Производитель", "ООО \"Подушечки\"");
        addProduct("Наволочка",s0, 400, vendor, attributes);

        attributes = new HashMap<>();
        attributes.put("Цвет", "Черный");
        attributes.put("Вес", "900гр.");
        attributes.put("Материал", "Кожа");
        attributes.put("Страна", "Китай");
        addProduct("Ботинок (левый)",s1, 700, vendor, attributes);

        attributes = new HashMap<>();
        attributes.put("Цвет", "Золотой");
        attributes.put("Вес", "0гр.");
        attributes.put("Материал", "Чистая математика");
        attributes.put("Страна", "Китай");
        attributes.put("Волатильность", "Ещё какая!");
        attributes.put("Целесообразность", "Сомнительно");
        addProduct("Мокрый биткоин",s2, 275490, vendor, attributes);

        attributes = new HashMap<>();
        attributes.put("Цвет", "Ржавый металл");
        attributes.put("Вес", "1кг.");
        attributes.put("Материал", "Оцинкованная жесть");
        attributes.put("Страна", "Молдавия");
        attributes.put("Объём", "12л");
        attributes.put("Серьёзно?", "Да!");
        addProduct("Ведро компрессии",s3, 27800, vendor, attributes);

        attributes = new HashMap<>();
        attributes.put("Состав", "Мука, вода, сахар, дрожжи, что-то ещё...");
        attributes.put("Вес", "1кг.");
        attributes.put("Срок годности", "2 дня");
        attributes.put("Страна", "Россия");
        attributes.put("Комплектация", "Дырка поставляется отдельно");
        addProduct("Бублик (без дырки)",s4, 40, vendor, attributes);

        attributes = new HashMap<>();
        attributes.put("Состав", "Пустота");
        attributes.put("Вес", "0кг.");
        attributes.put("Срок годности", "Неограниченно");
        attributes.put("Страна", "Россия");
        attributes.put("Комплектация", "Бублик поставляется отдельно");
        addProduct("Дырка (от бублика)", "o", 8, vendor, attributes);

        printCountAll();
    }

    private void addProduct(String name, String description, Integer price, Company company, Map<String, String> attributes) {
        try {
            if(productService.findByName(name).isPresent()){
                log.info("Товар уже есть в базе");
            }
            else{
                var nProduct = new ProductDto();
                nProduct.setName(name);
                nProduct.setDescription(description);
                nProduct.setPrice(price);
                nProduct.setCompany(company);
                //nProduct.setAttributes(attributes);
                var entity = ProductMapper.DtoToEntity(nProduct);
                var addedProduct = productService.addProduct(entity).orElseThrow();
                log.info("Добавлен Product {}", addedProduct.getName());
            }
        } catch (Exception e) {
            log.info("Исключение {0}", e);
        }
    }
    private Company addCompanyWitchUserAndRoleInfo(String vendorName, String identifier, User user) {

        var company = companyService.findByName(vendorName).orElseGet(()->{
            Company nCompany = new Company();
            nCompany.setUser(user);
            nCompany.setIdentifier(identifier);
            nCompany.setName(vendorName);
            return companyService.addCompany(nCompany).orElseThrow();
        });
        log.info("Добавлен Company {}", company);
        return company;
    }
    private Person addPersonWitchUserAndRoleInfo(String name, String surname, LocalDate birthdate, User user) {

        var person = personService.findByName(name).orElseGet(()->{
            Person nPerson = new Person();
            nPerson.setUser(user);
            nPerson.setName(name);
            nPerson.setSurname(surname);
            nPerson.setBirthDate(birthdate);
            return personService.addPerson(nPerson).orElseThrow();
        });
        log.info("Добавлен Person {}", person);
        return person;
    }
    private User addUserWithRole(String username, String rawPassword, String email, Role role) {
        User user = userService.findByUsername(username)
                .orElseGet(
                        ()-> {
                            User nUser = new User();
                            nUser.setUsername(username);
                            nUser.setPassword(passwordEncoder.encode(rawPassword));
                            nUser.setEmail(email);
                            nUser.setRole(role);
                            return userService.addUser(nUser).orElseThrow();
                        });
        log.info("Добавлен User {}", user);
        return user;
    }
    private Role addRole(String roleName){
        //Ищем уже имеющуюся роль или добавляем новую или копаемся в ошибках...
        var role = roleService.findByName(roleName)
                .orElseGet(
                        ()-> roleService.addRole(new Role(roleName))
                                .orElseThrow()
                );

        log.info("Добавлен Role {}", role.getName());
        return role;
    }

    private void printCountAll(){
        log.info("Количество записей в таблицах.");
        log.info("Role: {}", roleService.getCount());
        log.info("User: {}", userService.getCount());
        log.info("Person: {}", personService.getCount());
        log.info("Company: {}", companyService.getCount());
        log.info("Product: {}", productService.getCount());
    }
    private void ClearAllTables(){
        roleService.ClearTable();
        userService.ClearTable();
        personService.ClearTable();
        companyService.ClearTable();
        productService.ClearTable();
    }
}