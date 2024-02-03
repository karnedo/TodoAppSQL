-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jan 16, 2024 at 04:19 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `kraTodoApp`
--
CREATE DATABASE IF NOT EXISTS `kraTodoApp` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `kraTodoApp`;

-- --------------------------------------------------------

--
-- Table structure for table `kraTASKS`
--

CREATE TABLE `kraTASKS` (
  `idTask` int(11) NOT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `date` date NOT NULL,
  `priority` varchar(1) NOT NULL,
  `isChecked` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `kraUSERS`
--

CREATE TABLE `kraUSERS` (
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `picture` longblob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `kraTASKS`
--
ALTER TABLE `kraTASKS`
  ADD PRIMARY KEY (`idTask`),
  ADD KEY `FK_KRATASKS_KRAUSERS` (`email`);

--
-- Indexes for table `kraUSERS`
--
ALTER TABLE `kraUSERS`
  ADD PRIMARY KEY (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `kraTASKS`
--
ALTER TABLE `kraTASKS`
  MODIFY `idTask` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `kraTASKS`
--
ALTER TABLE `kraTASKS`
  ADD CONSTRAINT `FK_KRATASKS_KRAUSERS` FOREIGN KEY (`email`) REFERENCES `kraUSERS` (`email`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
