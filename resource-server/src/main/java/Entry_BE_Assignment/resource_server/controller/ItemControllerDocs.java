package Entry_BE_Assignment.resource_server.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import Entry_BE_Assignment.resource_server.dto.BaseApiResponse;
import Entry_BE_Assignment.resource_server.dto.ItemDto;
import Entry_BE_Assignment.resource_server.dto.ItemRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Item", description = "상품 관련 API")
public interface ItemControllerDocs {

	@Operation(summary = "상품 생성", description = "판매자가 요청한 상품을 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "아이템이 성공적으로 생성되었습니다."),
		@ApiResponse(responseCode = "403", description = "해당 요청에 대한 권한이 없습니다.")
	})
	public BaseApiResponse<Void> createItem(
		ItemRequest itemRequest,
		String token);

	@Operation(summary = "특정상품 조회", description = "판매자가 요청한 상품을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "아이템이 성공적으로 처리되었습니다."),
		@ApiResponse(responseCode = "403", description = "해당 요청에 대한 권한이 없습니다.")
	})
	public ResponseEntity<ItemDto> getItemById(Long itemId);

	@Operation(summary = "모든 상품 조회", description = "모든 상품을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "아이템이 성공적으로 처리되었습니다.")
	})
	public BaseApiResponse<List<ItemDto>> getAllItems();

	@Operation(summary = "특정상품 수정", description = "판매자가 요청한 상품 정보를 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "아이템이 성공적으로 처리되었습니다."),
		@ApiResponse(responseCode = "403", description = "해당 요청에 대한 권한이 없습니다.")
	})
	public BaseApiResponse<Void> updateItem(
		Long itemId,
		ItemRequest itemRequest,
		String token);

	@Operation(summary = "특정상품 삭제", description = "판매자가 요청한 상품 정보를 삭제합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "아이템이 성공적으로 처리되었습니다."),
		@ApiResponse(responseCode = "403", description = "해당 요청에 대한 권한이 없습니다.")
	})
	public BaseApiResponse<Void> deleteItem(
		Long itemId,
		String token);
}
