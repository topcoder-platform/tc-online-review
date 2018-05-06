-- Structure for the id_sequences table

CREATE TABLE id_sequences (
    name varchar(255) NOT NULL,
    next_block_start decimal(20,0) NOT NULL,
    block_size decimal(11,0) NOT NULL,
    exhausted decimal(11,0) NOT NULL default '0',
    PRIMARY KEY (name)
);

-- Data for the id_sequences table

INSERT INTO id_sequences (name, next_block_start, block_size, exhausted) VALUES
    ('unit_test_id_sequence', 0, 10, 0);
