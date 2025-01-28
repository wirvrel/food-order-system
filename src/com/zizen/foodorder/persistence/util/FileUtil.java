package com.zizen.foodorder.persistence.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zizen.foodorder.persistence.exception.JsonFileIOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class FileUtil {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static <T> void saveToJson(String filePath, List<T> entities)
        throws JsonFileIOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            String jsonData = gson.toJson(entities);
            writer.write(jsonData);
        } catch (IOException e) {
            throw new JsonFileIOException("Не вдалося зберегти дані в JSON файл: " + filePath, e);
        }
    }

    public static <T> List<T> loadFromJson(String filePath, Type typeOfT)
        throws JsonFileIOException {
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, typeOfT);
        } catch (IOException e) {
            throw new JsonFileIOException("Не вдалося завантажити дані з JSON файлу: " + filePath,
                e);
        }
    }
}
