-- Keep previously applied creation scripts immutable. Reject oversized values rather than truncate them.
ALTER TABLE transactions.transaction_type ALTER COLUMN description TYPE varchar(255);
