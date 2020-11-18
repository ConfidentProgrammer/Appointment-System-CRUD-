DROP TABLE IF EXISTS appointment;
CREATE TABLE appointment(
id LONG PRIMARY KEY AUTO_INCREMENT,
firstName VARCHAR(255),
email VARCHAR(255),
appointmentDate DATE,
appointmentTime TIME
);
INSERT INTO appointment(firstName, email, appointmentDate, appointmentTime) VALUES
('Frank', 'frank@frank.com', '2020-01-01', '12:00:00'),
('Hao', 'hao@gmail.com', '2020-02-02', '08:15:00'),
('Sue', 'sue@yahoo.ca', '2020-03-03', '14:30:00'),
('Jaspreet', 'jaspreet@outlook.com', '2020-04-04', '10:15:00');