INSERT INTO measure_type (id, grams, name) VALUES (0, 0.001, 'мг')
ON CONFLICT (id) DO NOTHING;
INSERT INTO measure_type (id, grams, name) VALUES (1, 0.000001, 'мкг')
ON CONFLICT (id) DO NOTHING;
INSERT INTO measure_type (id, grams, name) VALUES (2, 1, 'г')
ON CONFLICT (id) DO NOTHING;
INSERT INTO measure_type (id, grams, name) VALUES (3, 1, 'ккал')
ON CONFLICT (id) DO NOTHING;

INSERT INTO "user" (id, login, password) VALUES (4, 'test@test.ru', 'test')
ON CONFLICT (id) DO NOTHING;
