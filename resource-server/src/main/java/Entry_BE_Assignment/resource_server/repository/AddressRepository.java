package Entry_BE_Assignment.resource_server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Entry_BE_Assignment.resource_server.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
	Optional<Address> findByZipCodeAndStreetAddressAndAddressDetail(String zipCode, String streetAddress,
		String addressDetail);
}
