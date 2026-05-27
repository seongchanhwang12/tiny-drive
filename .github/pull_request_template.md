## 개요

<!--
이 PR이 해결하는 문제와 변경 목적을 간결히 작성한다.
관련 이슈가 있다면 `Closes #이슈번호` 형식으로 연결한다.
-->

## 변경 사항

<!--
주요 변경 내용을 체크리스트 또는 bullet로 작성한다.
domain behavior, use case orchestration, API, persistence, security, test, documentation 변경을 구분해 적는다.
-->

## 아키텍처 경계

<!--
Target Aggregate, Target Use Case, Transaction Boundary를 명시한다.
변경이 aggregate boundary, parentId source of truth, async upload lifecycle, entity exposure 금지 원칙을 침범하지 않았는지 설명한다.
-->

## 검증 결과

<!--
실행한 검증 명령과 결과를 작성한다.
실행하지 못한 검증이 있다면 이유와 잔여 리스크를 명시한다.
-->

```bash
./gradlew test --tests "*Domain*"
./gradlew test --tests "*Repository*"
./gradlew test --tests "*Service*"
./gradlew clean build dependencies
```

## 리뷰 포인트

<!--
리뷰어가 집중해서 봐야 할 설계 결정, trade-off, 변경 위험 지점을 작성한다.
특히 transaction boundary, N+1 가능성, security filter, error handling, domain invariant 변경은 명확히 표시한다.
-->

## 체크리스트

<!--
해당하지 않는 항목은 이유를 함께 적거나 삭제한다.
-->

- [ ] aggregate boundary를 침범하지 않았다.
- [ ] controller에서 repository에 직접 접근하지 않는다.
- [ ] JPA entity를 API layer에 노출하지 않는다.
- [ ] transaction boundary는 application layer에 유지된다.
- [ ] 불필요한 long-running 또는 nested transaction을 추가하지 않았다.
- [ ] 새로운 N+1 위험이나 uncontrolled fetch join을 추가하지 않았다.
- [ ] domain layer에 framework/infrastructure 의존을 추가하지 않았다.
- [ ] 민감 정보와 JWT를 로그에 남기지 않는다.
- [ ] upload lifecycle의 비동기 보장을 훼손하지 않았다.
- [ ] 변경 범위에 맞는 테스트를 추가하거나 갱신했다.
