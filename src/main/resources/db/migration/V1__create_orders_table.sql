create table if not exists orders (
                                      order_id     varchar(100)   not null primary key,
    customer_id  varchar(100)   not null,
    product_code varchar(50)    not null,
    quantity     integer        not null,
    total_amount numeric(15, 2) not null,
    currency     char(3)        not null default 'IDR',
    status       varchar(20)    not null,
    processed_at timestamptz    not null
    );