-- Match TransactionType.code without changing checksummed historical scripts.
-- The existing code constraint limits values to 80 characters. Keep that
-- constraint and all data; no truncating cast or USING expression is needed.
ALTER TABLE transactions.transaction_type
    ALTER COLUMN code TYPE varchar(255);
