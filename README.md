# 쓸만한 채팅 만들기

## 블로그

[채팅 만들기_1(basic)](https://donghokim.tistory.com/89)
[채팅 만들기_2(chatting_room)](https://donghokim.tistory.com/90)

## 참고

[Terian의 IT 도전기](https://terianp.tistory.com/146)
> 위의 블로그 내용에 따라 공부 하였습니다.

[pub/sub란?](https://cloud.google.com/pubsub/docs/overview?hl=ko)

## 프로젝트 목표

> 진짜 서비스 할수 있을 만큼의 채팅 서비스 만들기

## 기본 개녕

### STOMP

> Simple Text Oriented Messaging Protocol의 약자로 메시지 전송을 위한 프로토콜이다.

#### WebSocket과 다른점

> **WebSocket 특징**
> 기본적인 과 가장 크게 다른 점은 기존의 **WebSocket**만을 사용한 통신은 발신자와 수신자를 Spring단에서 직접 관리 해야만 했다.
> 즉 `**WebSocketHandler**를 만들어서 **WebSocket**통신을 하는 **User**들을 **Map**으로 직접 관리를 하고
> **Client**에서 들어오는 **Message**를 다른 **User**에게 전달하는 코드를 직접 구현해야 했다.

> **STOMP 특징**
> [**Pub/Sub**](https://cloud.google.com/pubsub/docs/overview?hl=ko) 기반으로 동작 하기 떄문에 메시지의 **전송/수신**에 대한 처리를 명확하게 정의 할수있다.
> 즉. @MessagingMapping 같은 어노테이션을 사용하여 메시지 **전송/수신**을 처리 할수있다.
> ****

## Import Gradle

```gradle
	// Spring Web Socket
	implementation 'org.springframework.boot:spring-boot-starter-websocket'

    // sockjs
    implementation 'org.webjars:sockjs-client:1.1.2'

    // stomp
    implementation 'org.webjars:stomp-websocket:2.3.4'
```

## 사용 SQL DDL

> 테스트 용으로 만들다 보니 최대한 간략 하게 구성 하였습니다.

```sql

/* chatting db definition */
CREATE DATABASE `chatting` 
/*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

-- room definition
CREATE TABLE `room` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `user_cnt` varchar(100) NOT NULL DEFAULT '0',
  `reg_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

```