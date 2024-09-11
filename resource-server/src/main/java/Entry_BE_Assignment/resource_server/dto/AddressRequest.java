package Entry_BE_Assignment.resource_server.dto;

import lombok.Getter;

@Getter
public class AddressRequest {

	private String zipCode;
	private String streetAddress;
	private String addressDetail;

}
