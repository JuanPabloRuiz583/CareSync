package agendamento_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "consulta.exchange";
    public static final String QUEUE = "notificacao.queue";
    public static final String ROUTING_KEY_CREATED = "consulta.created";
    public static final String ROUTING_KEY_UPDATED = "consulta.updated";

    @Bean
    public TopicExchange consultaExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue notificacaoQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Binding binding(Queue notificacaoQueue, TopicExchange consultaExchange) {
        return BindingBuilder.bind(notificacaoQueue).to(consultaExchange).with("consulta.*");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
