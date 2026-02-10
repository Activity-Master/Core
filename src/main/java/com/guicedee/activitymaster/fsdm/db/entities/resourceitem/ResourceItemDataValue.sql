
drop table if exists resource.resourceitemdatavalue;
-- payload table (LOGGED because you cannot lose data)
CREATE TABLE IF NOT EXISTS resource.resourceitemdatavalue
(
    resourceitemdatavalueid uuid NOT NULL,          -- == resourceitemid
    resourceitemdatavalue   bytea NULL,             -- optional payload
    CONSTRAINT resourceitemdatavalue_pkey PRIMARY KEY (resourceitemdatavalueid)
);

ALTER TABLE resource.resourceitemdatavalue
  ALTER COLUMN resourceitemdatavalue SET STORAGE EXTENDED;

ALTER TABLE resource.resourceitemdatavalue
  ALTER COLUMN resourceitemdatavalue SET COMPRESSION lz4;

DO
$$
DECLARE
  missing_payload_rows bigint;
  orphan_payload_rows  bigint;
  data_without_item    bigint;
BEGIN
  -- 1) link column on data (optional)
  IF NOT EXISTS (
      SELECT 1
      FROM information_schema.columns
      WHERE table_schema='resource'
        AND table_name='resourceitemdata'
        AND column_name='resourceitemdatavalueid'
  ) THEN
    ALTER TABLE resource.resourceitemdata
      ADD COLUMN resourceitemdatavalueid uuid;
  END IF;

  -- 2) copy payloads for rows that actually have payload
  INSERT INTO resource.resourceitemdatavalue (resourceitemdatavalueid, resourceitemdatavalue)
  SELECT d.resourceitemid, d.resourceitemdata
  FROM resource.resourceitemdata d
  WHERE d.resourceitemdata IS NOT NULL
  ON CONFLICT (resourceitemdatavalueid) DO NOTHING;

  -- 3) set the link only where payload exists
  UPDATE resource.resourceitemdata d
  SET resourceitemdatavalueid = d.resourceitemid
  WHERE d.resourceitemdata IS NOT NULL
    AND d.resourceitemdatavalueid IS DISTINCT FROM d.resourceitemid;

END
$$;

-- autovac + toast tuning (payload churn table)
ALTER TABLE resource.resourceitemdatavalue SET (
  autovacuum_vacuum_scale_factor = 0.005,
  autovacuum_analyze_scale_factor = 0.005,
  autovacuum_vacuum_threshold = 2000,
  autovacuum_analyze_threshold = 2000,
  autovacuum_vacuum_cost_limit = 10000,
  toast_tuple_target = 2048
);

CREATE INDEX IF NOT EXISTS resourceitemdata_resourceitemdatavalueid_notnull_idx
ON resource.resourceitemdata (resourceitemdatavalueid)
WHERE resourceitemdatavalueid IS NOT NULL;



select count(*) from resource.resourceitemdatavalue; 

select count(*) from resource.resourceitemdatavalue d where d.resourceitemdatavalue is null; 

select count(*) from resource.resourceitemdata;
select * from resource.resourceitem where resourceitemid = 'b8eb4da4-b596-4c92-8979-a2304950571a'; 
select * from resource.resourceitemdata where resourceitemid = 'b8eb4da4-b596-4c92-8979-a2304950571a'; 
select * from resource.resourceitemdatavalue where resourceitemdatavalueid = 'b8eb4da4-b596-4c92-8979-a2304950571a';


--alter table resource.resourceitemdata 	drop COLUMN resourceitemdata;


