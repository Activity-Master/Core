DROP PROCEDURE [dbo].[GetDatabaseDictionaryForTable]
GO
DROP PROCEDURE [dbo].[GetDatabaseDictionaryForAllTables]
GO
DROP PROCEDURE [dbo].[GetDatabaseDictionary]
GO
ALTER TABLE [dbo].[YesNoXClassificationSecurityToken] DROP CONSTRAINT [FK_YesNoXClassificationSecurityTokens_YesNoXClassification]
GO
ALTER TABLE [dbo].[YesNoXClassificationSecurityToken] DROP CONSTRAINT [FK_YesNoXClassificationSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[YesNoXClassificationSecurityToken] DROP CONSTRAINT [FK_YesNoXClassificationSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[YesNoXClassificationSecurityToken] DROP CONSTRAINT [FK_YesNoXClassificationSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[YesNoXClassificationSecurityToken] DROP CONSTRAINT [FK_YesNoXClassificationSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[YesNoXClassificationSecurityToken] DROP CONSTRAINT [FK__YesNoXClassificationSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[YesNoXClassification] DROP CONSTRAINT [FK_YesNoXClassification_YesNo]
GO
ALTER TABLE [dbo].[YesNoXClassification] DROP CONSTRAINT [FK_YesNoXClassification_Systems]
GO
ALTER TABLE [dbo].[YesNoXClassification] DROP CONSTRAINT [FK_YesNoXClassification_Enterprise]
GO
ALTER TABLE [dbo].[YesNoXClassification] DROP CONSTRAINT [FK_YesNoXClassification_ClassificationXType]
GO
ALTER TABLE [dbo].[YesNoXClassification] DROP CONSTRAINT [FK_YesNoXClassification_ActiveFlags]
GO
ALTER TABLE [dbo].[YesNoSecurityToken] DROP CONSTRAINT [FK_YesNoSecurityTokens_YesNo]
GO
ALTER TABLE [dbo].[YesNoSecurityToken] DROP CONSTRAINT [FK_YesNoSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[YesNoSecurityToken] DROP CONSTRAINT [FK_YesNoSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[YesNoSecurityToken] DROP CONSTRAINT [FK_YesNoSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[YesNoSecurityToken] DROP CONSTRAINT [FK_YesNoSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[YesNoSecurityToken] DROP CONSTRAINT [FK__YesNoSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[YesNo] DROP CONSTRAINT [FK_YesNo_Systems]
GO
ALTER TABLE [dbo].[YesNo] DROP CONSTRAINT [FK_YesNo_ActiveFlags]
GO
ALTER TABLE [dbo].[YesNo] DROP CONSTRAINT [FK__YesNo__SystemID__3B6BB5BF]
GO
ALTER TABLE [dbo].[YesNo] DROP CONSTRAINT [FK__YesNo__Enterpris__3A779186]
GO
ALTER TABLE [dbo].[SystemXClassificationSecurityToken] DROP CONSTRAINT [FK_SystemXClassificationSecurityTokens_SystemXClassification]
GO
ALTER TABLE [dbo].[SystemXClassificationSecurityToken] DROP CONSTRAINT [FK_SystemXClassificationSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[SystemXClassificationSecurityToken] DROP CONSTRAINT [FK_SystemXClassificationSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[SystemXClassificationSecurityToken] DROP CONSTRAINT [FK_SystemXClassificationSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[SystemXClassificationSecurityToken] DROP CONSTRAINT [FK_SystemXClassificationSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[SystemXClassificationSecurityToken] DROP CONSTRAINT [FK__SystemXClassificationSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[SystemXClassification] DROP CONSTRAINT [FK_SystemXClassification_Systems]
GO
ALTER TABLE [dbo].[SystemXClassification] DROP CONSTRAINT [FK_SystemXClassification_System]
GO
ALTER TABLE [dbo].[SystemXClassification] DROP CONSTRAINT [FK_SystemXClassification_Enterprise]
GO
ALTER TABLE [dbo].[SystemXClassification] DROP CONSTRAINT [FK_SystemXClassification_ClassificationXType]
GO
ALTER TABLE [dbo].[SystemXClassification] DROP CONSTRAINT [FK_SystemXClassification_ActiveFlags]
GO
ALTER TABLE [dbo].[SystemsSecurityToken] DROP CONSTRAINT [FK_SystemsSecurityTokens_System]
GO
ALTER TABLE [dbo].[SystemsSecurityToken] DROP CONSTRAINT [FK_SystemsSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[SystemsSecurityToken] DROP CONSTRAINT [FK_SystemsSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[SystemsSecurityToken] DROP CONSTRAINT [FK_SystemsSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[SystemsSecurityToken] DROP CONSTRAINT [FK_SystemSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[SystemsSecurityToken] DROP CONSTRAINT [FK_SystemSecurityTokens_System]
GO
ALTER TABLE [dbo].[SystemsSecurityToken] DROP CONSTRAINT [FK_SystemSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[SystemsSecurityToken] DROP CONSTRAINT [FK_SystemSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[SystemsSecurityToken] DROP CONSTRAINT [FK__SystemsSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[SystemsSecurityToken] DROP CONSTRAINT [FK__SystemSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[Systems] DROP CONSTRAINT [FK_Systems_Enterprise]
GO
ALTER TABLE [dbo].[Systems] DROP CONSTRAINT [FK_Systems_ActiveFlags]
GO
ALTER TABLE [dbo].[SecurityTokenXSecurityToken] DROP CONSTRAINT [FK_SecurityTokenXSecurityTokenID_SecurityTokens1]
GO
ALTER TABLE [dbo].[SecurityTokenXSecurityToken] DROP CONSTRAINT [FK_SecurityTokenXSecurityTokenID_SecurityTokens]
GO
ALTER TABLE [dbo].[SecurityTokenXSecurityToken] DROP CONSTRAINT [FK_SecurityTokenXSecurityTokenID_ActiveFlags]
GO
ALTER TABLE [dbo].[SecurityTokenXSecurityToken] DROP CONSTRAINT [FK_SecurityTokenXSecurityToken_System]
GO
ALTER TABLE [dbo].[SecurityTokenXSecurityToken] DROP CONSTRAINT [FK_SecurityTokenXSecurityToken_Classification]
GO
ALTER TABLE [dbo].[SecurityTokenXSecurityToken] DROP CONSTRAINT [FK__SecurityT__Syste__24885067]
GO
ALTER TABLE [dbo].[SecurityTokenXSecurityToken] DROP CONSTRAINT [FK__SecurityT__Enter__23942C2E]
GO
ALTER TABLE [dbo].[SecurityTokenXClassificationSecurityToken] DROP CONSTRAINT [FK_SecurityTokenXClassificationSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[SecurityTokenXClassificationSecurityToken] DROP CONSTRAINT [FK_SecurityTokenXClassificationSecurityTokens_SecurityTokenXClassification]
GO
ALTER TABLE [dbo].[SecurityTokenXClassificationSecurityToken] DROP CONSTRAINT [FK_SecurityTokenXClassificationSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[SecurityTokenXClassificationSecurityToken] DROP CONSTRAINT [FK_SecurityTokenXClassificationSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[SecurityTokenXClassificationSecurityToken] DROP CONSTRAINT [FK_SecurityTokenXClassificationSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[SecurityTokenXClassificationSecurityToken] DROP CONSTRAINT [FK__SecurityTokenXClassificationSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[SecurityTokenXClassification] DROP CONSTRAINT [FK_SecurityTokenXClassification_Systems1]
GO
ALTER TABLE [dbo].[SecurityTokenXClassification] DROP CONSTRAINT [FK_SecurityTokenXClassification_Systems]
GO
ALTER TABLE [dbo].[SecurityTokenXClassification] DROP CONSTRAINT [FK_SecurityTokenXClassification_System]
GO
ALTER TABLE [dbo].[SecurityTokenXClassification] DROP CONSTRAINT [FK_SecurityTokenXClassification_SecurityToken]
GO
ALTER TABLE [dbo].[SecurityTokenXClassification] DROP CONSTRAINT [FK_SecurityTokenXClassification_Enterprise]
GO
ALTER TABLE [dbo].[SecurityTokenXClassification] DROP CONSTRAINT [FK_SecurityTokenXClassification_ClassificationXType]
GO
ALTER TABLE [dbo].[SecurityTokenXClassification] DROP CONSTRAINT [FK_SecurityTokenXClassification_ActiveFlags]
GO
ALTER TABLE [dbo].[SecurityTokensSecurityToken] DROP CONSTRAINT [FK_SecurityTokensSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[SecurityTokensSecurityToken] DROP CONSTRAINT [FK_SecurityTokenSecurityTokensSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[SecurityTokensSecurityToken] DROP CONSTRAINT [FK_SecurityTokenSecurityTokensSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[SecurityTokensSecurityToken] DROP CONSTRAINT [FK_SecurityTokenSecurityTokensSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[SecurityTokensSecurityToken] DROP CONSTRAINT [FK_SecurityTokenAccess_SecurityToken]
GO
ALTER TABLE [dbo].[SecurityTokensSecurityToken] DROP CONSTRAINT [FK__SecurityTokenSecurityTokensSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[SecurityToken] DROP CONSTRAINT [FK_SecurityTokens_Classification]
GO
ALTER TABLE [dbo].[SecurityToken] DROP CONSTRAINT [FK_SecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[SecurityToken] DROP CONSTRAINT [FK_SecurityToken_System]
GO
ALTER TABLE [dbo].[SecurityToken] DROP CONSTRAINT [FK__SecurityT__Syste__135DC465]
GO
ALTER TABLE [dbo].[SecurityToken] DROP CONSTRAINT [FK__SecurityT__Enter__1269A02C]
GO
ALTER TABLE [dbo].[ResourceItemXResourceItemTypeSecurityToken] DROP CONSTRAINT [FK_ResourceItemXResourceItemTypeSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ResourceItemXResourceItemTypeSecurityToken] DROP CONSTRAINT [FK_ResourceItemXResourceItemTypeSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[ResourceItemXResourceItemTypeSecurityToken] DROP CONSTRAINT [FK_ResourceItemXResourceItemTypeSecurityTokens_ResourceItemXResourceItemType]
GO
ALTER TABLE [dbo].[ResourceItemXResourceItemTypeSecurityToken] DROP CONSTRAINT [FK_ResourceItemXResourceItemTypeSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ResourceItemXResourceItemTypeSecurityToken] DROP CONSTRAINT [FK_ResourceItemXResourceItemTypeSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ResourceItemXResourceItemTypeSecurityToken] DROP CONSTRAINT [FK__ResourceItemXResourceItemTypeSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[ResourceItemXResourceItemType] DROP CONSTRAINT [FK_ResourceItemXResourceItemType_Systems]
GO
ALTER TABLE [dbo].[ResourceItemXResourceItemType] DROP CONSTRAINT [FK_ResourceItemXResourceItemType_ResourceItemType]
GO
ALTER TABLE [dbo].[ResourceItemXResourceItemType] DROP CONSTRAINT [FK_ResourceItemXResourceItemType_ResourceItem]
GO
ALTER TABLE [dbo].[ResourceItemXResourceItemType] DROP CONSTRAINT [FK_ResourceItemXResourceItemType_ActiveFlags]
GO
ALTER TABLE [dbo].[ResourceItemXResourceItemType] DROP CONSTRAINT [FK__ResourceItemKey__Syste__1B33F057]
GO
ALTER TABLE [dbo].[ResourceItemXResourceItemType] DROP CONSTRAINT [FK__ResourceItemKey__Enter__1A3FCC1E]
GO
ALTER TABLE [dbo].[ResourceItemXClassificationSecurityToken] DROP CONSTRAINT [FK_ResourceItemXClassificationSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ResourceItemXClassificationSecurityToken] DROP CONSTRAINT [FK_ResourceItemXClassificationSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[ResourceItemXClassificationSecurityToken] DROP CONSTRAINT [FK_ResourceItemXClassificationSecurityTokens_ResourceItemXClassification]
GO
ALTER TABLE [dbo].[ResourceItemXClassificationSecurityToken] DROP CONSTRAINT [FK_ResourceItemXClassificationSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ResourceItemXClassificationSecurityToken] DROP CONSTRAINT [FK_ResourceItemXClassificationSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ResourceItemXClassificationSecurityToken] DROP CONSTRAINT [FK__ResourceItemXClassificationSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[ResourceItemXClassification] DROP CONSTRAINT [FK_ResourceItemXClassification_Systems]
GO
ALTER TABLE [dbo].[ResourceItemXClassification] DROP CONSTRAINT [FK_ResourceItemXClassification_ResourceItem]
GO
ALTER TABLE [dbo].[ResourceItemXClassification] DROP CONSTRAINT [FK_ResourceItemXClassification_ClassificationXType]
GO
ALTER TABLE [dbo].[ResourceItemXClassification] DROP CONSTRAINT [FK_ResourceItemXClassification_ActiveFlags]
GO
ALTER TABLE [dbo].[ResourceItemXClassification] DROP CONSTRAINT [FK__ResourceI__Syste__2784B8A3]
GO
ALTER TABLE [dbo].[ResourceItemXClassification] DROP CONSTRAINT [FK__ResourceI__Syste__2235F3A1]
GO
ALTER TABLE [dbo].[ResourceItemXClassification] DROP CONSTRAINT [FK__ResourceI__Enter__2690946A]
GO
ALTER TABLE [dbo].[ResourceItemXClassification] DROP CONSTRAINT [FK__ResourceI__Enter__2141CF68]
GO
ALTER TABLE [dbo].[ResourceItemTypeSecurityToken] DROP CONSTRAINT [FK_ResourceItemTypeSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ResourceItemTypeSecurityToken] DROP CONSTRAINT [FK_ResourceItemTypeSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[ResourceItemTypeSecurityToken] DROP CONSTRAINT [FK_ResourceItemTypeSecurityTokens_ResourceItemType]
GO
ALTER TABLE [dbo].[ResourceItemTypeSecurityToken] DROP CONSTRAINT [FK_ResourceItemTypeSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ResourceItemTypeSecurityToken] DROP CONSTRAINT [FK_ResourceItemTypeSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ResourceItemTypeSecurityToken] DROP CONSTRAINT [FK__ResourceItemTypeSecurityToken__ActiveFlag]
GO
ALTER TABLE [dbo].[ResourceItemType] DROP CONSTRAINT [FK_ResourceItemType_Systems1]
GO
ALTER TABLE [dbo].[ResourceItemType] DROP CONSTRAINT [FK_ResourceItemType_Systems]
GO
ALTER TABLE [dbo].[ResourceItemType] DROP CONSTRAINT [FK_ResourceItemType_Enterprise]
GO
ALTER TABLE [dbo].[ResourceItemType] DROP CONSTRAINT [FK_ResourceItemType_ActiveFlags]
GO
ALTER TABLE [dbo].[ResourceItemSecurityToken] DROP CONSTRAINT [FK_ResourceItemSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ResourceItemSecurityToken] DROP CONSTRAINT [FK_ResourceItemSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[ResourceItemSecurityToken] DROP CONSTRAINT [FK_ResourceItemSecurityTokens_ResourceItem]
GO
ALTER TABLE [dbo].[ResourceItemSecurityToken] DROP CONSTRAINT [FK_ResourceItemSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ResourceItemSecurityToken] DROP CONSTRAINT [FK_ResourceItemSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ResourceItemSecurityToken] DROP CONSTRAINT [FK__ResourceItemSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[ResourceItemDataXClassificationSecurityToken] DROP CONSTRAINT [FK_ResourceItemDataXClassificationSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ResourceItemDataXClassificationSecurityToken] DROP CONSTRAINT [FK_ResourceItemDataXClassificationSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[ResourceItemDataXClassificationSecurityToken] DROP CONSTRAINT [FK_ResourceItemDataXClassificationSecurityTokens_ResourceItemDataXClassification]
GO
ALTER TABLE [dbo].[ResourceItemDataXClassificationSecurityToken] DROP CONSTRAINT [FK_ResourceItemDataXClassificationSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ResourceItemDataXClassificationSecurityToken] DROP CONSTRAINT [FK_ResourceItemDataXClassificationSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ResourceItemDataXClassificationSecurityToken] DROP CONSTRAINT [FK__ResourceItemDataXClassificationSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[ResourceItemDataXClassification] DROP CONSTRAINT [FK_ResourceItemDataXClassification_Systems]
GO
ALTER TABLE [dbo].[ResourceItemDataXClassification] DROP CONSTRAINT [FK_ResourceItemDataXClassification_ResourceItemData]
GO
ALTER TABLE [dbo].[ResourceItemDataXClassification] DROP CONSTRAINT [FK_ResourceItemDataXClassification_ClassificationXType]
GO
ALTER TABLE [dbo].[ResourceItemDataXClassification] DROP CONSTRAINT [FK_ResourceItemDataXClassification_ActiveFlags]
GO
ALTER TABLE [dbo].[ResourceItemDataXClassification] DROP CONSTRAINT [FK__ResourceI__Syste__24A84BF8]
GO
ALTER TABLE [dbo].[ResourceItemDataXClassification] DROP CONSTRAINT [FK__ResourceI__Syste__0F231F2D]
GO
ALTER TABLE [dbo].[ResourceItemDataXClassification] DROP CONSTRAINT [FK__ResourceI__Enter__23B427BF]
GO
ALTER TABLE [dbo].[ResourceItemDataXClassification] DROP CONSTRAINT [FK__ResourceI__Enter__0D3AD6BB]
GO
ALTER TABLE [dbo].[ResourceItemDataSecurityToken] DROP CONSTRAINT [FK_ResourceItemDataSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ResourceItemDataSecurityToken] DROP CONSTRAINT [FK_ResourceItemDataSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[ResourceItemDataSecurityToken] DROP CONSTRAINT [FK_ResourceItemDataSecurityTokens_ResourceItemData]
GO
ALTER TABLE [dbo].[ResourceItemDataSecurityToken] DROP CONSTRAINT [FK_ResourceItemDataSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ResourceItemDataSecurityToken] DROP CONSTRAINT [FK_ResourceItemDataSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ResourceItemDataSecurityToken] DROP CONSTRAINT [FK__ResourceItemDataSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[ResourceItemData] DROP CONSTRAINT [FK_ResourceItemData_Systems]
GO
ALTER TABLE [dbo].[ResourceItemData] DROP CONSTRAINT [FK_ResourceItemData_ResourceItem]
GO
ALTER TABLE [dbo].[ResourceItemData] DROP CONSTRAINT [FK_ResourceItemData_ActiveFlags]
GO
ALTER TABLE [dbo].[ResourceItemData] DROP CONSTRAINT [FK__ResourceI__Syste__7FE0DB9D]
GO
ALTER TABLE [dbo].[ResourceItemData] DROP CONSTRAINT [FK__ResourceI__Syste__21CBDF4D]
GO
ALTER TABLE [dbo].[ResourceItemData] DROP CONSTRAINT [FK__ResourceI__Enter__7EECB764]
GO
ALTER TABLE [dbo].[ResourceItemData] DROP CONSTRAINT [FK__ResourceI__Enter__20D7BB14]
GO
ALTER TABLE [dbo].[ResourceItem] DROP CONSTRAINT [FK_ResourceItem_Systems]
GO
ALTER TABLE [dbo].[ResourceItem] DROP CONSTRAINT [FK_ResourceItem_Classification1]
GO
ALTER TABLE [dbo].[ResourceItem] DROP CONSTRAINT [FK_ResourceItem_ActiveFlags]
GO
ALTER TABLE [dbo].[ResourceItem] DROP CONSTRAINT [FK__ResourceI__Syste__7933DE0E]
GO
ALTER TABLE [dbo].[ResourceItem] DROP CONSTRAINT [FK__ResourceI__Syste__1FE396DB]
GO
ALTER TABLE [dbo].[ResourceItem] DROP CONSTRAINT [FK__ResourceI__Enter__783FB9D5]
GO
ALTER TABLE [dbo].[ResourceItem] DROP CONSTRAINT [FK__ResourceI__Enter__1EEF72A2]
GO
ALTER TABLE [dbo].[ProductXResourceItemSecurityToken] DROP CONSTRAINT [FK_ProductXResourceItemSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ProductXResourceItemSecurityToken] DROP CONSTRAINT [FK_ProductXResourceItemSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[ProductXResourceItemSecurityToken] DROP CONSTRAINT [FK_ProductXResourceItemSecurityTokens_ProductXResourceItem]
GO
ALTER TABLE [dbo].[ProductXResourceItemSecurityToken] DROP CONSTRAINT [FK_ProductXResourceItemSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ProductXResourceItemSecurityToken] DROP CONSTRAINT [FK__ProductXResourceItemSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[ProductXResourceItemSecurityToken] DROP CONSTRAINT [FK__ProductXR__Enter__1BF1A582]
GO
ALTER TABLE [dbo].[ProductXResourceItem] DROP CONSTRAINT [FK_ProductXResourceItem_Systems]
GO
ALTER TABLE [dbo].[ProductXResourceItem] DROP CONSTRAINT [FK_ProductXResourceItem_ResourceItem]
GO
ALTER TABLE [dbo].[ProductXResourceItem] DROP CONSTRAINT [FK_ProductXResourceItem_Product]
GO
ALTER TABLE [dbo].[ProductXResourceItem] DROP CONSTRAINT [FK_ProductXResourceItem_ActiveFlags]
GO
ALTER TABLE [dbo].[ProductXResourceItem] DROP CONSTRAINT [FK__ProductXR__Syste__6AE5BEB7]
GO
ALTER TABLE [dbo].[ProductXResourceItem] DROP CONSTRAINT [FK__ProductXR__Syste__58FC18A6]
GO
ALTER TABLE [dbo].[ProductXResourceItem] DROP CONSTRAINT [FK__ProductXR__Enter__69F19A7E]
GO
ALTER TABLE [dbo].[ProductXResourceItem] DROP CONSTRAINT [FK__ProductXR__Enter__5807F46D]
GO
ALTER TABLE [dbo].[ProductXProductTypeSecurityToken] DROP CONSTRAINT [FK_ProductXProductTypeSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ProductXProductTypeSecurityToken] DROP CONSTRAINT [FK_ProductXProductTypeSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[ProductXProductTypeSecurityToken] DROP CONSTRAINT [FK_ProductXProductTypeSecurityTokens_ProductXProductType]
GO
ALTER TABLE [dbo].[ProductXProductTypeSecurityToken] DROP CONSTRAINT [FK_ProductXProductTypeSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ProductXProductTypeSecurityToken] DROP CONSTRAINT [FK_ProductXProductTypeSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ProductXProductTypeSecurityToken] DROP CONSTRAINT [FK__ProductXProductTypeSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[ProductXProductType] DROP CONSTRAINT [FK_ProductXProductType_Systems1]
GO
ALTER TABLE [dbo].[ProductXProductType] DROP CONSTRAINT [FK_ProductXProductType_Systems]
GO
ALTER TABLE [dbo].[ProductXProductType] DROP CONSTRAINT [FK_ProductXProductType_Enterprise]
GO
ALTER TABLE [dbo].[ProductXProductType] DROP CONSTRAINT [FK_ProductXProductType_ActiveFlags]
GO
ALTER TABLE [dbo].[ProductXProductSecurityToken] DROP CONSTRAINT [FK_ProductXProductSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ProductXProductSecurityToken] DROP CONSTRAINT [FK_ProductXProductSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[ProductXProductSecurityToken] DROP CONSTRAINT [FK_ProductXProductSecurityTokens_ProductXProduct]
GO
ALTER TABLE [dbo].[ProductXProductSecurityToken] DROP CONSTRAINT [FK_ProductXProductSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ProductXProductSecurityToken] DROP CONSTRAINT [FK_ProductXProductSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ProductXProductSecurityToken] DROP CONSTRAINT [FK__ProductXProductSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[ProductXProduct] DROP CONSTRAINT [FK_ProductXProduct_Systems]
GO
ALTER TABLE [dbo].[ProductXProduct] DROP CONSTRAINT [FK_ProductXProduct_ParentProduct]
GO
ALTER TABLE [dbo].[ProductXProduct] DROP CONSTRAINT [FK_ProductXProduct_ChildProduct]
GO
ALTER TABLE [dbo].[ProductXProduct] DROP CONSTRAINT [FK_ProductXProduct_ActiveFlags]
GO
ALTER TABLE [dbo].[ProductXProduct] DROP CONSTRAINT [FK__ProductXP__Syste__54F67D98]
GO
ALTER TABLE [dbo].[ProductXProduct] DROP CONSTRAINT [FK__ProductXP__Syste__430CD787]
GO
ALTER TABLE [dbo].[ProductXProduct] DROP CONSTRAINT [FK__ProductXP__Enter__5402595F]
GO
ALTER TABLE [dbo].[ProductXProduct] DROP CONSTRAINT [FK__ProductXP__Enter__4218B34E]
GO
ALTER TABLE [dbo].[ProductXClassificationSecurityToken] DROP CONSTRAINT [FK_ProductXClassificationSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ProductXClassificationSecurityToken] DROP CONSTRAINT [FK_ProductXClassificationSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[ProductXClassificationSecurityToken] DROP CONSTRAINT [FK_ProductXClassificationSecurityTokens_ProductXClassification]
GO
ALTER TABLE [dbo].[ProductXClassificationSecurityToken] DROP CONSTRAINT [FK_ProductXClassificationSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ProductXClassificationSecurityToken] DROP CONSTRAINT [FK_ProductXClassificationSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ProductXClassificationSecurityToken] DROP CONSTRAINT [FK__ProductXClassificationSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[ProductXClassification] DROP CONSTRAINT [FK_ProductXClassification_Systems]
GO
ALTER TABLE [dbo].[ProductXClassification] DROP CONSTRAINT [FK_ProductXClassification_Product]
GO
ALTER TABLE [dbo].[ProductXClassification] DROP CONSTRAINT [FK_ProductXClassification_Classification]
GO
ALTER TABLE [dbo].[ProductXClassification] DROP CONSTRAINT [FK_ProductXClassification_ActiveFlags]
GO
ALTER TABLE [dbo].[ProductXClassification] DROP CONSTRAINT [FK__ProductXC__Syste__4984CAEC]
GO
ALTER TABLE [dbo].[ProductXClassification] DROP CONSTRAINT [FK__ProductXC__Syste__1471E42F]
GO
ALTER TABLE [dbo].[ProductXClassification] DROP CONSTRAINT [FK__ProductXC__Enter__4890A6B3]
GO
ALTER TABLE [dbo].[ProductXClassification] DROP CONSTRAINT [FK__ProductXC__Enter__137DBFF6]
GO
ALTER TABLE [dbo].[ProductSecurityToken] DROP CONSTRAINT [FK_ProductSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ProductSecurityToken] DROP CONSTRAINT [FK_ProductSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[ProductSecurityToken] DROP CONSTRAINT [FK_ProductSecurityTokens_Product]
GO
ALTER TABLE [dbo].[ProductSecurityToken] DROP CONSTRAINT [FK_ProductSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ProductSecurityToken] DROP CONSTRAINT [FK_ProductSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ProductSecurityToken] DROP CONSTRAINT [FK__ProductSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[Product] DROP CONSTRAINT [FK_Product_Systems]
GO
ALTER TABLE [dbo].[Product] DROP CONSTRAINT [FK_Product_ActiveFlags]
GO
ALTER TABLE [dbo].[Product] DROP CONSTRAINT [FK__Product__SystemI__3671F678]
GO
ALTER TABLE [dbo].[Product] DROP CONSTRAINT [FK__Product__SystemI__11957784]
GO
ALTER TABLE [dbo].[Product] DROP CONSTRAINT [FK__Product__Enterpr__357DD23F]
GO
ALTER TABLE [dbo].[Product] DROP CONSTRAINT [FK__Product__Enterpr__10A1534B]
GO
ALTER TABLE [dbo].[InvolvedPartyXResourceItemSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXResourceItemSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyXResourceItemSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXResourceItemSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[InvolvedPartyXResourceItemSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXResourceItemSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[InvolvedPartyXResourceItemSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXResourceItemSecurityTokens_InvolvedPartyXResourceItem]
GO
ALTER TABLE [dbo].[InvolvedPartyXResourceItemSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXResourceItemSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedPartyXResourceItemSecurityToken] DROP CONSTRAINT [FK__InvolvedPartyXResourceItemSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[InvolvedPartyXResourceItem] DROP CONSTRAINT [FK_InvolvedPartyXResourceItem_Systems1]
GO
ALTER TABLE [dbo].[InvolvedPartyXResourceItem] DROP CONSTRAINT [FK_InvolvedPartyXResourceItem_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyXResourceItem] DROP CONSTRAINT [FK_InvolvedPartyXResourceItem_ResourceItem]
GO
ALTER TABLE [dbo].[InvolvedPartyXResourceItem] DROP CONSTRAINT [FK_InvolvedPartyXResourceItem_InvolvedParty]
GO
ALTER TABLE [dbo].[InvolvedPartyXResourceItem] DROP CONSTRAINT [FK_InvolvedPartyXResourceItem_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedPartyXResourceItem] DROP CONSTRAINT [FK_InvolvedPartyXResourceItem_Classification]
GO
ALTER TABLE [dbo].[InvolvedPartyXResourceItem] DROP CONSTRAINT [FK_InvolvedPartyXResourceItem_ActiveFlags]
GO
ALTER TABLE [dbo].[InvolvedPartyXProductSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXProductSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyXProductSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXProductSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[InvolvedPartyXProductSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXProductSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[InvolvedPartyXProductSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXProductSecurityTokens_InvolvedPartyXProduct]
GO
ALTER TABLE [dbo].[InvolvedPartyXProductSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXProductSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedPartyXProductSecurityToken] DROP CONSTRAINT [FK__InvolvedPartyXProductSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[InvolvedPartyXProduct] DROP CONSTRAINT [FK_InvolvedPartyXProduct_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyXProduct] DROP CONSTRAINT [FK_InvolvedPartyXProduct_Product]
GO
ALTER TABLE [dbo].[InvolvedPartyXProduct] DROP CONSTRAINT [FK_InvolvedPartyXProduct_InvolvedParty]
GO
ALTER TABLE [dbo].[InvolvedPartyXProduct] DROP CONSTRAINT [FK_InvolvedPartyXProduct_ClassificationValueTYpe]
GO
ALTER TABLE [dbo].[InvolvedPartyXProduct] DROP CONSTRAINT [FK_InvolvedPartyXProduct_Classification]
GO
ALTER TABLE [dbo].[InvolvedPartyXProduct] DROP CONSTRAINT [FK_InvolvedPartyXProduct_ActiveFlags]
GO
ALTER TABLE [dbo].[InvolvedPartyXProduct] DROP CONSTRAINT [FK__InvolvedP__Syste__08E035F2]
GO
ALTER TABLE [dbo].[InvolvedPartyXProduct] DROP CONSTRAINT [FK__InvolvedP__Enter__07EC11B9]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyTypeSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyTypeSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyTypeSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyTypeSecurityTokens_InvolvedPartyXInvolvedPartyType]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyTypeSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyTypeSecurityToken] DROP CONSTRAINT [FK__InvolvedPartyXInvolvedPartyTypeSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyType] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyType_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyType] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyType_InvolvedPartyType]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyType] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyType_InvolvedParty]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyType] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyType_ActiveFlags]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyType] DROP CONSTRAINT [FK__InvolvedP__Syste__1B33F057]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyType] DROP CONSTRAINT [FK__InvolvedP__Enter__1A3FCC1E]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartySecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartySecurityTokens_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartySecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartySecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartySecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartySecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartySecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartySecurityTokens_InvolvedPartyXInvolvedParty]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartySecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartySecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartySecurityToken] DROP CONSTRAINT [FK__InvolvedPartyXInvolvedPartySecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyNameTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyNameTypeSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyNameTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyNameTypeSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyNameTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyNameTypeSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyNameTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyNameTypeSecurityTokens_InvolvedPartyXInvolvedPartyNameType]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyNameTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyNameTypeSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyNameTypeSecurityToken] DROP CONSTRAINT [FK__InvolvedPartyXInvolvedPartyNameTypeSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyNameType] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyNameType_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyNameType] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyNameType_InvolvedPartyType]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyNameType] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyNameType_InvolvedParty]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyNameType] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyNameType_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyNameType] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyNameType_ActiveFlags]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyNameType] DROP CONSTRAINT [FK__InvolvedP__Syste__0A096455]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyIdentificationTypeSecurityTokens_InvolvedPartyXInvolvedPartyIdentificationType]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken_InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken] DROP CONSTRAINT [FK__InvolvedPartyXInvolvedPartyIdentificationTypeSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyIdentificationType] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyIdentificationType_Systems1]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyIdentificationType] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyIdentificationType_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyIdentificationType] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyIdentificationType_InvolvedPartyIdentificationType]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyIdentificationType] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyIdentificationType_InvolvedParty]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyIdentificationType] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyIdentificationType_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedPartyIdentificationType] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedPartyIdentificationType_ActiveFlags]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedParty] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedParty_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedParty] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedParty_Classification]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedParty] DROP CONSTRAINT [FK_InvolvedPartyXInvolvedParty_ActiveFlags]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedParty] DROP CONSTRAINT [FK_InvolvedParty_ParentInvolvedParty]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedParty] DROP CONSTRAINT [FK_InvolvedParty_ChildInvolvedParty]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedParty] DROP CONSTRAINT [FK__InvolvedP__Syste__5B196B42]
GO
ALTER TABLE [dbo].[InvolvedPartyXInvolvedParty] DROP CONSTRAINT [FK__InvolvedP__Enter__5A254709]
GO
ALTER TABLE [dbo].[InvolvedPartyXClassificationSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXClassificationSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyXClassificationSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXClassificationSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[InvolvedPartyXClassificationSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXClassificationSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[InvolvedPartyXClassificationSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXClassificationSecurityTokens_InvolvedPartyXClassification]
GO
ALTER TABLE [dbo].[InvolvedPartyXClassificationSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXClassificationSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedPartyXClassificationSecurityToken] DROP CONSTRAINT [FK__InvolvedPartyXClassificationSecurityToken__ActiveFlag]
GO
ALTER TABLE [dbo].[InvolvedPartyXClassification] DROP CONSTRAINT [FK_InvolvedPartyXClassification_Systems1]
GO
ALTER TABLE [dbo].[InvolvedPartyXClassification] DROP CONSTRAINT [FK_InvolvedPartyXClassification_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyXClassification] DROP CONSTRAINT [FK_InvolvedPartyXClassification_InvolvedParty]
GO
ALTER TABLE [dbo].[InvolvedPartyXClassification] DROP CONSTRAINT [FK_InvolvedPartyXClassification_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedPartyXClassification] DROP CONSTRAINT [FK_InvolvedPartyXClassification_ClassificationID]
GO
ALTER TABLE [dbo].[InvolvedPartyXClassification] DROP CONSTRAINT [FK_InvolvedPartyXClassification_ActiveFlags]
GO
ALTER TABLE [dbo].[InvolvedPartyXAddressSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXAddressSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyXAddressSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXAddressSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[InvolvedPartyXAddressSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXAddressSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[InvolvedPartyXAddressSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXAddressSecurityTokens_InvolvedPartyXAddress]
GO
ALTER TABLE [dbo].[InvolvedPartyXAddressSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyXAddressSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedPartyXAddressSecurityToken] DROP CONSTRAINT [FK__InvolvedPartyXAddressSecurityToken__ActiveFlag]
GO
ALTER TABLE [dbo].[InvolvedPartyXAddress] DROP CONSTRAINT [FK_InvolvedPartyXAddress_Systems1]
GO
ALTER TABLE [dbo].[InvolvedPartyXAddress] DROP CONSTRAINT [FK_InvolvedPartyXAddress_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyXAddress] DROP CONSTRAINT [FK_InvolvedPartyXAddress_InvolvedParty]
GO
ALTER TABLE [dbo].[InvolvedPartyXAddress] DROP CONSTRAINT [FK_InvolvedPartyXAddress_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedPartyXAddress] DROP CONSTRAINT [FK_InvolvedPartyXAddress_Classification]
GO
ALTER TABLE [dbo].[InvolvedPartyXAddress] DROP CONSTRAINT [FK_InvolvedPartyXAddress_Address]
GO
ALTER TABLE [dbo].[InvolvedPartyXAddress] DROP CONSTRAINT [FK_InvolvedPartyXAddress_ActiveFlags]
GO
ALTER TABLE [dbo].[InvolvedPartyTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyTypeSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyTypeSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[InvolvedPartyTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyTypeSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[InvolvedPartyTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyTypeSecurityTokens_InvolvedPartyType]
GO
ALTER TABLE [dbo].[InvolvedPartyTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyTypeSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedPartyTypeSecurityToken] DROP CONSTRAINT [FK__InvolvedPartyTypeSecurityToken__ActiveFlag]
GO
ALTER TABLE [dbo].[InvolvedPartyType] DROP CONSTRAINT [FK_InvolvedPartyType_Systems1]
GO
ALTER TABLE [dbo].[InvolvedPartyType] DROP CONSTRAINT [FK_InvolvedPartyType_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyType] DROP CONSTRAINT [FK_InvolvedPartyType_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedPartyType] DROP CONSTRAINT [FK_InvolvedPartyType_ActiveFlags]
GO
ALTER TABLE [dbo].[InvolvedPartySecurityToken] DROP CONSTRAINT [FK_InvolvedPartySecurityTokens_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartySecurityToken] DROP CONSTRAINT [FK_InvolvedPartySecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[InvolvedPartySecurityToken] DROP CONSTRAINT [FK_InvolvedPartySecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[InvolvedPartySecurityToken] DROP CONSTRAINT [FK_InvolvedPartySecurityTokens_InvolvedParty]
GO
ALTER TABLE [dbo].[InvolvedPartySecurityToken] DROP CONSTRAINT [FK_InvolvedPartySecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedPartySecurityToken] DROP CONSTRAINT [FK__InvolvedPartySecurityToken__ActiveFlag]
GO
ALTER TABLE [dbo].[InvolvedPartyOrganicTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyOrganicTypeSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyOrganicTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyOrganicTypeSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[InvolvedPartyOrganicTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyOrganicTypeSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[InvolvedPartyOrganicTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyOrganicTypeSecurityTokens_InvolvedPartyOrganicType]
GO
ALTER TABLE [dbo].[InvolvedPartyOrganicTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyOrganicTypeSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedPartyOrganicTypeSecurityToken] DROP CONSTRAINT [FK__InvolvedPartyOrganicTypeSecurityToken__ActiveFlag]
GO
ALTER TABLE [dbo].[InvolvedPartyOrganicType] DROP CONSTRAINT [FK_InvolvedPartyOrganicType_Systems1]
GO
ALTER TABLE [dbo].[InvolvedPartyOrganicType] DROP CONSTRAINT [FK_InvolvedPartyOrganicType_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyOrganicType] DROP CONSTRAINT [FK_InvolvedPartyOrganicType_ActiveFlags]
GO
ALTER TABLE [dbo].[InvolvedPartyOrganicType] DROP CONSTRAINT [FK__InvolvedP__Enter__47477CBF]
GO
ALTER TABLE [dbo].[InvolvedPartyOrganicSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyOrganicSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyOrganicSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyOrganicSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[InvolvedPartyOrganicSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyOrganicSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[InvolvedPartyOrganicSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyOrganicSecurityTokens_InvolvedPartyOrganic]
GO
ALTER TABLE [dbo].[InvolvedPartyOrganicSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyOrganicSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedPartyOrganicSecurityToken] DROP CONSTRAINT [FK__InvolvedPartyOrganicSecurityToken__ActiveFlag]
GO
ALTER TABLE [dbo].[InvolvedPartyOrganic] DROP CONSTRAINT [FK_InvolvedPartyOrganic_Systems1]
GO
ALTER TABLE [dbo].[InvolvedPartyOrganic] DROP CONSTRAINT [FK_InvolvedPartyOrganic_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyOrganic] DROP CONSTRAINT [FK_InvolvedPartyOrganic_InvolvedParty]
GO
ALTER TABLE [dbo].[InvolvedPartyOrganic] DROP CONSTRAINT [FK_InvolvedPartyOrganic_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedPartyOrganic] DROP CONSTRAINT [FK_InvolvedPartyOrganic_ActiveFlags]
GO
ALTER TABLE [dbo].[InvolvedPartyNonOrganicSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyNonOrganicSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyNonOrganicSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyNonOrganicSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[InvolvedPartyNonOrganicSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyNonOrganicSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[InvolvedPartyNonOrganicSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyNonOrganicSecurityTokens_InvolvedPartyNonOrganic]
GO
ALTER TABLE [dbo].[InvolvedPartyNonOrganicSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyNonOrganicSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedPartyNonOrganicSecurityToken] DROP CONSTRAINT [FK__InvolvedPartyNonOrganicSecurityToken__ActiveFlag]
GO
ALTER TABLE [dbo].[InvolvedPartyNonOrganic] DROP CONSTRAINT [FK_InvolvedPartyNonOrganic_Systems1]
GO
ALTER TABLE [dbo].[InvolvedPartyNonOrganic] DROP CONSTRAINT [FK_InvolvedPartyNonOrganic_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyNonOrganic] DROP CONSTRAINT [FK_InvolvedPartyNonOrganic_InvolvedParty]
GO
ALTER TABLE [dbo].[InvolvedPartyNonOrganic] DROP CONSTRAINT [FK_InvolvedPartyNonOrganic_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedPartyNonOrganic] DROP CONSTRAINT [FK_InvolvedPartyNonOrganic_ActiveFlags]
GO
ALTER TABLE [dbo].[InvolvedPartyNameTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyNameTypeSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyNameTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyNameTypeSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[InvolvedPartyNameTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyNameTypeSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[InvolvedPartyNameTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyNameTypeSecurityTokens_InvolvedPartyNameType]
GO
ALTER TABLE [dbo].[InvolvedPartyNameTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyNameTypeSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedPartyNameTypeSecurityToken] DROP CONSTRAINT [FK__InvolvedPartyNameTypeSecurityToken__ActiveFlag]
GO
ALTER TABLE [dbo].[InvolvedPartyNameType] DROP CONSTRAINT [FK_InvolvedPartyNameType_Systems1]
GO
ALTER TABLE [dbo].[InvolvedPartyNameType] DROP CONSTRAINT [FK_InvolvedPartyNameType_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyNameType] DROP CONSTRAINT [FK_InvolvedPartyNameType_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedPartyNameType] DROP CONSTRAINT [FK_InvolvedPartyNameType_ActiveFlags]
GO
ALTER TABLE [dbo].[InvolvedPartyIdentificationTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyIdentificationTypeSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyIdentificationTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyIdentificationTypeSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[InvolvedPartyIdentificationTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyIdentificationTypeSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[InvolvedPartyIdentificationTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyIdentificationTypeSecurityTokens_InvolvedPartyIdentificationType]
GO
ALTER TABLE [dbo].[InvolvedPartyIdentificationTypeSecurityToken] DROP CONSTRAINT [FK_InvolvedPartyIdentificationTypeSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedPartyIdentificationTypeSecurityToken] DROP CONSTRAINT [FK__InvolvedPartyIdentificationTypeSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[InvolvedPartyIdentificationType] DROP CONSTRAINT [FK_InvolvedPartyIdentificationType_Systems]
GO
ALTER TABLE [dbo].[InvolvedPartyIdentificationType] DROP CONSTRAINT [FK_InvolvedPartyIdentificationType_ActiveFlags]
GO
ALTER TABLE [dbo].[InvolvedPartyIdentificationType] DROP CONSTRAINT [FK__InvolvedP__Syste__4469AB30]
GO
ALTER TABLE [dbo].[InvolvedPartyIdentificationType] DROP CONSTRAINT [FK__InvolvedP__Enter__437586F7]
GO
ALTER TABLE [dbo].[InvolvedParty] DROP CONSTRAINT [FK_InvolvedParty_Systems1]
GO
ALTER TABLE [dbo].[InvolvedParty] DROP CONSTRAINT [FK_InvolvedParty_Systems]
GO
ALTER TABLE [dbo].[InvolvedParty] DROP CONSTRAINT [FK_InvolvedParty_Enterprise]
GO
ALTER TABLE [dbo].[InvolvedParty] DROP CONSTRAINT [FK_InvolvedParty_ActiveFlags]
GO
ALTER TABLE [dbo].[GeographyXResourceItemSecurityToken] DROP CONSTRAINT [FK_GeographyXResourceItemSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[GeographyXResourceItemSecurityToken] DROP CONSTRAINT [FK_GeographyXResourceItemSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[GeographyXResourceItemSecurityToken] DROP CONSTRAINT [FK_GeographyXResourceItemSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[GeographyXResourceItemSecurityToken] DROP CONSTRAINT [FK_GeographyXResourceItemSecurityTokens_GeographyXResourceItem]
GO
ALTER TABLE [dbo].[GeographyXResourceItemSecurityToken] DROP CONSTRAINT [FK_GeographyXResourceItemSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[GeographyXResourceItemSecurityToken] DROP CONSTRAINT [FK__GeographyXResourceItemSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[GeographyXResourceItem] DROP CONSTRAINT [FK_GeographyXResourceItem_Systems]
GO
ALTER TABLE [dbo].[GeographyXResourceItem] DROP CONSTRAINT [FK_GeographyXResourceItem_ResourceItem]
GO
ALTER TABLE [dbo].[GeographyXResourceItem] DROP CONSTRAINT [FK_GeographyXResourceItem_Geography]
GO
ALTER TABLE [dbo].[GeographyXResourceItem] DROP CONSTRAINT [FK_GeographyXResourceItem_Classification]
GO
ALTER TABLE [dbo].[GeographyXResourceItem] DROP CONSTRAINT [FK_GeographyXResourceItem_ActiveFlags]
GO
ALTER TABLE [dbo].[GeographyXResourceItem] DROP CONSTRAINT [FK__Geography__Syste__750E476F]
GO
ALTER TABLE [dbo].[GeographyXResourceItem] DROP CONSTRAINT [FK__Geography__Syste__741A2336]
GO
ALTER TABLE [dbo].[GeographyXResourceItem] DROP CONSTRAINT [FK__Geography__Enter__7325FEFD]
GO
ALTER TABLE [dbo].[GeographyXResourceItem] DROP CONSTRAINT [FK__Geography__Enter__7231DAC4]
GO
ALTER TABLE [dbo].[GeographyXGeographySecurityToken] DROP CONSTRAINT [FK_GeographyXGeographySecurityTokens_Systems]
GO
ALTER TABLE [dbo].[GeographyXGeographySecurityToken] DROP CONSTRAINT [FK_GeographyXGeographySecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[GeographyXGeographySecurityToken] DROP CONSTRAINT [FK_GeographyXGeographySecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[GeographyXGeographySecurityToken] DROP CONSTRAINT [FK_GeographyXGeographySecurityTokens_GeographyXGeography]
GO
ALTER TABLE [dbo].[GeographyXGeographySecurityToken] DROP CONSTRAINT [FK_GeographyXGeographySecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[GeographyXGeographySecurityToken] DROP CONSTRAINT [FK__GeographyXGeographySecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[GeographyXGeography] DROP CONSTRAINT [FK_GeographyXGeography_Systems]
GO
ALTER TABLE [dbo].[GeographyXGeography] DROP CONSTRAINT [FK_GeographyXGeography_Geography]
GO
ALTER TABLE [dbo].[GeographyXGeography] DROP CONSTRAINT [FK_GeographyXGeography_Classification]
GO
ALTER TABLE [dbo].[GeographyXGeography] DROP CONSTRAINT [FK_GeographyXGeography_ChildGeography]
GO
ALTER TABLE [dbo].[GeographyXGeography] DROP CONSTRAINT [FK_GeographyXGeography_ActiveFlags]
GO
ALTER TABLE [dbo].[GeographyXGeography] DROP CONSTRAINT [FK__Geography__Syste__65CC03DF]
GO
ALTER TABLE [dbo].[GeographyXGeography] DROP CONSTRAINT [FK__Geography__Syste__64D7DFA6]
GO
ALTER TABLE [dbo].[GeographyXGeography] DROP CONSTRAINT [FK__Geography__Enter__63E3BB6D]
GO
ALTER TABLE [dbo].[GeographyXGeography] DROP CONSTRAINT [FK__Geography__Enter__62EF9734]
GO
ALTER TABLE [dbo].[GeographyXClassificationSecurityToken] DROP CONSTRAINT [FK_GeographyXClassificationSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[GeographyXClassificationSecurityToken] DROP CONSTRAINT [FK_GeographyXClassificationSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[GeographyXClassificationSecurityToken] DROP CONSTRAINT [FK_GeographyXClassificationSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[GeographyXClassificationSecurityToken] DROP CONSTRAINT [FK_GeographyXClassificationSecurityTokens_GeographyXClassification]
GO
ALTER TABLE [dbo].[GeographyXClassificationSecurityToken] DROP CONSTRAINT [FK_GeographyXClassificationSecurityToken_ActiveFlag]
GO
ALTER TABLE [dbo].[GeographyXClassificationSecurityToken] DROP CONSTRAINT [FK__GeographyXClassificationSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[GeographyXClassification] DROP CONSTRAINT [FK_GeographyXClassifications_Systems]
GO
ALTER TABLE [dbo].[GeographyXClassification] DROP CONSTRAINT [FK_GeographyXClassifications_Geographys]
GO
ALTER TABLE [dbo].[GeographyXClassification] DROP CONSTRAINT [FK_GeographyXClassifications_Classification]
GO
ALTER TABLE [dbo].[GeographyXClassification] DROP CONSTRAINT [FK_GeographyXClassifications_ActiveFlags]
GO
ALTER TABLE [dbo].[GeographyXClassification] DROP CONSTRAINT [FK__Geography__Syste__188B28F2]
GO
ALTER TABLE [dbo].[GeographyXClassification] DROP CONSTRAINT [FK__Geography__Syste__179704B9]
GO
ALTER TABLE [dbo].[GeographyXClassification] DROP CONSTRAINT [FK__Geography__Enter__16A2E080]
GO
ALTER TABLE [dbo].[GeographyXClassification] DROP CONSTRAINT [FK__Geography__Enter__15AEBC47]
GO
ALTER TABLE [dbo].[GeographySecurityToken] DROP CONSTRAINT [FK_GeographySecurityTokens_Systems]
GO
ALTER TABLE [dbo].[GeographySecurityToken] DROP CONSTRAINT [FK_GeographySecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[GeographySecurityToken] DROP CONSTRAINT [FK_GeographySecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[GeographySecurityToken] DROP CONSTRAINT [FK_GeographySecurityTokens_Geography]
GO
ALTER TABLE [dbo].[GeographySecurityToken] DROP CONSTRAINT [FK_GeographySecurityToken__ActiveFlag]
GO
ALTER TABLE [dbo].[GeographySecurityToken] DROP CONSTRAINT [FK__Geography__Enter__0FF5E2F1]
GO
ALTER TABLE [dbo].[GeographySecurityToken] DROP CONSTRAINT [FK__Geography__Enter__0F01BEB8]
GO
ALTER TABLE [dbo].[Geography] DROP CONSTRAINT [FK_Geography_Systems]
GO
ALTER TABLE [dbo].[Geography] DROP CONSTRAINT [FK_Geography_Classification]
GO
ALTER TABLE [dbo].[Geography] DROP CONSTRAINT [FK_Geography_ActiveFlags]
GO
ALTER TABLE [dbo].[Geography] DROP CONSTRAINT [FK__Geography__Syste__5E15D37E]
GO
ALTER TABLE [dbo].[Geography] DROP CONSTRAINT [FK__Geography__Syste__5D21AF45]
GO
ALTER TABLE [dbo].[Geography] DROP CONSTRAINT [FK__Geography__Syste__5C0D8F7B]
GO
ALTER TABLE [dbo].[Geography] DROP CONSTRAINT [FK__Geography__Syste__5B196B42]
GO
ALTER TABLE [dbo].[Geography] DROP CONSTRAINT [FK__Geography__Enter__5C2D8B0C]
GO
ALTER TABLE [dbo].[Geography] DROP CONSTRAINT [FK__Geography__Enter__5B3966D3]
GO
ALTER TABLE [dbo].[Geography] DROP CONSTRAINT [FK__Geography__Enter__5A254709]
GO
ALTER TABLE [dbo].[Geography] DROP CONSTRAINT [FK__Geography__Enter__593122D0]
GO
ALTER TABLE [dbo].[EventXResourceItemSecurityToken] DROP CONSTRAINT [FK_EventXResourceItemSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[EventXResourceItemSecurityToken] DROP CONSTRAINT [FK_EventXResourceItemSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[EventXResourceItemSecurityToken] DROP CONSTRAINT [FK_EventXResourceItemSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[EventXResourceItemSecurityToken] DROP CONSTRAINT [FK_EventXResourceItemSecurityTokens_EventXResourceItem]
GO
ALTER TABLE [dbo].[EventXResourceItemSecurityToken] DROP CONSTRAINT [FK_EventXResourceItemSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[EventXResourceItemSecurityToken] DROP CONSTRAINT [FK__EventXResourceItemSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[EventXResourceItem] DROP CONSTRAINT [FK_EventXResourceItem_Systems]
GO
ALTER TABLE [dbo].[EventXResourceItem] DROP CONSTRAINT [FK_EventXResourceItem_ResourceItem]
GO
ALTER TABLE [dbo].[EventXResourceItem] DROP CONSTRAINT [FK_EventXResourceItem_Events]
GO
ALTER TABLE [dbo].[EventXResourceItem] DROP CONSTRAINT [FK_EventXResourceItem_Classification]
GO
ALTER TABLE [dbo].[EventXResourceItem] DROP CONSTRAINT [FK_EventXResourceItem_ActiveFlags]
GO
ALTER TABLE [dbo].[EventXResourceItem] DROP CONSTRAINT [FK__EventXRes__Syste__3AE1A5DA]
GO
ALTER TABLE [dbo].[EventXResourceItem] DROP CONSTRAINT [FK__EventXRes__Syste__39ED81A1]
GO
ALTER TABLE [dbo].[EventXResourceItem] DROP CONSTRAINT [FK__EventXRes__Enter__38F95D68]
GO
ALTER TABLE [dbo].[EventXResourceItem] DROP CONSTRAINT [FK__EventXRes__Enter__3805392F]
GO
ALTER TABLE [dbo].[EventXProductSecurityToken] DROP CONSTRAINT [FK_EventXProductSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[EventXProductSecurityToken] DROP CONSTRAINT [FK_EventXProductSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[EventXProductSecurityToken] DROP CONSTRAINT [FK_EventXProductSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[EventXProductSecurityToken] DROP CONSTRAINT [FK_EventXProductSecurityTokens_EventXProduct]
GO
ALTER TABLE [dbo].[EventXProductSecurityToken] DROP CONSTRAINT [FK_EventXProductSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[EventXProductSecurityToken] DROP CONSTRAINT [FK__EventXProductSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[EventXProduct] DROP CONSTRAINT [FK_EventXProduct_Systems]
GO
ALTER TABLE [dbo].[EventXProduct] DROP CONSTRAINT [FK_EventXProduct_Product]
GO
ALTER TABLE [dbo].[EventXProduct] DROP CONSTRAINT [FK_EventXProduct_Events]
GO
ALTER TABLE [dbo].[EventXProduct] DROP CONSTRAINT [FK_EventXProduct_Classification]
GO
ALTER TABLE [dbo].[EventXProduct] DROP CONSTRAINT [FK_EventXProduct_ActiveFlags]
GO
ALTER TABLE [dbo].[EventXProduct] DROP CONSTRAINT [FK__EventXPro__Syste__2B9F624A]
GO
ALTER TABLE [dbo].[EventXProduct] DROP CONSTRAINT [FK__EventXPro__Syste__2AAB3E11]
GO
ALTER TABLE [dbo].[EventXProduct] DROP CONSTRAINT [FK__EventXPro__Enter__29B719D8]
GO
ALTER TABLE [dbo].[EventXProduct] DROP CONSTRAINT [FK__EventXPro__Enter__28C2F59F]
GO
ALTER TABLE [dbo].[EventXInvolvedPartySecurityToken] DROP CONSTRAINT [FK_EventXInvolvedPartySecurityTokens_Systems]
GO
ALTER TABLE [dbo].[EventXInvolvedPartySecurityToken] DROP CONSTRAINT [FK_EventXInvolvedPartySecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[EventXInvolvedPartySecurityToken] DROP CONSTRAINT [FK_EventXInvolvedPartySecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[EventXInvolvedPartySecurityToken] DROP CONSTRAINT [FK_EventXInvolvedPartySecurityTokens_EventXInvolvedParty]
GO
ALTER TABLE [dbo].[EventXInvolvedPartySecurityToken] DROP CONSTRAINT [FK_EventXInvolvedPartySecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[EventXInvolvedPartySecurityToken] DROP CONSTRAINT [FK__EventXInvolvedPartySecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[EventXInvolvedParty] DROP CONSTRAINT [FK_EventXInvolvedParty_Systems]
GO
ALTER TABLE [dbo].[EventXInvolvedParty] DROP CONSTRAINT [FK_EventXInvolvedParty_InvolvedParty]
GO
ALTER TABLE [dbo].[EventXInvolvedParty] DROP CONSTRAINT [FK_EventXInvolvedParty_Events]
GO
ALTER TABLE [dbo].[EventXInvolvedParty] DROP CONSTRAINT [FK_EventXInvolvedParty_Classification]
GO
ALTER TABLE [dbo].[EventXInvolvedParty] DROP CONSTRAINT [FK_EventXInvolvedParty_ActiveFlags]
GO
ALTER TABLE [dbo].[EventXInvolvedParty] DROP CONSTRAINT [FK__EventXInv__Syste__1C5D1EBA]
GO
ALTER TABLE [dbo].[EventXInvolvedParty] DROP CONSTRAINT [FK__EventXInv__Syste__1B68FA81]
GO
ALTER TABLE [dbo].[EventXInvolvedParty] DROP CONSTRAINT [FK__EventXInv__Enter__1A74D648]
GO
ALTER TABLE [dbo].[EventXInvolvedParty] DROP CONSTRAINT [FK__EventXInv__Enter__1980B20F]
GO
ALTER TABLE [dbo].[EventXGeographySecurityToken] DROP CONSTRAINT [FK_EventXGeographySecurityTokens_Systems]
GO
ALTER TABLE [dbo].[EventXGeographySecurityToken] DROP CONSTRAINT [FK_EventXGeographySecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[EventXGeographySecurityToken] DROP CONSTRAINT [FK_EventXGeographySecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[EventXGeographySecurityToken] DROP CONSTRAINT [FK_EventXGeographySecurityTokens_EventXGeography]
GO
ALTER TABLE [dbo].[EventXGeographySecurityToken] DROP CONSTRAINT [FK_EventXGeographySecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[EventXGeographySecurityToken] DROP CONSTRAINT [FK__EventXGeographySecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[EventXGeography] DROP CONSTRAINT [FK_EventXGeography_Systems]
GO
ALTER TABLE [dbo].[EventXGeography] DROP CONSTRAINT [FK_EventXGeography_Geography]
GO
ALTER TABLE [dbo].[EventXGeography] DROP CONSTRAINT [FK_EventXGeography_Events]
GO
ALTER TABLE [dbo].[EventXGeography] DROP CONSTRAINT [FK_EventXGeography_Classification]
GO
ALTER TABLE [dbo].[EventXGeography] DROP CONSTRAINT [FK_EventXGeography_ActiveFlags]
GO
ALTER TABLE [dbo].[EventXGeography] DROP CONSTRAINT [FK__EventXGeo__Syste__2B9F624A]
GO
ALTER TABLE [dbo].[EventXGeography] DROP CONSTRAINT [FK__EventXGeo__Syste__2AAB3E11]
GO
ALTER TABLE [dbo].[EventXGeography] DROP CONSTRAINT [FK__EventXGeo__Enter__29B719D8]
GO
ALTER TABLE [dbo].[EventXGeography] DROP CONSTRAINT [FK__EventXGeo__Enter__28C2F59F]
GO
ALTER TABLE [dbo].[EventXEventTypeSecurityToken] DROP CONSTRAINT [FK_EventXEventTypeSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[EventXEventTypeSecurityToken] DROP CONSTRAINT [FK_EventXEventTypeSecurityTokens_SecurityTokens]
GO
ALTER TABLE [dbo].[EventXEventTypeSecurityToken] DROP CONSTRAINT [FK_EventXEventTypeSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[EventXEventTypeSecurityToken] DROP CONSTRAINT [FK_EventXEventTypeSecurityTokens_EventXEventType]
GO
ALTER TABLE [dbo].[EventXEventTypeSecurityToken] DROP CONSTRAINT [FK_EventXEventTypeSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[EventXEventTypeSecurityToken] DROP CONSTRAINT [FK__EventXEventTypeSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[EventXEventType] DROP CONSTRAINT [FK_EventXEventType_EventType]
GO
ALTER TABLE [dbo].[EventXEventType] DROP CONSTRAINT [FK_EventXEventType_Events]
GO
ALTER TABLE [dbo].[EventXEventType] DROP CONSTRAINT [FK_EventXEventType_ActiveFlags]
GO
ALTER TABLE [dbo].[EventXEventType] DROP CONSTRAINT [FK__EventXEve__Syste__0F03239C]
GO
ALTER TABLE [dbo].[EventXEventType] DROP CONSTRAINT [FK__EventXEve__Syste__0E0EFF63]
GO
ALTER TABLE [dbo].[EventXEventType] DROP CONSTRAINT [FK__EventXEve__Enter__0D1ADB2A]
GO
ALTER TABLE [dbo].[EventXEventType] DROP CONSTRAINT [FK__EventXEve__Enter__0C26B6F1]
GO
ALTER TABLE [dbo].[EventXClassificationSecurityToken] DROP CONSTRAINT [FK_EventXClassificationsSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[EventXClassificationSecurityToken] DROP CONSTRAINT [FK_EventXClassificationsSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[EventXClassificationSecurityToken] DROP CONSTRAINT [FK_EventXClassificationsSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[EventXClassificationSecurityToken] DROP CONSTRAINT [FK_EventXClassificationsSecurityTokens_EventXClassifications]
GO
ALTER TABLE [dbo].[EventXClassificationSecurityToken] DROP CONSTRAINT [FK_EventXClassificationSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[EventXClassificationSecurityToken] DROP CONSTRAINT [FK__EventXClassificationsSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[EventXClassification] DROP CONSTRAINT [FK_EventXClassifications_Systems]
GO
ALTER TABLE [dbo].[EventXClassification] DROP CONSTRAINT [FK_EventXClassifications_Events]
GO
ALTER TABLE [dbo].[EventXClassification] DROP CONSTRAINT [FK_EventXClassifications_Classification]
GO
ALTER TABLE [dbo].[EventXClassification] DROP CONSTRAINT [FK_EventXClassifications_ActiveFlags]
GO
ALTER TABLE [dbo].[EventXClassification] DROP CONSTRAINT [FK_EventXClassification_Systems]
GO
ALTER TABLE [dbo].[EventXClassification] DROP CONSTRAINT [FK_EventXClassification_Enterprise]
GO
ALTER TABLE [dbo].[EventXArrangementsSecurityToken] DROP CONSTRAINT [FK_EventXArrangementsSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[EventXArrangementsSecurityToken] DROP CONSTRAINT [FK_EventXArrangementsSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[EventXArrangementsSecurityToken] DROP CONSTRAINT [FK_EventXArrangementsSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[EventXArrangementsSecurityToken] DROP CONSTRAINT [FK_EventXArrangementsSecurityTokens_EventXArrangements]
GO
ALTER TABLE [dbo].[EventXArrangementsSecurityToken] DROP CONSTRAINT [FK_EventXArrangementsSecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[EventXArrangementsSecurityToken] DROP CONSTRAINT [FK_EventXArrangementsSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[EventXArrangement] DROP CONSTRAINT [FK_EventXArrangements_Systems]
GO
ALTER TABLE [dbo].[EventXArrangement] DROP CONSTRAINT [FK_EventXArrangements_Events]
GO
ALTER TABLE [dbo].[EventXArrangement] DROP CONSTRAINT [FK_EventXArrangements_Classification]
GO
ALTER TABLE [dbo].[EventXArrangement] DROP CONSTRAINT [FK_EventXArrangements_Arrangement]
GO
ALTER TABLE [dbo].[EventXArrangement] DROP CONSTRAINT [FK_EventXArrangements_ActiveFlags]
GO
ALTER TABLE [dbo].[EventXArrangement] DROP CONSTRAINT [FK__EventXArr__Syste__7172C0B5]
GO
ALTER TABLE [dbo].[EventXArrangement] DROP CONSTRAINT [FK__EventXArr__Syste__707E9C7C]
GO
ALTER TABLE [dbo].[EventXArrangement] DROP CONSTRAINT [FK__EventXArr__Enter__6F8A7843]
GO
ALTER TABLE [dbo].[EventXArrangement] DROP CONSTRAINT [FK__EventXArr__Enter__6E96540A]
GO
ALTER TABLE [dbo].[EventXAddressSecurityToken] DROP CONSTRAINT [FK_EventXAddressSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[EventXAddressSecurityToken] DROP CONSTRAINT [FK_EventXAddressSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[EventXAddressSecurityToken] DROP CONSTRAINT [FK_EventXAddressSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[EventXAddressSecurityToken] DROP CONSTRAINT [FK_EventXAddressSecurityTokens_EventXAddress]
GO
ALTER TABLE [dbo].[EventXAddressSecurityToken] DROP CONSTRAINT [FK_EventXAddressSecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[EventXAddressSecurityToken] DROP CONSTRAINT [FK_EventXAddressSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[EventXAddress] DROP CONSTRAINT [FK_EventXAddress_Systems]
GO
ALTER TABLE [dbo].[EventXAddress] DROP CONSTRAINT [FK_EventXAddress_OriginalSystems]
GO
ALTER TABLE [dbo].[EventXAddress] DROP CONSTRAINT [FK_EventXAddress_Events]
GO
ALTER TABLE [dbo].[EventXAddress] DROP CONSTRAINT [FK_EventXAddress_Classification]
GO
ALTER TABLE [dbo].[EventXAddress] DROP CONSTRAINT [FK_EventXAddress_Address]
GO
ALTER TABLE [dbo].[EventXAddress] DROP CONSTRAINT [FK_EventXAddress_ActiveFlags]
GO
ALTER TABLE [dbo].[EventXAddress] DROP CONSTRAINT [FK__EventXAdd__Syste__613C58EC]
GO
ALTER TABLE [dbo].[EventXAddress] DROP CONSTRAINT [FK__EventXAdd__Syste__604834B3]
GO
ALTER TABLE [dbo].[EventXAddress] DROP CONSTRAINT [FK__EventXAdd__Enter__5F54107A]
GO
ALTER TABLE [dbo].[EventXAddress] DROP CONSTRAINT [FK__EventXAdd__Enter__5E5FEC41]
GO
ALTER TABLE [dbo].[EventTypesSecurityToken] DROP CONSTRAINT [FK_EventTypesSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[EventTypesSecurityToken] DROP CONSTRAINT [FK_EventTypesSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[EventTypesSecurityToken] DROP CONSTRAINT [FK_EventTypesSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[EventTypesSecurityToken] DROP CONSTRAINT [FK_EventTypesSecurityTokens_EventTypes]
GO
ALTER TABLE [dbo].[EventTypesSecurityToken] DROP CONSTRAINT [FK_EventTypesSecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[EventTypesSecurityToken] DROP CONSTRAINT [FK_EventTypesSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[EventType] DROP CONSTRAINT [FK_EventTypes_Systems]
GO
ALTER TABLE [dbo].[EventType] DROP CONSTRAINT [FK_EventTypes_ActiveFlags]
GO
ALTER TABLE [dbo].[EventType] DROP CONSTRAINT [FK_EventType_Systems]
GO
ALTER TABLE [dbo].[EventType] DROP CONSTRAINT [FK_EventType_Enterprise]
GO
ALTER TABLE [dbo].[EventSecurityToken] DROP CONSTRAINT [FK_EventsSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[EventSecurityToken] DROP CONSTRAINT [FK_EventsSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[EventSecurityToken] DROP CONSTRAINT [FK_EventsSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[EventSecurityToken] DROP CONSTRAINT [FK_EventsSecurityTokens_Events]
GO
ALTER TABLE [dbo].[EventSecurityToken] DROP CONSTRAINT [FK_EventsSecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[EventSecurityToken] DROP CONSTRAINT [FK_EventSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[Event] DROP CONSTRAINT [FK_Events_OriginalSystems]
GO
ALTER TABLE [dbo].[Event] DROP CONSTRAINT [FK_Events_ActiveFlags]
GO
ALTER TABLE [dbo].[Event] DROP CONSTRAINT [FK_Event_Systems]
GO
ALTER TABLE [dbo].[EnterpriseXClassificationSecurityToken] DROP CONSTRAINT [FK_EnterpriseXClassificationSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[EnterpriseXClassificationSecurityToken] DROP CONSTRAINT [FK_EnterpriseXClassificationSecurityTokens_EnterpriseXClassification]
GO
ALTER TABLE [dbo].[EnterpriseXClassificationSecurityToken] DROP CONSTRAINT [FK_EnterpriseXClassificationSecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[EnterpriseXClassificationSecurityToken] DROP CONSTRAINT [FK_EnterpriseXClassificationSecurityToken_Systems1]
GO
ALTER TABLE [dbo].[EnterpriseXClassificationSecurityToken] DROP CONSTRAINT [FK_EnterpriseXClassificationSecurityToken_Systems]
GO
ALTER TABLE [dbo].[EnterpriseXClassificationSecurityToken] DROP CONSTRAINT [FK_EnterpriseXClassificationSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[EnterpriseXClassification] DROP CONSTRAINT [FK_EnterpriseXClassification_Systems1]
GO
ALTER TABLE [dbo].[EnterpriseXClassification] DROP CONSTRAINT [FK_EnterpriseXClassification_Systems]
GO
ALTER TABLE [dbo].[EnterpriseXClassification] DROP CONSTRAINT [FK_EnterpriseXClassification_Enterprise]
GO
ALTER TABLE [dbo].[EnterpriseXClassification] DROP CONSTRAINT [FK_EnterpriseXClassification_Classification]
GO
ALTER TABLE [dbo].[EnterpriseXClassification] DROP CONSTRAINT [FK_EnterpriseXClassification_ActiveFlags]
GO
ALTER TABLE [dbo].[EnterpriseSecurityToken] DROP CONSTRAINT [FK_EnterprisesSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[EnterpriseSecurityToken] DROP CONSTRAINT [FK_EnterprisesSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[EnterpriseSecurityToken] DROP CONSTRAINT [FK_EnterprisesSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[EnterpriseSecurityToken] DROP CONSTRAINT [FK_EnterprisesSecurityTokens_Enterprises]
GO
ALTER TABLE [dbo].[EnterpriseSecurityToken] DROP CONSTRAINT [FK_EnterprisesSecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[ClassificationXResourceItemSecurityToken] DROP CONSTRAINT [FK_ClassificationXResourceItemSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ClassificationXResourceItemSecurityToken] DROP CONSTRAINT [FK_ClassificationXResourceItemSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[ClassificationXResourceItemSecurityToken] DROP CONSTRAINT [FK_ClassificationXResourceItemSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ClassificationXResourceItemSecurityToken] DROP CONSTRAINT [FK_ClassificationXResourceItemSecurityTokens_ClassificationXResourceItem]
GO
ALTER TABLE [dbo].[ClassificationXResourceItemSecurityToken] DROP CONSTRAINT [FK_ClassificationXResourceItemSecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[ClassificationXResourceItemSecurityToken] DROP CONSTRAINT [FK_ClassificationXResourceItemSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ClassificationXResourceItem] DROP CONSTRAINT [FK_ClassificationXResourceItem_Systems1]
GO
ALTER TABLE [dbo].[ClassificationXResourceItem] DROP CONSTRAINT [FK_ClassificationXResourceItem_Systems]
GO
ALTER TABLE [dbo].[ClassificationXResourceItem] DROP CONSTRAINT [FK_ClassificationXResourceItem_ResourceItem]
GO
ALTER TABLE [dbo].[ClassificationXResourceItem] DROP CONSTRAINT [FK_ClassificationXResourceItem_OriginalSystems]
GO
ALTER TABLE [dbo].[ClassificationXResourceItem] DROP CONSTRAINT [FK_ClassificationXResourceItem_Enterprise]
GO
ALTER TABLE [dbo].[ClassificationXResourceItem] DROP CONSTRAINT [FK_ClassificationXResourceItem_Classification]
GO
ALTER TABLE [dbo].[ClassificationXResourceItem] DROP CONSTRAINT [FK_ClassificationXResourceItem_ActiveFlags]
GO
ALTER TABLE [dbo].[ClassificationXClassificationSecurityToken] DROP CONSTRAINT [FK_ClassificationXClassificationSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ClassificationXClassificationSecurityToken] DROP CONSTRAINT [FK_ClassificationXClassificationSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[ClassificationXClassificationSecurityToken] DROP CONSTRAINT [FK_ClassificationXClassificationSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ClassificationXClassificationSecurityToken] DROP CONSTRAINT [FK_ClassificationXClassificationSecurityTokens_ClassificationXClassification]
GO
ALTER TABLE [dbo].[ClassificationXClassificationSecurityToken] DROP CONSTRAINT [FK_ClassificationXClassificationSecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[ClassificationXClassificationSecurityToken] DROP CONSTRAINT [FK_ClassificationXClassificationSecurityToken_Systems]
GO
ALTER TABLE [dbo].[ClassificationXClassificationSecurityToken] DROP CONSTRAINT [FK_ClassificationXClassificationSecurityToken_Enterprise1]
GO
ALTER TABLE [dbo].[ClassificationXClassificationSecurityToken] DROP CONSTRAINT [FK_ClassificationXClassificationSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ClassificationXClassification] DROP CONSTRAINT [FK_ClassificationXClassification_Systems1]
GO
ALTER TABLE [dbo].[ClassificationXClassification] DROP CONSTRAINT [FK_ClassificationXClassification_Systems]
GO
ALTER TABLE [dbo].[ClassificationXClassification] DROP CONSTRAINT [FK_ClassificationXClassification_OriginalSystems]
GO
ALTER TABLE [dbo].[ClassificationXClassification] DROP CONSTRAINT [FK_ClassificationXClassification_Enterprise]
GO
ALTER TABLE [dbo].[ClassificationXClassification] DROP CONSTRAINT [FK_ClassificationXClassification_ClassificationParent]
GO
ALTER TABLE [dbo].[ClassificationXClassification] DROP CONSTRAINT [FK_ClassificationXClassification_Classification]
GO
ALTER TABLE [dbo].[ClassificationXClassification] DROP CONSTRAINT [FK_ClassificationXClassification_ActiveFlags]
GO
ALTER TABLE [dbo].[ClassificationSecurityToken] DROP CONSTRAINT [FK_ClassificationSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ClassificationSecurityToken] DROP CONSTRAINT [FK_ClassificationSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[ClassificationSecurityToken] DROP CONSTRAINT [FK_ClassificationSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ClassificationSecurityToken] DROP CONSTRAINT [FK_ClassificationSecurityTokens_Classification]
GO
ALTER TABLE [dbo].[ClassificationSecurityToken] DROP CONSTRAINT [FK_ClassificationSecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[ClassificationSecurityToken] DROP CONSTRAINT [FK_ClassificationSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ClassificationDataConceptXResourceItemSecurityToken] DROP CONSTRAINT [FK_ClassificationDataConceptXResourceItemSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[ClassificationDataConceptXResourceItemSecurityToken] DROP CONSTRAINT [FK_ClassificationDataConceptXResourceItemSecurityTokens_ClassificationDataConceptXResourceItem]
GO
ALTER TABLE [dbo].[ClassificationDataConceptXResourceItemSecurityToken] DROP CONSTRAINT [FK_ClassificationDataConceptXResourceItemSecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[ClassificationDataConceptXResourceItemSecurityToken] DROP CONSTRAINT [FK_ClassificationDataConceptXResourceItemSecurityToken_Systems]
GO
ALTER TABLE [dbo].[ClassificationDataConceptXResourceItemSecurityToken] DROP CONSTRAINT [FK_ClassificationDataConceptXResourceItemSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ClassificationDataConceptXResourceItem] DROP CONSTRAINT [FK_ClassificationDataConceptXResourceItem_Systems]
GO
ALTER TABLE [dbo].[ClassificationDataConceptXResourceItem] DROP CONSTRAINT [FK_ClassificationDataConceptXResourceItem_ResourceItem]
GO
ALTER TABLE [dbo].[ClassificationDataConceptXResourceItem] DROP CONSTRAINT [FK_ClassificationDataConceptXResourceItem_Enterprise]
GO
ALTER TABLE [dbo].[ClassificationDataConceptXResourceItem] DROP CONSTRAINT [FK_ClassificationDataConceptXResourceItem_ClassificationDataConcept]
GO
ALTER TABLE [dbo].[ClassificationDataConceptXResourceItem] DROP CONSTRAINT [FK_ClassificationDataConceptXResourceItem_ActiveFlags]
GO
ALTER TABLE [dbo].[ClassificationDataConceptXClassificationSecurityToken] DROP CONSTRAINT [FK_ClassificationDataConceptXClassificationSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[ClassificationDataConceptXClassificationSecurityToken] DROP CONSTRAINT [FK_ClassificationDataConceptXClassificationSecurityTokens_ClassificationDataConceptXClassification]
GO
ALTER TABLE [dbo].[ClassificationDataConceptXClassificationSecurityToken] DROP CONSTRAINT [FK_ClassificationDataConceptXClassificationSecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[ClassificationDataConceptXClassificationSecurityToken] DROP CONSTRAINT [FK_ClassificationDataConceptXClassificationSecurityToken_Systems]
GO
ALTER TABLE [dbo].[ClassificationDataConceptXClassificationSecurityToken] DROP CONSTRAINT [FK_ClassificationDataConceptXClassificationSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ClassificationDataConceptXClassification] DROP CONSTRAINT [FK_ClassificationDataConceptXClassification_Systems]
GO
ALTER TABLE [dbo].[ClassificationDataConceptXClassification] DROP CONSTRAINT [FK_ClassificationDataConceptXClassification_Enterprise]
GO
ALTER TABLE [dbo].[ClassificationDataConceptXClassification] DROP CONSTRAINT [FK_ClassificationDataConceptXClassification_ClassificationID]
GO
ALTER TABLE [dbo].[ClassificationDataConceptXClassification] DROP CONSTRAINT [FK_ClassificationDataConceptXClassification_ClassificationDataConcept]
GO
ALTER TABLE [dbo].[ClassificationDataConceptXClassification] DROP CONSTRAINT [FK_ClassificationDataConceptXClassification_ActiveFlags]
GO
ALTER TABLE [dbo].[ClassificationDataConceptSecurityToken] DROP CONSTRAINT [FK_ClassificationDataConceptSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ClassificationDataConceptSecurityToken] DROP CONSTRAINT [FK_ClassificationDataConceptSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[ClassificationDataConceptSecurityToken] DROP CONSTRAINT [FK_ClassificationDataConceptSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ClassificationDataConceptSecurityToken] DROP CONSTRAINT [FK_ClassificationDataConceptSecurityTokens_ClassificationDataConcept]
GO
ALTER TABLE [dbo].[ClassificationDataConceptSecurityToken] DROP CONSTRAINT [FK_ClassificationDataConceptSecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[ClassificationDataConceptSecurityToken] DROP CONSTRAINT [FK_ClassificationDataConceptSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ClassificationDataConcept] DROP CONSTRAINT [FK_ClassificationDataConcept_Systems]
GO
ALTER TABLE [dbo].[ClassificationDataConcept] DROP CONSTRAINT [FK_ClassificationDataConcept_OriginalSystems]
GO
ALTER TABLE [dbo].[ClassificationDataConcept] DROP CONSTRAINT [FK_ClassificationDataConcept_Enterprise]
GO
ALTER TABLE [dbo].[ClassificationDataConcept] DROP CONSTRAINT [FK_ClassificationDataConcept_ActiveFlags]
GO
ALTER TABLE [dbo].[Classification] DROP CONSTRAINT [FK_Classification_Systems]
GO
ALTER TABLE [dbo].[Classification] DROP CONSTRAINT [FK_Classification_ClassificationDataConcept]
GO
ALTER TABLE [dbo].[Classification] DROP CONSTRAINT [FK_Classification_ActiveFlags]
GO
ALTER TABLE [dbo].[Classification] DROP CONSTRAINT [FK__Classific__Syste__707E9C7C]
GO
ALTER TABLE [dbo].[Classification] DROP CONSTRAINT [FK__Classific__Syste__6F8A7843]
GO
ALTER TABLE [dbo].[Classification] DROP CONSTRAINT [FK__Classific__Syste__08411774]
GO
ALTER TABLE [dbo].[Classification] DROP CONSTRAINT [FK__Classific__Syste__074CF33B]
GO
ALTER TABLE [dbo].[Classification] DROP CONSTRAINT [FK__Classific__Enter__6E96540A]
GO
ALTER TABLE [dbo].[Classification] DROP CONSTRAINT [FK__Classific__Enter__6DA22FD1]
GO
ALTER TABLE [dbo].[Classification] DROP CONSTRAINT [FK__Classific__Enter__0658CF02]
GO
ALTER TABLE [dbo].[Classification] DROP CONSTRAINT [FK__Classific__Enter__0564AAC9]
GO
ALTER TABLE [dbo].[ArrangementXResourceItemSecurityToken] DROP CONSTRAINT [FK_ArrangementXResourceItemSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ArrangementXResourceItemSecurityToken] DROP CONSTRAINT [FK_ArrangementXResourceItemSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[ArrangementXResourceItemSecurityToken] DROP CONSTRAINT [FK_ArrangementXResourceItemSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ArrangementXResourceItemSecurityToken] DROP CONSTRAINT [FK_ArrangementXResourceItemSecurityTokens_ArrangementXResourceItem]
GO
ALTER TABLE [dbo].[ArrangementXResourceItemSecurityToken] DROP CONSTRAINT [FK_ArrangementXResourceItemSecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[ArrangementXResourceItemSecurityToken] DROP CONSTRAINT [FK_ArrangementXResourceItemSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ArrangementXResourceItem] DROP CONSTRAINT [FK_ArrangementXResourceItem_Systems]
GO
ALTER TABLE [dbo].[ArrangementXResourceItem] DROP CONSTRAINT [FK_ArrangementXResourceItem_ResourceItem]
GO
ALTER TABLE [dbo].[ArrangementXResourceItem] DROP CONSTRAINT [FK_ArrangementXResourceItem_Classification]
GO
ALTER TABLE [dbo].[ArrangementXResourceItem] DROP CONSTRAINT [FK_ArrangementXResourceItem_Arrangement]
GO
ALTER TABLE [dbo].[ArrangementXResourceItem] DROP CONSTRAINT [FK_ArrangementXResourceItem_ActiveFlags]
GO
ALTER TABLE [dbo].[ArrangementXResourceItem] DROP CONSTRAINT [FK__Arrangeme__Syste__5F891AA4]
GO
ALTER TABLE [dbo].[ArrangementXResourceItem] DROP CONSTRAINT [FK__Arrangeme__Syste__5E94F66B]
GO
ALTER TABLE [dbo].[ArrangementXResourceItem] DROP CONSTRAINT [FK__Arrangeme__Enter__5DA0D232]
GO
ALTER TABLE [dbo].[ArrangementXResourceItem] DROP CONSTRAINT [FK__Arrangeme__Enter__5CACADF9]
GO
ALTER TABLE [dbo].[ArrangementXProductSecurityToken] DROP CONSTRAINT [FK_ArrangementXProductSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ArrangementXProductSecurityToken] DROP CONSTRAINT [FK_ArrangementXProductSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[ArrangementXProductSecurityToken] DROP CONSTRAINT [FK_ArrangementXProductSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ArrangementXProductSecurityToken] DROP CONSTRAINT [FK_ArrangementXProductSecurityTokens_ArrangementXProduct]
GO
ALTER TABLE [dbo].[ArrangementXProductSecurityToken] DROP CONSTRAINT [FK_ArrangementXProductSecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[ArrangementXProductSecurityToken] DROP CONSTRAINT [FK_ArrangementXProductSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ArrangementXProduct] DROP CONSTRAINT [FK_ArrangementXProduct_Systems1]
GO
ALTER TABLE [dbo].[ArrangementXProduct] DROP CONSTRAINT [FK_ArrangementXProduct_Systems]
GO
ALTER TABLE [dbo].[ArrangementXProduct] DROP CONSTRAINT [FK_ArrangementXProduct_Product]
GO
ALTER TABLE [dbo].[ArrangementXProduct] DROP CONSTRAINT [FK_ArrangementXProduct_OriginalSystems]
GO
ALTER TABLE [dbo].[ArrangementXProduct] DROP CONSTRAINT [FK_ArrangementXProduct_Enterprise1]
GO
ALTER TABLE [dbo].[ArrangementXProduct] DROP CONSTRAINT [FK_ArrangementXProduct_Enterprise]
GO
ALTER TABLE [dbo].[ArrangementXProduct] DROP CONSTRAINT [FK_ArrangementXProduct_Classification]
GO
ALTER TABLE [dbo].[ArrangementXProduct] DROP CONSTRAINT [FK_ArrangementXProduct_ArrangementXProduct]
GO
ALTER TABLE [dbo].[ArrangementXProduct] DROP CONSTRAINT [FK_ArrangementXProduct_Arrangement]
GO
ALTER TABLE [dbo].[ArrangementXProduct] DROP CONSTRAINT [FK_ArrangementXProduct_ActiveFlags]
GO
ALTER TABLE [dbo].[ArrangementXInvolvedPartySecurityToken] DROP CONSTRAINT [FK_ArrangementXInvolvedPartySecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ArrangementXInvolvedPartySecurityToken] DROP CONSTRAINT [FK_ArrangementXInvolvedPartySecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[ArrangementXInvolvedPartySecurityToken] DROP CONSTRAINT [FK_ArrangementXInvolvedPartySecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ArrangementXInvolvedPartySecurityToken] DROP CONSTRAINT [FK_ArrangementXInvolvedPartySecurityTokens_ArrangementXInvolvedParty]
GO
ALTER TABLE [dbo].[ArrangementXInvolvedPartySecurityToken] DROP CONSTRAINT [FK_ArrangementXInvolvedPartySecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[ArrangementXInvolvedPartySecurityToken] DROP CONSTRAINT [FK_ArrangementXInvolvedPartySecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ArrangementXInvolvedParty] DROP CONSTRAINT [FK_ArrangementXInvolvedParty_Systems1]
GO
ALTER TABLE [dbo].[ArrangementXInvolvedParty] DROP CONSTRAINT [FK_ArrangementXInvolvedParty_Systems]
GO
ALTER TABLE [dbo].[ArrangementXInvolvedParty] DROP CONSTRAINT [FK_ArrangementXInvolvedParty_OriginalSystems]
GO
ALTER TABLE [dbo].[ArrangementXInvolvedParty] DROP CONSTRAINT [FK_ArrangementXInvolvedParty_InvolvedParty]
GO
ALTER TABLE [dbo].[ArrangementXInvolvedParty] DROP CONSTRAINT [FK_ArrangementXInvolvedParty_Enterprise]
GO
ALTER TABLE [dbo].[ArrangementXInvolvedParty] DROP CONSTRAINT [FK_ArrangementXInvolvedParty_Classification]
GO
ALTER TABLE [dbo].[ArrangementXInvolvedParty] DROP CONSTRAINT [FK_ArrangementXInvolvedParty_Arrangement]
GO
ALTER TABLE [dbo].[ArrangementXInvolvedParty] DROP CONSTRAINT [FK_ArrangementXInvolvedParty_ActiveFlags]
GO
ALTER TABLE [dbo].[ArrangementXClassificationSecurityToken] DROP CONSTRAINT [FK_ArrangementXClassificationSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ArrangementXClassificationSecurityToken] DROP CONSTRAINT [FK_ArrangementXClassificationSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[ArrangementXClassificationSecurityToken] DROP CONSTRAINT [FK_ArrangementXClassificationSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ArrangementXClassificationSecurityToken] DROP CONSTRAINT [FK_ArrangementXClassificationSecurityTokens_ArrangementXClassification]
GO
ALTER TABLE [dbo].[ArrangementXClassificationSecurityToken] DROP CONSTRAINT [FK_ArrangementXClassificationSecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[ArrangementXClassificationSecurityToken] DROP CONSTRAINT [FK_ArrangementXClassificationSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ArrangementXClassification] DROP CONSTRAINT [FK_ArrangementXClassification_Systems1]
GO
ALTER TABLE [dbo].[ArrangementXClassification] DROP CONSTRAINT [FK_ArrangementXClassification_Systems]
GO
ALTER TABLE [dbo].[ArrangementXClassification] DROP CONSTRAINT [FK_ArrangementXClassification_Enterprise]
GO
ALTER TABLE [dbo].[ArrangementXClassification] DROP CONSTRAINT [FK_ArrangementXClassification_ClassificationID]
GO
ALTER TABLE [dbo].[ArrangementXClassification] DROP CONSTRAINT [FK_ArrangementXClassification_Arrangement]
GO
ALTER TABLE [dbo].[ArrangementXClassification] DROP CONSTRAINT [FK_ArrangementXClassification_ActiveFlags]
GO
ALTER TABLE [dbo].[ArrangementXArrangementTypeSecurityToken] DROP CONSTRAINT [FK_ArrangementXArrangementTypeSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ArrangementXArrangementTypeSecurityToken] DROP CONSTRAINT [FK_ArrangementXArrangementTypeSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[ArrangementXArrangementTypeSecurityToken] DROP CONSTRAINT [FK_ArrangementXArrangementTypeSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ArrangementXArrangementTypeSecurityToken] DROP CONSTRAINT [FK_ArrangementXArrangementTypeSecurityTokens_ArrangementXArrangementType]
GO
ALTER TABLE [dbo].[ArrangementXArrangementTypeSecurityToken] DROP CONSTRAINT [FK_ArrangementXArrangementTypeSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ArrangementXArrangementTypeSecurityToken] DROP CONSTRAINT [FK__ArrangementXArrangementTypeSecurityToken__ActiveeFlag]
GO
ALTER TABLE [dbo].[ArrangementXArrangementType] DROP CONSTRAINT [FK_ArrangementXArrangementType_Systems]
GO
ALTER TABLE [dbo].[ArrangementXArrangementType] DROP CONSTRAINT [FK_ArrangementXArrangementType_OriginalSystems]
GO
ALTER TABLE [dbo].[ArrangementXArrangementType] DROP CONSTRAINT [FK_ArrangementXArrangementType_ArrangementType]
GO
ALTER TABLE [dbo].[ArrangementXArrangementType] DROP CONSTRAINT [FK_ArrangementXArrangementType_Arrangement]
GO
ALTER TABLE [dbo].[ArrangementXArrangementType] DROP CONSTRAINT [FK_ArrangementXArrangementType_ActiveFlags]
GO
ALTER TABLE [dbo].[ArrangementXArrangementType] DROP CONSTRAINT [FK__Arrangeme__Syste__1249A49B]
GO
ALTER TABLE [dbo].[ArrangementXArrangementType] DROP CONSTRAINT [FK__Arrangeme__Enter__11558062]
GO
ALTER TABLE [dbo].[ArrangementXArrangementSecurityToken] DROP CONSTRAINT [FK_ArrangementXArrangementSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ArrangementXArrangementSecurityToken] DROP CONSTRAINT [FK_ArrangementXArrangementSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[ArrangementXArrangementSecurityToken] DROP CONSTRAINT [FK_ArrangementXArrangementSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ArrangementXArrangementSecurityToken] DROP CONSTRAINT [FK_ArrangementXArrangementSecurityTokens_ArrangementXArrangement]
GO
ALTER TABLE [dbo].[ArrangementXArrangementSecurityToken] DROP CONSTRAINT [FK_ArrangementXArrangementSecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[ArrangementXArrangementSecurityToken] DROP CONSTRAINT [FK__Arrangeme__Enter__64817507]
GO
ALTER TABLE [dbo].[ArrangementXArrangementSecurityToken] DROP CONSTRAINT [FK__Arrangeme__Enter__638D50CE]
GO
ALTER TABLE [dbo].[ArrangementXArrangement] DROP CONSTRAINT [FK_ParentArrangement]
GO
ALTER TABLE [dbo].[ArrangementXArrangement] DROP CONSTRAINT [FK_ChildArrangement]
GO
ALTER TABLE [dbo].[ArrangementXArrangement] DROP CONSTRAINT [FK_ArrangementXArrangementType]
GO
ALTER TABLE [dbo].[ArrangementXArrangement] DROP CONSTRAINT [FK_ArrangementXArrangement_Systems]
GO
ALTER TABLE [dbo].[ArrangementXArrangement] DROP CONSTRAINT [FK_ArrangementXArrangement_OriginalSystems]
GO
ALTER TABLE [dbo].[ArrangementXArrangement] DROP CONSTRAINT [FK_ArrangementXArrangement_Classification]
GO
ALTER TABLE [dbo].[ArrangementXArrangement] DROP CONSTRAINT [FK_ArrangementXArrangement_ActiveFlags]
GO
ALTER TABLE [dbo].[ArrangementXArrangement] DROP CONSTRAINT [FK__Arrangeme__Syste__5BEC2F06]
GO
ALTER TABLE [dbo].[ArrangementXArrangement] DROP CONSTRAINT [FK__Arrangeme__Syste__5AF80ACD]
GO
ALTER TABLE [dbo].[ArrangementXArrangement] DROP CONSTRAINT [FK__Arrangeme__Enter__5A03E694]
GO
ALTER TABLE [dbo].[ArrangementXArrangement] DROP CONSTRAINT [FK__Arrangeme__Enter__590FC25B]
GO
ALTER TABLE [dbo].[ArrangementTypeSecurityToken] DROP CONSTRAINT [FK_ArrangementTypeSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ArrangementTypeSecurityToken] DROP CONSTRAINT [FK_ArrangementTypeSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[ArrangementTypeSecurityToken] DROP CONSTRAINT [FK_ArrangementTypeSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ArrangementTypeSecurityToken] DROP CONSTRAINT [FK_ArrangementTypeSecurityTokens_ArrangementType]
GO
ALTER TABLE [dbo].[ArrangementTypeSecurityToken] DROP CONSTRAINT [FK_ArrangementTypeSecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[ArrangementTypeSecurityToken] DROP CONSTRAINT [FK__Arrangeme__Enter__5356E905]
GO
ALTER TABLE [dbo].[ArrangementTypeSecurityToken] DROP CONSTRAINT [FK__Arrangeme__Enter__5262C4CC]
GO
ALTER TABLE [dbo].[ArrangementType] DROP CONSTRAINT [FK_ArrangementType_Systems]
GO
ALTER TABLE [dbo].[ArrangementType] DROP CONSTRAINT [FK_ArrangementType_OriginalSystems]
GO
ALTER TABLE [dbo].[ArrangementType] DROP CONSTRAINT [FK_ArrangementType_Enterprise]
GO
ALTER TABLE [dbo].[ArrangementType] DROP CONSTRAINT [FK_ArrangementType_ActiveFlags]
GO
ALTER TABLE [dbo].[ArrangementSecurityToken] DROP CONSTRAINT [FK_ArrangementSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ArrangementSecurityToken] DROP CONSTRAINT [FK_ArrangementSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[ArrangementSecurityToken] DROP CONSTRAINT [FK_ArrangementSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ArrangementSecurityToken] DROP CONSTRAINT [FK_ArrangementSecurityTokens_Arrangement]
GO
ALTER TABLE [dbo].[ArrangementSecurityToken] DROP CONSTRAINT [FK_ArrangementSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ArrangementSecurityToken] DROP CONSTRAINT [FK__ArrangementSecurity__ActiveFlag]
GO
ALTER TABLE [dbo].[Arrangement] DROP CONSTRAINT [FK_Arrangement_Systems1]
GO
ALTER TABLE [dbo].[Arrangement] DROP CONSTRAINT [FK_Arrangement_Systems]
GO
ALTER TABLE [dbo].[Arrangement] DROP CONSTRAINT [FK_Arrangement_Enterprise]
GO
ALTER TABLE [dbo].[Arrangement] DROP CONSTRAINT [FK_Arrangement_ActiveFlags]
GO
ALTER TABLE [dbo].[AddressXResourceItemSecurityToken] DROP CONSTRAINT [FK_AddressXResourceItemSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[AddressXResourceItemSecurityToken] DROP CONSTRAINT [FK_AddressXResourceItemSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[AddressXResourceItemSecurityToken] DROP CONSTRAINT [FK_AddressXResourceItemSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[AddressXResourceItemSecurityToken] DROP CONSTRAINT [FK_AddressXResourceItemSecurityTokens_AddressXResourceItem]
GO
ALTER TABLE [dbo].[AddressXResourceItemSecurityToken] DROP CONSTRAINT [FK_AddressXResourceItemSecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[AddressXResourceItemSecurityToken] DROP CONSTRAINT [FK_AddressXResourceItemSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[AddressXResourceItem] DROP CONSTRAINT [FK_AddressXResourceItem_Systems]
GO
ALTER TABLE [dbo].[AddressXResourceItem] DROP CONSTRAINT [FK_AddressXResourceItem_ResourceItem]
GO
ALTER TABLE [dbo].[AddressXResourceItem] DROP CONSTRAINT [FK_AddressXResourceItem_Enterprise]
GO
ALTER TABLE [dbo].[AddressXResourceItem] DROP CONSTRAINT [FK_AddressXResourceItem_Classification]
GO
ALTER TABLE [dbo].[AddressXResourceItem] DROP CONSTRAINT [FK_AddressXResourceItem_Address]
GO
ALTER TABLE [dbo].[AddressXResourceItem] DROP CONSTRAINT [FK_AddressXResourceItem_ActiveFlags]
GO
ALTER TABLE [dbo].[AddressXResourceItem] DROP CONSTRAINT [FK__AddressXR__Syste__72D0F942]
GO
ALTER TABLE [dbo].[AddressXResourceItem] DROP CONSTRAINT [FK__AddressXR__Syste__71DCD509]
GO
ALTER TABLE [dbo].[AddressXGeographySecurityToken] DROP CONSTRAINT [FK_AddressXGeographySecurityTokens_Systems]
GO
ALTER TABLE [dbo].[AddressXGeographySecurityToken] DROP CONSTRAINT [FK_AddressXGeographySecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[AddressXGeographySecurityToken] DROP CONSTRAINT [FK_AddressXGeographySecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[AddressXGeographySecurityToken] DROP CONSTRAINT [FK_AddressXGeographySecurityTokens_AddressXGeography]
GO
ALTER TABLE [dbo].[AddressXGeographySecurityToken] DROP CONSTRAINT [FK_AddressXGeographySecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[AddressXGeographySecurityToken] DROP CONSTRAINT [FK_AddressXGeographySecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[AddressXGeography] DROP CONSTRAINT [FK_AddressXGeography_Systems]
GO
ALTER TABLE [dbo].[AddressXGeography] DROP CONSTRAINT [FK_AddressXGeography_Link]
GO
ALTER TABLE [dbo].[AddressXGeography] DROP CONSTRAINT [FK_AddressXGeography_Enterprise]
GO
ALTER TABLE [dbo].[AddressXGeography] DROP CONSTRAINT [FK_AddressXGeography_Classification]
GO
ALTER TABLE [dbo].[AddressXGeography] DROP CONSTRAINT [FK_AddressXGeography_Address]
GO
ALTER TABLE [dbo].[AddressXGeography] DROP CONSTRAINT [FK_AddressXGeography_ActiveFlags]
GO
ALTER TABLE [dbo].[AddressXGeography] DROP CONSTRAINT [FK__AddressXG__Syste__6576FE24]
GO
ALTER TABLE [dbo].[AddressXGeography] DROP CONSTRAINT [FK__AddressXG__Syste__6482D9EB]
GO
ALTER TABLE [dbo].[AddressXClassificationSecurityToken] DROP CONSTRAINT [FK_AddressXClassificationSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[AddressXClassificationSecurityToken] DROP CONSTRAINT [FK_AddressXClassificationSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[AddressXClassificationSecurityToken] DROP CONSTRAINT [FK_AddressXClassificationSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[AddressXClassificationSecurityToken] DROP CONSTRAINT [FK_AddressXClassificationSecurityTokens_AddressXClassification]
GO
ALTER TABLE [dbo].[AddressXClassificationSecurityToken] DROP CONSTRAINT [FK_AddressXClassificationSecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[AddressXClassificationSecurityToken] DROP CONSTRAINT [FK_AddressXClassificationSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[AddressXClassification] DROP CONSTRAINT [FK_AddressXClassification_Systems1]
GO
ALTER TABLE [dbo].[AddressXClassification] DROP CONSTRAINT [FK_AddressXClassification_Systems]
GO
ALTER TABLE [dbo].[AddressXClassification] DROP CONSTRAINT [FK_AddressXClassification_Enterprise]
GO
ALTER TABLE [dbo].[AddressXClassification] DROP CONSTRAINT [FK_AddressXClassification_ClassificationID]
GO
ALTER TABLE [dbo].[AddressXClassification] DROP CONSTRAINT [FK_AddressXClassification_Address]
GO
ALTER TABLE [dbo].[AddressXClassification] DROP CONSTRAINT [FK_AddressXClassification_ActiveFlags]
GO
ALTER TABLE [dbo].[AddressSecurityToken] DROP CONSTRAINT [FK_AddressSecurityTokens_Systems1]
GO
ALTER TABLE [dbo].[AddressSecurityToken] DROP CONSTRAINT [FK_AddressSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[AddressSecurityToken] DROP CONSTRAINT [FK_AddressSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[AddressSecurityToken] DROP CONSTRAINT [FK_AddressSecurityTokens_Address]
GO
ALTER TABLE [dbo].[AddressSecurityToken] DROP CONSTRAINT [FK_AddressSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[AddressSecurityToken] DROP CONSTRAINT [FK__AddressSecurityToken__ActiveFlag]
GO
ALTER TABLE [dbo].[Address] DROP CONSTRAINT [FK_Address_Systems]
GO
ALTER TABLE [dbo].[Address] DROP CONSTRAINT [FK_Address_Enterprise]
GO
ALTER TABLE [dbo].[Address] DROP CONSTRAINT [FK_Address_Classification]
GO
ALTER TABLE [dbo].[Address] DROP CONSTRAINT [FK_Address_ActiveFlags]
GO
ALTER TABLE [dbo].[Address] DROP CONSTRAINT [FK__Address__SystemI__14659253]
GO
ALTER TABLE [dbo].[Address] DROP CONSTRAINT [FK__Address__SystemI__13716E1A]
GO
ALTER TABLE [dbo].[ActiveFlagXClassificationSecurityToken] DROP CONSTRAINT [FK_ActiveFlagXClassificationSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ActiveFlagXClassificationSecurityToken] DROP CONSTRAINT [FK_ActiveFlagXClassificationSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[ActiveFlagXClassificationSecurityToken] DROP CONSTRAINT [FK_ActiveFlagXClassificationSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ActiveFlagXClassificationSecurityToken] DROP CONSTRAINT [FK_ActiveFlagXClassificationSecurityTokens_ActiveFlagXClassification]
GO
ALTER TABLE [dbo].[ActiveFlagXClassificationSecurityToken] DROP CONSTRAINT [FK_ActiveFlagXClassificationSecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[ActiveFlagXClassificationSecurityToken] DROP CONSTRAINT [FK_ActiveFlagXClassificationSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ActiveFlagXClassification] DROP CONSTRAINT [FK_ActiveFlagXClassification_Systems1]
GO
ALTER TABLE [dbo].[ActiveFlagXClassification] DROP CONSTRAINT [FK_ActiveFlagXClassification_Systems]
GO
ALTER TABLE [dbo].[ActiveFlagXClassification] DROP CONSTRAINT [FK_ActiveFlagXClassification_Enterprise]
GO
ALTER TABLE [dbo].[ActiveFlagXClassification] DROP CONSTRAINT [FK_ActiveFlagXClassification_ClassificationID]
GO
ALTER TABLE [dbo].[ActiveFlagXClassification] DROP CONSTRAINT [FK_ActiveFlagXClassification_ActiveFlags]
GO
ALTER TABLE [dbo].[ActiveFlagXClassification] DROP CONSTRAINT [FK_ActiveFlagXClassification_ActiveFlag]
GO
ALTER TABLE [dbo].[ActiveFlagSecurityToken] DROP CONSTRAINT [FK_ActiveFlagSecurityTokens_Systems]
GO
ALTER TABLE [dbo].[ActiveFlagSecurityToken] DROP CONSTRAINT [FK_ActiveFlagSecurityTokens_SecurityToken]
GO
ALTER TABLE [dbo].[ActiveFlagSecurityToken] DROP CONSTRAINT [FK_ActiveFlagSecurityTokens_OriginalSystems]
GO
ALTER TABLE [dbo].[ActiveFlagSecurityToken] DROP CONSTRAINT [FK_ActiveFlagSecurityTokens_ActiveFlags]
GO
ALTER TABLE [dbo].[ActiveFlagSecurityToken] DROP CONSTRAINT [FK_ActiveFlagSecurityTokens_ActiveFlag]
GO
ALTER TABLE [dbo].[ActiveFlagSecurityToken] DROP CONSTRAINT [FK_ActiveFlagSecurityToken_Enterprise]
GO
ALTER TABLE [dbo].[ActiveFlagSecurityToken] DROP CONSTRAINT [FK_ActiveFlagSecurityToken_ActiveFlag1]
GO
ALTER TABLE [dbo].[ActiveFlagSecurityToken] DROP CONSTRAINT [FK_ActiveFlagSecurityToken_ActiveFlag]
GO
ALTER TABLE [dbo].[ActiveFlag] DROP CONSTRAINT [FK_ActiveFlag_Enterprise]
GO
DROP TABLE [dbo].[YesNoXClassificationSecurityToken]
GO
DROP TABLE [dbo].[YesNoXClassification]
GO
DROP TABLE [dbo].[YesNoSecurityToken]
GO
DROP TABLE [dbo].[YesNo]
GO
DROP TABLE [dbo].[SystemXClassificationSecurityToken]
GO
DROP TABLE [dbo].[SystemXClassification]
GO
DROP TABLE [dbo].[SystemsSecurityToken]
GO
DROP TABLE [dbo].[Systems]
GO
DROP TABLE [dbo].[SecurityTokenXClassificationSecurityToken]
GO
DROP TABLE [dbo].[SecurityTokenXClassification]
GO
DROP TABLE [dbo].[SecurityTokensSecurityToken]
GO
DROP TABLE [dbo].[ResourceItemXResourceItemTypeSecurityToken]
GO
DROP TABLE [dbo].[ResourceItemXResourceItemType]
GO
DROP TABLE [dbo].[ResourceItemXClassificationSecurityToken]
GO
DROP TABLE [dbo].[ResourceItemXClassification]
GO
DROP TABLE [dbo].[ResourceItemTypeSecurityToken]
GO
DROP TABLE [dbo].[ResourceItemType]
GO
DROP TABLE [dbo].[ResourceItemSecurityToken]
GO
DROP TABLE [dbo].[ResourceItemDataXClassificationSecurityToken]
GO
DROP TABLE [dbo].[ResourceItemDataXClassification]
GO
DROP TABLE [dbo].[ResourceItemDataSecurityToken]
GO
DROP TABLE [dbo].[ResourceItemData]
GO
DROP TABLE [dbo].[ResourceItem]
GO
DROP TABLE [dbo].[ProductXResourceItemSecurityToken]
GO
DROP TABLE [dbo].[ProductXResourceItem]
GO
DROP TABLE [dbo].[ProductXProductTypeSecurityToken]
GO
DROP TABLE [dbo].[ProductXProductType]
GO
DROP TABLE [dbo].[ProductXProductSecurityToken]
GO
DROP TABLE [dbo].[ProductXClassificationSecurityToken]
GO
DROP TABLE [dbo].[ProductXClassification]
GO
DROP TABLE [dbo].[ProductSecurityToken]
GO
DROP TABLE [dbo].[InvolvedPartyXResourceItemSecurityToken]
GO
DROP TABLE [dbo].[InvolvedPartyXResourceItem]
GO
DROP TABLE [dbo].[InvolvedPartyXProductSecurityToken]
GO
DROP TABLE [dbo].[InvolvedPartyXProduct]
GO
DROP TABLE [dbo].[InvolvedPartyXInvolvedPartyTypeSecurityToken]
GO
DROP TABLE [dbo].[InvolvedPartyXInvolvedPartyType]
GO
DROP TABLE [dbo].[InvolvedPartyXInvolvedPartySecurityToken]
GO
DROP TABLE [dbo].[InvolvedPartyXInvolvedPartyNameTypeSecurityToken]
GO
DROP TABLE [dbo].[InvolvedPartyXInvolvedPartyNameType]
GO
DROP TABLE [dbo].[InvolvedPartyXInvolvedPartyIdentificationTypeSecurityToken]
GO
DROP TABLE [dbo].[InvolvedPartyXInvolvedPartyIdentificationType]
GO
DROP TABLE [dbo].[InvolvedPartyXClassificationSecurityToken]
GO
DROP TABLE [dbo].[InvolvedPartyXClassification]
GO
DROP TABLE [dbo].[InvolvedPartyXAddressSecurityToken]
GO
DROP TABLE [dbo].[InvolvedPartyXAddress]
GO
DROP TABLE [dbo].[InvolvedPartyTypeSecurityToken]
GO
DROP TABLE [dbo].[InvolvedPartyType]
GO
DROP TABLE [dbo].[InvolvedPartySecurityToken]
GO
DROP TABLE [dbo].[InvolvedPartyOrganicTypeSecurityToken]
GO
DROP TABLE [dbo].[InvolvedPartyOrganicType]
GO
DROP TABLE [dbo].[InvolvedPartyOrganicSecurityToken]
GO
DROP TABLE [dbo].[InvolvedPartyOrganic]
GO
DROP TABLE [dbo].[InvolvedPartyNonOrganicSecurityToken]
GO
DROP TABLE [dbo].[InvolvedPartyNonOrganic]
GO
DROP TABLE [dbo].[InvolvedPartyNameTypeSecurityToken]
GO
DROP TABLE [dbo].[InvolvedPartyNameType]
GO
DROP TABLE [dbo].[InvolvedPartyIdentificationTypeSecurityToken]
GO
DROP TABLE [dbo].[InvolvedPartyIdentificationType]
GO
DROP TABLE [dbo].[GeographyXResourceItemSecurityToken]
GO
DROP TABLE [dbo].[GeographyXResourceItem]
GO
DROP TABLE [dbo].[GeographyXGeographySecurityToken]
GO
DROP TABLE [dbo].[GeographyXClassificationSecurityToken]
GO
DROP TABLE [dbo].[GeographyXClassification]
GO
DROP TABLE [dbo].[GeographySecurityToken]
GO
DROP TABLE [dbo].[EventXResourceItemSecurityToken]
GO
DROP TABLE [dbo].[EventXResourceItem]
GO
DROP TABLE [dbo].[EventXProductSecurityToken]
GO
DROP TABLE [dbo].[EventXProduct]
GO
DROP TABLE [dbo].[EventXInvolvedPartySecurityToken]
GO
DROP TABLE [dbo].[EventXInvolvedParty]
GO
DROP TABLE [dbo].[EventXGeographySecurityToken]
GO
DROP TABLE [dbo].[EventXGeography]
GO
DROP TABLE [dbo].[EventXEventTypeSecurityToken]
GO
DROP TABLE [dbo].[EventXEventType]
GO
DROP TABLE [dbo].[EventXClassificationSecurityToken]
GO
DROP TABLE [dbo].[EventXClassification]
GO
DROP TABLE [dbo].[EventXArrangementsSecurityToken]
GO
DROP TABLE [dbo].[EventXArrangement]
GO
DROP TABLE [dbo].[EventXAddressSecurityToken]
GO
DROP TABLE [dbo].[EventXAddress]
GO
DROP TABLE [dbo].[EventTypesSecurityToken]
GO
DROP TABLE [dbo].[EventType]
GO
DROP TABLE [dbo].[EventSecurityToken]
GO
DROP TABLE [dbo].[Event]
GO
DROP TABLE [dbo].[EnterpriseXClassificationSecurityToken]
GO
DROP TABLE [dbo].[EnterpriseXClassification]
GO
DROP TABLE [dbo].[EnterpriseSecurityToken]
GO
DROP TABLE [dbo].[Enterprise]
GO
DROP TABLE [dbo].[ClassificationXResourceItemSecurityToken]
GO
DROP TABLE [dbo].[ClassificationXResourceItem]
GO
DROP TABLE [dbo].[ClassificationXClassificationSecurityToken]
GO
DROP TABLE [dbo].[ClassificationSecurityToken]
GO
DROP TABLE [dbo].[ClassificationDataConceptXResourceItemSecurityToken]
GO
DROP TABLE [dbo].[ClassificationDataConceptXResourceItem]
GO
DROP TABLE [dbo].[ClassificationDataConceptXClassificationSecurityToken]
GO
DROP TABLE [dbo].[ClassificationDataConceptXClassification]
GO
DROP TABLE [dbo].[ClassificationDataConceptSecurityToken]
GO
DROP TABLE [dbo].[ClassificationDataConcept]
GO
DROP TABLE [dbo].[ArrangementXResourceItemSecurityToken]
GO
DROP TABLE [dbo].[ArrangementXResourceItem]
GO
DROP TABLE [dbo].[ArrangementXProductSecurityToken]
GO
DROP TABLE [dbo].[ArrangementXProduct]
GO
DROP TABLE [dbo].[ArrangementXInvolvedPartySecurityToken]
GO
DROP TABLE [dbo].[ArrangementXInvolvedParty]
GO
DROP TABLE [dbo].[ArrangementXClassificationSecurityToken]
GO
DROP TABLE [dbo].[ArrangementXClassification]
GO
DROP TABLE [dbo].[ArrangementXArrangementTypeSecurityToken]
GO
DROP TABLE [dbo].[ArrangementXArrangementType]
GO
DROP TABLE [dbo].[ArrangementXArrangementSecurityToken]
GO
DROP TABLE [dbo].[ArrangementTypeSecurityToken]
GO
DROP TABLE [dbo].[ArrangementType]
GO
DROP TABLE [dbo].[ArrangementSecurityToken]
GO
DROP TABLE [dbo].[AddressXResourceItemSecurityToken]
GO
DROP TABLE [dbo].[AddressXResourceItem]
GO
DROP TABLE [dbo].[AddressXGeographySecurityToken]
GO
DROP TABLE [dbo].[AddressXGeography]
GO
DROP TABLE [dbo].[AddressXClassificationSecurityToken]
GO
DROP TABLE [dbo].[AddressXClassification]
GO
DROP TABLE [dbo].[AddressSecurityToken]
GO
DROP TABLE [dbo].[Address]
GO
DROP TABLE [dbo].[ActiveFlagXClassificationSecurityToken]
GO
DROP TABLE [dbo].[ActiveFlagXClassification]
GO
DROP TABLE [dbo].[ActiveFlagSecurityToken]
GO
DROP TABLE [dbo].[ActiveFlag]
GO
DROP VIEW [dbo].[ProductHierarchyView]
GO
DROP TABLE [dbo].[Product]
GO
DROP TABLE [dbo].[ProductXProduct]
GO
DROP VIEW [dbo].[SecurityHierarchyView]
GO
DROP TABLE [dbo].[SecurityToken]
GO
DROP TABLE [dbo].[SecurityTokenXSecurityToken]
GO
DROP VIEW [dbo].[InvolvedPartyHierarchyView]
GO
DROP TABLE [dbo].[InvolvedPartyXInvolvedParty]
GO
DROP TABLE [dbo].[InvolvedParty]
GO
DROP VIEW [dbo].[GeographyHierarchyView]
GO
DROP TABLE [dbo].[GeographyXGeography]
GO
DROP TABLE [dbo].[Geography]
GO
DROP VIEW [dbo].[ArrangementHierarchyView]
GO
DROP TABLE [dbo].[ArrangementXArrangement]
GO
DROP TABLE [dbo].[Arrangement]
GO
DROP VIEW [dbo].[ClassificationHierarchyView]
GO
DROP TABLE [dbo].[ClassificationXClassification]
GO
DROP TABLE [dbo].[Classification]
GO
