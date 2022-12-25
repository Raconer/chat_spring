<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <link rel="shortcut icon" href="data:image/x-icon;," type="image/x-icon">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="icon" href="data:,">
        <title>채팅 상세 페이지</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
        <link href="css/main.css" rel="stylesheet" />
        
    </head>
    <body>
        <nav class="navbar bg-light">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">Chatting</a>
            </div>
        </nav>
     
        <div class="container-md mt-5 chat" >
            <div class="card h-75" >
                <div class="card-body" style="min-height: 75%;" >
                    <ul id="chat-content" style="overflow-y: scroll;">
                    </ul>
                    <div class="input-group container-md mb-5">
                        <textarea id="message-text" class="form-control" rows="3"></textarea>
                        <div class="input-group-append">
                            <button id="message-submit" class="btn btn-outline-primary" type="button">전송</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
   
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/jquery-3.6.1.min.js" integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.4/sockjs.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
    <script src="js/common/index.js" type="text/javascript" ></script>
    <script src="js/chat/video.js" type="text/javascript" ></script>
  
</html>