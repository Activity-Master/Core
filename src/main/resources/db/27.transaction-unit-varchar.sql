-- Align existing and fresh databases with Transaction.unit.
-- Preserve update 16 and its recorded checksum. The existing unit-code
-- constraint limits valid values to 16 characters; no truncating cast is used.
ALTER TABLE transactions.entry
    ALTER COLUMN unit TYPE varchar(255);
