package graphql_api.application.port.out;

import graphql_api.dto.Patient;

public interface PatientGateway { Patient findById(Long id); }
