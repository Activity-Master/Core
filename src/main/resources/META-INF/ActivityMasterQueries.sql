use FSDM;

select * from Classification.Classification where ClassificationName = 'Grader'

select * from Classification.ClassificationDataConcept


select * from Classification.Classification where ClassificationName = 'ComPortNumber'
select * from Classification.Classification where ClassificationID =  '943CF5D0-12DC-411A-890D-3CFCB75C50AC'

select * from Classification.Classification where ClassificationID =  'AE0F749A-07DD-45EC-8442-F7E92E911582'


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
select * from Party.InvolvedPartyIdentificationType
select * from Party.InvolvedPartyType
select * from Party.InvolvedPartyXResourceItem




select * from Resource.ResourceItemXResourceItem



select *  
from Resource.ResourceItemXClassification  
where ClassificationID = 'E07BEB3D-AF8E-404F-9BD0-A1116039D1C2' 




select * from Resource.ResourceItemXResourceItem



select * from Arrangement.Arrangement



select * from Arrangement.ArrangementType
select * from Arrangement.ArrangementXArrangement
select * from Arrangement.ArrangementXArrangementType
select * from Arrangement.ArrangementXInvolvedParty
select * from Arrangement.ArrangementXResourceItem




select * from Arrangement.ArrangementXClassification;





select * from Product.ProductXProductType






select * from LU_Classifications




select rt.RulesTypeName, c.ClassificationName, c.ClassificationDesc, * from Rules.RulesXRulesType rxt
	inner join Classification.Classification c
		on rxt.ClassificationID = c.ClassificationID
	inner join Rules.RulesType rt
		on rxt.RulesTypeID = rt.RulesTypeID

		where rxt.RulesID = '3e64dbe8-247f-4836-8974-27a426fec09e'
		and c.ClassificationID = '557456d7-7e1d-434a-a28c-e04b596092a4'

		