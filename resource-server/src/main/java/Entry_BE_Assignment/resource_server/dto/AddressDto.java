package Entry_BE_Assignment.resource_server.dto;

import Entry_BE_Assignment.resource_server.entity.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
public class AddressDto {

	@Schema(description = "주소 ID", example = "1")
	Long id;

	@Schema(description = "우편번호", example = "12345")
	String zipCode;

	@Schema(description = "기본 주소", example = "서울특별시 강남구 테헤란로 123")
	String streetAddress;

	@Schema(description = "상세 주소", example = "101호")
	String addressDetail;

	public static AddressDto fromEntity(Address address) {
		return new AddressDto(
			address.getId(),
			address.getZipCode(),
			address.getStreetAddress(),
			address.getAddressDetail()
		);
	}
}
