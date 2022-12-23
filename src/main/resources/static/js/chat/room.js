$(document).on("click", "#chatRoomAddBtn", () => {
    let name = document.getElementById("chatRoomName").value;
    if (!name) {
        alert("Please Enter the name For Chatting room");
        return;
    }

    let data = {
        name: name
    };
    postAjax("/api/room", data)
        .then((data) => {
            location.reload();
        }).catch(() => {
            alert("Failed");
        });
});
