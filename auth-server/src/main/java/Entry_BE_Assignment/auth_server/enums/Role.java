package Entry_BE_Assignment.auth_server.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {

	SELLER,
	BUYER;

	@JsonCreator
	public static Role from(String value) {
		return Role.valueOf(value.toUpperCase());
	}

	@JsonValue
	public String toValue() {
		return this.name();
	}

}