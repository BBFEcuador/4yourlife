ALTER TABLE stores ADD COLUMN IF NOT EXISTS create_by text default 'system';

ALTER TABLE stores ADD COLUMN IF NOT EXISTS isActive BOOLEAN default true;