package com.example.trelloproject.notification;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    @Value(value = "${slack.bot-token}")
    private String token;

    @Value(value = "${slack.channel.monitor}")
    private String channel;

    @Override
    public void sendMessageToSlack(String message) {
        MethodsClient methods = Slack.getInstance().methods(token);
        ChatPostMessageRequest request = ChatPostMessageRequest.builder().channel(channel).text(message).build();

        try {
            methods.chatPostMessage(request);
        } catch (Exception e) {
            throw new RuntimeException("슬랙에 메시지를 보내지 못했습니다.", e);
        }
    }

}
