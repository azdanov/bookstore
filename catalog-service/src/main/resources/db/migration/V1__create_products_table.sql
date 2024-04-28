create sequence product_id_seq start with 1 increment by 50;

create table products
(
    id          bigint                   default nextval('product_id_seq') not null,
    code        text                                                       not null unique,
    name        text                                                       not null,
    description text,
    image_url   text,
    price       numeric                                                    not null check (price >= 0),
    created_at  timestamp with time zone default current_timestamp         not null,
    updated_at  timestamp with time zone,
    primary key (id)
);
