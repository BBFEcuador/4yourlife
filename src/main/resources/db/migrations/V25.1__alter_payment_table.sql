ALTER TABLE payments
    ADD COLUMN IF NOT EXISTS training_id VARCHAR(36);

ALTER TABLE participants
    ADD COLUMN IF NOT EXISTS original_training VARCHAR(36);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_payments_training'
    ) THEN
ALTER TABLE payments
    ADD CONSTRAINT fk_payments_training
        FOREIGN KEY (training_id)
            REFERENCES training(id)
            ON DELETE RESTRICT;
END IF;
END $$;