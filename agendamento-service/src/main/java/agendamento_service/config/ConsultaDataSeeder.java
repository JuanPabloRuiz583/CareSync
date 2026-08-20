package agendamento_service.config;

import agendamento_service.domain.Consulta;
import agendamento_service.repository.ConsultaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ConsultaDataSeeder implements CommandLineRunner {

    private final ConsultaRepository consultaRepository;

    public ConsultaDataSeeder(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    @Override
    public void run(String... args) {
        consultaRepository.save(new Consulta(1L, "Dr. Carlos Lima", LocalDateTime.now().minusDays(10), "Consulta de rotina"));
        consultaRepository.save(new Consulta(1L, "Dra. Ana Souza", LocalDateTime.now().plusDays(5), "Retorno"));
        consultaRepository.save(new Consulta(2L, "Dr. Carlos Lima", LocalDateTime.now().plusDays(2), "Primeira consulta"));
    }
}
