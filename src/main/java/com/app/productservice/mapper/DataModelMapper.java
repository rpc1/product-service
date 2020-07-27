package com.app.productservice.mapper;

public interface DataModelMapper {
    <D> D map(Object source, Class<D> destinationType);
}

