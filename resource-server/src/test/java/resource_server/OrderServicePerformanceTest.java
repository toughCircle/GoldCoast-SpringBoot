package resource_server;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import Entry_BE_Assignment.resource_server.ResourceServerApplication;
import Entry_BE_Assignment.resource_server.dto.AddressRequest;
import Entry_BE_Assignment.resource_server.dto.OrderItemRequest;
import Entry_BE_Assignment.resource_server.dto.OrderRequest;
import Entry_BE_Assignment.resource_server.entity.Item;
import Entry_BE_Assignment.resource_server.enums.ItemType;
import Entry_BE_Assignment.resource_server.grpc.UserResponse;
import Entry_BE_Assignment.resource_server.repository.AddressRepository;
import Entry_BE_Assignment.resource_server.repository.ItemRepository;
import Entry_BE_Assignment.resource_server.repository.OrderRepository;
import Entry_BE_Assignment.resource_server.service.OrderService;

@SpringBootTest(classes = ResourceServerApplication.class)
@ActiveProfiles("test")
public class OrderServicePerformanceTest {

	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private OrderService orderService;

	private static final int THREAD_COUNT = 100;       // 동시 요청 쓰레드 수
	private static final int REQUESTS_PER_THREAD = 10; // 각 쓰레드당 요청 수
	private static Long ITEM_ID;           // 테스트 상품 ID

	// 실패 횟수 추적
	private final AtomicInteger failureCount = new AtomicInteger(0);

	@BeforeEach
	public void setup() {
		orderRepository.deleteAll();
		addressRepository.deleteAll();
		itemRepository.deleteAll();

		// given: 테스트 데이터를 초기화
		Item item = Item.createItem(ItemType.GOLD_24,
			10000, BigDecimal.valueOf(10000), 1L);
		itemRepository.save(item);
		ITEM_ID = item.getId();
	}

	@Test
	public void testInventoryUpdateFailures() throws InterruptedException {
		// given: 테스트 준비
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
		BigDecimal decrement = new BigDecimal("1.5"); // 감소할 수량
		long startTime = System.currentTimeMillis();

		// when: 여러 쓰레드에서 동시에 주문 요청
		for (int i = 0; i < THREAD_COUNT; i++) {
			executor.submit(() -> {
				for (int j = 0; j < REQUESTS_PER_THREAD; j++) {
					try {
						int uniqueJ = (int)(j + (THREAD_COUNT * Thread.currentThread().getId()));
						// given: 주문 요청 데이터 생성
						OrderRequest request = createOrderRequest(
							decrement,
							"12345",
							"서울특별시 강남구 테헤란로 123-" + uniqueJ,
							uniqueJ + "호"
						);

						// when: 주문 처리
						orderService.createOrder(request, createMockUserResponse());
					} catch (Exception e) {
						// then: 실패 시 실패 횟수 증가 및 로그 출력
						System.err.println("Request failed: " + e.getMessage());
						e.printStackTrace();
						failureCount.incrementAndGet();
					}
				}
			});
		}

		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.MINUTES);

		long endTime = System.currentTimeMillis();

		// then: 결과 검증
		int totalRequests = THREAD_COUNT * REQUESTS_PER_THREAD;
		int successfulRequests = totalRequests - failureCount.get();

		System.out.println("Total requests: " + totalRequests);
		System.out.println("Total failures: " + failureCount.get());
		System.out.println("Total time taken: " + (endTime - startTime) + "ms");

		// 실패율 검증
		assertTrue(failureCount.get() < totalRequests * 0.1, // 실패율 10% 이내로 제한
			"Failure count is too high!");

		// 저장된 주문 수 검증
		long savedOrders = orderRepository.count();
		assertEquals(successfulRequests, savedOrders,
			"Saved orders count mismatch!");

		// 재고 검증
		Item item = itemRepository.findById(ITEM_ID).orElseThrow();

		BigDecimal expectedQuantity = BigDecimal.valueOf(10000)
			.subtract(decrement.multiply(BigDecimal.valueOf(totalRequests)))
			.setScale(2, RoundingMode.HALF_UP);
		System.out.println("Expected Quantity: " + expectedQuantity);
		System.out.println("Actual Quantity: " + item.getQuantity());
		assertEquals(expectedQuantity, item.getQuantity(),
			"Quantity mismatch!");

	}

	// OrderRequest 객체 생성 메서드
	private OrderRequest createOrderRequest(BigDecimal quantity, String zipCode, String streetAddress,
		String addressDetail) {
		// OrderItemRequest 생성
		OrderItemRequest orderItem = new OrderItemRequest();
		orderItem.setItemId(ITEM_ID);
		orderItem.setQuantity(quantity);

		List<OrderItemRequest> orderItems = new ArrayList<>();
		orderItems.add(orderItem);

		// AddressRequest 생성
		AddressRequest address = new AddressRequest();
		address.setZipCode(zipCode);
		address.setStreetAddress(streetAddress);
		address.setAddressDetail(addressDetail);

		// OrderRequest 생성
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setOrderItems(orderItems);
		orderRequest.setAddress(address);

		return orderRequest;
	}

	private UserResponse createMockUserResponse() {
		return UserResponse.newBuilder()
			.setUserId(1001L)             // 사용자 ID 설정
			.setUsername("John Doe")      // 사용자 이름 설정
			.setEmail("john.doe@example.com") // 이메일 설정
			.setRole("BUYER")             // 사용자 역할 설정
			.build();                     // UserResponse 객체 생성
	}
}
