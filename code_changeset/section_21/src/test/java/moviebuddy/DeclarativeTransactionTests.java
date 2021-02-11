package moviebuddy;

import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import moviebuddy.domain.Movie;
import moviebuddy.domain.MovieReader;

@ActiveProfiles("examples")
@SpringJUnitConfig(classes = DeclarativeTransactionTests.ProxyTransactionManagementConfiguration.class)
public class DeclarativeTransactionTests {

	@Autowired
	MovieReader movieReader;

	@Test
	void useTransactional() {
		movieReader.loadMovies();
	}

	static class DatabaseMovieReader implements MovieReader {

		final Logger log = LoggerFactory.getLogger(getClass());

		// JDBC 트랜잭션 시작
		@Override
		@Transactional(readOnly = true)
		public List<Movie> loadMovies() {
			log.info("데이터베이스 처리");

			return Collections.emptyList();
		}
		// JDBC 트랜잭션 종료(커밋 또는 롤백)

	}

	@Profile("examples")
	@Configuration
	@EnableTransactionManagement
	static class ProxyTransactionManagementConfiguration {

		@Bean
		public MovieReader movieReader() {
			return new DatabaseMovieReader();
		}

		@Bean
		public DataSource dataSource() {
			return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
		}

		@Bean
		public PlatformTransactionManager transactionManager(DataSource dataSource) {
			return new DataSourceTransactionManager(dataSource);
		}
		
		/*		 
		@Bean
		public TransactionAttributeSource transactionAttributeSource() {
			return new AnnotationTransactionAttributeSource();
		}

		@Bean
		public TransactionInterceptor transactionInterceptor(TransactionAttributeSource transactionAttributeSource, PlatformTransactionManager transactionManager) {
			TransactionInterceptor interceptor = new TransactionInterceptor();
			interceptor.setTransactionAttributeSource(transactionAttributeSource);
			interceptor.setTransactionManager(transactionManager);
			return interceptor;
		}

		@Bean(name = TransactionManagementConfigUtils.TRANSACTION_ADVISOR_BEAN_NAME)
		public BeanFactoryTransactionAttributeSourceAdvisor transactionAdvisor(TransactionAttributeSource transactionAttributeSource, TransactionInterceptor transactionInterceptor) {
			BeanFactoryTransactionAttributeSourceAdvisor advisor = new BeanFactoryTransactionAttributeSourceAdvisor();
			advisor.setTransactionAttributeSource(transactionAttributeSource);
			advisor.setAdvice(transactionInterceptor);
			return advisor;
		}

		@Bean
		public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
			return new DefaultAdvisorAutoProxyCreator();
		}
		*/

	}

}
