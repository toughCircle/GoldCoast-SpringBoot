package Entry_BE_Assignment.resource_server.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Entry_BE_Assignment.resource_server.entity.GoldPrice;
import Entry_BE_Assignment.resource_server.enums.ItemType;
import Entry_BE_Assignment.resource_server.enums.StatusCode;
import Entry_BE_Assignment.resource_server.exception.customException.BusinessException;
import Entry_BE_Assignment.resource_server.repository.GoldPriceRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoldPriceService {

	private final RestTemplate restTemplate;
	private final GoldPriceRepository goldPriceRepository;

	@Value("${api.key}")
	private String apiKey;

	@Value("${api.baseUrl}")
	private String baseUrl;

	public String getGoldPriceInKRW() {
		String symbol = "XAU";
		String currency = "KRW";
		String url = String.format("%s/%s/%s", baseUrl, symbol, currency);

		HttpHeaders headers = new HttpHeaders();
		headers.set("x-access-token", apiKey);
		headers.set("Content-Type", "application/json");

		try {
			HttpEntity<String> entity = new HttpEntity<>(headers);
			ResponseEntity<String> response = restTemplate.exchange(
				url,
				HttpMethod.GET,
				entity,
				String.class
			);
			return response.getBody();
		} catch (Exception e) {
			System.out.println("Error occurred: " + e.getMessage());
			throw new BusinessException(StatusCode.INTERNAL_SERVER_ERROR);
		}
	}

	public int extractPriceFromResponse(String goldPriceResponse, String itemType) {
		try {
			// Jackson ObjectMapper를 사용하여 JSON 응답 파싱
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(goldPriceResponse);

			// 품목에 맞는 가격을 추출
			BigDecimal pricePerGram = BigDecimal.ZERO;
			switch (itemType) {
				case "GOLD_24":
					pricePerGram = rootNode.get("price_gram_24k").decimalValue();
					break;
				case "GOLD_22":
					pricePerGram = rootNode.get("price_gram_22k").decimalValue();
					break;
				case "GOLD_21":
					pricePerGram = rootNode.get("price_gram_21k").decimalValue();
					break;
				case "GOLD_18":
					pricePerGram = rootNode.get("price_gram_18k").decimalValue();
					break;
				default:
					throw new IllegalArgumentException("Unsupported item type: " + itemType);
			}

			// 소수점을 반올림하여 int로 변환
			return pricePerGram.setScale(0, RoundingMode.HALF_UP).intValue();
		} catch (Exception e) {
			// 예외 처리
			System.out.println("Error while extracting price: " + e.getMessage());
			return 0;  // 에러 발생 시 기본값 반환
		}
	}

	@Transactional
	public void updateGoldPrice() {
		// GoldAPI에서 현재 금 시세를 가져옴
		String goldPriceResponse = getGoldPriceInKRW();

		// 응답에서 필요한 금 시세 데이터 추출
		int pricePerGram24k = extractPriceFromResponse(goldPriceResponse, ItemType.GOLD_24.name());
		int pricePerGram22k = extractPriceFromResponse(goldPriceResponse, ItemType.GOLD_22.name());
		int pricePerGram21k = extractPriceFromResponse(goldPriceResponse, ItemType.GOLD_21.name());
		int pricePerGram18k = extractPriceFromResponse(goldPriceResponse, ItemType.GOLD_18.name());

		// 각 품목별 시세 저장 또는 업데이트
		saveGoldPrice(ItemType.GOLD_24, pricePerGram24k);
		saveGoldPrice(ItemType.GOLD_22, pricePerGram22k);
		saveGoldPrice(ItemType.GOLD_21, pricePerGram21k);
		saveGoldPrice(ItemType.GOLD_18, pricePerGram18k);
	}

	public void saveGoldPrice(ItemType goldType, int pricePerGram) {
		// 금 시세가 이미 저장되어 있는지 확인
		GoldPrice existingGoldPrice = goldPriceRepository.findByGoldType(goldType)
			.orElse(null);

		// 기존 데이터가 없는 경우 새로 저장
		if (existingGoldPrice == null) {
			GoldPrice goldPrice = GoldPrice.createGoldPrice(goldType, pricePerGram);
			goldPriceRepository.save(goldPrice);
		} else {
			// 기존 가격과 비교하여 다를 경우에만 업데이트
			if (existingGoldPrice.getPrice() != pricePerGram) {
				existingGoldPrice.updatePrice(pricePerGram);
			}
		}
	}

}

