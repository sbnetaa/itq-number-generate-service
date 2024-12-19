package ru.terentyev.itq_number_generate_service.entities;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "number_objects")
public class NumberObject extends AbstractEntity {

        @Indexed(unique = true)
        private String number;


        public String getNumber() {
                return number;
        }

        public void setNumber(String number) {
                this.number = number;
        }
}
