package Entry_BE_Assignment.resource_server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AddressRequest {

	@Schema(description = "우편번호", example = "12345")
	private String zipCode;

	@Schema(description = "기본 주소", example = "서울특별시 강남구 테헤란로 123")
	private String streetAddress;

	@Schema(description = "상세 주소", example = "101호")
	private String addressDetail;

}
