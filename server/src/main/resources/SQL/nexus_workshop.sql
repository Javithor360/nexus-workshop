-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost:3306
-- Tiempo de generación: 24-09-2024 a las 04:17:22
-- Versión del servidor: 8.0.30
-- Versión de PHP: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `nexus_workshop`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `activities`
--

CREATE TABLE `activities` (
                              `id` bigint NOT NULL,
                              `project_id` bigint NOT NULL,
                              `user_id` bigint NOT NULL,
                              `title` varchar(100) NOT NULL,
                              `description` varchar(255) DEFAULT NULL,
                              `percentage` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                              `type_id` bigint DEFAULT NULL,
                              `created_at` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `activity_types`
--

CREATE TABLE `activity_types` (
                                  `id` bigint NOT NULL,
                                  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `activity_types`
--

INSERT INTO `activity_types` (`id`, `name`) VALUES
                                                (1, 'Design'),
                                                (2, 'Prototyping'),
                                                (3, 'Manufacturing'),
                                                (4, 'Quality Control'),
                                                (5, 'Packaging'),
                                                (6, 'Shipping'),
                                                (7, 'Client Meeting'),
                                                (8, 'Maintenance');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clients`
--

CREATE TABLE `clients` (
                           `id` bigint NOT NULL,
                           `name` varchar(100) NOT NULL,
                           `email` varchar(100) NOT NULL,
                           `address` varchar(150) NOT NULL,
                           `phone` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `logs`
--

CREATE TABLE `logs` (
                        `id` bigint NOT NULL,
                        `project_id` bigint DEFAULT NULL,
                        `activity_id` bigint DEFAULT NULL,
                        `date` date NOT NULL,
                        `description` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `projects`
--

CREATE TABLE `projects` (
                            `id` bigint NOT NULL,
                            `client_id` bigint DEFAULT NULL,
                            `user_id` bigint DEFAULT NULL,
                            `title` varchar(100) NOT NULL,
                            `description` varchar(255) DEFAULT NULL,
                            `status_id` bigint NOT NULL,
                            `start_date` date NOT NULL,
                            `end_date` date DEFAULT NULL,
                            `due_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `project_statuses`
--

CREATE TABLE `project_statuses` (
                                    `id` bigint NOT NULL,
                                    `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `project_statuses`
--

INSERT INTO `project_statuses` (`id`, `name`) VALUES
                                                  (1, 'Pending'),
                                                  (2, 'In Progress'),
                                                  (3, 'On Hold'),
                                                  (4, 'Completed'),
                                                  (5, 'Cancelled'),
                                                  (6, 'Under Review');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
                         `id` bigint NOT NULL,
                         `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id`, `name`) VALUES
                                       (1, 'admin'),
                                       (2, 'employee');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
                         `id` bigint NOT NULL,
                         `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                         `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                         `role_id` bigint NOT NULL,
                         `dui` varchar(10) NOT NULL,
                         `email` varchar(50) NOT NULL,
                         `gender` tinytext NOT NULL,
                         `birthday` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `activities`
--
ALTER TABLE `activities`
    ADD PRIMARY KEY (`id`),
  ADD KEY `type_id` (`type_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indices de la tabla `activity_types`
--
ALTER TABLE `activity_types`
    ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `clients`
--
ALTER TABLE `clients`
    ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `logs`
--
ALTER TABLE `logs`
    ADD PRIMARY KEY (`id`),
  ADD KEY `project_id` (`project_id`),
  ADD KEY `activity_id` (`activity_id`);

--
-- Indices de la tabla `projects`
--
ALTER TABLE `projects`
    ADD PRIMARY KEY (`id`),
  ADD KEY `client_id` (`client_id`),
  ADD KEY `status_id` (`status_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indices de la tabla `project_statuses`
--
ALTER TABLE `project_statuses`
    ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
    ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
    ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `dui` (`dui`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `role_id` (`role_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `activities`
--
ALTER TABLE `activities`
    MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `activity_types`
--
ALTER TABLE `activity_types`
    MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `clients`
--
ALTER TABLE `clients`
    MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `logs`
--
ALTER TABLE `logs`
    MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `projects`
--
ALTER TABLE `projects`
    MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `project_statuses`
--
ALTER TABLE `project_statuses`
    MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
    MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
    MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `activities`
--
ALTER TABLE `activities`
    ADD CONSTRAINT `activities_ibfk_1` FOREIGN KEY (`type_id`) REFERENCES `activity_types` (`id`),
  ADD CONSTRAINT `activities_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Filtros para la tabla `logs`
--
ALTER TABLE `logs`
    ADD CONSTRAINT `logs_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `projects` (`id`),
  ADD CONSTRAINT `logs_ibfk_2` FOREIGN KEY (`activity_id`) REFERENCES `activities` (`id`);

--
-- Filtros para la tabla `projects`
--
ALTER TABLE `projects`
    ADD CONSTRAINT `projects_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`),
  ADD CONSTRAINT `projects_ibfk_2` FOREIGN KEY (`status_id`) REFERENCES `project_statuses` (`id`),
  ADD CONSTRAINT `projects_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Filtros para la tabla `users`
--
ALTER TABLE `users`
    ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;