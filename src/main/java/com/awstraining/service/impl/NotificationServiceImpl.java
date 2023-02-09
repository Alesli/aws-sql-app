package com.awstraining.service.impl;

import com.amazonaws.services.sqs.model.Message;
import com.awstraining.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void subscribeEmail(String email) {

    }

    @Override
    public void unsubscribeEmail(String email) {

    }

    @Override
    public void sendMessageToQueue(String message) {

    }

    @Override
    public void sendMessageToTopic(String message) {

    }

    @Override
    public List<Message> readMessages() {
        return null;
    }
}
