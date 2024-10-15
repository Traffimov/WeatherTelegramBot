package org.example.telegramweatherbot.service;

import lombok.AllArgsConstructor;
import org.example.telegramweatherbot.config.WeatherConfig;
import org.example.telegramweatherbot.model.WeatherResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class WeatherService {

    private final WeatherConfig weatherConfig;
    private final RestTemplate restTemplate;

    public String getWeather(String cityName) {
        String apiUrl = String.format("%s?q=%s&appid=%s&units=metric", weatherConfig.getUrl(), cityName, weatherConfig.getApiKey());
        WeatherResponse response = restTemplate.getForObject(apiUrl, WeatherResponse.class);
        if (response == null || response.getMain() == null) {
            return "Ошибка получения данных о погоде.";
        }
        return String.format("Температура: %s°C, \n" +
                             "Облачность: %s°C, \n" +
                             "Скорость ветра: %s°C, \n" +
                             "Влажность: %s%%, \n" +
                             "Описание: %s",
                response.getMain().getTemp(),
                response.getClouds().getAll(),
                response.getWind().getSpeed(),
                response.getMain().getHumidity(),
                response.getWeather().get(0).getDescription());
    }
}
