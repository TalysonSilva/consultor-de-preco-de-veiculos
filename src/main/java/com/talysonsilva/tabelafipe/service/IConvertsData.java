package com.talysonsilva.tabelafipe.service;

import java.util.List;

public interface IConvertsData {
    <T> T getJsonData(String json, Class<T> tClass);
    <T> List<T> getJsonList(String json, Class<T> tClass);
}
