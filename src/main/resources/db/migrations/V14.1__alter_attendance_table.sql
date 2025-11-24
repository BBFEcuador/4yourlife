BEGIN;

ALTER TABLE public.attendances
DROP CONSTRAINT IF EXISTS fk_user_on_attendance;

UPDATE public.attendances a
SET user_id = p.user_id
    FROM public.participants p
WHERE a.user_id = p.id;

ALTER TABLE public.attendances
    ADD CONSTRAINT fk_user_on_attendance
        FOREIGN KEY (user_id)
            REFERENCES public.users(id)
            ON UPDATE CASCADE
            ON DELETE CASCADE;
COMMIT;
