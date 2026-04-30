-- ==========================================
-- CZYSZCZENIE TABEL (kolejność odwrotna do relacji)
-- ==========================================
--DELETE FROM reserved_seats;
--ALTER TABLE reserved_seats ALTER COLUMN id RESTART WITH 1;
--
--DELETE FROM tickets;
--ALTER TABLE tickets ALTER COLUMN id RESTART WITH 1;
--
--DELETE FROM screenings;
--ALTER TABLE screenings ALTER COLUMN id RESTART WITH 1;
--
--DELETE FROM seats;
--ALTER TABLE seats ALTER COLUMN id RESTART WITH 1;
--
--DELETE FROM rooms;
--ALTER TABLE rooms ALTER COLUMN id RESTART WITH 1;
--
--DELETE FROM movies;
--ALTER TABLE movies ALTER COLUMN id RESTART WITH 1;

TRUNCATE TABLE reserved_seats, tickets, screenings, seats, rooms, movies RESTART IDENTITY CASCADE;

-- ==========================================
-- 1. FILMY (25 Tytułów - różnorodne gatunki)
-- ==========================================
INSERT INTO movies (id, title, description, photo_url) VALUES
(1, 'Diuna: Część Druga', 'Epicka kontynuacja adaptacji kultowej powieści sci-fi. Paul Atryda staje na czele Fremenów.', 'https://example.com/diuna2.jpg'),
(2, 'Deadpool & Wolverine', 'Wolverine powraca, by połączyć siły z pyskatym najemnikiem w alternatywnych wymiarach.', 'https://example.com/dpw.jpg'),
(3, 'W głowie się nie mieści 2', 'Riley staje się nastolatką, co oznacza gruntowny remont w centrali sterowania i nowe emocje!', 'https://example.com/insideout2.jpg'),
(4, 'Gladiator II', 'Kontynuacja oscarowego hitu. Lucjusz, bratanek Kommodusa, wkracza na arenę.', 'https://example.com/gladiator2.jpg'),
(5, 'Joker: Folie à Deux', 'Arthur Fleck trafia do Azylu Arkham, gdzie poznaje Harley Quinn. Musicalowy thriller.', 'https://example.com/joker2.jpg'),
(6, 'Furiosa: Saga Mad Max', 'Historia pochodzenia potężnej wojowniczki Furiosy i jej walki o przetrwanie.', 'https://example.com/furiosa.jpg'),
(7, 'Królestwo Planety Małp', 'Wiele lat po rządach Cezara, młoda małpa wyrusza w podróż, która zmieni przyszłość.', 'https://example.com/apes.jpg'),
(8, 'Bad Boys: Ride or Die', 'Marcus i Mike wracają, ale tym razem to oni są uciekinierami ściganymi przez prawo.', 'https://example.com/badboys.jpg'),
(9, 'Ciche miejsce: Dzień pierwszy', 'Zobacz, jak świat ucichł. Prequel popularnej serii horrorów o inwazji kosmitów.', 'https://example.com/quietplace.jpg'),
(10, 'Obcy: Romulus', 'Grupa młodych kolonizatorów kosmosu staje twarzą w twarz z najbardziej przerażającą formą życia.', 'https://example.com/alien.jpg'),
(11, 'Garfield', 'Kultowy, nienawidzący poniedziałków kot wyrusza na dziką przygodę na świeżym powietrzu.', 'https://example.com/garfield.jpg'),
(12, 'Gru i Minionki: Pod przykrywką', 'Gru, Lucy i ich rodzina muszą uciekać przed nowym, potężnym wrogiem.', 'https://example.com/gru.jpg'),
(13, 'Kaskader (The Fall Guy)', 'Zlekceważony kaskader musi odnaleźć zaginioną gwiazdę filmową, by uratować film swojej byłej.', 'https://example.com/fallguy.jpg'),
(14, 'Istoty fantastyczne', 'Dziewczynka odkrywa, że potrafi widzieć wymyślonych przyjaciół wszystkich ludzi.', 'https://example.com/if.jpg'),
(15, 'Twisters', 'Nowe pokolenie łowców burz mierzy się z ekstremalnymi zjawiskami pogodowymi w Oklahomie.', 'https://example.com/twisters.jpg'),
(16, 'Borderlands', 'Lilith wraca na swoją ojczystą planetę Pandora, by odnaleźć zaginioną córkę potężnego Atlasa.', 'https://example.com/borderlands.jpg'),
(17, 'Sok z żuka 2 (Beetlejuice Beetlejuice)', 'Rodzina Deetzów powraca do Winter River, a demoniczny bio-egzorcysta znów sieje zamęt.', 'https://example.com/beetlejuice.jpg'),
(18, 'Venom 3: Ostatni taniec', 'Eddie i Venom muszą podjąć trudne decyzje w obliczu zbliżającego się końca ich wspólnej drogi.', 'https://example.com/venom3.jpg'),
(19, 'Kraven Łowca', 'Historia jednego z najbardziej ikonicznych przeciwników Spider-Mana.', 'https://example.com/kraven.jpg'),
(20, 'Władca Pierścieni: Wojna Rohirrimów', 'Animowany film osadzony w świecie Tolkiena, opowiadający o legendarnym królu Rohanu.', 'https://example.com/lotr.jpg'),
(21, 'Mufasa: Król Lew', 'Prequel kultowej animacji, opowiadający o młodości Mufasy i jego relacji ze Skazą.', 'https://example.com/mufasa.jpg'),
(22, 'Sonic 3: Szybki jak błyskawica', 'Jeż Sonic i jego przyjaciele muszą zmierzyć się z nowym, mrocznym zagrożeniem - Shadowem.', 'https://example.com/sonic3.jpg'),
(23, 'Nosferatu', 'Gotycka opowieść o obsesji pomiędzy nawiedzoną młodą kobietą a przerażającym wampirem.', 'https://example.com/nosferatu.jpg'),
(24, 'Mickey 17', 'Pracownik jednorazowego użytku wysłany na niebezpieczną misję kolonizacyjną zyskuje samoświadomość.', 'https://example.com/mickey17.jpg'),
(25, 'Paddington w Peru', 'Uroczy miś wyrusza w podróż do Ameryki Południowej, aby odwiedzić swoją ukochaną ciocię Lucy.', 'https://example.com/paddington3.jpg');

-- ==========================================
-- 2. SALE KINOWE (8 Sal)
-- ==========================================
INSERT INTO rooms (id, name) VALUES
(1, 'Sala 1 - IMAX (Największa)'),
(2, 'Sala 2 - Dolby Atmos (Duża)'),
(3, 'Sala 3 - Standard A'),
(4, 'Sala 4 - Standard B'),
(5, 'Sala 5 - Standard C'),
(6, 'Sala 6 - 3D'),
(7, 'Sala 7 - Premium (Rozkładane Fotele)'),
(8, 'Sala 8 - Kameralna / Studio');

-- ==========================================
-- 3. MIEJSCA W SALACH
-- ==========================================
-- Sala 1 (IMAX) - 8 rzędów po 15 miejsc (120 miejsc)
INSERT INTO seats (room_id, row_number, seat_number) VALUES
(1,1,1),(1,1,2),(1,1,3),(1,1,4),(1,1,5),(1,1,6),(1,1,7),(1,1,8),(1,1,9),(1,1,10),(1,1,11),(1,1,12),(1,1,13),(1,1,14),(1,1,15),
(1,2,1),(1,2,2),(1,2,3),(1,2,4),(1,2,5),(1,2,6),(1,2,7),(1,2,8),(1,2,9),(1,2,10),(1,2,11),(1,2,12),(1,2,13),(1,2,14),(1,2,15),
(1,3,1),(1,3,2),(1,3,3),(1,3,4),(1,3,5),(1,3,6),(1,3,7),(1,3,8),(1,3,9),(1,3,10),(1,3,11),(1,3,12),(1,3,13),(1,3,14),(1,3,15),
(1,4,1),(1,4,2),(1,4,3),(1,4,4),(1,4,5),(1,4,6),(1,4,7),(1,4,8),(1,4,9),(1,4,10),(1,4,11),(1,4,12),(1,4,13),(1,4,14),(1,4,15),
(1,5,1),(1,5,2),(1,5,3),(1,5,4),(1,5,5),(1,5,6),(1,5,7),(1,5,8),(1,5,9),(1,5,10),(1,5,11),(1,5,12),(1,5,13),(1,5,14),(1,5,15),
(1,6,1),(1,6,2),(1,6,3),(1,6,4),(1,6,5),(1,6,6),(1,6,7),(1,6,8),(1,6,9),(1,6,10),(1,6,11),(1,6,12),(1,6,13),(1,6,14),(1,6,15),
(1,7,1),(1,7,2),(1,7,3),(1,7,4),(1,7,5),(1,7,6),(1,7,7),(1,7,8),(1,7,9),(1,7,10),(1,7,11),(1,7,12),(1,7,13),(1,7,14),(1,7,15),
(1,8,1),(1,8,2),(1,8,3),(1,8,4),(1,8,5),(1,8,6),(1,8,7),(1,8,8),(1,8,9),(1,8,10),(1,8,11),(1,8,12),(1,8,13),(1,8,14),(1,8,15);

-- Sale 2 do 6 (Standard/Dolby/3D) - 6 rzędów po 12 miejsc (72 miejsca na salę)
-- Wykorzystujemy prostą wstawkę dla wszystkich pięciu sal naraz
INSERT INTO seats (room_id, row_number, seat_number)
SELECT room, row_n, seat_n
FROM (VALUES (2),(3),(4),(5),(6)) AS r(room)
CROSS JOIN (VALUES (1),(2),(3),(4),(5),(6)) AS row_t(row_n)
CROSS JOIN (VALUES (1),(2),(3),(4),(5),(6),(7),(8),(9),(10),(11),(12)) AS seat_t(seat_n);

-- Sala 7 (Premium) - 4 rzędy po 8 dużych foteli rozkładanych (32 miejsca)
INSERT INTO seats (room_id, row_number, seat_number)
SELECT 7, row_n, seat_n
FROM (VALUES (1),(2),(3),(4)) AS row_t(row_n)
CROSS JOIN (VALUES (1),(2),(3),(4),(5),(6),(7),(8)) AS seat_t(seat_n);

-- Sala 8 (Studio) - 3 rzędy po 6 miejsc (18 miejsc)
INSERT INTO seats (room_id, row_number, seat_number)
SELECT 8, row_n, seat_n
FROM (VALUES (1),(2),(3)) AS row_t(row_n)
CROSS JOIN (VALUES (1),(2),(3),(4),(5),(6)) AS seat_t(seat_n);


-- ==========================================
-- 4. SEANSY (REPERTUAR NA WEEKEND)
-- ==========================================

-- ------------- PIĄTEK, 15 Maja 2026 -------------
INSERT INTO screenings (movie_id, room_id, start_time, end_time) VALUES
-- Sala 1 (IMAX - Blockbustery)
(1, 1, '2026-05-15 10:00:00', '2026-05-15 12:50:00'), -- Diuna 2
(4, 1, '2026-05-15 13:30:00', '2026-05-15 16:00:00'), -- Gladiator II
(1, 1, '2026-05-15 16:45:00', '2026-05-15 19:35:00'), -- Diuna 2
(2, 1, '2026-05-15 20:15:00', '2026-05-15 22:25:00'), -- Deadpool & Wolverine
(10, 1, '2026-05-15 23:00:00', '2026-05-16 01:00:00'), -- Obcy (Nocny seans)

-- Sala 2 (Atmos - Akcja i Dźwięk)
(15, 2, '2026-05-15 11:00:00', '2026-05-15 13:00:00'), -- Twisters
(8, 2, '2026-05-15 13:40:00', '2026-05-15 15:40:00'), -- Bad Boys
(6, 2, '2026-05-15 16:15:00', '2026-05-15 18:45:00'), -- Furiosa
(5, 2, '2026-05-15 19:30:00', '2026-05-15 21:45:00'), -- Joker 2
(9, 2, '2026-05-15 22:15:00', '2026-05-16 00:00:00'), -- Ciche Miejsce

-- Sala 3 (Standard - Familijne rano, komedia wieczorem)
(11, 3, '2026-05-15 10:30:00', '2026-05-15 12:15:00'), -- Garfield
(3, 3, '2026-05-15 12:45:00', '2026-05-15 14:30:00'), -- W głowie się nie mieści 2
(12, 3, '2026-05-15 15:00:00', '2026-05-15 16:45:00'), -- Gru i Minionki
(13, 3, '2026-05-15 17:15:00', '2026-05-15 19:15:00'), -- Kaskader
(17, 3, '2026-05-15 20:00:00', '2026-05-15 21:50:00'), -- Sok z żuka 2

-- Sala 4 (Standard - Mix)
(14, 4, '2026-05-15 11:30:00', '2026-05-15 13:20:00'), -- Istoty Fantastyczne
(22, 4, '2026-05-15 14:00:00', '2026-05-15 16:00:00'), -- Sonic 3
(16, 4, '2026-05-15 16:30:00', '2026-05-15 18:30:00'), -- Borderlands
(7, 4, '2026-05-15 19:00:00', '2026-05-15 21:30:00'), -- Królestwo Planety Małp
(18, 4, '2026-05-15 22:00:00', '2026-05-16 00:10:00'), -- Venom 3

-- Sala 5 (Standard - Mix 2)
(21, 5, '2026-05-15 10:15:00', '2026-05-15 12:15:00'), -- Mufasa
(25, 5, '2026-05-15 12:45:00', '2026-05-15 14:45:00'), -- Paddington
(20, 5, '2026-05-15 15:30:00', '2026-05-15 18:00:00'), -- Władca Pierścieni
(19, 5, '2026-05-15 18:40:00', '2026-05-15 20:40:00'), -- Kraven
(5, 5, '2026-05-15 21:15:00', '2026-05-15 23:30:00'), -- Joker 2

-- Sala 6 (3D)
(3, 6, '2026-05-15 11:15:00', '2026-05-15 13:00:00'), -- W głowie się nie mieści 2 (3D)
(12, 6, '2026-05-15 13:30:00', '2026-05-15 15:15:00'), -- Gru i Minionki (3D)
(2, 6, '2026-05-15 16:00:00', '2026-05-15 18:10:00'), -- Deadpool (3D)
(1, 6, '2026-05-15 18:45:00', '2026-05-15 21:35:00'), -- Diuna 2 (3D)

-- Sala 7 (Premium)
(20, 7, '2026-05-15 14:30:00', '2026-05-15 17:00:00'), -- Władca Pierścieni
(4, 7, '2026-05-15 18:00:00', '2026-05-15 20:30:00'), -- Gladiator II
(1, 7, '2026-05-15 21:30:00', '2026-05-16 00:20:00'), -- Diuna 2

-- Sala 8 (Studio - Kameralne / Thrillery / Horrory)
(24, 8, '2026-05-15 16:15:00', '2026-05-15 18:30:00'), -- Mickey 17
(23, 8, '2026-05-15 19:15:00', '2026-05-15 21:20:00'), -- Nosferatu
(9, 8, '2026-05-15 22:00:00', '2026-05-15 23:45:00'); -- Ciche Miejsce


-- ------------- SOBOTA, 16 Maja 2026 -------------
INSERT INTO screenings (movie_id, room_id, start_time, end_time) VALUES
-- Sala 1 (IMAX)
(4, 1, '2026-05-16 10:30:00', '2026-05-16 13:00:00'),
(1, 1, '2026-05-16 13:45:00', '2026-05-16 16:35:00'),
(2, 1, '2026-05-16 17:15:00', '2026-05-16 19:25:00'),
(1, 1, '2026-05-16 20:15:00', '2026-05-16 23:05:00'),

-- Sala 2 (Atmos)
(6, 2, '2026-05-16 11:30:00', '2026-05-16 14:00:00'),
(8, 2, '2026-05-16 14:40:00', '2026-05-16 16:40:00'),
(15, 2, '2026-05-16 17:20:00', '2026-05-16 19:20:00'),
(5, 2, '2026-05-16 20:00:00', '2026-05-16 22:15:00'),

-- Sala 3 (Standard - Dzień dziecka na pełnej)
(3, 3, '2026-05-16 09:30:00', '2026-05-16 11:15:00'),
(11, 3, '2026-05-16 11:45:00', '2026-05-16 13:30:00'),
(3, 3, '2026-05-16 14:00:00', '2026-05-16 15:45:00'),
(12, 3, '2026-05-16 16:15:00', '2026-05-16 18:00:00'),
(13, 3, '2026-05-16 18:45:00', '2026-05-16 20:45:00'),
(17, 3, '2026-05-16 21:30:00', '2026-05-16 23:20:00'),

-- Sala 4 (Standard)
(25, 4, '2026-05-16 10:00:00', '2026-05-16 12:00:00'),
(21, 4, '2026-05-16 12:30:00', '2026-05-16 14:30:00'),
(22, 4, '2026-05-16 15:00:00', '2026-05-16 17:00:00'),
(7, 4, '2026-05-16 17:45:00', '2026-05-16 20:15:00'),
(18, 4, '2026-05-16 21:00:00', '2026-05-16 23:10:00'),

-- Sala 5 (Standard)
(14, 5, '2026-05-16 11:00:00', '2026-05-16 12:50:00'),
(20, 5, '2026-05-16 13:30:00', '2026-05-16 16:00:00'),
(16, 5, '2026-05-16 16:45:00', '2026-05-16 18:45:00'),
(19, 5, '2026-05-16 19:30:00', '2026-05-16 21:30:00'),
(10, 5, '2026-05-16 22:15:00', '2026-05-17 00:15:00'),

-- Sala 6 (3D)
(12, 6, '2026-05-16 10:15:00', '2026-05-16 12:00:00'),
(3, 6, '2026-05-16 12:45:00', '2026-05-16 14:30:00'),
(1, 6, '2026-05-16 15:15:00', '2026-05-16 18:05:00'),
(2, 6, '2026-05-16 18:45:00', '2026-05-16 20:55:00'),

-- Sala 7 (Premium)
(1, 7, '2026-05-16 13:00:00', '2026-05-16 15:50:00'),
(5, 7, '2026-05-16 16:45:00', '2026-05-16 19:00:00'),
(4, 7, '2026-05-16 20:00:00', '2026-05-16 22:30:00'),

-- Sala 8 (Studio)
(24, 8, '2026-05-16 15:30:00', '2026-05-16 17:45:00'),
(23, 8, '2026-05-16 18:30:00', '2026-05-16 20:35:00'),
(9, 8, '2026-05-16 21:15:00', '2026-05-16 23:00:00');


-- ------------- NIEDZIELA, 17 Maja 2026 -------------
INSERT INTO screenings (movie_id, room_id, start_time, end_time) VALUES
-- Pule dla głównych sal
(1, 1, '2026-05-17 11:00:00', '2026-05-17 13:50:00'),
(4, 1, '2026-05-17 14:30:00', '2026-05-17 17:00:00'),
(2, 1, '2026-05-17 17:45:00', '2026-05-17 19:55:00'),
(1, 1, '2026-05-17 20:45:00', '2026-05-17 23:35:00'),

(15, 2, '2026-05-17 12:00:00', '2026-05-17 14:00:00'),
(5, 2, '2026-05-17 14:45:00', '2026-05-17 17:00:00'),
(6, 2, '2026-05-17 17:45:00', '2026-05-17 20:15:00'),
(8, 2, '2026-05-17 21:00:00', '2026-05-17 23:00:00'),

(3, 3, '2026-05-17 10:00:00', '2026-05-17 11:45:00'),
(12, 3, '2026-05-17 12:15:00', '2026-05-17 14:00:00'),
(3, 3, '2026-05-17 14:30:00', '2026-05-17 16:15:00'),
(17, 3, '2026-05-17 17:00:00', '2026-05-17 18:50:00'),
(13, 3, '2026-05-17 19:30:00', '2026-05-17 21:30:00');