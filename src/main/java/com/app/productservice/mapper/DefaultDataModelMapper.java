package com.app.productservice.mapper;

import org.modelmapper.ModelMapper;

public class DefaultDataModelMapper implements DataModelMapper {

    private final ModelMapper modelMapper;

    public DefaultDataModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public <D> D map(Object source, Class<D> destinationType) {
        return modelMapper.map(source, destinationType);
    }
}
