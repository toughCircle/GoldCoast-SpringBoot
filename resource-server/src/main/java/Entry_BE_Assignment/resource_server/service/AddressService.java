package Entry_BE_Assignment.resource_server.service;

import org.springframework.stereotype.Service;

import Entry_BE_Assignment.resource_server.dto.AddressRequest;
import Entry_BE_Assignment.resource_server.entity.Address;
import Entry_BE_Assignment.resource_server.repository.AddressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {

	private final AddressRepository addressRepository;

	@Transactional
	public Address getOrCreateAddress(AddressRequest addressRequest) {
		return addressRepository.findByZipCodeAndStreetAddressAndAddressDetail(
				addressRequest.getZipCode(),
				addressRequest.getStreetAddress(),
				addressRequest.getAddressDetail())
			.orElseGet(() -> addressRepository.save(Address.createAddress(
				addressRequest.getZipCode(),
				addressRequest.getStreetAddress(),
				addressRequest.getAddressDetail())));
	}

}
