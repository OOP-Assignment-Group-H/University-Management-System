-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 17, 2026 at 03:53 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `university_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `courses_enrolled`
--

CREATE TABLE `courses_enrolled` (
  `id` int(11) NOT NULL,
  `student_id` varchar(20) NOT NULL,
  `course_code` varchar(20) NOT NULL,
  `course_name` varchar(150) NOT NULL,
  `grade` varchar(10) DEFAULT NULL,
  `gpa` decimal(3,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `courses_enrolled`
--

INSERT INTO `courses_enrolled` (`id`, `student_id`, `course_code`, `course_name`, `grade`, `gpa`) VALUES
(1, 'CT/2022/077', 'CTEC22061', 'Systems and Network Laboratory', 'A-', 3.70),
(2, 'CT/2022/077', 'GTEC22033', 'Mathematics for Technology - IV', 'B+', 3.30),
(3, 'CT/2022/077', 'CTEC22043', 'Object Oriented Programming', 'A', 4.00),
(4, 'CT/2022/077', 'CTEC22032', 'Software Engineering', 'A-', 3.70),
(5, 'CT/2022/077', 'CTEC22053', 'Computer Architecture & Operating Systems', 'B', 3.00),
(6, 'CT/2022/077', 'GTEC23032', 'Projects in Technology - II', 'In Progres', NULL),
(7, 'CT/2022/014', 'CTEC22061', 'Systems and Network Laboratory', 'B', 3.00),
(8, 'CT/2022/014', 'GTEC22033', 'Mathematics for Technology - IV', 'B', 3.00),
(9, 'CT/2022/014', 'CTEC22043', 'Object Oriented Programming', 'A-', 3.70),
(10, 'CT/2022/014', 'CTEC22032', 'Software Engineering', 'A', 4.00),
(11, 'CT/2022/014', 'CTEC22053', 'Computer Architecture & Operating Systems', 'B+', 3.30),
(12, 'CT/2022/014', 'GTEC23032', 'Projects in Technology - II', 'In Progres', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `student_id` varchar(20) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `degree_program` varchar(150) DEFAULT NULL,
  `status` varchar(20) DEFAULT 'Active',
  `batch` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`student_id`, `full_name`, `email`, `dob`, `degree_program`, `status`, `batch`) VALUES
('CT/2022/014', 'Shashika Sandaruwan', 'shashika04@gmail.com', '2004-01-23', 'Bachelor (Hons) of Information Communication and Technology', 'Active', 'CT/2022'),
('CT/2022/077', 'Pandithasundara P.S.D.V.Y.Y.T', 'Pandith-ct22077@stu.kn.ac.lk', '2004-01-20', 'Bachelor (Hons) of Information Communication and Technology', 'Active', 'CT/2022'),
('CT/2023/001', 'Kavindu Kalhara', 'kavindukalhara@gmail.com', '2003-10-20', 'Bacholor of ICT', 'Active', 'CT/2023');

-- --------------------------------------------------------

--
-- Table structure for table `timetable`
--

CREATE TABLE `timetable` (
  `id` int(11) NOT NULL,
  `batch` varchar(20) DEFAULT NULL,
  `time_slot` varchar(20) DEFAULT NULL,
  `day_of_week` varchar(10) DEFAULT NULL,
  `course_code` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `timetable`
--

INSERT INTO `timetable` (`id`, `batch`, `time_slot`, `day_of_week`, `course_code`) VALUES
(1, 'CT/2022', '08:00 - 08.55', 'Wed', 'CTEC 21063'),
(2, 'CT/2022', '08:00 - 08.55', 'Thu', 'GTEC 21023'),
(3, 'CT/2022', '08:00 - 08.55', 'Fri', 'GTEC 21043'),
(4, 'CT/2022', '09:00 - 09.55', 'Tue', 'GTEC 21043'),
(5, 'CT/2022', '09:00 - 09.55', 'Wed', 'CTEC 21063'),
(6, 'CT/2022', '09:00 - 09.55', 'Thu', 'GTEC 21023'),
(7, 'CT/2022', '09:00 - 09.55', 'Fri', 'GTEC 21043'),
(8, 'CT/2022', '10:00 - 10.55', 'Tue', 'GTEC 21043'),
(9, 'CT/2022', '10:00 - 10.55', 'Thu', 'CTEC 21042'),
(10, 'CT/2022', '10:00 - 10.55', 'Fri', 'CTEC 21052'),
(11, 'CT/2022', '11:00 - 11.55', 'Tue', 'GTEC 21043'),
(12, 'CT/2022', '11:00 - 11.55', 'Thu', 'CTEC 21042'),
(13, 'CT/2022', '11:00 - 11.55', 'Fri', 'CTEC 21052'),
(14, 'CT/2022', '13:00 - 13.55', 'Wed', 'CTEC 21063'),
(15, 'CT/2022', '14:00 - 14.55', 'Tue', 'DELT 21512'),
(16, 'CT/2022', '14:00 - 14.55', 'Wed', 'CTEC 21063'),
(17, 'CT/2022', '15:00 - 15.55', 'Tue', 'DELT 21512'),
(18, 'CT/2022', '15:00 - 15.55', 'Wed', 'GTEC 23032'),
(19, 'CT/2022', '16:00 - 16.55', 'Wed', 'GTEC 23032'),
(20, 'CT/2023', '08:00 - 08.55', 'Mon', 'GTEC 23032'),
(21, 'CT/2023', '10:00 - 10.55', 'Mon', 'CTEC 22023'),
(22, 'CT/2023', '14:00 - 14.55', 'Mon', 'CTEC 22061'),
(23, 'CT/2023', '10:00 - 10.55', 'Tue', 'CTEC 22043'),
(24, 'CT/2023', '14:00 - 14.55', 'Tue', 'DELT 22552'),
(25, 'CT/2023', '08:00 - 08.55', 'Wed', 'CTEC 22053'),
(26, 'CT/2023', '10:00 - 10.55', 'Wed', 'CTEC 22053'),
(27, 'CT/2023', '14:00 - 14.55', 'Wed', 'CTEC 22023'),
(28, 'CT/2023', '10:00 - 10.55', 'Thu', 'CTEC 22032'),
(29, 'CT/2023', '13:00 - 13.55', 'Thu', 'CTEC 22043'),
(30, 'CT/2023', '08:00 - 08.55', 'Fri', 'GTEC 22033');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(20) NOT NULL,
  `student_id` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `full_name`, `username`, `email`, `password`, `role`, `student_id`) VALUES
(1, 'shashika sandaruwan', 'ssk23', 'shashika04@gmail.com', '123456', 'Student', 'CT/2022/014'),
(2, 'Vihanga Yasiru', 'vihanga@2004', 'vihanga2004@gmail.com', '654321', 'Student', 'CT/2022/077'),
(3, 'kavindu Kalhara', 'kk23', 'kavindu@gmail.com', 'kk123456', 'Student', 'CT/2023/001');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `courses_enrolled`
--
ALTER TABLE `courses_enrolled`
  ADD PRIMARY KEY (`id`),
  ADD KEY `student_id` (`student_id`);

--
-- Indexes for table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`student_id`);

--
-- Indexes for table `timetable`
--
ALTER TABLE `timetable`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `student_id` (`student_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `courses_enrolled`
--
ALTER TABLE `courses_enrolled`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `timetable`
--
ALTER TABLE `timetable`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `courses_enrolled`
--
ALTER TABLE `courses_enrolled`
  ADD CONSTRAINT `courses_enrolled_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
