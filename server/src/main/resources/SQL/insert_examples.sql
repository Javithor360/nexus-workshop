USE nexus_workshop;

-- Iniciar transacción
START TRANSACTION;

-- Insertar datos de prueba para la tabla `clients`
INSERT INTO `clients` (`id`, `name`, `email`, `address`, `phone`) VALUES
                                                                      (1, 'John Doe', 'john@example.com', '123 Elm St, Springfield', '555-1234'),
                                                                      (2, 'Jane Smith', 'jane@example.com', '456 Oak St, Springfield', '555-5678'),
                                                                      (3, 'Alice Johnson', 'alice@example.com', '789 Pine St, Springfield', '555-8765');

-- Insertar datos de prueba para la tabla `users`
INSERT INTO `users` (`id`, `username`, `password`, `role_id`, `dui`, `email`, `gender`, `birthday`) VALUES
                                                                                                        (1, '$2y$06$mOc48bVddtjQCqwAOWRWGevEEkBZYtDy4DfdovU1KV0lT3G0I8ufG', 'hashed_password1', 1, 'DUI123456', 'john@example.com', 'M', '1980-01-01'),
                                                                                                        (2, 'janesmith', '$2y$06$MxEBkz6JIdSZXYMVEBFAj.hoBnWhb8uqHM9k.CltNwz.BCy6wFumW', 2, 'DUI654321', 'jane@example.com', 'F', '1990-02-02'),
                                                                                                        (3, 'alicej', '$2y$06$EbfilW7mUWxCVZSLHyeP/.urCcM37Lm2ph4kps/JCDE.yf3dcfcWm', 2, 'DUI789012', 'alice@example.com', 'F', '1985-03-03');

-- Insertar datos de prueba para la tabla `projects`
INSERT INTO `projects` (`id`, `client_id`, `user_id`, `title`, `description`, `status_id`, `start_date`, `end_date`, `due_date`) VALUES
                                                                                                                                     (1, 1, 1, 'Project Alpha', 'Description of Project Alpha', 1, '2024-01-01', '2024-06-01', '2024-05-15'),
                                                                                                                                     (2, 2, 2, 'Project Beta', 'Description of Project Beta', 2, '2024-02-01', '2024-07-01', '2024-06-15'),
                                                                                                                                     (3, 3, 3, 'Project Gamma', 'Description of Project Gamma', 3, '2024-03-01', '2024-08-01', '2024-07-15');

-- Insertar datos de prueba para la tabla `activities`
INSERT INTO `activities` (`id`, `user_id`, `title`, `description`, `percentage`, `type_id`, `created_at`) VALUES
                                                                                                                            (1, 1, 'Initial Design', 'Initial design phase for Project Alpha', '50%', 1, '2024-01-10'),
                                                                                                                            (2, 2, 'Prototype Creation', 'Creating prototype for Project Alpha', '30%', 2, '2024-02-15'),
                                                                                                                            (3, 1, 'Market Research', 'Conducting market research for Project Beta', '70%', 1, '2024-03-20'),
                                                                                                                            (4, 3, 'Quality Assurance', 'Quality checks for Project Gamma', '20%', 4, '2024-04-25');

-- Insertar datos de prueba para la tabla `logs`
INSERT INTO `logs` (`id`, `project_id`, `activity_id`, `date`, `description`) VALUES
                                                                                  (1, 1, 1, '2024-01-15', 'Design phase started.'),
                                                                                  (2, 1, 2, '2024-02-20', 'Prototype completed.'),
                                                                                  (3, 2, 3, '2024-03-25', 'Market research results analyzed.'),
                                                                                  (4, 3, 4, '2024-04-30', 'Quality assurance completed.');

-- Confirmar la transacción
COMMIT;
