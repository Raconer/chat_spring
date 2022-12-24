// 메시지 좌우 구분으로 인한 변수 설정
let isSend = false;
// 임시 사용자 이름
let date = new Date();
const userName = "user_" + date.getHours() + "-" + date.getMinutes() + "-" + date.getSeconds();

// JS파일 존재시 실행
$(document).ready(function () {
    connect();
});

// 메시지 전송 이벤트 
$(document).on("click", "#message-submit", () => {
    sendMessage();
});
// STOMP 변수
let stompClient = null;

// Room Id 파라미터 가져오기
const url = new URL(location.href).searchParams;
const roomId = url.get("id");

// STOMP로 연결한다.
async function connect(event) {
    let socket = new SockJS("/ws-stomp");
    stompClient = await Stomp.over(socket);
    // 크롬 및 개발자 모드의 콘솔 추력 방지
    // stompClient.debug = null;

    
    // stomp Client 접속
    // onConnected : 채팅방 접근 및 접속 처리
    // onError : STOMP 에러 발생시
    await stompClient.connect({}, onConnected, onError);

}

// STOMP로 roomId별 채팅방에 접근을 한다.
async function onConnected() {
    let json = {
            roomId: roomId,
            userName : userName,
            type : "ENTER"
        }

    // Response하는 결과는 onMessageReceived에서 처리한다.
    await stompClient.subscribe("/sub/chat/room/" + roomId, onMessageReceived);

    // 사용자 입장
    await stompClient.send("/pub/chat/enterUser",
        {},
        JSON.stringify(json))
    
}

function onError(error) {
    alert("Error")
}

// 메시지 전송때는 JSON형식을 메시지를 전달
function sendMessage() {
    this.isSend = true;
    let messageElement = document.getElementById("message-text");
    console.log("MSG : " + messageElement.value)

    if (messageElement && stompClient) { 
        let chatMessage = {
            "roomId": roomId,
            "userName": userName,
            message: messageElement.value,
            type: "TALK"
        };

        stompClient.send("/pub/chat/sendMessage", {}, JSON.stringify(chatMessage));
        messageElement.value = '';
    }

}

// 메시지 받을 때도 마찬가지로 JSON 타입으로 받으며,
// 넘어온 JSON 형식의 메시지를 parse해서 사용한다.
function onMessageReceived(payload) {
    console.log('Receive Message')
    let chat = JSON.parse(payload.body);
    let messageElement = document.createElement('li');
    let type = chat.type;
    switch (type) {
        case 'ENTER':
        case 'LEAVE':
            messageElement.classList.add('notice');
            chat.content = chat.user + chat.message;
            break;
        default:
            let sendClass = "other";
            if (this.isSend) { 
                sendClass = "me";
            }
            messageElement.classList.add(sendClass);
            this.isSend = false;
            chat.content = chat.user + chat.message;

            break;
    }

    let textElement = document.createElement("p");
    let messageText = document.createTextNode(chat.message);
    
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);
    
    let content = document.getElementById("chat-content");
    content.append(messageElement);

}