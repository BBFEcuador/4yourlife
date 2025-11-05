BEGIN;

ALTER TABLE public.promises
DROP CONSTRAINT IF EXISTS fk_participant_on_promises;

UPDATE public.promises a
SET participant_id = p.user_id
    FROM public.participants p
WHERE a.participant_id = p.id;

ALTER TABLE public.promises
    ADD CONSTRAINT fk_participant_on_promises
        FOREIGN KEY (participant_id)
            REFERENCES public.users(id)
            ON UPDATE CASCADE
            ON DELETE CASCADE;
COMMIT;
