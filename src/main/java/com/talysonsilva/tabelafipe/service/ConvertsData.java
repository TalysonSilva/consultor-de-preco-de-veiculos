package com.talysonsilva.tabelafipe.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.List;

public class ConvertsData implements IConvertsData {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T getJsonData(String json, Class<T> tClass) {

        try {
            return mapper.readValue(json, tClass);
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> List<T> getJsonList(String json, Class<T> tClass) {

        CollectionType list = mapper.getTypeFactory()
                .constructCollectionType(List.class, tClass);
        try {
            return mapper.readValue(json,list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
