-- ==========================================
-- CZYSZCZENIE TABEL / WIDOKÓW
-- ==========================================
--DELETE FROM screening_seats_view;
--ALTER TABLE screening_seats_view ALTER COLUMN id RESTART WITH 1;
--
--DELETE FROM repertoire_view;

TRUNCATE TABLE screening_seats_view, repertoire_view RESTART IDENTITY CASCADE;

-- ==========================================
-- 1. ZDENORMALIZOWANY REPERTUAR (Widok)
-- ==========================================
INSERT INTO repertoire_view (screening_id, movie_id, movie_title, movie_description, movie_photo_url, room_name, start_time, end_time, duration_minutes)
WITH m(id, title, description, photo_url) AS (
    VALUES
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
    (25, 'Paddington w Peru', 'Uroczy miś wyrusza w podróż do Ameryki Południowej, aby odwiedzić swoją ukochaną ciocię Lucy.', 'https://example.com/paddington3.jpg')
),
r(id, name) AS (
    VALUES
    (1, 'Sala 1 - IMAX (Największa)'),
    (2, 'Sala 2 - Dolby Atmos (Duża)'),
    (3, 'Sala 3 - Standard A'),
    (4, 'Sala 4 - Standard B'),
    (5, 'Sala 5 - Standard C'),
    (6, 'Sala 6 - 3D'),
    (7, 'Sala 7 - Premium (Rozkładane Fotele)'),
    (8, 'Sala 8 - Kameralna / Studio')
),
s(id, movie_id, room_id, start_time, end_time) AS (
    VALUES
    -- Piątek
    (1, 1, 1, '2026-05-15 10:00:00', '2026-05-15 12:50:00'), (2, 4, 1, '2026-05-15 13:30:00', '2026-05-15 16:00:00'), (3, 1, 1, '2026-05-15 16:45:00', '2026-05-15 19:35:00'), (4, 2, 1, '2026-05-15 20:15:00', '2026-05-15 22:25:00'), (5, 10, 1, '2026-05-15 23:00:00', '2026-05-15 01:00:00'),
    (6, 15, 2, '2026-05-15 11:00:00', '2026-05-15 13:00:00'), (7, 8, 2, '2026-05-15 13:40:00', '2026-05-15 15:40:00'), (8, 6, 2, '2026-05-15 16:15:00', '2026-05-15 18:45:00'), (9, 5, 2, '2026-05-15 19:30:00', '2026-05-15 21:45:00'), (10, 9, 2, '2026-05-15 22:15:00', '2026-05-15 00:00:00'),
    (11, 11, 3, '2026-05-15 10:30:00', '2026-05-15 12:15:00'), (12, 3, 3, '2026-05-15 12:45:00', '2026-05-15 14:30:00'), (13, 12, 3, '2026-05-15 15:00:00', '2026-05-15 16:45:00'), (14, 13, 3, '2026-05-15 17:15:00', '2026-05-15 19:15:00'), (15, 17, 3, '2026-05-15 20:00:00', '2026-05-15 21:50:00'),
    (16, 14, 4, '2026-05-15 11:30:00', '2026-05-15 13:20:00'), (17, 22, 4, '2026-05-15 14:00:00', '2026-05-15 16:00:00'), (18, 16, 4, '2026-05-15 16:30:00', '2026-05-15 18:30:00'), (19, 7, 4, '2026-05-15 19:00:00', '2026-05-15 21:30:00'), (20, 18, 4, '2026-05-15 22:00:00', '2026-05-15 00:10:00'),
    (21, 21, 5, '2026-05-15 10:15:00', '2026-05-15 12:15:00'), (22, 25, 5, '2026-05-15 12:45:00', '2026-05-15 14:45:00'), (23, 20, 5, '2026-05-15 15:30:00', '2026-05-15 18:00:00'), (24, 19, 5, '2026-05-15 18:40:00', '2026-05-15 20:40:00'), (25, 5, 5, '2026-05-15 21:15:00', '2026-05-15 23:30:00'),
    (26, 3, 6, '2026-05-15 11:15:00', '2026-05-15 13:00:00'), (27, 12, 6, '2026-05-15 13:30:00', '2026-05-15 15:15:00'), (28, 2, 6, '2026-05-15 16:00:00', '2026-05-15 18:10:00'), (29, 1, 6, '2026-05-15 18:45:00', '2026-05-15 21:35:00'),
    (30, 20, 7, '2026-05-15 14:30:00', '2026-05-15 17:00:00'), (31, 4, 7, '2026-05-15 18:00:00', '2026-05-15 20:30:00'), (32, 1, 7, '2026-05-15 21:30:00', '2026-05-15 00:20:00'),
    (33, 24, 8, '2026-05-15 16:15:00', '2026-05-15 18:30:00'), (34, 23, 8, '2026-05-15 19:15:00', '2026-05-15 21:20:00'), (35, 9, 8, '2026-05-15 22:00:00', '2026-05-15 23:45:00'),
    -- Sobota
    (36, 4, 1, '2026-05-16 10:30:00', '2026-05-16 13:00:00'), (37, 1, 1, '2026-05-16 13:45:00', '2026-05-16 16:35:00'), (38, 2, 1, '2026-05-16 17:15:00', '2026-05-16 19:25:00'), (39, 1, 1, '2026-05-16 20:15:00', '2026-05-16 23:05:00'),
    (40, 6, 2, '2026-05-16 11:30:00', '2026-05-16 14:00:00'), (41, 8, 2, '2026-05-16 14:40:00', '2026-05-16 16:40:00'), (42, 15, 2, '2026-05-16 17:20:00', '2026-05-16 19:20:00'), (43, 5, 2, '2026-05-16 20:00:00', '2026-05-16 22:15:00'),
    (44, 3, 3, '2026-05-16 09:30:00', '2026-05-16 11:15:00'), (45, 11, 3, '2026-05-16 11:45:00', '2026-05-16 13:30:00'), (46, 3, 3, '2026-05-16 14:00:00', '2026-05-16 15:45:00'), (47, 12, 3, '2026-05-16 16:15:00', '2026-05-16 18:00:00'), (48, 13, 3, '2026-05-16 18:45:00', '2026-05-16 20:45:00'), (49, 17, 3, '2026-05-16 21:30:00', '2026-05-16 23:20:00'),
    (50, 25, 4, '2026-05-16 10:00:00', '2026-05-16 12:00:00'), (51, 21, 4, '2026-05-16 12:30:00', '2026-05-16 14:30:00'), (52, 22, 4, '2026-05-16 15:00:00', '2026-05-16 17:00:00'), (53, 7, 4, '2026-05-16 17:45:00', '2026-05-16 20:15:00'), (54, 18, 4, '2026-05-16 21:00:00', '2026-05-16 23:10:00'),
    (55, 14, 5, '2026-05-16 11:00:00', '2026-05-16 12:50:00'), (56, 20, 5, '2026-05-16 13:30:00', '2026-05-16 16:00:00'), (57, 16, 5, '2026-05-16 16:45:00', '2026-05-16 18:45:00'), (58, 19, 5, '2026-05-16 19:30:00', '2026-05-16 21:30:00'), (59, 10, 5, '2026-05-16 22:15:00', '2026-05-16 00:15:00'),
    (60, 12, 6, '2026-05-16 10:15:00', '2026-05-16 12:00:00'), (61, 3, 6, '2026-05-16 12:45:00', '2026-05-16 14:30:00'), (62, 1, 6, '2026-05-16 15:15:00', '2026-05-16 18:05:00'), (63, 2, 6, '2026-05-16 18:45:00', '2026-05-16 20:55:00'),
    (64, 1, 7, '2026-05-16 13:00:00', '2026-05-16 15:50:00'), (65, 5, 7, '2026-05-16 16:45:00', '2026-05-16 19:00:00'), (66, 4, 7, '2026-05-16 20:00:00', '2026-05-16 22:30:00'),
    (67, 24, 8, '2026-05-16 15:30:00', '2026-05-16 17:45:00'), (68, 23, 8, '2026-05-16 18:30:00', '2026-05-16 20:35:00'), (69, 9, 8, '2026-05-16 21:15:00', '2026-05-16 23:00:00'),
    -- Niedziela
    (70, 1, 1, '2026-05-17 11:00:00', '2026-05-17 13:50:00'), (71, 4, 1, '2026-05-17 14:30:00', '2026-05-17 17:00:00'), (72, 2, 1, '2026-05-17 17:45:00', '2026-05-17 19:55:00'), (73, 1, 1, '2026-05-17 20:45:00', '2026-05-17 23:35:00'),
    (74, 15, 2, '2026-05-17 12:00:00', '2026-05-17 14:00:00'), (75, 5, 2, '2026-05-17 14:45:00', '2026-05-17 17:00:00'), (76, 6, 2, '2026-05-17 17:45:00', '2026-05-17 20:15:00'), (77, 8, 2, '2026-05-17 21:00:00', '2026-05-17 23:00:00'),
    (78, 3, 3, '2026-05-17 10:00:00', '2026-05-17 11:45:00'), (79, 12, 3, '2026-05-17 12:15:00', '2026-05-17 14:00:00'), (80, 3, 3, '2026-05-17 14:30:00', '2026-05-17 16:15:00'), (81, 17, 3, '2026-05-17 17:00:00', '2026-05-17 18:50:00'), (82, 13, 3, '2026-05-17 19:30:00', '2026-05-17 21:30:00')
)
SELECT
    s.id, s.movie_id, m.title, m.description, m.photo_url, r.name,
    s.start_time::timestamp, s.end_time::timestamp,
    EXTRACT(EPOCH FROM (s.end_time::timestamp - s.start_time::timestamp))/60
FROM s
JOIN m ON s.movie_id = m.id
JOIN r ON s.room_id = r.id;


-- ==========================================
-- 2. ZDENORMALIZOWANE MIEJSCA DLA SEANSÓW
-- ==========================================
INSERT INTO screening_seats_view (screening_id, original_seat_id, row_number, seat_number, is_available)
WITH s(id, room_id) AS (
    SELECT screening_id,
           CASE
             WHEN room_name LIKE '%IMAX%' THEN 1
             WHEN room_name LIKE '%Atmos%' THEN 2
             WHEN room_name LIKE '%Standard A%' THEN 3
             WHEN room_name LIKE '%Standard B%' THEN 4
             WHEN room_name LIKE '%Standard C%' THEN 5
             WHEN room_name LIKE '%3D%' THEN 6
             WHEN room_name LIKE '%Premium%' THEN 7
             WHEN room_name LIKE '%Kameralna%' THEN 8
           END
    FROM repertoire_view
)
-- Sala 1 (IMAX - offset miejsc to 0)
SELECT s.id, 0 + ((row_n.n - 1) * 15) + seat_n.n, row_n.n, seat_n.n, true
FROM s
CROSS JOIN (VALUES (1),(2),(3),(4),(5),(6),(7),(8)) AS row_n(n)
CROSS JOIN (VALUES (1),(2),(3),(4),(5),(6),(7),(8),(9),(10),(11),(12),(13),(14),(15)) AS seat_n(n)
WHERE s.room_id = 1

UNION ALL
-- Sale 2 do 6 (Standardowe - offset wyliczany dynamicznie dla odpowiedniego room_id)
SELECT s.id,
       CASE s.room_id
           WHEN 2 THEN 120
           WHEN 3 THEN 192
           WHEN 4 THEN 264
           WHEN 5 THEN 336
           WHEN 6 THEN 408
       END + ((row_n.n - 1) * 12) + seat_n.n,
       row_n.n, seat_n.n, true
FROM s
CROSS JOIN (VALUES (1),(2),(3),(4),(5),(6)) AS row_n(n)
CROSS JOIN (VALUES (1),(2),(3),(4),(5),(6),(7),(8),(9),(10),(11),(12)) AS seat_n(n)
WHERE s.room_id IN (2, 3, 4, 5, 6)

UNION ALL
-- Sala 7 (Premium - offset 480)
SELECT s.id, 480 + ((row_n.n - 1) * 8) + seat_n.n, row_n.n, seat_n.n, true
FROM s
CROSS JOIN (VALUES (1),(2),(3),(4)) AS row_n(n)
CROSS JOIN (VALUES (1),(2),(3),(4),(5),(6),(7),(8)) AS seat_n(n)
WHERE s.room_id = 7

UNION ALL
-- Sala 8 (Studio - offset 512)
SELECT s.id, 512 + ((row_n.n - 1) * 6) + seat_n.n, row_n.n, seat_n.n, true
FROM s
CROSS JOIN (VALUES (1),(2),(3)) AS row_n(n)
CROSS JOIN (VALUES (1),(2),(3),(4),(5),(6)) AS seat_n(n)
WHERE s.room_id = 8;