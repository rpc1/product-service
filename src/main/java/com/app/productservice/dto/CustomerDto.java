package com.app.productservice.dto;

import com.app.productservice.model.AbstractDataObject;

import java.util.UUID;

public class CustomerDto extends AbstractDataObject {
        public CustomerDto() {

        }

        public CustomerDto(UUID id, String title) {
            super(id, title);
        }
}
