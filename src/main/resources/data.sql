

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
                                                                                                                          ('Adam', 'Kowalski', 'adamkowalski@student.agh.edu.pl', '$2a$10$mraQzwvvxI0GK.u.IvAwV.esajUJb5d2l.qpfNWIAx.kwu6Yy44QK', true, current_timestamp, current_timestamp, false, true),
                                                                                                                          ('Maria', 'Kowalska', 'mariakowalska@student.agh.edu.pl', '$2a$10$mraQzwvvxI0GK.u.IvAwV.esajUJb5d2l.qpfNWIAx.kwu6Yy44QK', true, current_timestamp, current_timestamp, false, true),
                                                                                                                          ('Marek', 'Król', 'marekkrol@student.agh.edu.pl', '$2a$10$mraQzwvvxI0GK.u.IvAwV.esajUJb5d2l.qpfNWIAx.kwu6Yy44QK', true, current_timestamp, current_timestamp, false, true),
                                                                                                                          ('Patryk', 'Kowal', 'patrykkowal@student.agh.edu.pl', '$2a$10$mraQzwvvxI0GK.u.IvAwV.esajUJb5d2l.qpfNWIAx.kwu6Yy44QK', true, current_timestamp, current_timestamp, false, true),
                                                                                                                          ('Michał', 'Markowski', 'michalmarkowski@student.agh.edu.pl', '$2a$10$mraQzwvvxI0GK.u.IvAwV.esajUJb5d2l.qpfNWIAx.kwu6Yy44QK', true, current_timestamp, current_timestamp, false, true),
                                                                                                                          ('Adrian', 'Król', 'adriankrol@student.agh.edu.pl', '$2a$10$mraQzwvvxI0GK.u.IvAwV.esajUJb5d2l.qpfNWIAx.kwu6Yy44QK', true, current_timestamp, current_timestamp, false, true),
                                                                                                                          ('Patryk', 'Markowski', 'patrykmarkowski@student.agh.edu.pl', '$2a$10$mraQzwvvxI0GK.u.IvAwV.esajUJb5d2l.qpfNWIAx.kwu6Yy44QK', true, current_timestamp, current_timestamp, false, true),
                                                                                                                          ('Admin', 'Adminowski', 'admin@student.agh.edu.pl', '$2a$10$mraQzwvvxI0GK.u.IvAwV.esajUJb5d2l.qpfNWIAx.kwu6Yy44QK', true, current_timestamp, current_timestamp, true, true);

ALTER TABLE entry ALTER COLUMN content TYPE VARCHAR(1000);
INSERT INTO entry(entry_id, user_id, entry_type_id, title, content, created_at, updated_at, is_deleted) VALUES
                                                                                                            (1, 4, 1, 'Zrozumienie zasad termodynamiki', 'Notatka ta ma na celu zrozumienie podstawowych zasad termodynamiki, kluczowej dziedziny fizyki, która jest niezbędna dla zrozumienia szeregu zjawisk naturalnych i technologicznych. W notatce zawarte są szczegółowe wyjaśnienia, rysunki oraz przykładowe zadania.

Tematyka notatki obejmuje:
- Podstawowe prawa termodynamiki
- Pojęcie energii wewnętrznej
- Procesy izotermiczne i adiabatyczne
- Pojęcie entropii
- Przemiany fazowe
- Maszyny cieplne

Plik znajduję się na moim dysku google, poniżej wklejam link do niego

https://drive.google.com/file/d/1NPnrH_UrqTQdgfdFOIJ05DGDEC5sdfYPsNU/view?usp=sharing

Na zamieszczonym zdjęciu znajduje się pierwsza strona notatek',
                                                                                                             current_timestamp - INTERVAL '160 minutes', current_timestamp, false),

                                                                                                            (2, 4, 2, 'Korepetycje z analizy matematycznej', 'Cześć, jestem studentem czwartego roku matematyki. Posiadam dobre zrozumienie analizy matematycznej i oferuję pomoc w zrozumieniu trudnych koncepcji oraz przygotowaniu do egzaminu. Materiały z lekcji mogę dostarczyć w formie elektronicznej. Zajęcia mogą odbywać się online lub osobiście, w zależności od Twojej preferencji.

Sesje mogą obejmować następujące tematy:
- Ciągi liczbowe
- Szeregi liczbowe
- Pochodne funkcji jednej zmiennej
- Całki niewłaściwe
- Szeregi funkcji
- Równania różniczkowe

Stawka to 30 zł / godzina.

Chętnych do nauki proszę o nawiązanie kontaktu poprzez system czatowy w aplikacji.',
                                                                                                             current_timestamp - INTERVAL '70 minutes', current_timestamp, false),
                                                                                                            (3, 4, 3, 'Problem z nieskończoną pętlą w algorytmie Bubble Sort', 'Cześć wszystkim, mam problem z implementacją algorytmu sortowania bąbelkowego w Pythonie. Poniżej znajduje się kod, który napisałem, ale niestety program wpada w nieskończoną pętlę. Czy mógłby mi ktoś pomóc?',
                                                                                                             current_timestamp - INTERVAL '90 minutes', current_timestamp, false),
                                                                                                            (4, 1, 1, 'Najlepsze praktyki z zakresu cyberbezpieczeństwa', 'Ta notatka omówi niektóre z najważniejszych zasad cyberbezpieczeństwa, które mogą być użyte do ochrony Twoich informacji. Dowiesz się jak stosować najlepsze praktyki z zakresu haseł, bezpiecznych nawyków przeglądania, dwuskładnikowego uwierzytelniania, i więcej. Zajrzyj pod poniższy link' ||
                                                                                                                                                                          '

                                                                                                                                                                          https://drive.google.com/file/d/1NPnrH_UrqTQdgfdFOIJ05DGDEC5sdfYPsNU/view?usp=sharing', current_timestamp - INTERVAL '160 minutes', current_timestamp, false),
                                                                                                            (5, 1, 3, 'Badanie wpływu kultury w erze cyfrowej post z obrazem', 'Ten post omówi sposoby, w jakie technologia cyfrowa, internet i media społecznościowe wpływają na kulturę, w tym na sposób, w jaki komunikujemy się, jak postrzegamy siebie i jak odnosimy się do innych.', current_timestamp, current_timestamp, false),
                                                                                                            (6, 1, 1, 'Piękno matematyki dyskretnej notatka z obrazem', 'Głębokie zanurzenie w piękny świat matematyki dyskretnej. W tej notatce wyjaśniam, jak matematyka dyskretna pomaga w rozwiązywaniu skomplikowanych problemów informatyki.', current_timestamp - INTERVAL '20 minutes', current_timestamp, false),
                                                                                                            (7, 2, 2, 'Pomoc w zadaniach domowych z algebry', 'Jeśli ktokolwiek ma problemy z zadaniem domowym z algebry, śmiało pisz. Mogę pomóc w tematach obejmujących równania liniowe, nierówności, wykresy, macierze, wielomiany i wyrażenia pierwiastkowe, równania i funkcje kwadratowe, wyrażenia wykładnicze i logarytmiczne, sekwencje i serie, prawdopodobieństwo i więcej.', current_timestamp - INTERVAL '30 minutes', current_timestamp, false),
                                                                                                            (8, 3, 1, 'Zrozumienie pochodnych w analizie matematycznej', 'W tej notatce wyjaśniłem pojęcie pochodnych w analizie matematycznej. Notatka zaczyna się od podstawowej definicji, po której następują reguły różniczkowania i zastosowania. Znajdują się tam przykłady i problemy do samodzielnego rozwiązania.' ||
                                                                                                                                                                          '
                                                                                                                                                                         https://drive.google.com/file/d/1NPnrH_UrqTQdgfdFOIJ05DGDEC5sdfYPsNU/view?usp=sharing', current_timestamp - INTERVAL '60 minutes', current_timestamp, false),
                                                                                                            (9, 3, 2, 'Poszukiwanie grupy studenckiej do nauki termodynamiki', 'Cześć wszystkim, staram się zorganizować grupę studiującą do nadchodzącego egzaminu z termodynamiki. Jeśli jesteś zainteresowany, proszę napisz do mnie wiadomość. Planujemy spotykać się dwa razy w tygodniu, aby omówić koncepcje, rozwiązać problemy i pomóc sobie nawzajem zrozumieć materiał.', current_timestamp - INTERVAL '70 minutes', current_timestamp, false),
                                                                                                            (10, 3, 3, 'Problem z programowaniem obiektowym', 'Mam problem ze zrozumieniem koncepcji dziedziczenia i polimorfizmu w programowaniu obiektowym. Czy ktoś mógłby to wyjaśnić za pomocą przykładu? Używam C++ na moim kursie.', current_timestamp - INTERVAL '80 minutes', current_timestamp, false),
                                                                                                            (11, 3, 2, 'Szukam partnera do nauki struktur danych', 'Szukam partnera do nauki na nadchodzący kurs struktur danych. Moim celem jest wspólne studiowanie i zdobycie 5.0 na tym kursie. Jeśli jesteś zainteresowany, odpisz na ten post.', current_timestamp - INTERVAL '90 minutes', current_timestamp, false),

                                                                                                            (12, 1, 2, 'Tytuł ogłoszenia', 'Content ogłoszenia', current_timestamp - INTERVAL '10 minutes', current_timestamp, false),
                                                                                                            (13, 1, 1, 'Tytuł notatki', 'Content notatki', current_timestamp, current_timestamp, false),
                                                                                                            (14, 1, 3, 'Tytuł postu', 'Content postu', current_timestamp - INTERVAL '20 minutes', current_timestamp, false),
                                                                                                            (15, 2, 1, 'Tytuł notatki', 'Content notatki', current_timestamp - INTERVAL '30 minutes', current_timestamp, false),
                                                                                                            (16, 2, 2, 'Tytuł ogłoszenia', 'Content ogłoszenia', current_timestamp - INTERVAL '3 days', current_timestamp, false),
                                                                                                            (17, 2, 3, 'Tytuł postu', 'Content postu', current_timestamp - INTERVAL '7 days', current_timestamp, false),
                                                                                                            (18, 3, 1, 'Tytuł notatki', 'Content notatki', current_timestamp - INTERVAL '8 days', current_timestamp, false),
                                                                                                            (19, 3, 2, 'Tytuł ogłoszenia', 'Content ogłoszenia', current_timestamp - INTERVAL '70 days', current_timestamp, false),
                                                                                                            (20, 3, 3, 'Tytuł postu', 'Content postu', current_timestamp - INTERVAL '200 days', current_timestamp, false),
                                                                                                            (21, 3, 3, 'Tytuł postu', 'Content postu', current_timestamp - INTERVAL '200 days', current_timestamp, false);

INSERT INTO entry_category(entry_id, category_id) VALUES
                                                         (1, 15),
                                                         (1, 24),

                                                         (2, 14),
                                                         (2, 21),
                                                         (2, 1),
                                                         (2, 6),

                                                         (3, 1),
                                                         (3, 6),
                                                         (3, 13),
                                                         (3, 16),

                                                         (4, 4),
                                                         (4, 1),
                                                         (4, 13),

                                                         (5, 3),
                                                         (5, 12),

                                                         (6, 22),
                                                         (6, 13),
                                                         (6, 1),
                                                         (6, 6),

                                                         (7, 20),
                                                         (7, 14),
                                                         (7, 1),
                                                         (7, 6),

                                                         (8, 21),
                                                        (8, 1),
                                                        (8, 6),
                                                        (8, 14),

                                                        (9, 2),
                                                        (9, 10),
                                                        (9, 15),
                                                        (9, 24),

                                                        (10, 13),
                                                        (10, 18),

                                                        (11, 16),
                                                        (11, 13),
                                                        (11, 1),
                                                        (11, 6),

                                                        (12, 1),
                                                        (12, 15),
                                                        (12, 6),
                                                        (12, 16),
                                                        (12, 17),

                                                        (13, 1),
                                                        (13, 6),
                                                        (13, 13),
                                                        (13, 16),

                                                        (14, 1),
                                                        (14, 6),
                                                        (14, 13),
                                                        (14, 16),

                                                        (15, 1),
                                                        (15, 6),
                                                        (15, 13),
                                                        (15, 16),

                                                        (16, 1),
                                                        (16, 6),
                                                        (16, 13),
                                                        (16, 16),

                                                        (17, 1),
                                                        (17, 6),
                                                        (17, 13),
                                                        (17, 16),

                                                        (18, 1),
                                                        (18, 7),
                                                        (18, 13),
                                                        (18, 16),

                                                        (19, 1),
                                                        (19, 7),
                                                        (19, 13),
                                                        (19, 16),

                                                        (20, 2),
                                                        (20, 9),
                                                        (20, 13),
                                                        (20, 16),

                                                        (21, 2),
                                                        (21, 9),
                                                        (21, 13),
                                                        (21, 16);



ALTER TABLE answer ALTER COLUMN content TYPE VARCHAR(1000);
INSERT INTO answer(answer_id, entry_id, user_id, content, created_at, is_deleted, top_answer) VALUES
                                                                                                  (51, 3, 2, '
Hej, może powinieneś spróbować użyć algorytmu sortowania przez wstawianie. Jest zazwyczaj szybszy niż sortowanie bąbelkowe.', current_timestamp - INTERVAL '3 minutes', false, false),
                                                                                                  (50, 3, 1, '
Problemem jest logika Twojego kodu. W rzeczywistości, kiedy sortowanie bąbelkowe zaczyna porównywanie elementów, powinno początkowo założyć, że lista jest posortowana. Jeśli znajdzie jakiekolwiek dwa elementy, które są w złej kolejności, powinno ustawić flagę informującą, że lista jest niesortowana. Ta flaga pozostanie prawdziwa tylko wtedy, gdy wszystkie elementy są w prawidłowej kolejności.

Dodatkowo warte wspomnienia jest to, że python umożliwia prostrzą zamianę wartości zmiennych bez tworzenia zmiennej tymczasowej',
                                                                                                   current_timestamp - INTERVAL '60 minutes', false, true);


INSERT INTO image(image) VALUES
                             ('image1.jpg'),
                             ('image2.jpg'),
                             ('image3.jpg'),
                             ('image4.jpg'),
                             ('image5.jpg');


INSERT INTO favorites(user_id, entry_id) VALUES (1, 1), (1, 2);

INSERT INTO image_entry(image_item_id, image_id) VALUES
                                                    (5, 1),
                                                    (6, 2),
                                                    (3, 3),
                                                    (1, 4);
INSERT INTO image_answer(image_item_id, image_id) VALUES
                                                    (50, 5);
INSERT INTO chat(user_one_last_read, user_two_last_read, user_one_id, user_two_id) VALUES
(current_timestamp - INTERVAL '92 minutes', current_timestamp - INTERVAL '89 minutes', 1, 2),
(current_timestamp - INTERVAL '55 minutes', current_timestamp - INTERVAL '55 minutes', 3, 1),
(current_timestamp - INTERVAL '70 minutes', current_timestamp - INTERVAL '67 minutes', 1, 4);

INSERT INTO messages(content, date_sent, chat_id, sender_id) VALUES
('Cześć, świetne notatki! Bardzo mi pomogły w ostatnim kolokwium. Masz może jeszcze jakieś z algebry?', current_timestamp - INTERVAL '90 minutes', 1, 2),
('Cześć, chciałbym się umówić na korepetycje z matematyki.', current_timestamp - INTERVAL '170 minutes', 2, 3),
('Cześć, jaki termin by ci odpowiadał?.', current_timestamp - INTERVAL '165 minutes', 2, 1),
('Najbardziej by mi odpowiadały czwartek lub wtorek w godzinach 16:00 - 18:00.', current_timestamp - INTERVAL '150 minutes', 2, 3),
('We wtorek w tym terminie mam zajęcia :( ale w czwartek nie ma problemu.', current_timestamp - INTERVAL '100 minutes', 2, 1),
('Świetnie! Do zobaczenia!', current_timestamp - INTERVAL '56 minutes', 2, 3),
('Cześć, widziałem twoje notatki z fizyki, czy prowadzisz też korepetycje?', current_timestamp - INTERVAL '300 minutes', 3, 4),
('Cześć, z fizyki nigdy nie prowadziłem ale mogę spróbować.', current_timestamp - INTERVAL '150 minutes', 3, 1),
('Świetnie! Czy odpowiadałaby ci sobota o 12:00?', current_timestamp - INTERVAL '68 minutes', 3, 4);



