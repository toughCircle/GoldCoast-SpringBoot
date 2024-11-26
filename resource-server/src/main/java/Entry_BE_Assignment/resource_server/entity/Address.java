package Entry_BE_Assignment.resource_server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "addresses", uniqueConstraints = @UniqueConstraint(columnNames = {"zipCode", "streetAddress",
	"addressDetail"}))
public class Address extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 5)  // 우편번호는 최대 5자
	private String zipCode;

	@Column(nullable = false)
	private String streetAddress;

	private String addressDetail;

	public static Address createAddress(String zipCode, String streetAddress, String addressDetail) {
		return Address.builder()
			.zipCode(zipCode)
			.streetAddress(streetAddress)
			.addressDetail(addressDetail)
			.build();
	}

}
