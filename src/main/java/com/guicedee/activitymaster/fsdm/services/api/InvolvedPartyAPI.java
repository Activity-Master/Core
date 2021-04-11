package com.guicedee.activitymaster.fsdm.services.api;

import com.google.inject.Inject;
import com.guicedee.guicedinjection.pairing.Pair;
import jakarta.ws.rs.*;
import com.guicedee.activitymaster.fsdm.client.services.IInvolvedPartyService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;

import java.util.UUID;

@Path("/fsdm/ip")
public class InvolvedPartyAPI
{
	@Inject
	private IInvolvedPartyService<?> involvedPartyService;
	
	@GET
	@Path("/ping")
	public String ping()
	{
		return involvedPartyService == null ? "error" : "pong";
	}
	
	@GET
	@Path("/get")
	public IInvolvedParty<?, ?> get()
	{
		return involvedPartyService.get();
	}
	
	@GET
	@Path("/findByID")
	public IInvolvedParty<?, ?> findByID(@QueryParam("id") UUID id)
	{
		return involvedPartyService.findByID(id);
	}
	
	@POST
	@Path("/createNameType")
	public IInvolvedPartyNameType<?, ?> createNameType(@HeaderParam("name") String name,@HeaderParam("description") String description,
	                                                   @HeaderParam("system") ISystems<?, ?> system,@HeaderParam("identityToken") UUID identityToken)
	{
		return involvedPartyService.createNameType(name, description, system, identityToken);
	}
	
	@POST
	@Path("/createIdentificationType")
	public IInvolvedPartyIdentificationType<?, ?> createIdentificationType(ISystems<?, ?> system, String name, String description, UUID identityToken)
	{
		return involvedPartyService.createIdentificationType(system, name, description, identityToken);
	}
	
	@POST
	@Path("/createType")
	public IInvolvedPartyType<?, ?> createType(ISystems<?, ?> system, String name, String description, UUID identityToken)
	{
		return involvedPartyService.createType(system, name, description, identityToken);
	}
	
	@GET
	@Path("/findInvolvedPartyIdentificationType")
	public IInvolvedPartyIdentificationType<?, ?> findInvolvedPartyIdentificationType(String idType, ISystems<?, ?> system, UUID identityToken)
	{
		return involvedPartyService.findInvolvedPartyIdentificationType(idType, system, identityToken);
	}
	
	@POST
	@Path("/create")
	public IInvolvedParty<?, ?> create(ISystems<?, ?> system, Pair<String, String> idTypes, boolean isOrganic, UUID identityToken)
	{
		return involvedPartyService.create(system, idTypes, isOrganic, identityToken);
	}
	
	@GET
	@Path("/findType")
	public IInvolvedPartyType<?, ?> findType(String type, ISystems<?, ?> system, UUID tokens)
	{
		return involvedPartyService.findType(type, system, tokens);
	}
	
	@GET
	@Path("/findType")
	public IInvolvedPartyNameType<?, ?> findInvolvedPartyNameType(String nameType, ISystems<?, ?> system, UUID tokens)
	{
		return involvedPartyService.findInvolvedPartyNameType(nameType, system, tokens);
	}
	
	@GET
	@Path("/findByToken")
	public IInvolvedParty<?, ?> findByToken(ISecurityToken<?, ?> token, UUID tokens)
	{
		return involvedPartyService.findByToken(token,tokens);
	}
	
	@GET
	@Path("/find")
	public IInvolvedParty<?, ?> find(UUID uuid)
	{
		return involvedPartyService.find(uuid);
	}
	
	@GET
	@Path("/findType")
	public IInvolvedPartyType<?, ?> findType(UUID uuid)
	{
		return involvedPartyService.findType(uuid);
	}
	
	@GET
	@Path("/findNameType")
	public IInvolvedPartyNameType<?, ?> findNameType(UUID uuid)
	{
		return involvedPartyService.findNameType(uuid);
	}
	
	@GET
	@Path("/findIdentificationType")
	public IInvolvedPartyIdentificationType<?, ?> findIdentificationType(UUID uuid)
	{
		return involvedPartyService.findIdentificationType(uuid);
	}
}
