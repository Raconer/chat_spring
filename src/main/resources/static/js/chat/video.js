$(document).ready(function (params) {
    start();
})


// 웹 소켓 생성
const socket = new WebSocket("wss://" + window.location.host + "/signal");

function start() {
    socket.onmessage = function (smg) { 
        let message = JSON.parse(msg.data);
        console.log(message);
    }
}


function handleErrorMessage(message) {
    console.error(message);
}