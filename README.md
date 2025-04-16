# 프로젝트 개요
PetCare는 반려동물의 일상 관리를 지원하는 애플리케이션으로, 사용자 인증, 반려동물 정보 관리, 다양한 일지 기록 기능을 제공합니다. 이 프로젝트는 Spring Boot 기반으로 설계되었으며, RESTful API를 통해 클라이언트와 상호작용합니다.

## 주요 기능
- 사용자 관리
  - 회원 정보 조회/수정, 비밀번호 변경, 로그아웃, 회원 탈퇴
- 인증/인가
  - JWT 기반 회원가입, 로그인, 리프레시 토큰을 통한 액세스 토큰 갱신
- 반려동물 관리
  - 반려동물 정보 등록, 단일/복수 조회, 수정, 삭제
- 일지 관리
  - 건강, 미용, 식사, 운동, 행동 등 카테고리별 일지 CRUD
  - 페이지네이션 및 필터링을 지원하는 일지 목록 조회

## 기술 스택
- 언어: Java 17
- 프레임워크: Spring Boot 3.4.2
- 데이터베이스: MySQL
- ORM: Spring Data JPA, Hibernate, QueryDSL 5.0.0
- 보안: Spring Security, JWT (jjwt 0.12.3)
- 빌드 도구: Gradle (Spring Dependency Management 1.1.7)
- 의존성:
  - spring-boot-starter-web: RESTful API 구현
  - spring-boot-starter-validation: 요청 데이터 유효성 검증
  - p6spy-spring-boot-starter: 쿼리 파라미터 로깅
  - lombok: 보일러플레이트 코드 최소화
  - mysql-connector-j: MySQL 연결
- CI/CD: Jenkins, Docker (별도 설정 필요)
- 테스트: JUnit, Spring Boot Test

## 프로젝트 구조
```
com.myproject.petcare.pet_diary
├── auth        # 인증/인가 관련 컨트롤러 및 서비스
├── diary       # 일지 관리 관련 컨트롤러 및 서비스
├── pet         # 반려동물 관리 관련 컨트롤러 및 서비스
├── user        # 사용자 관리 관련 컨트롤러 및 서비스
├── common      # 공통 DTO 및 유틸리티
└── jwt         # JWT 관련 설정 및 유틸리티
```

## API 엔드포인트
- 기본 URL: http://localhost:8080/api/v1
- 주요 엔드포인트 예시:
  - POST /auth/signup: 회원가입
  - POST /auth/login: 로그인
  - GET /user: 사용자 정보 조회
  - POST /pet: 반려동물 등록
  - GET /diary/{petId}: 일지 목록 조회
