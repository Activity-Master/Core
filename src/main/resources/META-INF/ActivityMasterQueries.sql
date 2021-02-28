use FSDM;

select * from Enterprise (nolock);
select * from ActiveFlag (nolock);
select * from Systems (nolock);

select * from 
Classification.ClassificationDataConcept (nolock)
order by ClassificationDataConceptName

select * from Classification.Classification (nolock)
where ClassificationName = 'UpdateClass'

select * from EnterpriseXClassification (nolock)


select * from Security.SecurityToken (nolock);
select * from SecurityHierarchyView;

select * from [dbo].[ActiveFlagSecurityToken] (nolock);
select * from [Address].AddressSecurityToken (nolock);

select * from Classification.Classification where ClassificationName = 'Grader'


select * from Time.Months;


select * from Classification.Classification where ClassificationName = 'Title'
select * from Classification.Classification where ClassificationID =  '943CF5D0-12DC-411A-890D-3CFCB75C50AC'

select * from Classification.Classification where ClassificationID =  'ABA1F0C4-DC3A-480F-8936-12BFFCF60E1A'


select * from Product.Product
select * from Product.ProductType
select * from Product.ProductTypeXClassification
select * from Product.ProductXResourceItem
select * from Product.ProductXProductType


select * from Resource.ResourceItem
select * from Resource.ResourceItemXClassification
select * from Resource.ResourceItemXResourceItemType
select * from Resource.ResourceItemData
select * from Resource.ResourceItemType




select * from Rules.Rules
select * from Rules.RulesType
select * from Rules.RulesXRulesType
select * from Rules.RulesXClassification
select * from Rules.RulesTypeXClassification
select * from Rules.RulesTypeXResourceItem
select * from Rules.RulesXRules




select * from Security.SecurityToken

select * from Rules.Rules where RulesID = '425563D0-6DF8-486F-9FCD-1772ABA3E014'
select * from Rules.Rules where RulesID = '481A5717-0F93-486C-8791-9629818153A7'




select * from Party.InvolvedPartyXInvolvedParty


select * from Party.InvolvedParty
select * from Party.InvolvedPartyXInvolvedPartyIdentificationType
select * from Party.InvolvedPartyIdentificationType
select * from Party.InvolvedPartyType
select * from Party.InvolvedPartyNameType
select * from Party.InvolvedPartyXResourceItem



select * from Event.Event
select * from Event.EventXClassification


select * from Resource.ResourceItemXResourceItem



select *  
from Resource.ResourceItemXClassification  
where ClassificationID = 'E07BEB3D-AF8E-404F-9BD0-A1116039D1C2' 




select * from Resource.ResourceItemXResourceItem



select * from Arrangement.Arrangement


select * from Arrangement.ArrangementXArrangementType

select * from Arrangement.ArrangementType
select * from Arrangement.ArrangementXArrangement
select * from Arrangement.ArrangementXClassification
select * from Arrangement.ArrangementXArrangementType
select * from Arrangement.ArrangementXInvolvedParty
select * from Arrangement.ArrangementXResourceItem




select * from Arrangement.ArrangementXClassification;



select * 
from Arrangement.ArrangementXClassification a


select at.ArrangementTypeName, c.ClassificationName,a.value,atx.value,* 
from Arrangement.ArrangementXResourceItem a
	inner join Classification.Classification c
		on a.ClassificationID = c.ClassificationID
	inner join Arrangement.Arrangement ar
		on a.ArrangementID = ar.ArrangementID
	inner join Arrangement.ArrangementXArrangementType atx
		on ar.ArrangementID = atx.ArrangementID
	inner join Arrangement.ArrangementType at
		on atx.ArrangementTypeID = at.ArrangementTypeID
		




select at.ArrangementTypeName, c.ClassificationName,a.value,atx.value,* 
from Arrangement.ArrangementXArrangement a
	inner join Classification.Classification c
		on a.ClassificationID = c.ClassificationID
	inner join Arrangement.Arrangement ar
		on a.ParentArrangementID = ar.ArrangementID
	inner join Arrangement.ArrangementXArrangementType atx
		on ar.ArrangementID = atx.ArrangementID
	inner join Arrangement.ArrangementType at
		on atx.ArrangementTypeID = at.ArrangementTypeID





select at.ArrangementTypeName, c.ClassificationName,a.value,atx.value,  a.EffectiveFromDate , a.EffectiveToDate ,  * 
from Arrangement.ArrangementXClassification a
	inner join Classification.Classification c
		on a.ClassificationID = c.ClassificationID
	inner join Arrangement.Arrangement ar
		on a.ArrangementID = ar.ArrangementID
	inner join Arrangement.ArrangementXArrangementType atx
		on ar.ArrangementID = atx.ArrangementID
	inner join Arrangement.ArrangementType at
		on atx.ArrangementTypeID = at.ArrangementTypeID
where
c.ClassificationName = 'SessionMeasurementP1'


and a.EffectiveToDate >= getDate()

--at.ArrangementTypeName = 'PackingSessionGrader'
--and
a.ArrangementID = 'f4548bfd-1e2b-41d6-be7d-0e44404ca551'






select * from Product.ProductXProductType






select * from LU_Classifications




select rt.RulesTypeName, c.ClassificationName, c.ClassificationDesc, * from Rules.RulesXRulesType rxt
	inner join Classification.Classification c
		on rxt.ClassificationID = c.ClassificationID
	inner join Rules.RulesType rt
		on rxt.RulesTypeID = rt.RulesTypeID

		where rxt.RulesID = '3e64dbe8-247f-4836-8974-27a426fec09e'
		and c.ClassificationID = '557456d7-7e1d-434a-a28c-e04b596092a4'

		



	select * from Geography.Geography