# hhplus-concert-booking
항해플러스 6기 백엔드 - 콘서트 예약 시스템

<aside>
💡 아래 명세를 잘 읽어보고, 서버를 구현합니다.

</aside>

## Description

- **`콘서트 예약 서비스`**를 구현해 봅니다.
- 대기열 시스템을 구축하고, 예약 서비스는 작업가능한 유저만 수행할 수 있도록 해야합니다.
- 사용자는 좌석예약 시에 미리 충전한 잔액을 이용합니다.
- 좌석 예약 요청시에, 결제가 이루어지지 않더라도 일정 시간동안 다른 유저가 해당 좌석에 접근할 수 없도록 합니다.

## Requirements

- 아래 5가지 API 를 구현합니다.
    - 유저 토큰 발급 API
    - 예약 가능 날짜 / 좌석 API
    - 좌석 예약 요청 API
    - 잔액 충전 / 조회 API
    - 결제 API
- 각 기능 및 제약사항에 대해 단위 테스트를 반드시 하나 이상 작성하도록 합니다.
- 다수의 인스턴스로 어플리케이션이 동작하더라도 기능에 문제가 없도록 작성하도록 합니다.
- 동시성 이슈를 고려하여 구현합니다.
- 대기열 개념을 고려해 구현합니다.

## API Specs

1️⃣ **`주요` 유저 대기열 토큰 기능**

- 서비스를 이용할 토큰을 발급받는 API를 작성합니다.
- 토큰은 유저의 UUID 와 해당 유저의 대기열을 관리할 수 있는 정보 ( 대기 순서 or 잔여 시간 등 ) 를 포함합니다.
- 이후 모든 API 는 위 토큰을 이용해 대기열 검증을 통과해야 이용 가능합니다.

> 기본적으로 폴링으로 본인의 대기열을 확인한다고 가정하며, 다른 방안 또한 고려해보고 구현해 볼 수 있습니다.
> 

**2️⃣ `기본` 예약 가능 날짜 / 좌석 API**

- 예약가능한 날짜와 해당 날짜의 좌석을 조회하는 API 를 각각 작성합니다.
- 예약 가능한 날짜 목록을 조회할 수 있습니다.
- 날짜 정보를 입력받아 예약가능한 좌석정보를 조회할 수 있습니다.

> 좌석 정보는 1 ~ 50 까지의 좌석번호로 관리됩니다.
> 

3️⃣ **`주요` 좌석 예약 요청 API**

- 날짜와 좌석 정보를 입력받아 좌석을 예약 처리하는 API 를 작성합니다.
- 좌석 예약과 동시에 해당 좌석은 그 유저에게 약 5분간 임시 배정됩니다. ( 시간은 정책에 따라 자율적으로 정의합니다. )
- 만약 배정 시간 내에 결제가 완료되지 않는다면 좌석에 대한 임시 배정은 해제되어야 하며 다른 사용자는 예약할 수 없어야 한다.

4️⃣ **`기본`**  **잔액 충전 / 조회 API**

- 결제에 사용될 금액을 API 를 통해 충전하는 API 를 작성합니다.
- 사용자 식별자 및 충전할 금액을 받아 잔액을 충전합니다.
- 사용자 식별자를 통해 해당 사용자의 잔액을 조회합니다.

5️⃣ **`주요` 결제 API**

- 결제 처리하고 결제 내역을 생성하는 API 를 작성합니다.
- 결제가 완료되면 해당 좌석의 소유권을 유저에게 배정하고 대기열 토큰을 만료시킵니다.

---

6️⃣ **`심화` 대기열 고도화**

### 심화 과제

- 다양한 전략을 통해 합리적으로 대기열을 제공할 방법을 고안합니다.
- e.g. 특정 시간 동안 N 명에게만 권한을 부여한다.
- e.g. 한번에 활성화된 최대 유저를 N 으로 유지한다.

<aside>
💡 **KEY POINT**

</aside>

- 유저간 대기열을 요청 순서대로 정확하게 제공할 방법을 고민해 봅니다.
- 동시에 여러 사용자가 예약 요청을 했을 때, 좌석이 중복으로 배정 가능하지 않도록 합니다.

### **`STEP 05`**

- 시나리오 선정 및 프로젝트 Milestone 제출
- 시나리오 요구사항 별 분석 자료 제출
    
    > 시퀀스 다이어그램, 플로우 차트 등
    > 
- 자료들을 리드미에 작성 후 PR 링크 제출

### **`STEP 06`**

- ERD 설계 자료 제출
- API 명세 및 Mock API 작성
- 자료들을 리드미에 작성 후 PR링크 제출 ( 채택할 기본 패키지 구조, 기술 스택 등 )

### 마일스톤



### 시퀀스 다이어그램

##### 1 토큰 검증 및 토큰 발급 후 대기열 등록

```mermaid
sequenceDiagram
    actor Client as 사용자
    participant API as 토큰 검증 API
    participant Token as 토큰 서비스
    participant Queue as 대기열

    Client ->> API: 사용자정보로 토큰 조회 요청

    API ->> Token: 토큰 조회 요청

    alt 유효한 토큰
		    Token -->> API: 유효한 토큰 반환
        API -->> Client: 유효한 토큰 반환
    else 토큰 정보 없음
        Token ->> Queue: 신규 토큰 발급 후 대기열 등록 요청
        Queue -->> Token: 대기열 등록 완료 응답
        Token -->> API: 새로운 토큰 반환
        API -->> Client: 새로운 토큰 반환
    end

    loop 대기 상태 확인 (폴링)
        API ->> Queue: 대기 상태 확인 요청
        Queue -->> API: 최신 대기열 상태 응답
        API -->> Client: 대기열 상태 응답
    end

```

##### 2-1 콘서트 일정 조회

```mermaid
sequenceDiagram
    actor Client as 사용자
    participant API as 일정 요청 API
    participant Token as 토큰 서비스
    participant Date as 일정 서비스
		
    Client ->> API: 예약 가능 날짜 조회 요청
    API ->> Token: 토큰 상태 확인 요청
    Token -->> API: 토큰 상태 응답 (유효한 토큰 여부)
		
		alt 유효하지 않은 토큰
	      API -->> Client: 토큰 에러 응답
		
    else 유효한 토큰
        API ->> Date: 예약 가능한 날짜 요청
        Date -->> API: 예약 가능한 날짜 목록 반환
        API -->> Client: 예약 가능한 날짜 목록 전달
    end
```

##### 2-2 콘서트 좌석 조회

```mermaid
sequenceDiagram
    actor Client as 사용자
    participant API as 좌석 요청 API
    participant Token as 토큰 서비스
    participant Seat as 좌석 서비스
	
    Client ->> API: 선택한 날짜의 좌석 조회 요청
    API ->> Token: 토큰 상태 확인 요청
    Token -->> API: 토큰 상태 응답 (유효한 토큰 여부)

    alt 유효하지 않은 토큰
	      API -->> Client: 토큰 에러 응답
    else 유효한 토큰    
	      API ->> Seat: 선택한 날짜의 좌석 정보 요청
	      Seat -->> API: 선택한 날짜의 좌석 목록 반환
	      API -->> Client: 좌석 목록 전달
    end

```

##### 3 콘서트 예약 신청 및 임시예약

```mermaid
sequenceDiagram
    actor Client as 사용자
    participant API as 좌석 예약 API
    participant Token as 토큰 서비스
    participant Seat as 좌석 서비스
    participant Payment as 결제 서비스
	
    Client ->> API: 좌석 예약 요청 (날짜, 좌석 정보)
    API ->> Token: 토큰 상태 확인 요청
    Token -->> API: 토큰 상태 응답 (유효한 토큰 여부)

    alt 유효하지 않은 토큰
        API -->> Client: 토큰 에러 응답
    else 유효한 토큰    
        API ->> Seat: 좌석 예약 요청
        Seat -->> API: 좌석 예약 성공 응답 (임시 배정 정보 포함)
        API ->> Payment: 결제 요청 (임시 배정된 좌석)
        
        loop 결제 대기
            Payment -->> API: 결제 완료 상태 응답
            alt 결제 완료
                API -->> Client: 예약 완료 응답
            else 결제 실패 또는 시간 초과
                API ->> Seat: 임시 배정 해제 요청
                Seat -->> API: 임시 배정 해제 완료 응답
                API -->> Client: 예약 실패 응답
            end
        end
    end

```

##### 4-1 포인트 잔액 조회

```mermaid
sequenceDiagram
    actor Client as 사용자
    participant API as 잔액 조회 API
    participant Token as 토큰 서비스
    participant Balance as 잔액 서비스
	
    Client ->> API: 잔액 조회 요청 (사용자 ID)
    API ->> Token: 토큰 상태 확인 요청
    Token -->> API: 토큰 상태 응답 (유효한 토큰 여부)

    alt 유효하지 않은 토큰
        API -->> Client: 토큰 에러 응답
    else 유효한 토큰    
        API ->> Balance: 잔액 조회 요청 (사용자 ID)
        Balance -->> API: 현재 잔액 응답
        API -->> Client: 잔액 정보 전달
    end

```

##### 4-2 포인트 충전

```mermaid
sequenceDiagram
    actor Client as 사용자
    participant API as 잔액 충전 API
    participant Token as 토큰 서비스
    participant Balance as 잔액 서비스
	
    Client ->> API: 잔액 충전 요청 (사용자 ID, 충전 금액)
    API ->> Token: 토큰 상태 확인 요청
    Token -->> API: 토큰 상태 응답 (유효한 토큰 여부)

    alt 유효하지 않은 토큰
        API -->> Client: 토큰 에러 응답
    else 유효한 토큰    
        API ->> Balance: 잔액 충전 요청 (사용자 ID, 충전 금액)
        Balance -->> API: 잔액 충전 성공 응답
        API -->> Client: 잔액 충전 완료 응답
    end

```
##### 5 포인트를 사용한 결제 및 대기열 삭제

```mermaid
sequenceDiagram
    actor Client as 사용자
    participant API as 결제 API
    participant Token as 토큰 서비스
    participant Payment as 결제 서비스
    participant Seat as 좌석 서비스
    participant Queue as 대기열 서비스
	
    Client ->> API: 결제 요청 (좌석 정보, 결제 금액)
    API ->> Token: 토큰 상태 확인 요청
    Token -->> API: 토큰 상태 응답 (유효한 토큰 여부)

    alt 유효하지 않은 토큰
        API -->> Client: 토큰 에러 응답
    else 유효한 토큰    
        API ->> Payment: 결제 처리 요청 (좌석 정보, 결제 금액)
        Payment -->> API: 결제 완료 응답
        
	      alt결제 실패
            API -->> Client: 결제 실패 응답
            
        else 결제 성공
            API ->> Seat: 좌석 소유권 배정 요청 (사용자 ID, 좌석 정보)
            Seat -->> API: 소유권 배정 완료 응답
            API ->> Queue: 대기열 토큰 만료 요청 (사용자 ID)
            Queue -->> API: 대기열 토큰 만료 완료 응답
            API -->> Client: 결제 완료 및 예약 성공 응답
         end
    end
```

### 플로우차트
```mermaid
flowchart TD
    A[사용자 좌석 예약 요청] --> B{토큰 유효성 확인}
    B -->|토큰 없음| C[신규 토큰 생성 요청 및 대기열 등록]
    C --> D[토큰 생성 완료 및 대기열 등록 응답]
    D --> G[대기열 상태 확인]

    B -->|토큰 있음| G[대기열 상태 확인]

    G -->|유효한 대기열| H[좌석 정보 조회 요청]
    H --> I[좌석 목록 반환]
    
    G -->|유효하지 않은 대기열| J[대기열 유효하지 않음 응답]

    I --> K[예약 신청 요청]
    K --> L[잔액 조회 요청]
    L --> M{잔액 확인}
    M -->|잔액 충분| N[결제 요청]
    M -->|잔액 부족| O[잔액 충전 요청]
    
    N --> P{결제 처리 결과}
    P -->|결제 성공| Q[좌석 소유권 배정 요청]
    P -->|결제 실패| R[결제 실패 응답]
    
    Q --> S[대기열 토큰 만료 요청]
    S --> T[예약 성공 응답]
```

