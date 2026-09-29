CREATE TABLE tb_currency (
                             id BIGSERIAL PRIMARY KEY,
                             source_currency CHAR(3) NOT NULL,
                             target_currency CHAR(3) NOT NULL,
                             conversion_rate DECIMAL(10,2) NOT NULL
);