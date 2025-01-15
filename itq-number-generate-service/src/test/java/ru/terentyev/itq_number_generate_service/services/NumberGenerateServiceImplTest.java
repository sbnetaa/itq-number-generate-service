package ru.terentyev.itq_number_generate_service.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import ru.terentyev.itq_number_generate_service.entities.NumberObject;
import ru.terentyev.itq_number_generate_service.repositories.NumberRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NumberGenerateServiceImplTest {

    @Mock
    private NumberRepository numberRepository;

    @InjectMocks
    private NumberGenerateServiceImpl numberGenerateService;

    @Test
    void startGeneration_ShouldSaveSuccessfullyWhenNumberIsUnique() {
        NumberObject mockNumberObject = new NumberObject();
        when(numberRepository.save(any(NumberObject.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        NumberObject result = numberGenerateService.startGeneration();
        assertNotNull(result.getNumber());
        verify(numberRepository, times(1)).save(any(NumberObject.class));
    }

    @Test
    void startGeneration_ShouldRetryIfNumberIsNotUniqueUntilSaved() {
        NumberObject mockNumberObject = new NumberObject();
        String datePart = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());
        when(numberRepository.save(any(NumberObject.class)))
                .thenThrow(new DuplicateKeyException("Duplicate key error"))
                .thenThrow(new DuplicateKeyException("Duplicate key error"))
                .thenThrow(new DuplicateKeyException("Duplicate key error"))
                .thenAnswer(invocation -> {
                    NumberObject arg = invocation.getArgument(0);
                    arg.setNumber("12345" + datePart);
                    return arg;
                });
        NumberObject result = numberGenerateService.startGeneration();
        assertNotNull(result.getNumber());
        verify(numberRepository, times(4)).save(any(NumberObject.class));
    }

    @Test
    void startGeneration_ShouldFailAfter10AttemptsIfNumberStillNotUnique() {
        when(numberRepository.save(any(NumberObject.class)))
                .thenThrow(new DuplicateKeyException("Duplicate key error"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            numberGenerateService.startGeneration();
        });
        assertTrue(exception.getMessage().contains("Не удалось сгенерировать уникальный номер"));
        verify(numberRepository, times(10)).save(any(NumberObject.class));
    }
}
