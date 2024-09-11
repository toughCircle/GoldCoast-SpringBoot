package Entry_BE_Assignment.resource_server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false) // 사용자의 기본 주소 업데이트를 위한 해시 비교
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "addresses")
public class Address extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@EqualsAndHashCode.Include
	private String zipCode;

	@EqualsAndHashCode.Include
	private String streetAddress;

	@EqualsAndHashCode.Include
	private String addressDetail;

	public static Address createAddress(String zipCode, String streetAddress, String addressDetail) {
		return Address.builder()
			.zipCode(zipCode)
			.streetAddress(streetAddress)
			.addressDetail(addressDetail)
			.build();
	}

}
