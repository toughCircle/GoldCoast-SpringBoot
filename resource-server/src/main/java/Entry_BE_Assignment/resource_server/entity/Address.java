package Entry_BE_Assignment.resource_server.entity;

import jakarta.persistence.Column;
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
	@Column(nullable = false, length = 10)  // 우편번호는 최대 10자
	private String zipCode;

	@EqualsAndHashCode.Include
	@Column(nullable = false, length = 255)  // 기본 주소는 최대 255자
	private String streetAddress;

	@EqualsAndHashCode.Include
	@Column(length = 255)  // 상세 주소는 선택 사항
	private String addressDetail;

	public static Address createAddress(String zipCode, String streetAddress, String addressDetail) {
		return Address.builder()
			.zipCode(zipCode)
			.streetAddress(streetAddress)
			.addressDetail(addressDetail)
			.build();
	}

}
