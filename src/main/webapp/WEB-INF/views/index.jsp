<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="shortcut icon" href="data:image/x-icon;," type="image/x-icon">
        <link rel="icon" href="data:,">
        <title>채팅 테스트</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
        <link href="css/main.css" rel="stylesheet" />
    </head>
    <body>
        <nav class="navbar bg-light">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">Chatting</a>
            </div>
        </nav>
        <div class="container-md mt-5">
             <div class="row">
                <div class="col">
                    <div class="input-group mb-3">
                        <input id="chatRoomName" type="text" class="form-control" placeholder="채팅방 추가" aria-label="채팅방 추가" aria-describedby="button-addon2">
                        <button id="chatRoomAddBtn" class="btn btn-outline-primary" type="button" >추가</button>
                    </div>
                </div>
            </div>
            <table class="table table-striped table-hover">
                <caption>채팅방 목록</caption>
                <tr class="table-primary">
                    <th>이름</th>
                    <th>접속한 사용자 수</th>
                    <th>생성일</th>
                </tr>
               <c:forEach var="chatRoom" items="${chatRoomList}" varStatus="status">
                <tr class="detail pointer" data-id="${chatRoom.id}"  onClick="location.href='/room?id=${chatRoom.id}'" >
                    <td>${chatRoom.name}</td>
                    <td>${chatRoom.userCnt}</td>
                    <td>
                        <fmt:formatDate value="${chatRoom.regDate}" pattern="yyyy-MM-dd"/>
                    </td>
                </tr>
                </c:forEach>
            </table>
        </div>
    </body>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>
    <script src="js/jquery/jquery-3.6.1.min.js" type="text/javascript" ></script>
    <script src="js/common/index.js" type="text/javascript" ></script>
    <script src="js/chat/room.js" type="text/javascript" ></script>
</html>