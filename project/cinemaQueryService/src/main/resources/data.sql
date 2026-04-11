DELETE FROM screening_seats_view;
ALTER TABLE screening_seats_view ALTER COLUMN id RESTART WITH 1;

DELETE FROM repertoire_view;

-- ==========================================
-- 1. ZDENORMALIZOWANY REPERTUAR (Widok)
-- ==========================================
INSERT INTO repertoire_view (screening_id, movie_id, movie_title, movie_description, movie_photo_url, room_name, start_time, end_time, duration_minutes) VALUES
(1, 1, 'Diuna: Część Druga', 'Epicka kontynuacja adaptacji kultowej powieści sci-fi. Paul Atryda staje na czele Fremenów.', 'https://example.com/diuna2.jpg', 'Sala 1 - IMAX', '2026-05-15 10:00:00', '2026-05-15 12:50:00', 170),
(2, 4, 'Gladiator II', 'Kontynuacja oscarowego hitu. Lucjusz, bratanek Kommodusa, wkracza na arenę.', 'https://example.com/gladiator2.jpg', 'Sala 1 - IMAX', '2026-05-15 13:30:00', '2026-05-15 16:00:00', 150),
(3, 15, 'Twisters', 'Nowe pokolenie łowców burz mierzy się z ekstremalnymi zjawiskami pogodowymi.', 'https://example.com/twisters.jpg', 'Sala 2 - Dolby Atmos', '2026-05-15 11:00:00', '2026-05-15 13:00:00', 120);

-- ==========================================
-- 2. ZDENORMALIZOWANE MIEJSCA DLA SEANSÓW
-- ==========================================
INSERT INTO screening_seats_view (screening_id, original_seat_id, row_number, seat_number, is_available)
SELECT 1,
       (row_n - 1) * 5 + seat_n AS original_seat_id,
       row_n,
       seat_n,
       true
FROM (VALUES (1),(2),(3)) AS row_t(row_n)
CROSS JOIN (VALUES (1),(2),(3),(4),(5)) AS seat_t(seat_n);