ALTER TABLE payments ADD COLUMN IF NOT EXISTS training_id VARCHAR(36);

ALTER TABLE payments
ADD CONSTRAINT fk_payments_training
FOREIGN KEY (training_id)
REFERENCES trainings(id)
ON DELETE RESTRICT;