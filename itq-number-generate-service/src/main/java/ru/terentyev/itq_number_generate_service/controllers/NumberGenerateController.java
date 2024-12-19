package ru.terentyev.itq_number_generate_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.terentyev.itq_number_generate_service.entities.NumberObject;
import ru.terentyev.itq_number_generate_service.services.NumberGenerateService;

@RestController
@RequestMapping("/api/v1/numbers")
public class NumberGenerateController {

    private final NumberGenerateService numberGenerateService;

    public NumberGenerateController(NumberGenerateService numberGenerateService) {
        this.numberGenerateService = numberGenerateService;
    }

    @GetMapping("/generate")
    public ResponseEntity<NumberObject> generateNumber() {
        return new ResponseEntity<>(numberGenerateService.startGeneration(), HttpStatus.CREATED);
    }
}
