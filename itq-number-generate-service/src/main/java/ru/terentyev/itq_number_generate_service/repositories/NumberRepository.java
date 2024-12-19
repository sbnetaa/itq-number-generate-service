package ru.terentyev.itq_number_generate_service.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.terentyev.itq_number_generate_service.entities.NumberObject;

@Repository
public interface NumberRepository extends MongoRepository<NumberObject, String> {
}
