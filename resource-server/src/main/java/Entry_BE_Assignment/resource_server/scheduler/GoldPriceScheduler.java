package Entry_BE_Assignment.resource_server.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import Entry_BE_Assignment.resource_server.service.GoldPriceService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GoldPriceScheduler {

	private final GoldPriceService goldPriceService;

	@Scheduled(cron = "0 0 19 * * ?", zone = "Asia/Seoul")  // 오후 7시
	public void updateGoldPriceAt7PM() {
		updateGoldPrices();
	}

	@Scheduled(cron = "0 30 23 * * ?", zone = "Asia/Seoul")  // 오후 11시 30분
	public void updateGoldPriceAt1130PM() {
		updateGoldPrices();
	}

	private void updateGoldPrices() {
		goldPriceService.updateGoldPrice();
	}

}
