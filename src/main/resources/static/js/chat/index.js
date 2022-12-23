let date = new Date();
let isSend = false;
const userName = "user_" + date.getHours() + "-" + date.getMinutes() + "-" + date.getSeconds();

$(document).ready(function () {
    connect();
});

$(document).on("click", "#message-submit", () => {
    sendMessage();
});

let stompClient = null;

// Room Id 파라미터 가져오기
const url = new URL(location.href).searchParams;
const roomId = url.get("id");

async function connect(event) {
    let socket = new SockJS("/ws-stomp");
    stompClient = await Stomp.over(socket);
    // stompClient.debug = null;
    await stompClient.connect({}, onConnected, onError);

}

async function onConnected() {
    let json = {
            roomId: roomId,
            userName : userName,
            type : "ENTER"
        }

    // SUB 할 url -> /sub/chat/room/roomId 로 구독한다.
    // 모든 Receive처리는 onMessageReceived 여기서 처리한다.
    await stompClient.subscribe("/sub/chat/room/" + roomId, onMessageReceived);

    // 서버에 userName을 가진 유저가 들어왔다는 것을 알림
    // /pub/chat/enterUser 로 메시지를 보낸다.
    await  stompClient.send("/pub/chat/enterUser",
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