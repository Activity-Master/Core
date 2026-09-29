SET search_path TO address,arrangement,classification,"dbo","event",geography,party,product,resource,rules,"security","time";


CREATE OR REPLACE FUNCTION public.create_event_types_view(event_type_desc TEXT)
    RETURNS VOID AS
$$
DECLARE
    view_name TEXT;
    query     TEXT;
BEGIN
    -- Generate the view name dynamically by replacing spaces with underscores and appending '_received_barcodes'
    view_name := LOWER(REPLACE(event_type_desc, ' ', '_'));

    -- Build the query to create or replace the view
    query := FORMAT($f$
        CREATE OR REPLACE VIEW public.%I AS
        SELECT DISTINCT exet.value
        FROM event.event e
                 JOIN event.eventxeventtype exet ON e.eventid::text = exet.eventid::text
                 JOIN event.eventtype et ON et.eventtypeid::text = exet.eventtypeid::text
        WHERE et.eventtypedesc::text = %L
    $f$, view_name, event_type_desc);

    -- Execute the query
    EXECUTE query;

    RAISE NOTICE 'View % created successfully.', view_name;
END;
$$ LANGUAGE plpgsql;

