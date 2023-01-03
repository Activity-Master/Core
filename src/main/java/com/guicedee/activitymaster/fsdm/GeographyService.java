package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IGeographyService;
import com.guicedee.activitymaster.fsdm.client.services.annotations.ActivityMasterDB;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.geography.IGeography;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IProgressable;
import com.guicedee.activitymaster.fsdm.client.types.classifications.geography.GeographyFeatureClassesClassifications;
import com.guicedee.activitymaster.fsdm.client.types.exceptions.GeographyException;
import com.guicedee.activitymaster.fsdm.client.types.geography.*;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.geography.Geography;
import com.guicedee.activitymaster.fsdm.db.entities.geography.builders.GeographyQueryBuilder;
import com.guicedee.activitymaster.fsdm.geography.*;
import com.guicedee.activitymaster.fsdm.systems.GeographySystem;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotNull;

import java.util.*;

import static com.guicedee.activitymaster.fsdm.client.types.classifications.EnterpriseClassificationDataConcepts.*;
import static com.guicedee.activitymaster.fsdm.client.types.classifications.geography.GeographyClassifications.*;
import static com.guicedee.guicedinjection.GuiceContext.*;

public class GeographyService
		implements IProgressable,
		           IGeographyService<GeographyService>
{
	@Inject
	@ActivityMasterDB
	private EntityManager entityManager;
	
	@Override
	public IGeography<?,?> createPlanet(@CacheKey @NotNull String value, String originalUniqueID, ISystems<?,?> system, java.util.UUID... identityToken)
	{
		PlanetService service = get(PlanetService.class);
		return service.createPlanet(value, "The planet " + value, originalUniqueID, system, identityToken);
	}
	
	@Override
	public IGeography<?,?> createContinent(String planetName, GeographyContinent continent, ISystems<?,?> originatingSystem, String originalUniqueID, java.util.UUID... identityToken)
	{
		PlanetService planetService = get(PlanetService.class);
		IGeography<Geography,GeographyQueryBuilder> planet = planetService.findPlanet(planetName, originatingSystem, identityToken);
		ContinentService service = get(ContinentService.class);
		return service.createContinent(planet, continent.getContinentCode(), continent.getContinentName(), originalUniqueID, originatingSystem, identityToken);
	}
	
	
	@Override
	@CacheResult
	public IGeography<?,?> findPlanet(@CacheKey String name, @CacheKey ISystems<?,?> originatingSystem, @CacheKey java.util.UUID... identityToken)
	{
		PlanetService service = get(PlanetService.class);
		return service.findPlanet(name, originatingSystem, identityToken);
	}
	
	
	@Override
	@CacheResult
	public GeographyContinent findContinent(@CacheKey GeographyContinent continent, @CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		ContinentService service = get(ContinentService.class);
		IGeography<?,?> continentGeo = service.findContinent(continent.getContinentCode(), system, identityToken);
		GeographyContinent gc = new GeographyContinent();
		gc.setContinentCode(continentGeo.getName());
		gc.setContinentName(continentGeo.getDescription());
		gc.setGeographyId(continentGeo.getId());
		return gc;
	}
	
	@Override
	@CacheResult
	public GeographyCountry findCountry(@CacheKey GeographyCountry country, @CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		CountryService cs = get(CountryService.class);
		IGeography<?,?> geo = cs.findCountry(country.getIso(), system, identityToken);
		GeographyCountry gc = new GeographyCountry();
		/*
		List<Object[]> values = geo.builder(entityManager).getClassificationsValuePivot(CountryISO3166.toString(), (String)null, system, identityToken,
				CountryISO3166_3.toString(),
				CountryISO_Numeric.toString(),
				CountryFips.toString(),
				CountryCapital.toString(),
				CountryAreaInSqKm.toString(),
				CountryTld.toString(),
				CountryPhone.toString(),
				CountryPostalCodeFormat.toString(),
				CountryPostalCodeRegex.toString());
		
		
		if (!values.isEmpty())
		{
			Object[] vals = values.stream()
			                      .findFirst()
			                      .get();
			gc.setIso(vals[0].toString());
			gc.setIso3(vals[1].toString());
			gc.setIsoNumeric(vals[2].toString());
			gc.setFips(vals[3].toString());
			gc.setCapital(vals[4].toString());
			gc.setAreaSqlKM(vals[5].toString());
			gc.setWebTld(vals[6].toString());
			gc.setCountryDialCode(vals[7].toString());
			gc.setPostalCodeDecimalFormat(vals[8].toString());
			gc.setPostalCodeRegexFormat(vals[9].toString());
		}
		
		if (geo.hasClassifications(Currency,null, system, identityToken))
		{
			String currency = geo.findClassification(Currency, system, identityToken)
			                     .orElseThrow()
			                     .getValue();
			CurrencyService currencyService = get(CurrencyService.class);
			IClassification<?,?> geoCurrency = currencyService.findCurrency(currency, system, identityToken);
			GeographyCurrency geographyCurrency = new GeographyCurrency();
			geographyCurrency.setCurrencyCode(geoCurrency.getName());
			geographyCurrency.setCurrencyName(geoCurrency.getDescription());
			gc.setCurrency(geographyCurrency);
		}
		
		gc.setGeographyId(geo.getId());
		if (!geo.getOriginalSourceSystemUniqueID()
		        .isEmpty())
		{
			gc.setGeonameId(Long.parseLong(geo.getOriginalSourceSystemUniqueID()));
		}
		*/
		return gc;
	}
	
	public GeographyCountry createCountry(GeographyCountry country, ISystems<?,?> system, java.util.UUID... identityToken)
	{
		CountryService countryService = get(CountryService.class);
		IGeography<Geography,GeographyQueryBuilder> geoContinent = get(ContinentService.class).findContinent(country.getContinent()
		                                                                                                                                                           .getContinentCode(), system, identityToken);
		var geoCountry =
				countryService.createCountry(geoContinent, country.getIso(), country.getCountryName(), country.getGeonameId() + "", system, identityToken);
		
		
		IClassification<?,?> currency = get(ClassificationService.class).find(country.getCurrency()
		                                                                             .getCurrencyCode(), ClassificationXClassification.classificationValue(), system, identityToken);
		geoCountry.addOrUpdateClassification(Currency, currency.getName(), system, identityToken);
		
		
		countryService.updateCountry(currency, country.getIso(), country.getCountryName(), country.getIso3(), country.getIsoNumeric(), country.getCountryDialCode(),
				country.getFips(), country.getCapital(), country.getAreaSqlKM(), country.getPostalCodeDecimalFormat(), country.getPostalCodeRegexFormat(),
				country.getPopulation(), country.getWebTld(), system, identityToken);
		
		country.setGeographyId(geoCountry.getId());
		return country;
	}
	
	
	/**
	 * By timezone ID
	 * <p>
	 * like Asia/Baghdad
	 *
	 * @param timezone
	 * @param system
	 *
	 * @return
	 */
	@Override
	@CacheResult(cacheName = "GeographyTimezones")
	public GeographyTimezone findTimezone(@CacheKey GeographyTimezone timezone, @CacheKey ISystems<?,?> system)
	{
		UUID identityToken = GuiceContext.get(GeographySystem.class).getSystemToken(system.getEnterprise());
		TimeZoneService timeZoneService = get(TimeZoneService.class);
		
		IClassification<?,?> timeZoneClassification = timeZoneService.findTimeZone(timezone.getTimezoneID(), system, identityToken);
		GeographyTimezone tz = new GeographyTimezone();
		tz.setTimezoneID(timeZoneClassification.getName());
		/*List<Object[]> values = timeZoneClassification.builder(entityManager).getClassificationsValuePivot(TimeZoneRawOffset.toString(),(String) null, system, new UUID[]{identityToken},
				TimeZoneOffsetJuly2016.toString(),
				TimeZoneOffsetJan2016.toString());
		if (!values.isEmpty())
		{
			if (values.get(0)[0] != null)
			{
				tz.setRawOffset(Double.parseDouble(values.get(0)[0].toString()));
			}
			if (values.get(0)[1] != null)
			{
				tz.setOffsetJuly2016(Double.parseDouble(values.get(0)[1].toString()));
			}
			if (values.get(0)[2] != null)
			{
				tz.setOffsetJan2016(Double.parseDouble(values.get(0)[2].toString()));
			}
		}*/
		
		return tz;
	}
	
	
	////@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public GeographyTimezone create(GeographyTimezone timezone, ISystems<?,?> system)
	{
		UUID identityToken = GuiceContext.get(GeographySystem.class).getSystemToken(system.getEnterprise());
		TimeZoneService timeZoneService = get(TimeZoneService.class);
		timeZoneService.createTimeZone(timezone.getTimezoneID(), timezone.getTimezoneID(), null, system, identityToken);
		
		timeZoneService.updateTimeZone(timezone.getTimezoneID(), null,
				timezone.getRawOffset() + "", timezone.getOffsetJuly2016() + "", timezone.getOffsetJan2016() + "",
				system, identityToken);
		return timezone;
	}
	
	
	@Override
	@CacheResult(cacheName = "GeographyPostalCodes")
	public GeographyPostalCode findPostalCode(@CacheKey GeographyPostalCode postalCode, @CacheKey ISystems<?,?> system, java.util.UUID... identityToken)
	{
		PostalCodeService postalCodeService = get(PostalCodeService.class);
		IGeography<?,?> geo = postalCodeService.findPostalCode(null, postalCode.getPostalCode(), system, identityToken);
		GeographyPostalCode result = new GeographyPostalCode();
		result.setGeographyId(geo.getId());
		result.setPostalCodePlaceName(geo.getDescription());
		if (geo.hasClassifications(Latitude,null, system, identityToken))
		{
			String latitude = geo.findClassification(Latitude, system, identityToken)
			                     .orElseThrow(() -> new GeographyException("Postal Code loaded without latitude"))
			                     .getValue();
			String longitude = geo.findClassification(Longitude, system, identityToken)
			                      .orElseThrow(() -> new GeographyException("Postal Code loaded without latitude"))
			                      .getValue();
			result.setCoordinates(new GeographyCoordinates(latitude, longitude));
		}
		return result;
	}
	
	
	@Override
	@CacheResult(cacheName = "GeographyPostalCodesSuburb")
	public GeographyPostalCode findPostalCodeSuburb(@CacheKey String code, @CacheKey String description, @CacheKey ISystems<?,?> system, java.util.UUID... identityToken)
	{
		PostalCodeService postalCodeService = get(PostalCodeService.class);
		IGeography<?,?> geo = postalCodeService.findPostalCodeSuburb(code, description, system, identityToken);

		GeographyPostalCode result = new GeographyPostalCode();
		result.setGeographyId(geo.getId());
		result.setPostalCodePlaceName(geo.getDescription());
		if (geo.hasClassifications(Latitude, null,system, identityToken))
		{
			String latitude = geo.findClassification(Latitude, system, identityToken)
			                     .orElseThrow(() -> new GeographyException("Postal Code loaded without latitude"))
			                     .getValue();
			String longitude = geo.findClassification(Longitude, system, identityToken)
			                      .orElseThrow(() -> new GeographyException("Postal Code loaded without latitude"))
			                      .getValue();
			result.setCoordinates(new GeographyCoordinates(latitude, longitude));
		}
		return result;
	}
	
	@Override
	@CacheResult(cacheName = "GeographyPostalCodesSuburb")
	public GeographyPostalCode findOrCreatePostalCodeSuburb(@CacheKey String code, @CacheKey String description, @CacheKey ISystems<?,?> system, java.util.UUID... identityToken)
	{
		PostalCodeService postalCodeService = get(PostalCodeService.class);
		IGeography<?,?> geo = postalCodeService.findOrCreatePostalCodeSuburb(code, description, system, identityToken);
		GeographyPostalCode result = new GeographyPostalCode();
		result.setGeographyId(geo.getId());
		result.setPostalCodePlaceName(geo.getDescription());
		if (geo.hasClassifications(Latitude,null, system, identityToken))
		{
			String latitude = geo.findClassification(Latitude, system, identityToken)
			                     .orElseThrow(() -> new GeographyException("Postal Code loaded without latitude"))
			                     .getValue();
			String longitude = geo.findClassification(Longitude, system, identityToken)
			                      .orElseThrow(() -> new GeographyException("Postal Code loaded without latitude"))
			                      .getValue();
			result.setCoordinates(new GeographyCoordinates(latitude, longitude));
		}
		return result;
	}
	
	@Override
	@CacheResult(cacheName = "GeographyFindGeographyById")
	public IGeography<?,?> findGeographyById(@CacheKey String geographyID, @CacheKey ISystems<?,?> system, @CacheKey java.util.UUID... identityToken)
	{
		return new Geography().builder(entityManager)
		                      .find(geographyID.toString())
		                      .inActiveRange()
		                      .inDateRange()
		                      .withEnterprise(system.getEnterprise())
		                      .get(true)
		                      .orElse(null);
	}
	
	
	@CacheResult(cacheName = "GeographyFindFeatureClass")
	public IClassification<?,?> findFeatureClass(@CacheKey GeographyFeatureClassesClassifications key, @CacheKey ISystems<?,?> system, java.util.UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		return classificationService.find(key.toString(), system, identityToken);
	}
	
	/**
	 * Created with everything populated,
	 *
	 * @param featureCode
	 * @param system
	 * @param identityToken
	 */
	////@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public GeographyFeatureCode create(GeographyFeatureCode featureCode, ISystems<?,?> system, java.util.UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?,?> classification = classificationService.find(FeatureCodes, system, identityToken);
		classificationService.create(featureCode.getCode(), featureCode.getDescription(), FeatureCodes.concept().classificationValue(), system,  0, classification, identityToken);
		return featureCode;
	}
	
	
	@Override
	@CacheResult(cacheName = "GeographyfindFeatureCode")
	public GeographyFeatureCode findFeatureCode(@CacheKey String featureCode, @CacheKey ISystems<?,?> system, java.util.UUID... identityToken)
	{
		IClassification<?,?> fClass = findFeatureCodeClassification(featureCode, system, identityToken);
		GeographyFeatureCode fCode = new GeographyFeatureCode().setCode(fClass.getName())
		                                                       .setDescription(fClass.getDescription());
		return fCode;
	}
	
	@Override
	@CacheResult(cacheName = "GeographyfindFeatureCodeClassification")
	public IClassification<?,?> findFeatureCodeClassification(@CacheKey String featureCode, @CacheKey ISystems<?,?> system, java.util.UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		return classificationService.find(featureCode, system, identityToken);
	}
	
	private void addToMap(Long id, Long child, Map<Long, List<Long>> map)
	{
		if (!map.containsKey(id))
		{
			map.put(id, new ArrayList<>());
		}
		map.get(id)
		   .add(child);
	}
	
	/**
	 * Created with everything populated,
	 *
	 * @param geoData
	 * @param system
	 * @param identityToken
	 */
	////@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public GeoNameDefaultData<?> create(GeoNameDefaultData<?> geoData, IClassification<?,?> classification, ISystems<?,?> system, java.util.UUID... identityToken)
	{
		if (geoData.getGeonameId() == null)
		{
			geoData.setGeonameId(-1L);
		}
		
		Long exists = new Geography()
				.builder(entityManager)
				.withGeoNameID(geoData.getGeonameId()
				                      .toString())
				.getCount();
		if (exists == 0)
		{
			Geography geo = new Geography();
			geo.setName(geoData.getName());
			geo.setDescription(geoData.getAsciiname());
			if (geoData.getGeonameId() != -1L)
			{
				geo.setOriginalSourceSystemUniqueID(Long.toString(geoData.getGeonameId()));
			}
			
			geo.setEnterpriseID(system.getEnterprise());
			geo.setClassification((Classification) classification);
			geo.setSystemID(system);
			geo.setActiveFlagID(system.getActiveFlagID());
			geo.setOriginalSourceSystemID(system);
			
			geo.setClassification((Classification) classification);
			
			geo.persist(entityManager);
			geoData.setGeographyId(geo.getId());
			
				geo.createDefaultSecurity(system, identityToken);
			
			if (geoData.getCoordinates() != null)
			{
				geo.addClassification(Latitude.toString(), geoData.getCoordinates()
				                         .getLatitude(), system, identityToken);
				geo.addClassification(Longitude.toString(), geoData.getCoordinates()
				                          .getLongitude(), system, identityToken);
			}
			if (geoData.getFeatureCode() != null)
			{
				try
				{
					IClassification<?,?> featureCode = findFeatureCodeClassification(geoData.getFeatureCode()
					                                                                      .getCode(), system, identityToken);
					geo.addClassification(featureCode.getName(), "", system, identityToken);
				}
				catch (NoSuchElementException e)
				{
					//System.out.println("No feature code");
				}
			}
			if (geoData.getFeatureClass() != null)
			{
				try
				{
					IClassification<?,?> featureClass = findFeatureClass(geoData.getFeatureClass(), system, identityToken);
					geo.addClassification(featureClass.getName(), "", system, identityToken);
				}
				catch (NoSuchElementException e)
				{
					//	System.out.println("No feature code");
				}
			}
			
			if (geoData.getPopulation() != 0)
			{
				geo.addClassification(Population.toString(), Integer.toString(geoData.getPopulation()), system, identityToken);
			}
			if (geoData.getElevation() != 0)
			{
				geo.addClassification(Elevation.toString(), Integer.toString(geoData.getElevation()), system, identityToken);
			}
			if (geoData.getDem() != 0)
			{
				geo.addClassification(DEM.toString(), Integer.toString(geoData.getDem()), system, identityToken);
			}
		}
		return geoData;
	}
	
	@CacheResult(cacheName = "GeographyByGeoNameID")
	public Geography findGeographyByID(@CacheKey UUID geographyID)
	{
		if (geographyID == null)
		{
			return null;
		}
		return new Geography().builder(entityManager)
		                      .find(geographyID.toString())
		                      .get()
		                      .orElseThrow();
	}
}
