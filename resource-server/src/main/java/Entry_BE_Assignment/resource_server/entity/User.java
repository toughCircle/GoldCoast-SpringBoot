package Entry_BE_Assignment.resource_server.entity;

import java.util.ArrayList;
import java.util.List;

import Entry_BE_Assignment.resource_server.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "users")
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;

	@Column(nullable = false, unique = true)
	private Long authUserId;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "default_address_id", referencedColumnName = "id")
	private Address defaultAddress;

	@Column(nullable = true)
	private String phoneNumber;

	@Column(nullable = false)
	private Role userType;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Order> orders = new ArrayList<>();

	// 구매자 생성
	public static User createBuyer(Long authUserId, String phoneNumber) {
		return User.builder()
			.authUserId(authUserId)
			.phoneNumber(phoneNumber)
			.userType(Role.BUYER)
			.build();
	}

	// 판매자 생성
	public static User createSeller(Long authUserId, String phoneNumber) {
		return User.builder()
			.authUserId(authUserId)
			.phoneNumber(phoneNumber)
			.userType(Role.SELLER)
			.build();
	}

	// 기본 주소 설정 메서드
	public void updateDefaultAddress(Address address) {
		if (this.defaultAddress != null && this.defaultAddress.equals(address)) {
			return;
		}
		this.defaultAddress = address;
	}

}
