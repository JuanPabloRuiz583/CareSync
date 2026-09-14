package agendamento_service.domain;

public class ConsultaNotFoundException extends RuntimeException {
    public ConsultaNotFoundException(Long id) { super("Consulta not found: id=" + id); }
}
