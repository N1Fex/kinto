package ru.n1fex.kinto.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {

        RabbitTemplate template =
                new RabbitTemplate(connectionFactory);

        template.setMessageConverter(messageConverter());

        return template;
    }

    @Bean
    public TopicExchange verificationExchange() {
        return new TopicExchange("verification.exchange");
    }

    @Bean
    public Queue mailVerificationQueue() {
        return new Queue("mail.verification.queue");
    }

    @Bean
    public Binding mailBinding(
            Queue mailVerificationQueue,
            TopicExchange verificationExchange
    ) {

        return BindingBuilder
                .bind(mailVerificationQueue)
                .to(verificationExchange)
                .with("verification.email");
    }
}
