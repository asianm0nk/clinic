-- Добавление больниц
INSERT INTO hospitals (name) VALUES 
('Городская больница №1'),
('Центральная районная больница'),
('Детская клиника "Здоровье"');

-- Добавление сотрудников (логин/пароль: doctor1/123, nurse1/123 и т.д.)
INSERT INTO staff (name, role, hospital_id, username, password) VALUES
('Иванов Иван', 'doctor', 1, 'doctor1', '123'),
('Петрова Анна', 'nurse', 1, 'nurse1', '123'),
('Сидоров Алексей', 'doctor', 2, 'doctor2', '123'),
('Маркова Елена', 'nurse', 2, 'nurse2', '123'),
('Кузнецов Дмитрий', 'admin', 3, 'admin3', 'admin');

-- Добавление пациентов
INSERT INTO patients (name, birth_date, phone) VALUES
('Алексеев Михаил', '1990-05-12', '+996700000001'),
('Смирнова Ольга', '1985-09-28', '+996700000002'),
('Громов Артём', '2002-11-03', '+996700000003'),
('Васильева Ирина', '1977-07-17', '+996700000004');

-- Добавление приёмов
INSERT INTO appointments (patient_id, staff_id, date, reason) VALUES
(1, 1, NOW() - INTERVAL '2 days', 'Общий осмотр'),
(2, 1, NOW() - INTERVAL '1 day', 'Головная боль'),
(3, 3, NOW(), 'Температура и кашель'),
(4, 2, NOW() - INTERVAL '3 days', 'Прививка');

-- Логирование действий (тест)
INSERT INTO logs (user_id, action, table_name, details) VALUES
(5, 'insert', 'patients', 'Добавлен пациент: Алексеев Михаил'),
(5, 'insert', 'appointments', 'Назначен приём для пациента ID 1 к врачу ID 1'),
(3, 'update', 'patients', 'Изменён телефон пациента ID 3'),
(2, 'delete', 'appointments', 'Удалён приём ID 4');
