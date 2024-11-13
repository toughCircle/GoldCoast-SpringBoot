package Entry_BE_Assignment.resource_server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Entry_BE_Assignment.resource_server.dto.BaseApiResponse;
import Entry_BE_Assignment.resource_server.dto.ItemDto;
import Entry_BE_Assignment.resource_server.dto.ItemRequest;
import Entry_BE_Assignment.resource_server.enums.StatusCode;
import Entry_BE_Assignment.resource_server.grpc.AuthServiceGrpc;
import Entry_BE_Assignment.resource_server.grpc.TokenRequest;
import Entry_BE_Assignment.resource_server.grpc.UserResponse;
import Entry_BE_Assignment.resource_server.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
@Slf4j
public class ItemController {

	private final ItemService itemService;
	private final AuthServiceGrpc.AuthServiceBlockingStub authServiceStub;  // gRPC 클라이언트

	// 아이템 생성 (판매자만 가능)
	@PostMapping
	public BaseApiResponse<Void> createItem(
		@RequestHeader("Authorization") String token,
		@RequestBody ItemRequest itemRequest) {

		UserResponse userResponse = getUserResponse(token);
		log.info(userResponse.getUsername());
		itemService.createItem(itemRequest, userResponse);
		return BaseApiResponse.of(StatusCode.ITEM_CREATED);
	}

	// 아이템 조회 (모든 사용자 가능)
	@GetMapping("/{itemId}")
	public BaseApiResponse<ItemDto> getItemById(@PathVariable Long itemId) {
		ItemDto item = itemService.getItemById(itemId);
		return BaseApiResponse.of(StatusCode.ITEM_SUCCESS, item);
	}

	// 전체 아이템 목록 조회 (모든 사용자 가능)
	@GetMapping
	public BaseApiResponse<List<ItemDto>> getAllItems() {
		List<ItemDto> items = itemService.getAllItems();
		return BaseApiResponse.of(StatusCode.ITEM_SUCCESS, items);
	}

	@GetMapping("/seller")
	public BaseApiResponse<List<ItemDto>> getAllSellerItems(
		@RequestHeader("Authorization") String token
	) {
		UserResponse userResponse = getUserResponse(token);
		List<ItemDto> allSellerItems = itemService.getAllSellerItems(userResponse);
		return BaseApiResponse.of(StatusCode.ITEM_SUCCESS, allSellerItems);
	}

	// 아이템 수정 (판매자만 가능)
	@PatchMapping("/{itemId}")
	public BaseApiResponse<Void> updateItem(
		@PathVariable Long itemId,
		@RequestBody ItemRequest itemRequest,
		@RequestHeader("Authorization") String token) {

		UserResponse userResponse = getUserResponse(token);
		itemService.updateItem(itemId, itemRequest, userResponse);
		return BaseApiResponse.of(StatusCode.ITEM_SUCCESS);
	}

	// 아이템 삭제 (판매자만 가능)
	@DeleteMapping("/{itemId}")
	public BaseApiResponse<Void> deleteItem(
		@PathVariable Long itemId,
		@RequestHeader("Authorization") String token) {

		UserResponse userResponse = getUserResponse(token);
		itemService.deleteItem(itemId, userResponse);
		return BaseApiResponse.of(StatusCode.ITEM_SUCCESS);
	}

	private UserResponse getUserResponse(String token) {
		String jwtToken = token.substring(7);
		TokenRequest tokenRequest = TokenRequest.newBuilder().setToken(jwtToken).build();
		return authServiceStub.validateToken(tokenRequest);
	}
}
