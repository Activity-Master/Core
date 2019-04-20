CREATE view [dbo].[SecurityHierarchyView] AS
  with tbl AS
    (
    select st.SecurityTokenID           'id'
         , st.SecurityTokenFriendlyName 'name'
         , sxs.ParentSecurityTokenID    'ParentID'
    from SecurityTokenXSecurityToken sxs (nolock)
           right outer join SecurityToken st (nolock)
                            on sxs.ChildSecurityTokenID = st.SecurityTokenID
           left join SecurityToken stChild (nolock)
                     on sxs.ChildSecurityTokenID = stChild.SecurityTokenID
    ),
    SecurityPath
      AS (
      -- anchor
      SELECT id,
             [Name],
             ParentID,
             CAST(([Name]) AS VARCHAR(1000)) AS "Pather",
             CAST((id) AS VARCHAR(1000))     AS "Path"
      FROM tbl
      WHERE ParentId IS NULL
      UNION ALL
      --recursive member
      SELECT t.id,
             t.[Name],
             t.ParentID,
             CAST((a.path + '/' + t.Name) AS VARCHAR(1000))                       AS "Path",
             CAST((a."Path" + '/' + cast(t.id as varchar(max))) AS VARCHAR(1000)) AS "PathCrypt"
      FROM tbl AS t
             JOIN SecurityPath AS a
                  ON t.ParentId = a.id
      )
    SELECT *
    FROM SecurityPath

GO


