# ClimbStyle

**클라이밍 기록과 패션을 함께 공유하는 클라이머 전용 소셜 커뮤니티**

🔗 **서비스 URL:** https://climbstyle.co.kr </br>
📅 **기간:** 2026.01 ~ 진행 중  </br>
👤 **인원:** 1인 (개인 프로젝트)

---

## 배경 및 목적

클라이머들은 완등 기록을 인스타그램에 공유하지만,
클라이밍 패션과 장비에 특화된 커뮤니티 공간은 찾기 어려웠습니다.

**ClimbStyle**은 클라이밍 패션과 스타일을 자유롭게 공유하고, </br>
좋아요 기반 랭킹 시스템을 통해 클라이머들이 자연스럽게 연결될 수 있는
클라이밍 특화 SNS 플랫폼입니다.

---

## 주요 기능

| 기능 | 설명 |
|---|---|
| 피드 | 완등 영상·사진·스타일 기록 작성, 좋아요·댓글 |
| 랭킹 | 좋아요 기반 실시간 / 주간 / 월간 랭킹 자동 집계 |
| 소셜 로그인 | 일반 회원가입, Google·Naver OAuth2 로그인 |
| 공지 / FAQ / 문의 | 공지 열람, FAQ 조회, 1:1 문의 접수 |
| 관리자 대시보드 | 사용자·공지·FAQ·문의·배너·메뉴 통합 관리 |

---

## 기술 스택

| 분류 | 기술 |
|---|---|
| Backend | Spring Boot 3.5.6, Java 17 |
| Persistence | MyBatis, PostgreSQL |
| Frontend | Thymeleaf, HTML/CSS/JavaScript |
| Security | Spring Security 6, OAuth2 (Google, Naver) |
| Storage | AWS S3 (prod), Local (local) |
| Infra | AWS EC2, Nginx, Let's Encrypt, Route 53 |
| CI/CD | GitHub Actions |

---

## 아키텍처

<img width="1476" height="797" alt="Image" src="https://github.com/user-attachments/assets/bf3ee00c-173b-4d65-8b4b-6609afe41d08" />

---

## ERD

<img width="1972" height="2761" alt="Image" src="https://github.com/user-attachments/assets/97462b60-f03f-4ef6-920a-8f0a5ad77410" />

---

## 로컬 실행

### 사전 요구사항

- Java 17
- PostgreSQL 실행 중 (database: `climbstyle`, port: `5432`)
- `src/main/resources/application-local.yml` 설정 완료

### 실행

```bash
# 의존성 설치 및 빌드
./gradlew build

# 로컬 프로파일로 실행 (기본값)
./gradlew bootRun

# 테스트 실행
./gradlew test
```

서버 기본 포트: `8080`

### application-local.yml 최소 설정 항목

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/climbstyle
    username: {DB_USER}
    password: {DB_PASSWORD}
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: {GOOGLE_CLIENT_ID}
            client-secret: {GOOGLE_CLIENT_SECRET}
          naver:
            client-id: {NAVER_CLIENT_ID}
            client-secret: {NAVER_CLIENT_SECRET}
  mail:
    username: {GMAIL_ADDRESS}
    password: {GMAIL_APP_PASSWORD}

file:
  upload:
    base-path: /path/to/upload/
  access:
    base-url: http://localhost:8080/uploads/
```

---

## 배포

`main` 브랜치에 push하면 GitHub Actions가 자동으로 EC2에 배포합니다.

필요한 GitHub Secrets:

| Secret | 설명 |
|---|---|
| `APP_PROD_YML` | `application-prod.yml` 전체 내용 |
| `EC2_HOST` | EC2 퍼블릭 IP |
| `EC2_SSH_KEY` | EC2 접속용 PEM 키 |

---

## API 응답 형식

```json
// 성공
{ "httpStatus": 200, "code": "1000", "message": "피드 조회 성공", "data": { ... } }

// 실패
{ "httpStatus": 400, "code": "5001", "message": "피드 제목 형식이 올바르지 않습니다." }
```

---

## 인증 흐름

| 경로 | 설명 |
|---|---|
| `POST /api/v1/login` | 일반 사용자 JSON 로그인 |
| `POST /api/v1/admin/login` | 관리자 JSON 로그인 |
| `GET /oauth2/authorization/google` | Google OAuth2 로그인 시작 |
| `GET /oauth2/authorization/naver` | Naver OAuth2 로그인 시작 |

OAuth2 최초 로그인 시 `ROLE_TEMP_USER` 부여 → `POST /api/v1/users/oauth2`로 닉네임 설정 후 `ROLE_USER` 전환

---
