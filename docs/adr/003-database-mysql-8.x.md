# ADR-003: MVP 데이터베이스로 MySQL 8.x를 선택한다

## Status
Accepted

---

# Context

본 프로젝트는 Google Drive 스타일의 문서 관리 시스템(Tiny Drive)을 목표로 한다.

현재 MVP 단계에서는 다음 기능을 우선 검증한다.

- 회원가입 및 로그인
- 파일/폴더 생성
- 목록 조회
- 이동
- 휴지통 및 복원
- 다운로드

시스템은 초기 단계에서 빠른 개발 생산성과 안정적인 관계형 데이터 저장소가 필요하다.

또한 추후 아래와 같은 확장 가능성을 고려한다.

- 클라우드 기반 인프라 확장
- 대용량 파일 메타데이터 관리
- 멀티 테넌트 구조
- 검색 및 권한 관리 기능 확장
- 읽기/쓰기 분리
- 샤딩 및 아카이빙 전략

데이터베이스 선택 시 다음 요소를 중요하게 고려하였다.

- Spring Boot/JPA 호환성
- 개발 생산성
- 운영 안정성
- 학습 비용
- 클라우드 환경 호환성
- 커뮤니티 및 레퍼런스 규모

---

# Decision

다음과 같은 데이터베이스 전략을 채택한다.

- MVP 데이터베이스로 MySQL 8.x를 사용한다.
- ORM은 Spring Data JPA(Hibernate)를 사용한다.
- 트랜잭션 기반 관계형 데이터 모델을 사용한다.
- UTF8MB4 문자셋을 기본 사용한다.
- 추후 읽기/쓰기 분리 및 클라우드 RDS 확장을 고려한다.

---

# Rationale

## 1. MySQL 8.x 채택 이유

### Positive

- Spring Boot/JPA 레퍼런스가 풍부함
- 학습 비용이 상대적으로 낮음
- 안정적인 관계형 데이터베이스
- Docker 및 로컬 개발 환경 구성 용이
- AWS RDS/Aurora 등 클라우드 환경과 호환성 우수
- 운영 경험 및 커뮤니티 자료 풍부
- MVP 단계에서 빠른 개발 가능
- Recursive 문법을 지원 (폴더/파일 계층구조 조회가 많은 DMS 프로젝트 특성상 유리하다고 판단)

### Negative

- PostgreSQL 대비 JSON/확장 기능 제한적
- 복잡한 쿼리 및 분석 기능은 상대적으로 약함
- PostgreSQL 대비 표준 SQL 준수성이 낮은 편
- 향후 고급 검색/통계 기능 확장 시 제약 가능성 존재

---

## 2. PostgreSQL 미채택 이유

### Positive

- JSONB 지원 우수
- 고급 인덱스(GIN/GiST) 지원
- SQL 표준 준수성이 높음
- 복잡한 쿼리 처리 강력
- OAuth/권한/검색 시스템 확장에 유리
- 클라우드 환경 친화적

### Negative

- 초기 학습 비용 증가 가능
- MVP 단계에서는 일부 기능이 과도할 수 있음
- 운영 경험 부족 시 진입 장벽 존재

---

# Consequences

## Positive

- 빠른 MVP 개발 가능
- Spring Boot/JPA와 자연스럽게 통합 가능
- 개발 및 운영 레퍼런스 확보 용이
- Docker/Testcontainers 기반 개발 환경 구성 용이
- 클라우드 RDS 이전 가능성 확보

---

## Negative

- PostgreSQL 대비 확장 기능 제한 가능성 존재
- 고급 검색/분석 기능 구현 시 추가 설계 필요
- 장기적으로 읽기/쓰기 분리 및 샤딩 전략 검토 필요
- 향후 PostgreSQL 전환 비용 발생 가능성 존재

---

# Follow-up

MVP 이후 아래 항목을 검토한다.

- Read Replica 기반 읽기/쓰기 분리
- Aurora/MySQL 클라우드 전환
- 검색 최적화 전략
- 대용량 파일 메타데이터 아카이빙
- PostgreSQL 전환 가능성 검토
- Elasticsearch/OpenSearch 연계 검토
- 멀티 테넌트 데이터 분리 전략 검토

---

# References

- https://dev.mysql.com/doc/refman/8.0/en/
- https://spring.io/projects/spring-data-jpa
- https://hibernate.org/orm/
- https://aws.amazon.com/rds/mysql/
- https://www.postgresql.org/docs/

---

# Date

2026-05-14