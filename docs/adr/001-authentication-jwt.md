# ADR-001: MVP 인증 구조로 Stateless JWT 인증을 채택한다

## Status
Accepted

---

# Context

본 프로젝트는 Google Drive 스타일의 문서 관리 시스템(Tiny Drive)을 목표로 한다.

현재 MVP 단계에서는 아래 기능을 우선 검증한다.

- 회원가입
- 로그인
- 파일/폴더 생성
- 목록 조회
- 이동
- 휴지통
- 복원
- 다운로드

인증 시스템은 Stateless 기반 구조를 목표로 하며,  
초기 단계에서는 인증 복잡도를 최소화하고 핵심 도메인 개발에 집중할 필요가 있다.
향후 S3 도입 이전 단계에서는 파일 바이너리를 관리하는 FileStorage 서버와 메타데이터를 관리하는 Drive 서버를 분리 운영하는 구조를 고려한다.
따라서 서버 간 인증 상태를 공유하지 않는 Stateless 기반 인증 구조가 필요하다.

Refresh Token 저장소 관리, 토큰 회전(Token Rotation), 로그아웃 무효화, 멀티 디바이스 세션 관리 등은 인증 고도화 영역으로 판단하였다.

---

# Decision

다음과 같은 인증 전략을 채택한다.

- MVP 단계에서는 Refresh Token을 도입하지 않는다.
- Access Token 기반 JWT 인증만 사용한다.
- JWT 라이브러리로 JJWT를 사용한다.
- 서버는 JWT 자체를 저장하지 않는다.
- 서버는 JWT의 아래 항목만 검증한다.
    - Signature
    - Expiration
    - Claims
- 인증 상태는 서버 세션에 저장하지 않는다.

---

# Rationale

## 1. Access Token Only 전략 채택 이유

### Positive

- 인증 구조 단순화 가능
- Stateless 아키텍처 유지 가능
- MVP 단계에서 구현 속도 향상
- 서버 세션 저장소 불필요
- 핵심 파일/폴더 도메인 개발에 집중 가능

### Negative

- 로그아웃 시 즉시 무효화 어려움
- 토큰 탈취 시 만료 전까지 사용 가능
- 재발급(Refresh) 기능 부재
- 장기 로그인 UX 제한

---

## 2. JWT 라이브러리 비교

### A. Nimbus JOSE + JWT

#### Positive

- OAuth2/OpenID Connect 친화적
- JWK/JWKS 지원
- RSA/ECDSA 지원 우수
- JWT 검증 기능 강력
- Spring Security OAuth2 진영에서 사실상 표준

#### Negative

- 구현 복잡도가 높음
- OAuth2 중심 개념 이해 필요
- MVP 단계에서는 과도한 복잡도 발생 가능

---

### B. Auth0 java-jwt

#### Positive

- API 단순
- 빠른 적용 가능
- 경량 라이브러리

#### Negative

- Java/Spring 친화적 유틸리티가 상대적으로 적음
- Claims/Parser 확장성이 제한적
- Spring 기반 레퍼런스가 JJWT 대비 적은 편

---

### C. JJWT (채택)

#### Positive

- Spring Boot 기반 예제가 풍부
- Java 개발자 친화적 API
- Claims 처리 및 Parser 구성이 직관적
- SecretKey/JCA 연동이 자연스러움
- MVP 수준 JWT 인증 구현에 적합

#### Negative

- OAuth2/OIDC 생태계 통합 기능은 제한적
- JWE(암호화 JWT) 지원이 강하지 않음

---

# Consequences

## Positive

- 인증 구현 복잡도 감소
- Stateless 인증 구조 유지 가능
- 서버 세션 저장 비용 제거
- 빠른 MVP 개발 가능
- JWT 인증 구조 학습 및 이해에 유리

---

## Negative

- 로그아웃 즉시 무효화 어려움
- Access Token 탈취 리스크 존재
- Refresh Token 기반 재발급 미지원
- 장기 세션 유지 어려움
- 토큰 회전(Token Rotation) 미적용

---

# Follow-up

MVP 이후 아래 기능을 순차적으로 도입한다.

- Refresh Token 저장소
- `/api/v1/auth/refresh`
- `/api/v1/auth/logout`
- Refresh Token Rotation
- OAuth 2.1 기반 인증 구조 검토
- Spring Authorization Server 검토

---

# References

- https://github.com/jwtk/jjwt
- https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html
- https://connect2id.com/products/nimbus-jose-jwt
- https://github.com/auth0/java-jwt
- https://datatracker.ietf.org/doc/html/rfc7519

---

# Date

2026-05-14