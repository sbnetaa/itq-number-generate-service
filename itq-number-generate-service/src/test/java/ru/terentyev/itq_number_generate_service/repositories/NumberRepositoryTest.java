package ru.terentyev.itq_number_generate_service.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import ru.terentyev.itq_number_generate_service.config.TestConfig;
import ru.terentyev.itq_number_generate_service.entities.NumberObject;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;


@DataMongoTest
@Import(TestConfig.class)
public class NumberRepositoryTest {

    @Autowired
    private NumberRepository numberRepository;
    private final Random random = new Random();

    @Test
    void save_ShouldThrowExceptionWhenNumberIsNotUnique() {
        NumberObject numberObject1 = new NumberObject();
        String number = String.valueOf(random.nextInt(0, 999999999));
        numberObject1.setNumber(number);
        numberRepository.save(numberObject1);
        NumberObject numberObject2 = new NumberObject();
        numberObject2.setNumber(number);
        assertThrows(DuplicateKeyException.class, () -> {
            numberRepository.save(numberObject2);
        });
    }

//    @Test
//    void testMongoDBContainer() {
//        try (MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0")) {
//            mongoDBContainer.start();
//            System.out.println("MongoDB Container started with URI: " + mongoDBContainer.getReplicaSetUrl());
//            assertTrue(mongoDBContainer.isRunning());
//        }
//    }
}
