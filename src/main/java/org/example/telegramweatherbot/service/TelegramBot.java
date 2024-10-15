package org.example.telegramweatherbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.telegramweatherbot.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final WeatherService weatherService;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            if (messageText.equals("/start")) {
                startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
            } else {
                try {
                    sendMessage(chatId, weatherService.getWeather(messageText));
                } catch (Exception e) {
                    sendMessage(chatId, "City not found");
                }
            }
        }
    }

    private void startCommandReceived(Long chatId, String firstName) {
        String answer = "Hi, " + firstName + ", in which country do you want to know the weather?";
        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String testTotSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(testTotSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Execute error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }
}
