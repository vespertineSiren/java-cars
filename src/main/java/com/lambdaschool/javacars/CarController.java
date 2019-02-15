package com.lambdaschool.javacars;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CarController {

    private final CarRepository carrepo;
    private final RabbitTemplate rt;

    public CarController(CarRepository carrepo, RabbitTemplate rt) {
        this.carrepo = carrepo;
        this.rt = rt;
    }



}
