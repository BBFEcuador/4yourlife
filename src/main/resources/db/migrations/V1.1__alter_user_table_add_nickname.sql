ALTER TABLE users
ADD COLUMN IF NOT EXISTS nickname text default null;

UPDATE users
SET nickname = name1
WHERE nickname IS NULL;