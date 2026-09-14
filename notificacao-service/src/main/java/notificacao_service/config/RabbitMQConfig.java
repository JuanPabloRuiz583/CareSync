package notificacao_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "consulta.exchange";
    public static final String QUEUE = "notificacao.queue";
    public static final String DEAD_LETTER_EXCHANGE = "notificacao.dlx";
    public static final String DEAD_LETTER_QUEUE = "notificacao.dlq";

    @Bean
    public TopicExchange consultaExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue notificacaoQueue() {
        return org.springframework.amqp.core.QueueBuilder.durable(QUEUE)
                .deadLetterExchange(DEAD_LETTER_EXCHANGE).deadLetterRoutingKey(DEAD_LETTER_QUEUE).build();
    }

    @Bean
    public Binding binding(Queue notificacaoQueue, TopicExchange consultaExchange) {
        return BindingBuilder.bind(notificacaoQueue).to(consultaExchange).with("consulta.*");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean public DirectExchange deadLetterExchange() { return new DirectExchange(DEAD_LETTER_EXCHANGE); }
    @Bean public Queue deadLetterQueue() { return new Queue(DEAD_LETTER_QUEUE, true); }
    @Bean public Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with(DEAD_LETTER_QUEUE);
    }
}
