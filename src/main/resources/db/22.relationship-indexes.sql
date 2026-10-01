-- Fresh schemas have no FKs to enumerate. Existing baselined schemas may skip
-- corrected 16/19, so explicitly install their new relationship indexes too.
CREATE INDEX IF NOT EXISTS am_party_identification_addressid ON party.involvedpartyxinvolvedpartyidentificationtype(addressid);
CREATE INDEX IF NOT EXISTS am_party_identification_addresstypeid ON party.involvedpartyxinvolvedpartyidentificationtype(addresstypeid);
CREATE INDEX IF NOT EXISTS am_address_addresstypeid ON address.address(addresstypeid);
CREATE INDEX IF NOT EXISTS am_address_component_owner ON address.addressxaddress(addressid);
CREATE INDEX IF NOT EXISTS am_address_component_target ON address.addressxaddress(componentaddressid);
CREATE INDEX IF NOT EXISTS am_address_type_security_owner ON address.addresstypesecuritytoken(addresstypeid);
CREATE INDEX IF NOT EXISTS am_address_component_security_owner ON address.addressxaddresssecuritytoken(addressxaddressid);
CREATE INDEX IF NOT EXISTS transaction_entry_type_direction ON transactions.entry(transaction_type_id, direction);
CREATE INDEX IF NOT EXISTS transaction_type_security_owner ON transactions.transaction_x_transaction_type_security_token(transaction_x_transaction_type_id);

-- Index-only relationship policy, including installations that applied the old
-- versions of 16/19 or generated constraints through Hibernate. Preserve primary
-- keys, unique constraints and checks. Never alter tables outside FSDM schemas.
-- Ensure a valid leading-column B-tree index exists BEFORE dropping each FK.
DO $fsdm_relationship_indexes$
DECLARE
    link record;
    columns_sql text;
BEGIN
    FOR link IN
        SELECT c.oid, c.conname, c.conrelid, c.conkey, n.nspname, t.relname
        FROM pg_constraint c
        JOIN pg_class t ON t.oid = c.conrelid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE c.contype = 'f'
          AND c.conparentid = 0
          AND n.nspname IN ('dbo', 'address', 'arrangement', 'classification',
              'event', 'geography', 'party', 'product', 'resource', 'rules',
              'security', 'time', 'transactions')
        ORDER BY n.nspname, t.relname, c.conname
    LOOP
        IF NOT EXISTS (
            SELECT 1 FROM pg_index i
            JOIN pg_class index_table ON index_table.oid = i.indexrelid
            JOIN pg_am access_method ON access_method.oid = index_table.relam
            WHERE i.indrelid = link.conrelid
              AND i.indisvalid AND i.indisready
              AND i.indpred IS NULL AND i.indexprs IS NULL
              AND access_method.amname = 'btree'
              AND ARRAY(SELECT column_id FROM unnest(i.indkey) WITH ORDINALITY AS k(column_id, ordinal)
                        WHERE ordinal <= cardinality(link.conkey) ORDER BY ordinal) = link.conkey
        ) THEN
            SELECT string_agg(format('%I', a.attname), ', ' ORDER BY k.ordinal)
            INTO columns_sql
            FROM unnest(link.conkey) WITH ORDINALITY AS k(column_id, ordinal)
            JOIN pg_attribute a ON a.attrelid = link.conrelid AND a.attnum = k.column_id;
            EXECUTE format('CREATE INDEX IF NOT EXISTS %I ON %I.%I (%s)',
                'idx_fsdm_join_' || substr(md5(link.nspname || '.' || link.relname || ':' || columns_sql), 1, 24),
                link.nspname, link.relname, columns_sql);
        END IF;
        EXECUTE format('ALTER TABLE %I.%I DROP CONSTRAINT %I', link.nspname, link.relname, link.conname);
    END LOOP;
END;
$fsdm_relationship_indexes$;

