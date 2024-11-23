 <aside>
💡 아래 명세를 잘 읽어보고, 서버를 구현합니다.

</aside>

## Description

- **`콘서트 예약 서비스`**를 구현해 봅니다.
- 대기열 시스템을 구축하고, 예약 서비스는 작업가능한 유저만 수행할 수 있도록 해야합니다.
- 사용자는 좌석예약 시에 미리 충전한 잔액을 이용합니다.
- 좌석 예약 요청시에, 결제가 이루어지지 않더라도 일정 시간동안 다른 유저가 해당 좌석에 접근할 수 없도록 합니다.

<aside>
🗓️ **Weekly Schedule Summary: 이번 챕터의 주간 일정 (금요일 오전 10시까지 제출)**

</aside>

### 9주차 과제


### **`STEP 17_기본`**
- docker 를 이용해 kafka 를 설치 및 실행하고 애플리케이션과 연결
- 각 프레임워크 (nest.js, spring) 에 적합하게 카프카 consumer, producer 를 연동 및 테스트

### **`STEP 18_심화`**

- 기존에 애플리케이션 이벤트를 카프카 메세지 발행으로 변경
- 카프카의 발행이 실패하는 것을 방지하기 위해 Transactional Outbox Pattern를 적용
- 카프카의 발행이 실패한 케이스에 대한 재처리를 구현 ( Scheduler or BatchProcess )

## STEP 17

카프카 클러스터링
![](https://velog.velcdn.com/images/saruru/post/f405ffd4-77cf-49c5-bc03-4358f610d8fa/image.png)

브로커 (총3개)
![](https://velog.velcdn.com/images/saruru/post/c5726a58-d1fc-4c12-90e5-2e704089a7a8/image.png)

카프카 이벤트 발행 (TOPIC: test-topic, Message: test-message)
![](https://velog.velcdn.com/images/saruru/post/2fa29cb1-361f-46ba-a58b-f2f73eb371f5/image.png)

카프카 컨슈머로 메시지 수신
![](https://velog.velcdn.com/images/saruru/post/6dfd523e-3d68-45dd-be2e-22ceb99b4386/image.png)

UI를 통하여 메시지 확인
![](https://velog.velcdn.com/images/saruru/post/634659ca-140b-403c-89a2-bdb402c7f947/image.png)

## STEP 18

BEFORE COMMIT 이벤트 -> 아웃박스 상태 변경, sent_at 마킹 
![](https://velog.velcdn.com/images/saruru/post/df5e973e-86d4-430e-ab2a-457d2fa841b3/image.png)

카프카 이벤트
![](https://velog.velcdn.com/images/saruru/post/ff80df33-3d4c-421a-82ed-fa54e7798e90/image.png)

슬랙 이벤트 
![](https://velog.velcdn.com/images/saruru/post/67434ca2-a205-4895-a1a9-31cfd364201e/image.png)

아웃박스 상태변경
![](https://velog.velcdn.com/images/saruru/post/a43a935d-c658-44ed-ba22-5e9f8c97e694/image.png)

아웃박스 테이블 조회 화면
![](https://velog.velcdn.com/images/saruru/post/96472726-7b3c-44a2-b9e7-0ad01fcb4d82/image.png)

슬랙 메시지 수신확인
![](https://velog.velcdn.com/images/saruru/post/f7048ab7-1c35-45a8-a50c-030f55d62ee9/image.png)
