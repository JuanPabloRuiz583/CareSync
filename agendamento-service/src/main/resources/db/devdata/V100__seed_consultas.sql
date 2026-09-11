INSERT INTO consultas (id, patient_id, doctor_name, date_time, status, reason) VALUES
    (1, 1, 'Dr. Carlos Lima', CURRENT_TIMESTAMP - INTERVAL '10 days', 'COMPLETED', 'Consulta de rotina'),
    (2, 1, 'Dra. Ana Souza', CURRENT_TIMESTAMP + INTERVAL '5 days', 'SCHEDULED', 'Retorno'),
    (3, 2, 'Dr. Carlos Lima', CURRENT_TIMESTAMP + INTERVAL '2 days', 'SCHEDULED', 'Primeira consulta')
ON CONFLICT (id) DO NOTHING;
SELECT setval(pg_get_serial_sequence('consultas','id'), GREATEST((SELECT MAX(id) FROM consultas), 1));
