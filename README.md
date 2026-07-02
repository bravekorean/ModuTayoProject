# ModuTayo

ModuTayo는 열차와 고속버스 정보를 한곳에서 조회하고 예매할 수 있도록 만든 통합 교통 예매 웹 서비스입니다. 2023년 9월부터 2023년 10월 말까지 대학교 팀프로젝트로 진행한 프로젝트입니다.

## 프로젝트 개요

- 열차 및 고속버스 출발지, 도착지, 날짜 기반 운행 정보 조회
- 회원가입, 로그인, JWT 기반 인증, 카카오 로그인 연동
- 열차/버스 예매 및 좌석, 인원, 마일리지 사용 처리
- 아임포트 결제 연동 및 카카오페이 결제 화면 구성
- 마이페이지에서 예매 내역, 결제 내역, 회원 정보 관리
- 공지사항 등록, 조회, 수정, 삭제 및 관리자용 검색 기능
- 관리자 페이지에서 회원, 예매, 결제 현황 조회

## 개발 기간

- 2023.09 ~ 2023.10 말
- 대학교 팀프로젝트

## 기술 스택

### Backend

- Java 11
- Spring Boot 2.7.x (`build.gradle` 기준 `2.7.15-SNAPSHOT`)
- Spring Security
- Spring Data JPA
- JWT (`jjwt 0.11.5`)
- OAuth2 Client
- MySQL
- Gradle

### Frontend

- React 18.2.0
- React Router DOM 6
- Axios
- Material UI
- Bootstrap / React Bootstrap
- Day.js
- Kakao Login SDK
- 아임포트 결제 SDK

## 주요 기능

### 회원 및 인증

- 일반 회원가입/로그인
- JWT 토큰 발급 및 인증 처리
- 카카오 로그인
- 아이디/비밀번호 찾기
- 회원 정보 수정 및 탈퇴
- 관리자 권한 확인

### 교통 정보 조회

- 공공데이터포털 API 기반 열차 도시 코드, 역 정보, 운행 정보 조회
- 고속버스 터미널 목록, 도착 터미널, 운행 정보 조회
- 왕복/편도, 출발지/도착지, 날짜, 인원 조건 기반 검색 화면

### 예매

- 열차 예매
- 고속버스 예매
- 좌석 및 인원 정보 저장
- 사용자별 예매 내역 조회
- 관리자용 전체 예매 내역 조회 및 검색

### 결제

- 아임포트 결제 연동
- 카카오페이 결제 UI
- 열차/버스 결제 내역 저장
- 사용자/관리자 결제 내역 조회
- 결제 수단 및 기간 기반 검색

### 공지사항

- 공지사항 목록/상세 조회
- 조회수 증가
- 공지사항 작성, 수정, 삭제
- 파일 첨부 정보 저장
- 제목/내용 기반 검색

## 프로젝트 구조

```text
ModuTayo
├── build.gradle
├── settings.gradle
├── gradle/
├── frontend/                         # React 18 프론트엔드 소스
│   ├── package.json
│   ├── public/
│   └── src/
│       ├── App.js
│       ├── component/                # 화면별 React 컴포넌트
│       └── images/                   # 노선/로그인/결제 이미지
└── src/
    ├── main/
    │   ├── java/com/group/express/
    │   │   ├── config/               # Security, JWT, Web 설정
    │   │   ├── controller/           # REST API Controller
    │   │   ├── domain/               # JPA Entity
    │   │   ├── DTO/                  # 요청/응답 DTO
    │   │   ├── repository/           # Spring Data JPA Repository
    │   │   └── service/              # 비즈니스 로직
    │   └── resources/
    │       ├── static/               # React 빌드 결과물
    │       └── templates/
    └── test/
```

## 백엔드 패키지 역할

- `controller`: 회원, 로그인, 예매, 결제, 공지사항, 공공 API 요청을 처리하는 REST 컨트롤러
- `service`: 로그인, JWT 발급, 예매/결제/공지사항 처리 등 비즈니스 로직
- `domain`: `Member`, `TrainBooking`, `BusBooking`, `Payment`, `Notice`, `Banner` 등 JPA 엔티티
- `repository`: 각 엔티티의 데이터베이스 접근 계층
- `config`: Spring Security, JWT 필터, CORS/Web 설정

## 실행 방법

### Backend

```bash
./gradlew bootRun
```

Windows 환경에서는 다음 명령어를 사용할 수 있습니다.

```bash
gradlew.bat bootRun
```

### Frontend

```bash
cd frontend
npm install
npm start
```

프론트엔드는 `package.json` 기준으로 `http://localhost:8083` 백엔드 서버를 proxy로 바라보도록 설정되어 있습니다.

## 설정 파일

데이터베이스, JWT, 공공데이터 API 키, 카카오 로그인 키 등 실행 환경별 값은 `application.properties` 또는 프론트엔드 환경 변수에 별도로 설정해야 합니다. 현재 저장소에서는 민감 정보가 포함될 수 있는 설정 파일은 `.gitignore` 정책에 따라 제외합니다.

예시로 필요한 설정 항목은 다음과 같습니다.

```properties
server.port=8083
spring.datasource.url=jdbc:mysql://localhost:3306/...
spring.datasource.username=...
spring.datasource.password=...
publicAPIurl=...
publicAPIkey=...
jwt.secret=...
```

프론트엔드 카카오 로그인에는 다음 환경 변수가 사용됩니다.

```text
REACT_APP_KAKAO_LOGIN_API_KEY=...
```

## 참고

- `frontend/`에는 React 원본 소스가 있습니다.
- `src/main/resources/static/`에는 Spring Boot에서 정적 파일로 서빙할 수 있는 React 빌드 결과물이 포함되어 있습니다.
- 기존 Gradle 설정에는 React 빌드 산출물을 Spring Boot static 리소스로 복사하는 태스크가 포함되어 있습니다.
