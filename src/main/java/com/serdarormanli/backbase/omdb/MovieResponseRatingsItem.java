package com.serdarormanli.backbase.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MovieResponseRatingsItem(

	@JsonProperty("Value")
	String value,

	@JsonProperty("Source")
	String source
) {
}