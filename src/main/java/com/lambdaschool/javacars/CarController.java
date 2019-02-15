package com.lambdaschool.javacars;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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


}
