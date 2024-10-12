# API 명세

### :one: 유저 대기열 토큰 기능

1-1. 유저 대기열 토큰 발급 API

  - URL : `/booking/user/{userId}/token`
  - Method : POST
  - Description : 유저 토큰 검증 및 발급

♦️ Request
  - Path variable
    - `userId`: 유저 고유 번호

♦️ Response
  - Body
    - `token_value`: 유저 발급 토큰 
```json
{
  "token_value": "abcd1234efgh5678"
}
```

1-2. 유저 대기열 확인 API

- URL : `/booking/user/{userId}/queue`
- Method :  
- Description : 유저 대기열 확인(폴링)

♦️ Request
- Path variable: `userId` 유저 고유 번호

♦️ Response
  - Body
    - `user_id`: 유저 고유 번호
    - `queue_rank`: 대기열 순번
```json
{
  "user_id": 1,
  "queue_rank": 1200
}
```

---

### :two: 예약 가능 날짜 / 좌석 API

2-1. 예약 가능 날짜 조회 API

  - URL : `/booking/concerts/{concertId}/dates`
  - Method : GET
  - Description : 특정 콘서트 날짜 조회

♦️ Request
  - `Authorization` -> Bearer 토큰 : 인증을 위한 토큰
  - Path variable
    - `concertId` 콘서트 고유 번호

♦️ Response
  - Body
    - `concert_id`: 콘서트 고유 번호
    - `concert_schedules`: 콘서트 일정 목록
      - `concert_schedule_id`: 일정 고유 번호
      - `concert_date`: 일정 날짜
```json
[
    {
        "concert_schedule_id": 1,
        "concert_date": "2024-10-10"
    },
    {
        "concert_schedule_id": 2,
        "concert_date": "2024-10-11"
    },
    {
        "concert_schedule_id": 3,
        "concert_date": "2024-10-12"
    }
]
```

2-2. 예약 가능 좌석 조회 API

  - URL : `/booking/concerts/{concertId}/seats`
  - Method : GET
  - Description : 특정 콘서트 날짜 조회

♦️ Request
  - Header
    - `Authorization`: Bearer 토큰 : 인증을 위한 토큰
  - Path variable
    - `concertId`: 콘서트 고유 번호
  - Body
    - `consert_schdule_id`: 콘서트 일정 고유 번호

♦️ Response
  - Body
    - `token_value` 유저 발급 토큰 
```json
 [
    {
        "concert_seat_id": 1,
        "seat_number": 1
    },
    {
        "concert_seat_id": 3,
        "seat_number": 3
    },
    {
        "concert_seat_id": 11,
        "seat_number": 11
    }
]
```

### :three: 좌석 임시 예약 요청 API

- URL : `/booking/bookings/temporary`
- Method : POST
- Description : 좌석 임시 예약 요청

♦️ Request
- Header
  - `Authorization`: Bearer 토큰 : 인증을 위한 토큰
- Body
  - `user_id`: 유저 고유 번호
  - `consert_seat_id`: 콘서트 좌석 고유 번호

♦️ Response
- Body
  - `booking_id`: 예약 고유 번호
  - `user_id`: 유저 고유 번호
  - `concert_schedule_id`: 콘서트 일정 고유 번호
  - `concert_seat_id`: 콘서트 좌석 고유 번호
  - `expiredAt`
  - `amountUsed`: 포인트 사용량
  - `status`: 현재 상태

```json
{
    "user_id": 1,
    "concert_seat_id": 1
}
```

```json
{
    "booking_id": 1,
    "user_id": 1,
    "concert_schedule_id": 1,
    "concert_seat_id": 1,
    "expiredAt": "2024-10-10 15:30:00",
    "amountUsed": 15000,
    "status": "PROCESSING"
}
```


### :four: 잔액 충전 / 조회 API

4-1. 잔액 충전 API

- URL : `/booking/points/{userId}/charge`
  - Method : PUT
  - Description : 특정 유저의 포인트 충전

♦️ Request
  - Header
    - `Authorization`: Bearer 토큰 : 인증을 위한 토큰
  - Path variable
    - `userId`: 유저 고유번호
  - Body
    - `amount`: 충전 할 양

♦️ Response
  - Body
    - `point_id` 포인트 고유 번호 
    - `user_id` 유저 고유 번호 
    - `balance` 유저 포인트 

```json
{
    "user_id": 1,
    "amount": 10000
}
```

```json
{
    "point_id": 1,
    "user_id": 1,
    "balance": 10000
}
```

4-2. 잔액 조회 API

- URL : `/booking/points/{userId}`
  - Method : PUT
  - Description : 특정 유저의 포인트 조회

♦️ Request
  - Header
    - `Authorization`: Bearer 토큰 : 인증을 위한 토큰
  - Path variable
    - `userId`: 유저 고유번호

♦️ Response
  - Body
    - `point_id` 포인트 고유 번호 
    - `user_id` 유저 고유 번호 
    - `balance` 유저 포인트 

```json
{
    "point_id": 1,
    "user_id": 1,
    "balance": 10000
}
```

---

### :five: 결제 API

- URL : `/booking/bookings/payment`
- Method : Put
- Description : 좌석 결제 및 예약 확정

♦️ Request
- Header
  - `Authorization`: Bearer 토큰 : 인증을 위한 토큰
- Body
  - `booking_id`: 예약 고유 번호

♦️ Response
- Body
  - `booking_id`: 예약 고유 번호
  - `user_id`: 유저 고유 번호
  - `concert_schedule_id`: 콘서트 일정 고유 번호
  - `concert_seat_id`: 콘서트 좌석 고유 번호
  - `expiredAt`
  - `amountUsed`: 포인트 사용량
  - `status`: 현재 상태

```json
{
    "booking_id": 1
}
```

```json
{
    "booking_id": 1,
    "user_id": 1,
    "concert_schedule_id": 1,
    "concert_seat_id": 1,
    "expiredAt": "2024-10-10 15:30:00",
    "amountUsed": 15000,
    "status": "PROCESS"
}
```

### 프로젝트 패키지 구성

```

booking
   ├── interfaces :각 패키지 컨트롤러, dto
   │   ├── booking
   │   ├── concert
   │   ├── point
   │   ├── user
   │   │      
   ├── application: 각 패키지 서비스
   │   ├── booking 
   │   ├── concert
   │   ├── point
   │   ├── user
   ├── domain: 각 패키지 Repository, Entity
   │   ├── booking
   │   ├── concert
   │   ├── point
   │   ├── user
   ├── infrastructure: 각 패키지 JpaRepositoryImpl
   │   ├── booking
   │   ├── concert
   │   ├── point
   │   ├── user
   ├── exception
```
