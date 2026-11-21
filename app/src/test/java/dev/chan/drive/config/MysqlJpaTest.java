package dev.chan.drive.config;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * MySQL Testcontainers 기반의 JPA Repository 테스트를 위한 합성 애너테이션
 *
 * <p>다음 설정을 포함한다:
 *
 * <ul>
 *   <li>{@link DataJpaTest}: JPA slice 테스트 구성
 *   <li>{@link MySqlTestcontainersConfig}: MySQL 컨테이너 및 연결 설정
 *   <li>{@link QuerydslConfig}: JPAQueryFactory 빈 제공
 * </ul>
 *
 * <p>각 테스트는 기본적으로 트랜잭션 안에서 실행되며 종료 후 rollback된다. 데이터소스는 임베디드 DB로 대체하지 않으며, 실행 시 Docker가 필요하다.
 *
 * <p>Repository 매핑과 쿼리 검증에 사용한다. 실제 commit 이후 이벤트나 비동기 처리 검증에는 별도 통합 테스트를 사용한다.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DataJpaTest
@Inherited
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QuerydslConfig.class, MySqlTestcontainersConfig.class})
public @interface MysqlJpaTest {}
