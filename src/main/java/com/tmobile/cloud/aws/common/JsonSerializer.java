package com.tmobile.cloud.aws.common;

import com.google.gson.Gson;

public class JsonSerializer<T> {
    private final Class<T> clazz;

    public JsonSerializer(Class<T> clazz){
        this.clazz = clazz;
    }

    public String toJson(T entity){
        Gson gson = new Gson();
        return gson.toJson(entity, entity.getClass());
    }
    public T fromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, this.clazz);
    }
}
