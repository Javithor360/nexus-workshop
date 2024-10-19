USE nexus_workshop;

-- Iniciar transacción
START TRANSACTION;

-- Insertar datos de prueba para la tabla `clients`
INSERT INTO `clients` (`id`, `name`, `email`, `address`, `phone`) VALUES
                                                                      (1, 'Doe Charles John Dickson', 'doe@example.com', '123 Elm St, Springfield', '555-1234'),
                                                                      (2, 'Smith Aero Jane Flowers', 'smith@example.com', '456 Oak St, Springfield', '555-5678'),
                                                                      (3, 'Johnson Karl Alice Williams', 'johnson@example.com', '789 Pine St, Springfield', '555-8765');

-- Insertar datos de prueba para la tabla `users`
INSERT INTO `users` (`id`, `username`, `password`, `role_id`, `dui`, `email`, `gender`, `birthday`) VALUES
                                                                                                        (1, 'admin', '$2y$10$aPQyBQX9cl4pKELxGdWPROF16PnF.HggKfyENsD/SZ/b8sPTDs4A2', 1, '00000000-0', 'a@dm.in', 'M', '2004-01-01'),
                                                                                                        (2, 'aero', '$2y$10$aPQyBQX9cl4pKELxGdWPROF16PnF.HggKfyENsD/SZ/b8sPTDs4A2', 2, '00000000-1', 'aero@example.com', 'M', '1980-01-01'),
                                                                                                        (3, 'martin', '$2y$10$aPQyBQX9cl4pKELxGdWPROF16PnF.HggKfyENsD/SZ/b8sPTDs4A2', 3, '00000000-2', 'martin@example.com', 'F', '1990-02-02'),
                                                                                                        (4, 'marcelo', '$2y$10$aPQyBQX9cl4pKELxGdWPROF16PnF.HggKfyENsD/SZ/b8sPTDs4A2', 3, '00000000-3', 'marcelo@example.com', 'F', '1985-03-03');

-- Insertar datos de prueba para la tabla `projects`
INSERT INTO `projects` (`id`, `client_id`, `user_id`, `title`, `description`, `status_id`, `start_date`, `end_date`, `due_date`) VALUES
                                                                                                                                     (1, 1, 3, 'Project Alpha', 'Description of Project Alpha', 2, '2024-01-01', '2024-06-01', '2024-05-15'),
                                                                                                                                     (2, 2, 3, 'Project Beta', 'Description of Project Beta', 2, '2024-02-01', '2024-07-01', '2024-06-15'),
                                                                                                                                     (3, 3, 4, 'Project Gamma', 'Description of Project Gamma', 2, '2024-03-01', '2024-08-01', '2024-07-15');

-- Insertar datos de prueba para la tabla `activities`
INSERT INTO `activities` (`id`, `user_id`, `title`, `description`, `percentage`, `type_id`, `created_at`) VALUES
                                                                                                              (1, 3, 'Initial Design', 'Initial design phase for Project Alpha', 50, 1, '2024-01-10'),
                                                                                                              (2, 3, 'Prototype Creation', 'Creating prototype for Project Alpha', 30, 2, '2024-02-15'),
                                                                                                              (3, 3, 'Market Research', 'Conducting market research for Project Beta', 70, 1, '2024-03-20'),
                                                                                                              (4, 4, 'Quality Assurance', 'Quality checks for Project Gamma', 20, 4, '2024-04-25');

-- Insertar datos de prueba para la tabla `logs`
INSERT INTO `logs` (`id`, `project_id`, `activity_id`) VALUES
                                                           (1, 1, 1),
                                                           (2, 1, 2),
                                                           (3, 2, 3),
                                                           (4, 3, 4);

-- Confirmar la transacción
COMMIT;