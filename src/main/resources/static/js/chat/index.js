'use strict';

document.write("<script src='../jquery/jquery-3.6.1.min.js'>")

let userNamePage = document.querySelector("#userName-page");
let chatPage = document.querySelector("#chat-page");
let userNameForm = document.querySelector("#userNameForm")
let messageForm = document.querySelector("#messageForm");
let messageInput = document.querySelector("#messageInput");
let messageArea = document.querySelector("#messageArea");
let connectingElement = document.querySelector(".connectingElement");

let stompClient = null;
let userName = null;
let colors = [
    '#39bbb0', '#FF9800', '#ff85af','#ffc107','#ff5652','#00BCD4','#32c787','#2196F3'
]

// Room Id 파라미터 가져오기
const url = new URL(location.href).searchParams;
const roomId = url.get("roomId");

function connect(event) {
    alert("Start Connect Function")
    userName = document.querySelector("#name").value.trim();

    // User Name 중복확인
    // isDuplicateName();

    // userNamePage에 hidden 속성 추가 후 ChatPage 등장
    let className = "hidden";
    userNamePage.classList.add(className);
    chatPage.classList.remove(className);

    // 연결하고자 하는 Socket의 EndPoint
    let socket = new SockJS("/ws-stomp");
    stompClient = Stomp.OVER(socket);

    stompClient.connect({}, onConnected, onError);

    event.preventDefault();
}

function onConnected() {
    // SUB 할 url -> /sub/chat/room/roomId 로 구독한다.
    stompClient.subscribe("/sub/chat/room/" + roomId, onMessageReceived);

    // 서버에 userName을 가진 유저가 들어왔다는 것을 알림
    // /pub/chat/enterUser 로 메시지를 보낸다.
    stompClient.send("/pub/chat/enterUSer",
        {},
        JSON.stringify({
            "roomId": roomId,
            "userName": userName,
            "type" : "ENTER"
        }))
    
    connectingElement.classList.add("hidden");
}

// User 닉네임 중복 확인
function isDuplicateName() {
    $.ajax({
        type: "GET",
        url: "/chat/duplicateName",
        data: {
            "userName": userName,
            "roomId" : roomId
        },
        SUCCESS: function (data) {
            console.log(data);
            username = data;
        }
    })
}

// User List 받기
// ajax로 User List를 받으며 Client가 입장, 퇴장 했다는 문구가 나왔을떄 마다 실행된다.
function getUserList() {
    const $list = $("list");
    $.ajax({
        type: "GET",
        url: "/chat/userList",
        data: {
            "roomId": roomId
        },
        SUCCESS: function (data) { 
            let users = "";
            for (let i = 0; i < data.length; i++) {
                users += "<li class='dropdown-item'>" + data[i] + "</li>"
            }
            $list.html(users);
        }
    })   
}

function onError(error) {
    connectingElement.textContent = "Could not connect to WebSocket Server. Please refresh this page to try again!";
    connectingElement.style.color = 'red';
}

// 메시지 전송때는 JSON형식을 메시지를 전달
function sendMessage(event) {
    let messageContent = messageInput.value.trim();

    if (messageContent && stompClient) { 
        let chatMessage = {
            "roomId": roomId,
            "userName": userName,
            message: messageInput.value,
            type: "TALK"
        };

        stompClient.send("/pub/chat/sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }

    event.preventDefault();
}

// 메시지 받을 때도 마찬가지로 JSON 타입으로 받으며,
// 넘어온 JSON 형식의 메시지를 parse해서 사용한다.
function onMessageReceived(payload) {
    let chat = JSON.parse(payload.body);
    let messageElement = document.createElement('li');
    let type = chat.type;

    switch (type) {
        case 'ENTER':
        case 'LEAVE':
            messageElement.classList.add('event-message');
            chat.content = chat.user + chat.message;
            getUserList();
            break;
        default:
            messageElement.classList.add('chat-message');

            let avatarElement = document.createElement("i");
            let avatarText = document.createTextNode(chat.user[0]);

            avatarElement.appendChild(avatarText);
            avatarElement.style['background-color'] = getAvatarColor(chat.userName);

            messageElement.appendChild(avatarElement);

            let userNameElement = document.createElement("span");
            let userNameText = document.createTextNode(chat.userName);
            userNameElement.appendChild(userNameText);
            messageElement.appendChild(userNameElement);
            break;
    }

    let textElement = document.createElement("p");
    let messageText = document.createTextNode(chat.message);
    
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);
    
    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;

}

function getAvatarColor(userName) {
    let hash = 0;
    for (let i = 0; i < userName.length; i++) { 
        hash = 31 * hash + userName.charCodeAt(i);
    }

    let index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener("submit", connect, true);
messageForm.addEventListener("submit", sendMessage, true);