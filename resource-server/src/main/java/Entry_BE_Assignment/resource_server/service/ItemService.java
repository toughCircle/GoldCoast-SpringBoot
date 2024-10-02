package Entry_BE_Assignment.resource_server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Entry_BE_Assignment.resource_server.dto.ItemRequest;
import Entry_BE_Assignment.resource_server.entity.GoldPrice;
import Entry_BE_Assignment.resource_server.entity.Item;
import Entry_BE_Assignment.resource_server.enums.StatusCode;
import Entry_BE_Assignment.resource_server.exception.customException.BusinessException;
import Entry_BE_Assignment.resource_server.grpc.UserResponse;
import Entry_BE_Assignment.resource_server.repository.GoldPriceRepository;
import Entry_BE_Assignment.resource_server.repository.ItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

	private final ItemRepository itemRepository;
	private final GoldPriceService goldPriceService;
	private final GoldPriceRepository goldPriceRepository;

	// 아이템 생성 (판매자만 가능)
	@Transactional
	public void createItem(ItemRequest itemRequest, UserResponse userResponse) {
		if (!userResponse.getRole().equals("SELLER")) {
			throw new BusinessException(StatusCode.FORBIDDEN);
		}

		// 금 가격 조회
		Optional<GoldPrice> goldPriceOptional = goldPriceRepository.findByGoldType(itemRequest.getItemType());

		int pricePerGram;

		if (goldPriceOptional.isPresent()) {
			// 금 가격이 존재하면 해당 가격 사용
			pricePerGram = goldPriceOptional.get().getPrice();
		} else {
			// 저장된 금 가격이 없을 경우 API 호출하여 조회 및 저장
			String goldPriceResponse = goldPriceService.getGoldPriceInKRW();  // API 호출
			pricePerGram = goldPriceService.extractPriceFromResponse(goldPriceResponse,
				itemRequest.getItemType().name());

			// 해당 타입의 금 시세 저장
			goldPriceService.saveGoldPrice(itemRequest.getItemType(), pricePerGram);
		}

		Item item = Item.createItem(itemRequest.getItemType(), pricePerGram, itemRequest.getQuantity(),
			userResponse.getUserId());

		itemRepository.save(item);
	}

	// 아이템 조회 (모든 사용자 가능)
	public Item getItemById(Long itemId) {
		return itemRepository.findById(itemId)
			.orElseThrow(() -> new BusinessException(StatusCode.ITEM_SUCCESS));
	}

	// 전체 아이템 목록 조회
	public List<Item> getAllItems() {
		return itemRepository.findAll();
	}

	// 아이템 수정 (판매자만 가능)
	@Transactional
	public void updateItem(Long itemId, ItemRequest itemRequest, UserResponse userResponse) {
		Item item = getItemById(itemId);

		if (!item.getSellerId().equals(userResponse.getUserId())) {
			throw new BusinessException(StatusCode.FORBIDDEN);
		}

		item.updateItem(itemRequest.getItemType(), itemRequest.getQuantity());
	}

	// 아이템 삭제 (판매자만 가능)
	@Transactional
	public void deleteItem(Long itemId, UserResponse userResponse) {
		Item item = getItemById(itemId);

		if (!item.getSellerId().equals(userResponse.getUserId())) {
			throw new BusinessException(StatusCode.FORBIDDEN);
		}

		itemRepository.delete(item);
	}
}

