package Entry_BE_Assignment.resource_server.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Entry_BE_Assignment.resource_server.dto.ItemRequest;
import Entry_BE_Assignment.resource_server.entity.Item;
import Entry_BE_Assignment.resource_server.enums.StatusCode;
import Entry_BE_Assignment.resource_server.exception.customException.BusinessException;
import Entry_BE_Assignment.resource_server.grpc.UserResponse;
import Entry_BE_Assignment.resource_server.repository.ItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

	private final ItemRepository itemRepository;

	// 아이템 생성 (판매자만 가능)
	@Transactional
	public void createItem(ItemRequest itemRequest, UserResponse userResponse) {
		if (!userResponse.getRole().equals("SELLER")) {
			throw new BusinessException(StatusCode.FORBIDDEN);
		}

		Item item = Item.createItem(itemRequest.getItemType(), itemRequest.getPrice(), userResponse.getUserId());

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

		item.updateItem(itemRequest.getItemType(), itemRequest.getPrice());
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

