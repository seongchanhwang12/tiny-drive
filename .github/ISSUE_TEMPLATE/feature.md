---
name: Feature
about: 기능 구현 이슈 템플릿
title: "[Feature] "
labels: feature
assignees: ""
---

## 배경

<!--
이 기능이 필요한 이유와 요구사항 출처를 작성한다.
관련 문서, 요구사항 번호, ADR, API 계약이 있다면 함께 연결한다.
-->

## 구현 목표

<!--
이 이슈가 완료되었을 때 사용자가 할 수 있어야 하는 동작이나 시스템이 보장해야 하는 결과를 작성한다.
구현 방법보다 달성해야 할 behavior 중심으로 작성한다.
-->

## 아키텍처 경계

<!--
Target Aggregate, Target Use Case, Transaction Boundary를 명시한다.
aggregate 간 직접 참조, controller의 repository 접근, domain layer의 infrastructure 의존이 생기지 않는지 확인한다.
-->

## 포함 작업

<!--
이 이슈에서 실제로 구현할 작업을 체크리스트로 작성한다.
API, use case, domain behavior, persistence, security, validation, error handling, test, documentation 변경을 필요한 만큼 분리한다.
-->

## 불포함 사항

<!--
이번 이슈에서 의도적으로 제외하는 작업을 작성한다.
후속 이슈로 분리할 범위, MVP 이후 기능, 고도화 작업, 관련 있지만 구현하지 않을 항목을 명확히 적는다.
-->

## 수용 기준

<!--
구현 완료 여부를 판단할 수 있는 검증 가능한 조건을 체크리스트로 작성한다.
정상 흐름, 실패 흐름, 보안/권한, transaction boundary, aggregate consistency 조건을 포함한다.
-->

## 검증

<!--
이 이슈 완료 전에 실행할 검증 명령과 확인 항목을 작성한다.
기본 검증 파이프라인은 AGENTS.md의 Gradle Wrapper 명령을 따른다.
-->

## 주의 사항

<!--
구현 중 반드시 지켜야 하는 architectural constraint와 금지 사항을 작성한다.
aggregate boundary, parentId source of truth, async upload lifecycle, entity exposure 금지, sensitive data logging 금지 등을 필요한 만큼 명시한다.
-->
