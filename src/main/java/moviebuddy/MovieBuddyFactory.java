package moviebuddy;

import java.util.concurrent.TimeUnit;

import javax.cache.annotation.CacheResult;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.github.benmanes.caffeine.cache.Caffeine;

import moviebuddy.cache.CachingAdvice;

@Configuration
@PropertySource("/application.properties")
@ComponentScan(basePackages = "moviebuddy")
@Import({ MovieBuddyFactory.DomainModuleConfig.class, MovieBuddyFactory.DataSourceModuleConfig.class })
public class MovieBuddyFactory {
	
	@Bean
	public Jaxb2Marshaller jaxb2Marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan("moviebuddy");

		return marshaller;
	}
	
	@Bean
    public CaffeineCacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(3, TimeUnit.SECONDS));

        return cacheManager;
    }
	
	@Bean
    public Advisor cachingAdvisor(CacheManager cacheManager) {
		// NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
		// pointcut.setMappedName("load*");
		
		AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(null, CacheResult.class);
		
        Advice advice = new CachingAdvice(cacheManager);

	    // Advisor = PointCut(대상 선정 알고리즘) + Advice(부가기능)
        return new DefaultPointcutAdvisor(pointcut, advice);
    }
	
	@Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		// 자동 프락시 생성 빈 후처리기는 스프링 컨테이너에 등록된 모든 Advisor를 찾아 프락시 생성시 사용한다
		return new DefaultAdvisorAutoProxyCreator();
	}
	

	@Configuration
	static class DomainModuleConfig {

	}

	@Configuration
	static class DataSourceModuleConfig {
		
	}

}
