package moviebuddy;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class CaffeineTests {

	@Test
	void useCache() throws InterruptedException {
		// 캐시를 200ms 동안 유지하고, 최대 100개까지 저장해 둘 수 있는 카페인 캐시 객체 생성하기
		Cache<String, Object> cache = Caffeine.newBuilder()
				.expireAfterWrite(200, TimeUnit.MILLISECONDS)
				.maximumSize(100)
				.build();
		
		String key = "springrunner";
		Object value = new Object();		

		Assertions.assertNull(cache.getIfPresent(key));
		
		cache.put(key, value);		
		Assertions.assertEquals(value, cache.getIfPresent(key));
		
		TimeUnit.MILLISECONDS.sleep(100);
		Assertions.assertEquals(value, cache.getIfPresent(key));
		
		TimeUnit.MILLISECONDS.sleep(100);
		Assertions.assertNull(cache.getIfPresent(key));
	}
	
}
