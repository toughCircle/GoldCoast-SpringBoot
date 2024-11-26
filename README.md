# 🛠 Gold-Coast

## 📖 프로젝트 소개

이 프로젝트는**금 거래 플랫폼의 거래 및 인증 API**를 제공하는 자원서버와 인증만을 담당하는 인증서버로 구성된 시스템입니다.

사용자 인증을 통해 안전하고 신뢰할 수 있는 거래를 지원하며, 구매자와 판매자가 금 거래를 편리하게 진행할 수 있도록 다양한 기능을 제공합니다.

**JAVA, GRPC**,**MariaDB**등의 기술을 활용해 성능과 확장성을 갖춘 구조를 구현했습니다.

---

## 📦 기술 스택

- `Java 17`, `Spring Boot`, `JPA`
- **데이터베이스**: `MariaDB`
- **배포**: `Docker`, `GCP`

---

## 📋 API 및 데이터베이스 설계

### 데이터베이스 다이어그램

DB 설계를 시각적으로 나타낸 다이어그램입니다.[DB Diagram](https://dbdiagram.io/d/66d7f7f6eef7e08f0e9fa844)을 통해 작성된 설계를 확인할 수 있습니다.

### API 스펙

**Swagger**를 통해 작성한 API 명세서와**Postman**을 통한 API 요청 예시 링크는 아래와 같습니다.

- Swagger API 스펙
- Postman Collection

---

## 🚀 주요 기능

<details>
<summary>구매 및 판매 주문 CRUD</summary>

- **품목**: 24K, 22K, 21K, 18K
- **수량**: 소수점 한 자리까지, 그램(g) 단위
- **주문 상태**: 구매와 판매에 따른 각각의 상태 관리 (예: "주문 완료", "발송 완료" 등)
- **저장 정보**: 주문번호, 주문일자, 주문자, 주문 상태, 품목, 수량, 금액, 배송지 정보 등
- **주문 생성 플로우 차트**

    ```mermaid
    flowchart TD
        Start([사용자 요청]) --> A[자원 서버로 주문 생성 요청]
        A --> B[JWT 토큰 포함 요청 검증]
        B --> C[자원 서버가 인증 서버로 검증 요청]
        C --> D[JWT 토큰 유효성 검증]
        D --> E{유효한 토큰인가?}
        E -- 아니요 --> Error[권한 없음 응답]
        E -- 예 --> F[사용자 정보 반환]
        F --> G[자원 서버로 사용자 정보 전달]
        G --> H[재고 확인 및 주문 생성]
        H --> I{재고 충분 여부}
        I -- 아니요 --> StockError[재고 부족 응답]
        I -- 예 --> J[주문 데이터 저장]
        J --> Success[주문 생성 완료 응답]
    
        Error --> End([처리 종료])
        StockError --> End
        Success --> End
    
    ```

- **주문 생성 시퀀스 다이어그램**

    ```mermaid
    sequenceDiagram
        participant User as 사용자
        participant ResourceServer as 자원 서버
        participant AuthServer as 인증 서버
        participant Database as 데이터베이스
    
        User->>ResourceServer: 주문 생성 요청 (JWT 포함)
        ResourceServer->>AuthServer: JWT 토큰 검증 요청
        AuthServer->>ResourceServer: 사용자 정보 반환
        ResourceServer->>Database: 재고 확인 및 주문 데이터 저장
        Database-->>ResourceServer: 저장 성공 응답
        ResourceServer-->>User: 주문 생성 성공 응답
        Note over ResourceServer: 재고 부족 시 에러 응답 반환
        Note over AuthServer: 토큰 유효하지 않으면 권한 없음 반환
    
    ```

</details>
<details>
<summary>인증 및 토큰 발급 서버</summary>

- **인증 정보**: 사용자 ,`email`,`password`관리
- **토큰 발급**: AccessToken과 RefreshToken 발급
- **JWT를 통한 권한 확인**: 자원서버 에 대한 모든 요청은 JWT 토큰으로 권한 검증
- **로그인 시퀀스 다이어그램**

    ```mermaid
    sequenceDiagram
        participant User as 사용자
        participant AuthAPI as 인증 API
        participant AuthDB as 인증 DB
    
        User->>AuthAPI: 로그인 요청 (email, password)
        AuthAPI->>AuthDB: 사용자 정보 확인
        AuthDB-->>AuthAPI: 사용자 정보 확인 성공
        AuthAPI->>AuthAPI: AccessToken 및 RefreshToken 생성
        AuthAPI-->>User: 토큰 발급 완료
    
        User->>AuthAPI: 인증 요청 (JWT 토큰)
        AuthAPI->>AuthAPI: JWT 검증
        AuthAPI-->>User: 인증 성공
    ```

</details>
<details>
<summary>페이징을 지원하는 주문 조회 API</summary>

- **입력 파라미터**: `page`,`limit`
- **응답 형식**: 성공 여부, 메시지, 요청한 사용자의 권한에 맞는 주문 데이터 반환

</details>

---

## 🖼️ UI 미리보기

<details>
<summary>미리보기</summary>

### 메인 화면

- 상품 목록 출력 (재고 유무로 판매 중/품절 상품 구분)
![image](https://github.com/user-attachments/assets/feb888db-fdef-4cc1-84d5-59cb92e38a89)
![image](https://github.com/user-attachments/assets/f34e9c05-403d-48eb-86f7-c2a3fe2b8997)

- 회원가입 및 로그인
![image](https://github.com/user-attachments/assets/86d8d517-eb81-42ec-84f9-53abb706e27b)
![image](https://github.com/user-attachments/assets/f0567e08-40f7-41f7-8e4b-50a787dace31)

- 상품 등록 (사용자가 판매자인 경우 상품 등록 버튼 활성화)
![image](https://github.com/user-attachments/assets/c6ed3fe9-3283-472d-a360-0c31e6c14a4d)
![image](https://github.com/user-attachments/assets/82386b89-be7a-485c-8cbc-5f2286f047be)

- 상품 구매
![image](https://github.com/user-attachments/assets/e142be1f-50bd-4010-ad04-c8af465ba737)
![image](https://github.com/user-attachments/assets/37f96a7f-b971-4214-94b7-e6d5ffef2b6d)
![image](https://github.com/user-attachments/assets/4501c7e7-83ef-4a0c-9a34-9f83b55d0363)

- 마이페이지 (판매자)
![image](https://github.com/user-attachments/assets/b4a212dc-0790-41d8-bf0c-749785c3b408)

- 등록 상품별 주문 내역 조회 (판매자)
![image](https://github.com/user-attachments/assets/393707de-30e0-4643-93e9-0cdf4db8b04a)

- 주문 상태 변경 가능 (판매자)
![image](https://github.com/user-attachments/assets/ec06123d-2624-4546-9872-bd291a292f87)

- 마이페이지 (구매자)
![image](https://github.com/user-attachments/assets/345110e5-d200-405c-a52f-c4dcd363b02e)

- 주문 취소 (주문 완료 상태 외 주문 취소 불가)
![image](https://github.com/user-attachments/assets/7a7c3746-662a-42e8-a4d1-718808237304)
![image](https://github.com/user-attachments/assets/6d3ba9f9-8eed-451c-b7d5-aab7ac8805ff)

</details>

---

## 📂 디렉토리 구조

<details>
<summary>구조</summary>

```bash
.
├── README.md
├── auth-server
│   ├── build
│   └── src
│      └── main
│         ├── java
│         │   ├── config
│         │   ├── controller
│         │   ├── dto
│         │   ├── entity
│         │   ├── enums
│         │   ├── exception
│         │   ├── jwt
│         │   ├── repository
│         │   ├── service
│         │   └── AuthServerApplication
│         ├── proto
│         └── resources
│             └── application-auth.yml
├── resource-server
│   ├── build
│   └── src
│      └── main
│         ├── java
│         │   ├── config
│         │   ├── controller
│         │   ├── dto
│         │   ├── entity
│         │   ├── enums
│         │   ├── exception
│         │   ├── scheduler
│         │   ├── repository
│         │   ├── service
│         │   ├── validation
│         │   └── ResourceServerApplication
│         ├── proto
│         └── resources
│             └── application-resource.yml
└── docker-compose.yml
```

</details>

---

## 🔍 문제와 해결

<details>
<summary>사용자 권한 검증의 분리</summary>

- JWT를 통해 인증 서버에서 사용자 정보를 검증하지만, 자원 서버에서는 모든 요청에서 사용자 권한과 데이터 접근의 신뢰성을 요구
- 매 요청마다**인증 서버와의 통신 오버헤드**를 줄이는 동시에, 권한 검증을 자원 서버에서도 일관되게 처리할 수 있는 구조가 필요
- **인증 서버와 자원 서버의 역할 분리**
    1. **JWT 검증을 인증 서버에서 전담**
        - 인증 서버는 JWT를 검증하여 유효성을 확인한 뒤, 사용자 정보(`UserResponse`)를 자원 서버로 전달
    2. **자원 서버는 사용자 정보 활용**
        - 자원 서버는 요청마다 전달받은`UserResponse`를 사용해 필요한 권한 검증과 데이터 접근 제어를 수행
        - 예: 구매자는 자신의 주문만, 판매자는 자신의 상품에 대한 주문만 조회 가능하도록 처리.
    3. **gRPC를 활용한 효율적 통신**
        - JWT 검증 요청은 gRPC를 통해 인증 서버와 자원 서버 간 빠르고 효율적으로 처리되도록 설계
        - HTTP 통신보다 낮은 오버헤드로 인증 정보를 자원 서버에 전달 가능

</details>
<details>
<summary>주문 번호 중복 문제</summary>

- 주문 번호 생성 로직에서 간헐적으로 중복된 주문 번호가 생성되어 데이터베이스 UNIQUE 제약 조건에 위배됨.
- 병렬 요청 주문 생성 API 호출 시 충돌로 인해 예외가 발생, 일부 요청 실패.
- **기존 주문 번호 생성 로직**
    - 날짜 정보(`yyyyMMdd`)와 난수(`Math.random()`)를 조합하여 주문 번호를 생성.
    - 랜덤 값 범위가 제한적(`0~9999`), 고성능 환경에서 동일 초 내 많은 요청 발생 시 중복 가능성 증가.
- **사용자 친화적인 주문 번호** 사용을 위해 날짜 정보를 필수로 사용.
    1. `SecureRandom` 을 사용해 난수 중복 확률 감소.
        - **100개의 동시 요청**에서 중복 발생 없이 성공했지만 요청 수를 늘리자 다시 중복 발생.
    2. **시간 정보 결합을 통한 해결**
        - 기존 날짜 정보(`yyyyMMdd`)에 밀리초 정보(`HHmmssSSS`)를 추가하여 중복 가능성 감소.
        - 현재 밀리초 값을 난수에 더해 중복 가능성 감소.
- **결과**
    - **1000개의 동시 요청**에서도 주문 번호 중복 발생 없이 모두 성공.

</details>
<details>
<summary>주문 생성시 상품 동시성 문제</summary>

- **멀티스레드 환경에서의 동시 접근**
    - 여러 스레드가 동일한 상품 재고를 읽고 수정하는 과정에서 경쟁 조건(Race Condition)이 발생.
    - 예를 들어, 두 스레드가 동시에 재고를 읽고 감소시키면, 감소 결과가 하나의 요청만 반영될 수 있음.
- **트랜잭션 격리 수준**
    - 기본적으로 데이터베이스는**Read Committed**또는**Repeatable Read**격리 수준을 사용.
    - 이는 트랜잭션 간 데이터 읽기를 허용하지만, 변경 사항이 충돌할 경우 제대로 처리되지 않을 수 있음.
- **비원자적 연산**
    - `item.setQuantity(item.getQuantity().subtract(orderQuantity))`는**읽기**와**쓰기**가 분리된 연산이므로, 중간에 다른 트랜잭션이 개입할 여지가 있음.
- **Lock**
    - **낙관적 락(Optimistic Lock)**:
        - 데이터 수정 시 충돌 여부를 확인하고, 충돌 발생 시`OptimisticLockException`을 발생.
        - 주로 충돌 가능성이 낮은 환경에서 효율적.
        - 충돌 재시도 로직 필요.
    - **비관적 락(Pessimistic Lock)**:
        - 데이터 수정 전, 데이터베이스에서 해당 데이터를 **잠금(Lock)**하여 다른 트랜잭션이 접근하지 못하도록 차단.
        - 충돌 가능성이 높은 환경에서 데이터 무결성 보장.
        - 오버헤드 발생률 증가.
- 두가지 방법을 비교 후 1000개의 동시 요청에 성능 차이가 적고 비교적 간단하게 구현 가능한 비관적 락을 사용하여 구현.
- 결과
    - **동시성 문제**: 주문 요청이 동시 발생하는 환경에서도 상품 재고 데이터의 무결성을 보장.
    - **구현 간소화**: 충돌 시 재시도 로직 없이, 안정적인 주문 생성 시스템 구축.
    - **성능 유지**: 낙관적 락 대비 성능 손실 없이, 데이터 무결성과 안정성을 동시에 확보.

</details>

---
