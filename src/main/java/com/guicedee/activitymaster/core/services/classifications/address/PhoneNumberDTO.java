package com.guicedee.activitymaster.core.services.classifications.address;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.Strings;
import lombok.Data;

import javax.validation.ValidationException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Domain for any given valid phone number on a global scale
 */
@Data
public class PhoneNumberDTO {

	/**
	 * Built according to global definition
	 */
	private static final String GenericPhoneNumberRegex = "[\\+]?([0-9]{2})?([0| ])?((\\d{2,3}))[\\)]?[\\-|\\/| ]?(\\d{3})[\\-|\\/| ]?(\\d{4})\\+?(.*)?";
	public static final Pattern GenericPhoneNumberPattern = Pattern.compile(GenericPhoneNumberRegex);

	private String countryCode;
	private String spacialCode;
	private String providerCode;
	private String providerSpacialCode;
	private String providerCodeTwice;
	private String areaCode;
	private String identifer;

	private String value;

	private String extension;

	public PhoneNumberDTO() {
		// No config required
	}

	public PhoneNumberDTO(String value) throws ValidationException {
		assignNumber(value);
	}

	public static PhoneNumberDTO fromString(String value) throws ValidationException {
		PhoneNumberDTO dto = new PhoneNumberDTO();
		dto.assignNumber(value);
		return dto;
	}

	public PhoneNumberDTO assignNumber(String value) throws ValidationException {
		this.value = value;
		Matcher matcher = getPattern().matcher(value);
		match(matcher);
		return this;
	}

	/**
	 * Overridable for specific types
	 *
	 * @return
	 */
	public Pattern getPattern() {
		return GenericPhoneNumberPattern;
	}

	private void match(Matcher matcher) throws ValidationException {
		if (matcher.find()) {
			countryCode = matcher.group(1);
			spacialCode = matcher.group(2);
			String providerC = matcher.group(3);
			if (providerC == null) {
				providerC = "";
			}
			providerCode = Strings.padStart(providerC, 3, '0')
								  .replace("\\(", "")
								  .replace("\\)", "");
			providerSpacialCode = matcher.group(4);
			areaCode = matcher.group(5);
			identifer = matcher.group(6);
			extension = matcher.group(7);

			if (spacialCode == null || spacialCode.trim()
												  .isEmpty()) {
				spacialCode = "0";
			}
			if (providerCodeTwice == null || providerCodeTwice.trim()
															  .isEmpty()) {
				providerCodeTwice = providerCode;
			}
			if (countryCode == null || countryCode.trim()
												  .isEmpty()) {
				countryCode = "27";
			}
		}
		else {
			throw new ValidationException("The value in the matcher is not a phone number of any identifyable kind.");
		}
	}

	/**
	 * Returns the phone number in the format of 270812345678
	 *
	 * @return
	 */
	@JsonValue
	public String getCompleteNumber() {
		if (value != null) {
			return getCountryCode() + getSpacialCode() + getProviderCode().substring(1) + getAreaCode() + getIdentifer() + (!Strings.isNullOrEmpty(extension) ? "+" + extension : "");
		}
		return "";
	}

	public String getWhatsappFormat() {
		if (value != null) {
			return getCountryCode() + getProviderCode().substring(1) + getAreaCode() + getIdentifer();
		}
		return "";
	}

	/**
	 * Returns the number as a local number (no 27)
	 *
	 * @return
	 */
	public String toStringLocal() {
		return getCompleteNumber().replaceFirst("27", "0");
	}

	public String toString() {
		return getCompleteNumber();
	}


}
