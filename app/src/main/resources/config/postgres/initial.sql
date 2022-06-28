CREATE TABLE users (
                              id int8 NOT NULL,
                              username varchar NOT NULL,
                              "password" varchar NOT NULL,
                              created_at timestamp NOT NULL,
                              CONSTRAINT users_pk null
);

CREATE TABLE posts (
                              id int8 NOT NULL,
                              message varchar NOT NULL,
                              created_at timestamp NOT NULL,
                              user_id int8 NOT NULL,
                              CONSTRAINT posts_pk PRIMARY KEY (id),
                              CONSTRAINT posts_fk FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE ON UPDATE CASCADE
);
