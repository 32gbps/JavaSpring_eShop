package project.Config;

import jakarta.annotation.PostConstruct;
import project.Models.Product.Attribute;
import project.Models.Product.ProductDto;
import project.Models.Product.ProductMapper;
import project.Models.User.Vendor.Vendor;
import project.Models.User.Person.Customer;
import project.Models.User.Role;
import project.Models.User.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;           // @Component
import org.slf4j.Logger;                                  // Logger
import org.slf4j.LoggerFactory;                           // LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder; // PasswordEncoder
import project.Services.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class DatabaseInitializer {

    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

    private final RoleService roleService;
    private final UserService userService;
    private final CustomerService customerService;
    private final VendorService vendorService;
    private final ProductService productService;

    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initTestData() {
        if(productService.getCount() > 0)
            return;
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
        var roleUser = addRole("CUSTOMER");
        var userCustomer = addUserWithRole("customer", "customer123", "customer@example.com", roleUser);
        addCustomerWitchUserAndRoleInfo("John", "Dow", LocalDate.of(1970,1,1), userCustomer);
        // Компания
        log.info("Добавление компании");
        var roleVendor = addRole("VENDOR");
        var userVendor = addUserWithRole("vendor", "vendor123", "vendor@example.com", roleVendor);
        var vendor = addVendorWitchUserAndRoleInfo("ООО \"Рога и копыта\"", "1234-5678-9abc-defg", userVendor);


        log.info("Добавление товаров");

        String deco = "Дрель-шуруповерт DEKO DKCD20FU-Li 063-4093 – удобный безударный инструмент, предназначенный для работы с крепежами. Он использует быстрозажимной патрон, что способствует более быстрой смене оснастки и обеспечивает повышение скорости работы. Применяется такой инструмент в слесарно-монтажных работах, позволяя с легкостью высверливать отверстия в различных предметах и поверхностях, извлекать или вкручивать крепеж.";
        String finePower = "Набор бит Finepower FPTR45 предназначен для оснащения винтовертов, дрелей-шуруповертов и шуруповертов. Комплект состоит из 45 предметов, в том числе торцевых головок 6, 8, 10 мм. Набор Finepower FPTR45 включает в себя биты T10, T15, T20, T25, T27, T30, T40, SL3, SL4, SL5, SL6, PH1, PH2, PH3, PZ1, PZ2, PZ3.";
        String haier = "Посудомоечная машина Haier HDWE11-36WE4RU белого цвета вмещает 11 комплектов посуды. Три корзины и складные держатели позволяют разместить внутри как крупногабаритные кастрюли, так и мелкие предметы. Конденсационная сушка обеспечивает равномерную просушку посуды без разводов. В модели предусмотрено 7 уровней регулировки жесткости воды.\n" +
                "Посудомоечная машина Haier HDWE11-36WE4RU поддерживает функцию отсрочки старта на 24 часа.";
        Map<String, String> attributes = new HashMap<>();
        attributes.put("Вид", "безударный");
        attributes.put("Тип двигателя", "щеточный");
        attributes.put("Тип патрона", "3/8\", быстрозажимной");
        attributes.put("Максимальный крутящий момент","42 Н·м ");
        attributes.put("Диаметр патрона","до 10 мм ");
        attributes.put("Питание","2 А*ч ");
        addProduct("Дрель-шуруповерт DEKO",deco, 2950, vendor, attributes);

        attributes = new HashMap<>();
        attributes.put("Биты","1/4\"");
        attributes.put("Головки 1/4\"","6 мм, 10 мм, 8 мм ");
        attributes.put("Предметов","45 шт");
        addProduct("Набор бит Finepower",finePower, 1250, vendor, attributes);

        attributes = new HashMap<>();
        attributes.put("Габариты","44.8 см x 86.6 см x 60.5 см ");
        attributes.put("Количество комплектов","11");
        attributes.put("Корзина д/приборов","есть");
        attributes.put("Автооткрывание дверцы","есть");
        attributes.put("Класс энергопотребления","A");
        attributes.put("Тип сушки","конденсационная");
        addProduct("Посудомоечная машина Haier",haier, 22899, vendor, attributes);

        attributes = new HashMap<>();
        attributes.put("Сезон","На любой сезон");
        attributes.put("Материал","Полиамид, Эластан, Хлопок");
        attributes.put("Состав материала","хлопок 85% полиамид 10% эластан 5%");
        attributes.put("Коллекция","Базовая коллекция");
        attributes.put("Стиль","Повседневный");
        attributes.put("Тип","Носки");
        String socks = "Представляем вашему вниманию носки мужские набор — идеальное решение для стильных и современных мужчин. В нашем наборе вы найдете разнообразные варианты, включая носки мужские короткие, которые идеально подходят для летнего сезона. Эти носки обеспечивают удобство и легкость в ношении, позволяя вашим ногам дышать.";
        addProduct("Носки, 4 пары ",socks, 116, vendor, attributes);

        attributes = new HashMap<>();
        attributes.put("Артикул","1549316942");
        attributes.put("Автор","Цысинь Лю");
        attributes.put("Автор на обложке","Лю Цысинь");
        attributes.put("Тип бумаги в книге","Офсетная");
        attributes.put("Тип обложки","Твердый переплет");
        attributes.put("Язык издания","Русский");
        String threeBodyDescription = "Китай охвачен хаосом «культурной революции». Несмотря на это, в ходе секретного военного проекта, в космос отправлялись сигналы с целью обнаружения инопланетного разума. Проект завершился успехом, и один из сигналов приняла иная цивилизация. Вот только чужие находятся на краю гибели и Земля кажется идеальной для вторжения.\n" +
                " В ожидании прибытия «гостей» часть человечества готова сдать инопланетянам Землю, чтобы те изменили порочный мир к лучшему. Другие же готовы бороться против вторжения до последнего.";
        addProduct("Задача трех тел",threeBodyDescription, 537, vendor, attributes);

        attributes = new HashMap<>();
        attributes.put("Артикул","1615382032");
        attributes.put("Тип","Кухонный модуль навесной");
        attributes.put("Ширина, см","60");
        attributes.put("Глубина, см","31.6");
        attributes.put("Тип модуля","Навесной");
        attributes.put("Высота, см","60");
        String kitchenModuleDescription = "Кухонные модули \"Настя\" - это серия кухонной мебели, сочетающая в себе лаконичный дизайн, доступность и функциональность. Большой ассортимент модулей позволяет комбинировать навесные и напольные шкафы различных размеров, чтобы создать идеальное решение для Вашего пространства.";
        addProduct("Кухонный модуль навесной Настя", kitchenModuleDescription, 2692, vendor, attributes);

        printCountAll();
    }

    private void addProduct(String name, String description, Integer price, Vendor vendor, Map<String, String> attributes) {
        try {
            if(productService.findByName(name).isPresent()){
                log.info("Товар уже есть в базе");
            }
            else{
                var nProduct = new ProductDto();
                nProduct.setName(name);
                nProduct.setDescription(description);
                nProduct.setPrice(price);
                //nProduct.setVendor(vendor);
                List<Attribute> attr = new ArrayList<>();
                attributes.forEach((key, value)->{
                    attr.add(new Attribute(key,value));
                });
                nProduct.setAttributes(attr);
                var entity = ProductMapper.DtoToEntity(nProduct);
                var addedProduct = productService.addProduct(entity).orElseThrow();
                log.info("Добавлен Product {}", addedProduct.getName());
            }
        } catch (Exception e) {
            log.info("Исключение {0}", e);
        }
    }
    private Vendor addVendorWitchUserAndRoleInfo(String vendorName, String identifier, User user) {

        var vendor = vendorService.findByName(vendorName).orElseGet(()->{
            Vendor nVendor = new Vendor();
            nVendor.setUser(user);
            nVendor.setIdentifier(identifier);
            nVendor.setName(vendorName);
            return vendorService.addCompany(nVendor).orElseThrow();
        });
        log.info("Добавлен Company {}", vendor);
        return vendor;
    }
    private Customer addCustomerWitchUserAndRoleInfo(String name, String surname, LocalDate birthdate, User user) {

        var customer = customerService.findByName(name).orElseGet(()->{
            Customer nCustomer = new Customer();
            nCustomer.setUser(user);
            nCustomer.setName(name);
            nCustomer.setSurname(surname);
            nCustomer.setBirthDate(birthdate);
            return customerService.addPerson(nCustomer).orElseThrow();
        });
        log.info("Добавлен Person {}", customer);
        return customer;
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
        log.info("Person: {}", customerService.getCount());
        log.info("Company: {}", vendorService.getCount());
        log.info("Product: {}", productService.getCount());
    }
    private void ClearAllTables(){
        roleService.ClearTable();
        userService.ClearTable();
        customerService.ClearTable();
        vendorService.ClearTable();
        productService.ClearTable();
    }
}