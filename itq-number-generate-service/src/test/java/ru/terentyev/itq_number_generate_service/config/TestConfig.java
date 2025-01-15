package ru.terentyev.itq_number_generate_service.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

/*
 * За примерно неделю так и не удалось заставить тесты работать в контейнере Docker.
 * Пробовал использовать библиотеки Flapdoodle и TestContainers. Первая неверно определяет
 * операционную систему в контейнере, а указать ее явно не дает, и из-за этого ошибка.
 * Вторая просто не видит файлы. На всякий случай оставляю этот класс: вдруг у вас получится? :)
 * Самому интересно что мне нужно было сделать :)
 */

@TestConfiguration
public class TestConfig {

    @Bean
    @ConditionalOnProperty(name = "testcontainers.enabled", havingValue = "true")
    public MongoDBContainer mongoDBContainer() {
        MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0")
                .withEnv("TESTCONTAINERS_URI", "")
                .withEnv("TESTCONTAINERS_ENABLED", "true")
                .withEnv("TESTCONTAINERS_HOST_OVERRIDE", "host.docker.internal")
        ;
        mongoDBContainer.withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("postgres")));
        mongoDBContainer.start();
        System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getReplicaSetUrl());
        return mongoDBContainer;
    }

    @PostConstruct
    public void configureDockerInsideDocker() {
        System.out.println("configureDockerInsideDocker вызван");
        System.setProperty("testcontainers.host.override", "host.docker.internal"); // DockerClientConfigUtils.getDockerHostIpAddress(), DockerHost.instance().getAddress()
        System.setProperty("TESTCONTAINERS_HOST_OVERRIDE", "host.docker.internal");
    }
}
