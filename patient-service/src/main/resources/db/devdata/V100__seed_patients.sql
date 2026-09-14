INSERT INTO patients (id, name, email) VALUES
    (1, 'Maria Silva', 'maria.silva@example.com'),
    (2, 'Joao Souza', 'joao.souza@example.com')
ON CONFLICT (id) DO NOTHING;
