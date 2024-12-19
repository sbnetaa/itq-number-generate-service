package ru.terentyev.itq_number_generate_service.entities;

import org.springframework.data.annotation.Id;

public abstract class AbstractEntity {
    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
