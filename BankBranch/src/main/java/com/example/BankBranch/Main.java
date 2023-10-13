package com.example.BankBranch;

import com.example.BankBranch.model.SalePoint;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {



        String filePath = "BankBranch/src/main/resources/offices.json";

        try {


            String jsonString = readFileAsString(filePath);

            ObjectMapper objectMapper = new ObjectMapper();
            SalePoint[] salePoints = objectMapper.readValue(jsonString, SalePoint[].class);

            System.out.println("salePoints.length " + salePoints.length);
            System.out.println(salePoints[0]);

            // Теперь у вас есть массив объектов SalePoint, содержащий данные из JSON
//            for (SalePoint salePoint : salePoints) {
//                System.out.println("Sale Point Name: " + salePoint.getSalePointName());
//                System.out.println("Address: " + salePoint.getAddress());
//                System.out.println("Status: " + salePoint.getStatus());
//                // ... и так далее
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readFileAsString(String filePath) throws IOException {
        // Используем Paths.get для создания объекта Path из строки с путем к файлу
        Path path = Paths.get(filePath).toAbsolutePath().normalize();



        // Используем метод Files.readString для чтения содержимого файла в виде строки
        // В данном случае мы указываем кодировку UTF-8
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
