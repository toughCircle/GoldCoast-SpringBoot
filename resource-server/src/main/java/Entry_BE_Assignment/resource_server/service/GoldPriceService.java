package Entry_BE_Assignment.resource_server.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Entry_BE_Assignment.resource_server.enums.StatusCode;
import Entry_BE_Assignment.resource_server.exception.customException.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoldPriceService {

	private final RestTemplate restTemplate;

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
}

