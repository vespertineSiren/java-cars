package com.lambdaschool.javacars;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class CarController {

    private final CarRepository carrepo;
    private final RabbitTemplate rt;

    public CarController(CarRepository carrepo, RabbitTemplate rt) {
        this.carrepo = carrepo;
        this.rt = rt;
    }

    //GET functions
    @GetMapping("/cars/id[id]")
    public Car returnCarBasedonID(@PathVariable Long id){
        return carrepo.findById(id).orElseThrow(()-> new CarNotFoundException(id));
    }

    @GetMapping("/cars/year/{year}")
    public List<Car> returnCarListbyYear(@PathVariable int year) {
        return carrepo.findAll()
                .stream()
                .filter(c -> c.getYear() == year)
                .collect(Collectors.toList());
    }

    @GetMapping("/cars/brand/{brand}")
    public List<Car> returnCarListbyBrandwithMessage(@PathVariable String brand) {

        CarLog message = new CarLog("search for " + brand);
        rt.convertAndSend(JavacarsApplication.QUEUE_NAME, message.toString());
        log.info("search for {" + brand + "}");

        return carrepo.findAll()
                .stream()
                .filter(c -> c.getBrand().equalsIgnoreCase(brand))
                .collect(Collectors.toList());
    }

    //POST
    @PostMapping("/cars/upload")
    public List<Car> newCar(@RequestBody List<Car> newCars){

        CarLog message = new CarLog("Data loaded");
        rt.convertAndSend(JavacarsApplication.QUEUE_NAME, message.toString());
        log.info("Data loaded");

        return carrepo.saveAll(newCars);
    }

    @DeleteMapping("/cars/delete/{id}")
    public ObjectNode deleteEmployee(@PathVariable Long id)
    {
        carrepo.deleteById(id);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode deleted = mapper.createObjectNode();
        deleted.put("id", id);

        CarLog message = new CarLog("Deleted car " + id.toString());
        rt.convertAndSend(JavacarsApplication.QUEUE_NAME, message.toString());
        log.info("{" + id.toString() + "} Data deleted");

        return deleted;
    }


}
