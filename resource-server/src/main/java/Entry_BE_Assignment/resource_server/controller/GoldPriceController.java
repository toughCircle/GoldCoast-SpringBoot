package Entry_BE_Assignment.resource_server.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Entry_BE_Assignment.resource_server.dto.BaseApiResponse;
import Entry_BE_Assignment.resource_server.dto.GoldPriceDto;
import Entry_BE_Assignment.resource_server.enums.StatusCode;
import Entry_BE_Assignment.resource_server.service.GoldPriceService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goldPrice")
public class GoldPriceController {

	private final GoldPriceService goldPriceService;

	@GetMapping
	public BaseApiResponse<List<GoldPriceDto>> getPrice() {
		List<GoldPriceDto> prices = goldPriceService.getPrices();
		return BaseApiResponse.of(HttpStatus.OK, StatusCode.SUCCESS.getMessage(), prices);
	}
}
