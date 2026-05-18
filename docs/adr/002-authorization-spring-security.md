# ADR-002: Spring Security를 인증/인가 프레임워크로 사용한다

## Status
Accepted

---

# Context

본 프로젝트는 Google Drive 스타일의 REST API 기반 문서 관리 시스템(Tiny Drive)을 목표로 한다.

사용자는 로그인 이후:
- 파일 조회
- 업로드
- 이동
- 삭제
- 다운로드

등의 API를 호출하게 되며, 서버는 요청마다 사용자를 식별해야 한다.

시스템은 Stateless 아키텍처를 목표로 하며, 서버 세션을 저장하지 않는다.

직접 Servlet Filter 또는 Interceptor를 구현하여 인증/인가를 처리할 수도 있으나,
보안 필터 체인과 인증 흐름을 직접 구현할 경우 다음 문제가 발생할 수 있다.

- 인증 누락 가능성
- 예외 처리 중복
- 보안 설정 분산
- 권한 처리 일관성 저하
- 향후 OAuth2/SSO 확장 어려움

따라서 Spring 생태계의 표준 보안 프레임워크 도입이 필요하다.

---

# Decision

다음과 같은 인증/인가 전략을 채택한다.

- 인증 및 인가 처리는 Spring Security를 사용한다.
- 인증 방식은 JWT 기반 Stateless 인증을 사용한다.
- 서버는 세션을 생성하지 않는다.
- 모든 인증 요청은 Authorization Header의 Bearer Token을 검증한다.
- 인증 사용자 정보는 SecurityContext를 통해 관리한다.
- SecurityFilterChain 기반으로 보안 정책을 구성한다.

---

# Rationale

## 1. Spring Security 채택 이유

### Positive

- Spring 생태계의 사실상 표준 보안 프레임워크
- 표준화된 Security Filter Chain 제공
- 인증/인가 책임 분리 가능
- Authentication/SecurityContext 기반 일관된 인증 모델 제공
- 향후 OAuth2/OIDC/SSO 확장 용이
- Method Security 확장 가능
- Spring Boot와 통합성이 우수함

### Negative

- 초기 학습 비용 존재
- 내부 동작 구조가 복잡함
- SecurityFilterChain, Authentication, Authorization 개념 이해 필요
- 디버깅 난이도가 상대적으로 높음

---

## 2. 직접 구현 방식 미채택 이유

### A. Servlet Filter 직접 구현

#### Positive

- 구조 단순
- 의존성 최소화 가능
- 원하는 방식으로 자유롭게 구현 가능

#### Negative

- 인증/인가 책임이 애플리케이션 코드에 분산됨
- 예외 처리 및 필터 순서 관리 복잡
- 보안 누락 가능성 존재
- 권한 처리 구조 확장 어려움
- 유지보수 비용 증가

---

### B. Spring Interceptor 기반 구현

#### Positive

- Spring MVC 구조와 자연스럽게 연결 가능
- 구현 난이도가 비교적 낮음

#### Negative

- SecurityContext 같은 표준 인증 모델 부재
- 인증/인가 책임 분리가 어려움
- Method Security 확장 어려움
- 보안 프레임워크 수준 기능 부족

---

# Consequences

## Positive

- 표준화된 인증/인가 구조 사용 가능
- Security Filter Chain 기반 보안 일관성 확보
- 인증 책임과 비즈니스 로직 분리 가능
- Stateless JWT 구조와 자연스럽게 통합 가능
- 향후 OAuth2/SSO/RBAC 확장 용이
- Spring Security 생태계 활용 가능

---

## Negative

- 초기 설정 복잡도 증가
- Spring Security 내부 구조 학습 필요
- JWT 인증 필터 별도 구현 필요
- AuthenticationEntryPoint / AccessDeniedHandler 등 예외 처리 구조 추가 필요
- 디버깅 시 Filter Chain 이해 필요

---

# Follow-up

MVP 이후 아래 기능을 검토한다.

- Method Security (`@PreAuthorize`)
- RBAC(Role-Based Access Control)
- OAuth2 Login
- SSO 연동
- Spring Authorization Server 연동
- 권한 정책 분리
- 멀티 테넌트 보안 정책 적용

---

# References

- https://docs.spring.io/spring-security/reference/index.html
- https://spring.io/projects/spring-security
- https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html
- https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html

---

# Date

2026-05-14