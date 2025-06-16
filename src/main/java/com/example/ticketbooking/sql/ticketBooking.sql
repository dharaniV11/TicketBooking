-- public.movies definition

-- Drop table

-- DROP TABLE public.movies;

CREATE TABLE public.movies (
                               id uuid NOT NULL,
                               genre varchar(255) NULL,
                               movie_name varchar(255) NOT NULL,
                               price numeric(5, 2) NULL,
                               rating float8 NOT NULL,
                               CONSTRAINT movies_pkey PRIMARY KEY (id),
                               CONSTRAINT uknh6l2fpydr26j7vopnxjs9qkj UNIQUE (movie_name)
);


-- public.show_time definition

-- Drop table

-- DROP TABLE public.show_time;

CREATE TABLE public.show_time (
                                  id uuid NOT NULL,
                                  show_time timestamp(6) NOT NULL,
                                  CONSTRAINT show_time_pkey PRIMARY KEY (id),
                                  CONSTRAINT ukhvv0asux57xe09moe2fq4ac6i UNIQUE (show_time)
);


-- public.theater definition

-- Drop table

-- DROP TABLE public.theater;

CREATE TABLE public.theater (
                                id uuid NOT NULL,
                                theater_name varchar(255) NOT NULL,
                                total_seats int4 NULL,
                                CONSTRAINT theater_pkey PRIMARY KEY (id),
                                CONSTRAINT uk6qewdxxylk4d7qtqjby3qt07x UNIQUE (theater_name)
);


-- public.users definition

-- Drop table

-- DROP TABLE public.users;

CREATE TABLE public.users (
                              id uuid NOT NULL,
                              age int4 NOT NULL,
                              email varchar(255) NOT NULL,
                              gender varchar(255) NOT NULL,
                              mobile varchar(255) NOT NULL,
                              username varchar(255) NOT NULL,
                              CONSTRAINT uk63cf888pmqtt5tipcne79xsbm UNIQUE (mobile),
                              CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email),
                              CONSTRAINT users_gender_check CHECK (((gender)::text = ANY ((ARRAY['MALE'::character varying, 'FEMALE'::character varying, 'OTHER'::character varying])::text[]))),
	CONSTRAINT users_pkey PRIMARY KEY (id)
);


-- public.movie_show_details definition

-- Drop table

-- DROP TABLE public.movie_show_details;

CREATE TABLE public.movie_show_details (
                                           id uuid NOT NULL,
                                           available_seats int4 NULL,
                                           movie_id uuid NULL,
                                           show_time_id uuid NULL,
                                           theater_id uuid NULL,
                                           CONSTRAINT movie_show_details_pkey PRIMARY KEY (id),
                                           CONSTRAINT fk479k16jtd92m4hujo8kg1h8tj FOREIGN KEY (movie_id) REFERENCES public.movies(id) ON DELETE CASCADE ,
                                           CONSTRAINT fkj6bb29alhakd0djxrfu4b1h0 FOREIGN KEY (show_time_id) REFERENCES public.show_time(id) ON DELETE CASCADE,
                                           CONSTRAINT fkjuoa5c6ph7f0iudowxu034b2j FOREIGN KEY (theater_id) REFERENCES public.theater(id) ON DELETE CASCADE
);


-- public.booking definition

-- Drop table

-- DROP TABLE public.booking;

CREATE TABLE public.booking (
                                id uuid NOT NULL,
                                email varchar(255) NULL,
                                is_paid bool NOT NULL,
                                seat_count int4 NULL,
                                total_amount numeric(38, 2) NULL,
                                movie_entity_id uuid NULL,
                                movie_show_details_entity_id uuid NULL,
                                show_time_entity_id uuid NULL,
                                theater_entity_id uuid NULL,
                                CONSTRAINT booking_pkey PRIMARY KEY (id),
                                CONSTRAINT fk2d3x1k7gmenpl35j4h3kbqed FOREIGN KEY (theater_entity_id) REFERENCES public.theater(id),
                                CONSTRAINT fk8xx3scsi5nrq21a3uxbvgktwp FOREIGN KEY (show_time_entity_id) REFERENCES public.show_time(id),
                                CONSTRAINT fkct6sf2kdvw44606k7cd7cg7p1 FOREIGN KEY (movie_entity_id) REFERENCES public.movies(id),
                                CONSTRAINT fkepqkb0re835cmo15mpyextt0y FOREIGN KEY (movie_show_details_entity_id) REFERENCES public.movie_show_details(id)
);