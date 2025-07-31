# PetCare (Spring Boot 3.4.2, JSP, JWT)

반려견 등록·조회, 일기(활동/행동/미용/건강/식사) CRUD, 회원가입/로그인/비밀번호 변경/탈퇴를 제공하는 **JSP 기반 Spring Boot** 애플리케이션입니다.  
인증은 **JWT(Access/Refresh) + HttpOnly 쿠키**로 처리하며, **Spring Security stateless** 정책을 사용합니다.  
일기 도메인은 **JPA 단일 도메인 상속(JOINED 전략)**으로 모델링했습니다. 목록 조회는 **QueryDSL**로 다중 조건 필터링과 페이징을 지원합니다.

---

## 주요 기능(Features)

- **인증/인가**
  - 회원가입, 로그인(JWT 발급), 토큰 재발급(Refresh → Access), 로그아웃, 회원정보 수정·탈퇴
  - Access/Refresh를 **HttpOnly 쿠키**에 저장 `Secure` 적용
- **반려견(Pet)**
  - 등록/수정/삭제/조회(목록, 단건), 사용자-반려견 1:N 관리
- **일기(Diary)**
  - 유형별 Activity/Behavior/Grooming/Health/Meal **상속 엔티티**
  - 다중 필터(petId[], dtype[]) + 페이지네이션 + 최신일자 내림차순
  - 단건 조회, 생성, 수정(타입 변경 시 재생성), 삭제
- **프론트엔드**
  - JSP + jQuery로 **/WEB-INF/views/** 렌더링
  - 팝업 기반 작성/수정 화면, 클라이언트 유효성 검증
- **로깅/개발 보조**
  - P6Spy로 **SQL 및 바인딩 파라미터** 출력
  - 글로벌 예외 처리(`@RestControllerAdvice`)로 일관된 `ResponseDto`

---

## 기술 스택(Tech Stack)

- **Backend**: Spring Boot 3.4.2, Spring MVC, Spring Data JPA, Spring Security
- **Auth**: `jjwt` 0.12.x (HS256), BCrypt
- **DB**: MySQL 8.x (Dialect: `MySQLDialect`)
- **Query**: QueryDSL 5 (jakarta)
- **View**: JSP/JSTL (embedded Tomcat Jasper)
- **Build**: Gradle, Java 17
- **Dev**: P6Spy, Lombok, Validation(Jakarta)

---

## 아키텍처 개요

### 패키지 구조(요지)
- `auth/` : 인증 컨트롤러/서비스/DTO
- `user/` : 사용자 컨트롤러/서비스/엔티티/DTO
- `pet/`  : 반려견 컨트롤러/서비스/엔티티/DTO
- `diary/`: 일기 컨트롤러/서비스/엔티티/DTO/리포지토리(QueryDSL)
- `jwt/`  : `JwtUtil`, `JwtAuthenticationFilter`, `CustomUserDetailsService`, `CustomUserDetails`
- `common/`: `ResponseDto`, `BaseEntity(감사 필드)`, 예외/필터, 유틸(CookieUtils), Validation

### 데이터 모델
- `User (1) — (N) Pet (1) — (N) Diary`
- `Diary`는 상속 루트(`JOINED`)이며 하위 타입:
  - `Activity`, `Behavior`, `Grooming`, `Health`, `Meal`
- 상속 구분은 DB `dtype`(DiscriminatorColumn)으로 관리하며 읽기 전용 필드로 노출.

### ERD
<img width="635" height="597" alt="erd" src="https://github.com/user-attachments/assets/1b0157fe-4e7a-4c25-90d4-72c002e3e0a7" />


### 보안 흐름(AuthN/AuthZ)
1. **/auth/login (POST)**: 이메일/비밀번호 검증 → JWT(Access/Refresh) 발급 → **HttpOnly 쿠키 저장**
2. 요청 시 **`JwtAuthenticationFilter`**가 `access` 쿠키를 검증, 인증 컨텍스트 주입
3. Access 만료 시 **/api/v1/auth/refresh (POST)**로 재발급 (요청 쿠키의 Refresh 검증)
4. **로그아웃** 시 Refresh 무효화(DB null) + 쿠키 제거

`SecurityConfig` 요약:
- 허용: `/`, `/resources/**`, `/static/**`, `/WEB-INF/**`, `/favicon.ico`, `/api/v1/auth/**`, `/auth/**`
- 그 외는 인증 필요
- 세션: `STATELESS`, CSRF/FormLogin/HttpBasic 비활성화

---

## API 개요

모든 API 응답은 다음 형태의 `ResponseDto`를 사용합니다.

```json
{ "success": true, "message": "설명", "data": { } }
```

### 인증(Auth)
- **이메일 중복 확인**  
  `GET /api/v1/auth/check-email?email={email}`
- **로그인 (쿠키 발급)**  
  `POST /auth/login` (JSP 폼 또는 API)  
  Body(JSON 예): `{"email":"a@b.com","password":"P@ssw0rd!"}`  
  Set-Cookie: `access`, `refresh`
- **토큰 재발급**  
  `POST /api/v1/auth/refresh` (Cookie: `refresh` 필수)
- **회원가입**  
  `POST /auth/signup` (JSP 폼) — 서버측 유효성 및 이메일 중복검사 필요

### 사용자(User)
- **내 정보 (View)**: `GET /user`
- **수정**: `PUT /api/v1/user`  
  Body: `{"name":"홍길동","phone":"010-1234-5678"}`
- **비밀번호 변경**: `PUT /api/v1/user/password`  
  Body: `{"currentPassword":"...","changedPassword":"...","changedPasswordCheck":"..."}`
- **인증 확인(AJAX)**: `GET /api/v1/user/verify-auth` → `success: true/false`
- **로그아웃**: `PUT /api/v1/user/logout` → Refresh 무효화 + 쿠키 삭제
- **탈퇴**: `DELETE /api/v1/user` → User → Pet(ALL, orphanRemoval) → Diary 삭제

### 반려견(Pet)
- **등록**: `POST /api/v1/pet`
  ```json
  {
    "name": "Mong",
    "breed": "Poodle",
    "birthDate": "2023-05-10",
    "gender": "MALE",
    "weight": 5.25,
    "description": "활발합니다"
  }
  ```
- **내 반려견 목록**: `GET /api/v1/pet`
- **수정**: `PUT /api/v1/pet/{petId}`
- **삭제**: `DELETE /api/v1/pet/{petId}` (소유자 검증)

### 일기(Diary)
- **목록 조회(필터 + 페이징)**  
  `GET /api/v1/diary?page=0&size=6&petId=1&petId=2&dtype=activity&dtype=meal`  
  응답: `Page<DiaryInfoWithJoinResDto>`
- **단건 조회**: `GET /api/v1/diary/{diaryId}`
- **작성**: `POST /api/v1/diary/{petId}` — Body: `DiaryReqDto`
  - `dtype`: `activity|behavior|grooming|health|meal`
- **수정**: `PUT /api/v1/diary/{diaryId}`  
  - **타입 변경 지원**: 타입이 달라지면 기존 삭제 후 새 타입으로 재생성
- **삭제**: `DELETE /api/v1/diary/{diaryId}` (소유자 검증)

`DiaryReqDto` 예시:

- `activity`
  ```json
  {
    "dtype": "activity",
    "date": "2025-07-30",
    "description": "저녁 산책",
    "activityType": "WALK",
    "duration": 35,
    "distance": 2.4,
    "location": "한강공원"
  }
  ```

- `behavior`
  ```json
  {
    "dtype": "behavior",
    "date": "2025-07-30",
    "behaviorType": "짖음",
    "behaviorIntensity": "MEDIUM",
    "description": "초인종에 반응"
  }
  ```

---

## 프론트엔드(View)

- JSP 경로: `/WEB-INF/views/*.jsp`
- 주요 페이지
  - `/auth/signup`, `/auth/login`
  - `/user` (내 정보 + 반려견 카드 + 팝업(비번/반려견 등록·수정))
  - `/diary` (필터/검색/페이징 + 팝업(일기 작성/수정))
- AJAX는 `fetch(..., { credentials: 'include' })`로 쿠키 포함
- 클라이언트 유효성 검증: jQuery 기반(패턴/범위 체크)

### 주요 화면
- 로그인
  <img width="950" height="581" alt="image" src="https://github.com/user-attachments/assets/76ed8998-8ad9-4235-885d-298141bd66f0" />
  
- 회원 가입
  <img width="954" height="817" alt="image" src="https://github.com/user-attachments/assets/d69f0067-8337-4ffb-86df-801f03e0f411" />
  
- 유저 및 반려견 정보
  <img width="938" height="892" alt="image" src="https://github.com/user-attachments/assets/d96c29dd-9e8c-4992-98c1-0817859bbc24" />
  
- 반려견 일기 리스트
  <img width="939" height="897" alt="image" src="https://github.com/user-attachments/assets/caa65b5a-d374-4110-81f8-5e5a8b843568" />
  
- 반려견 일기 작성

  <img width="416" height="844" alt="image" src="https://github.com/user-attachments/assets/e504306a-fb62-49cf-b38b-939db011f32a" />

---

## 예외/에러 응답

### DTO 검증 실패
```json
{
  "success": false,
  "message": "DTO 검증 오류",
  "data": { "fieldName": "에러 메시지" }
}
```

### 인증 실패/비인가
- Ajax 요청은 `ResponseDto(false, "...")`로 응답
- 일부 RuntimeException은 `FilterExceptionFilter`에 의해 `/auth/login?error=unauthorized&returnUrl=...`로 리다이렉트

---
