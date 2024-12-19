package ru.terentyev.itq_number_generate_service.services;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import ru.terentyev.itq_number_generate_service.entities.NumberObject;
import ru.terentyev.itq_number_generate_service.repositories.NumberRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class NumberGenerateServiceImpl implements NumberGenerateService{
    private final NumberRepository numberRepository;
    private final Random random;


    public NumberGenerateServiceImpl(NumberRepository numberRepository) {
        this.numberRepository = numberRepository;
        this.random = new Random();
    }

    @Override
    public NumberObject startGeneration() {
        String datePart = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());
        return generateRandomPartAndSave(datePart, new NumberObject(), 0);
    }

    private NumberObject generateRandomPartAndSave(String datePart, NumberObject numberObject, int attemptsCount){
        if (attemptsCount >= 10) {
            throw new RuntimeException("Не удалось сгенерировать уникальный номер с " + attemptsCount + "-ого раза. Прекращаем попытки.");
        }
        try {
            numberObject.setNumber(random.nextInt(10000, 99999) + datePart);
            numberObject = numberRepository.save(numberObject);
            System.out.println("Уникальный номер " + numberObject.getNumber() + " создан.");
            return numberObject;
        } catch (DuplicateKeyException e) {
            System.out.println("Ошибка дублирования номера" + numberObject.getNumber() + ". Пробуем еще раз.");
            return generateRandomPartAndSave(datePart, numberObject, ++attemptsCount);
        }
    }

    //    private NumberObject generateRandomPartAndSave(String datePart, NumberObject numberObject){
//        try {
//            numberObject.setNumber(String.valueOf(random.nextInt(10000, 99999)) + datePart);
//            numberObject = numberRepository.save(numberObject);
//            System.out.println("Уникальный номер " + numberObject.getNumber() + " создан");
//        } catch (DuplicateKeyException e) {
//            System.out.println("Ошибка дублирования номера. Пробуем еще раз");
//            numberObject = numberRepository.save(generateRandomPartAndSave(datePart, numberObject));
//        }
//        return numberObject;
//    }
}
