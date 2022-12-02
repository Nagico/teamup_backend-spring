'use strict';
 
var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var receiverInput = document.querySelector('#receiver')
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
 
var stompClient = null;
var username = null;
 
var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];
 
function connect(event) {
    username = document.querySelector('#name').value.trim();
 
    if(username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');
        //////////////////////////////////////////////////////////////////////////////////////
        // ws (sockjs) url
        //////////////////////////////////////////////////////////////////////////////////////
        //var socket = new SockJS('https://api.teamup.nagico.cn/ws');
        var socket = new SockJS('http://localhost:8080/ws');
        stompClient = Stomp.over(socket);
 
        stompClient.connect({
            Authorization: "Bearer " + username
 
        }, onConnected, onError);
    }
    event.preventDefault();
}
 
 
function onConnected() {
 
    // Subscribe to the Public Message
    stompClient.subscribe('/topic/public', onMessageReceived);

    stompClient.subscribe('/user/topic/private', onMessageReceived);
 
    connectingElement.classList.add('hidden');
}
 
 
function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}
 
 
function sendMessage(event) {
    var messageContent = messageInput.value.trim();
 
    if(messageContent && stompClient) {
        var chatMessage = {
            id: uuid(),
            content: messageInput.value,
            type: 'Chat',
            receiver: receiverInput.value == "" ? null : receiverInput.value,
        };
        console.log("----- ："+ messageInput.value);
        stompClient.send("/app/send", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}
 
 
function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
 
    var messageElement = document.createElement('li');
 
    messageElement.classList.add('chat-message');

    var avatarElement = document.createElement('i');
    var avatarText = document.createTextNode(message.sender[0]);
    avatarElement.appendChild(avatarText);
    avatarElement.style['background-color'] = getAvatarColor(message.sender);

    messageElement.appendChild(avatarElement);

    var usernameElement = document.createElement('span');
    var usernameText = document.createTextNode(message.sender);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);

 
    var textElement = document.createElement('p');
    var messageText = document.createTextNode('消息：'+message.content);
    textElement.appendChild(messageText);
 
    messageElement.appendChild(textElement);
 
    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}
 
 
function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
 
    var index = Math.abs(hash % colors.length);
    return colors[index];
}
 
usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)

function uuid() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 32; i++) {
    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23];
    var uuid = s.join("");
    return uuid;
}