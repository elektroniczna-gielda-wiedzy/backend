INSERT INTO language(name) VALUES ('polish'), ('english');
INSERT INTO category(type, is_deleted, parent_id) VALUES (0, false, NULL),
                                                         (0, false, NULL),
                                                         (0, false, NULL);
INSERT INTO category(type, is_deleted, parent_id) VALUES (0, false, 1),
                                                         (0, false, 1),
                                                         (0, false, 1),
                                                         (0, false, 1),
                                                         (0, false, 2),
                                                         (0, false, 2),
                                                         (0, false, 2),
                                                         (0, false, 3),
                                                         (0, false, 3);
INSERT INTO category(type, is_deleted, parent_id) VALUES (1, false, NULL),
                                                         (1, false, NULL),
                                                         (1, false, NULL);
INSERT INTO category(type, is_deleted, parent_id) VALUES (1, false, 13),
                                                         (1, false, 13),
                                                         (1, false, 13),
                                                         (1, false, 13),
                                                         (1, false, 14),
                                                         (1, false, 14),
                                                         (1, false, 14),
                                                         (1, false, 15),
                                                         (1, false, 15);


INSERT INTO category_translation(translation, category_id, language_id) VALUES ('WIET', 1, 1),
                                                                               ('WIET', 1, 2),
                                                                               ('WEAIIB', 2, 1),
                                                                               ('WEAIIB', 2, 2),
                                                                               ('WH', 3, 1),
                                                                               ('WH', 3, 2),
                                                                               ('Cyberbezpieczeństwo', 4, 1),
                                                                               ('Cybersecurity', 4, 2),
                                                                               ('Elektronika', 5, 1),
                                                                               ('Electronics', 5, 2),
                                                                               ('Informatyka', 6, 1),
                                                                               ('Computer science', 6, 2),
                                                                               ('Teleinformatyka', 7, 1),
                                                                               ('Information and Communication Technology', 7, 2),
                                                                               ('Automatyka i Robotyka', 8, 1),
                                                                               ('Automatic Control and Robotics', 8, 2),
                                                                               ('Elektrotechnika', 9, 1),
                                                                               ('Electrical Engineering', 9, 2),
                                                                               ('Inżynieria Biomedyczna', 10, 1),
                                                                               ('Biomedical Engineering', 10, 2),
                                                                               ('Informatyka Społeczna', 11, 1),
                                                                               ('Community Informatics', 11, 2),
                                                                               ('Kulturoznawstwo', 12, 1),
                                                                               ('Cultural Studies', 12, 2),
                                                                               ('Informatyka', 13, 1),
                                                                               ('Computer Science', 13, 2),
                                                                               ('Matematyka', 14, 1),
                                                                               ('Mathematics', 14, 2),
                                                                               ('Fizyka', 15, 1),
                                                                               ('Physics', 15, 2),
                                                                               ('Algorytmy i Struktury Danych', 16, 1),
                                                                               ('Algorithmics and Data Structures', 16, 2),
                                                                               ('Bazy danych', 17, 1),
                                                                               ('Database systems', 17, 2),
                                                                               ('Programowanie obiektowe', 18, 1),
                                                                               ('Object Oriented Programming', 18, 2),
                                                                               ('Sieci komputerowe', 19, 1),
                                                                               ('Computer networks', 19, 2),
                                                                               ('Algebra', 20, 1),
                                                                               ('Algebra', 20, 2),
                                                                               ('Analiza matematyczna', 21, 1),
                                                                               ('Mathematical analysis', 21, 2),
                                                                               ('Matematyka dyskretna', 22, 1),
                                                                               ('Discrete mathematics', 22, 2),
                                                                               ('Fizyka Kwantowa', 23, 1),
                                                                               ('Quantum Physics', 23, 2),
                                                                               ('Termodynamika', 24, 1),
                                                                               ('Thermodynamics', 24, 2);
INSERT INTO entry_type(name) VALUES ('Notatka'),
                                    ('Ogłoszenie'),
                                    ('Post');

INSERT INTO users(first_name, last_name, email, password, is_email_auth, created_at, last_login, is_admin, is_active) VALUES
('Adam', 'Kowalski', 'adamkowalski@student.agh.edu.pl', 'password', true, current_timestamp, current_timestamp, false, true),
('Maria', 'Kowalska', 'mariakowalska@student.agh.edu.pl', 'password', true, current_timestamp, current_timestamp, false, true),
('Marek', 'Król', 'marekkrol@student.agh.edu.pl', 'password', true, current_timestamp, current_timestamp, false, true),
('Patryk', 'Kowal', 'patrykkowal@student.agh.edu.pl', 'password', true, current_timestamp, current_timestamp, false, true),
('Michał', 'Markowski', 'michalmarkowski@student.agh.edu.pl', 'password', true, current_timestamp, current_timestamp, false, true),
('Adrian', 'Król', 'adriankrol@student.agh.edu.pl', 'password', true, current_timestamp, current_timestamp, false, true),
('Patryk', 'Markowski', 'patrykmarkowski@student.agh.edu.pl', 'password', true, current_timestamp, current_timestamp, false, true);

INSERT INTO entry(user_id, entry_type_id, title, content, created_at, updated_at, is_deleted) VALUES
(1, 2, 'Tytuł ogłoszenia', 'Content ogłoszenia', current_timestamp - INTERVAL '10 minutes', current_timestamp, false),
(1, 1, 'Tytuł notatki z obrazem', 'Content notatki', current_timestamp, current_timestamp, false),
(1, 3, 'Tytuł postu z obrazem', 'Content postu', current_timestamp - INTERVAL '20 minutes', current_timestamp, false),
(2, 1, 'Tytuł notatki', 'Content notatki', current_timestamp - INTERVAL '30 minutes', current_timestamp, false),
(2, 2, 'Tytuł ogłoszenia', 'Content ogłoszenia', current_timestamp - INTERVAL '40 days', current_timestamp, false),
(2, 3, 'Tytuł postu', 'Content postu', current_timestamp - INTERVAL '50 days', current_timestamp, false),
(3, 1, 'Tytuł notatki', 'Content notatki', current_timestamp - INTERVAL '60 days', current_timestamp, false),
(3, 2, 'Tytuł ogłoszenia', 'Content ogłoszenia', current_timestamp - INTERVAL '70 days', current_timestamp, false),
(3, 3, 'Tytuł postu', 'Content postu', current_timestamp - INTERVAL '80 days', current_timestamp, false);

INSERT INTO entry_category(entry_id, category_id) VALUES (1, 1),
                                                         (1, 4),
                                                         (1, 14),
                                                         (1, 20),
                                                         (2, 3),
                                                         (2, 12),
                                                         (2, 15),
                                                         (3, 2),
                                                         (3, 10),
                                                         (3, 14),
                                                         (3, 22),
                                                            (4, 1),
                                                            (4, 4),
                                                            (4, 14),
                                                            (4, 20),
                                                            (5, 3),
                                                            (5, 12),
                                                            (5, 15),
                                                            (6, 2),
                                                            (6, 10),
                                                            (6, 14),
                                                            (6, 22),
                                                            (7, 1),
                                                            (7, 4),
                                                            (7, 14),
                                                            (7, 20),
                                                            (8, 3),
                                                            (8, 12),
                                                            (8, 15),
                                                            (9, 2),
                                                            (9, 10),
                                                            (9, 14),
                                                            (9, 22);

INSERT INTO image(image) VALUES
('http://localhost:8080/image1.jpg'),
('http://localhost:8080/image2.jpg');

INSERT INTO image_item(image_item_id, image_id) VALUES
(2, 1),
(3, 2);







