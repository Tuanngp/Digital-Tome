class ChatApp {
    constructor() {
        this.stompClient = null;
        this.currentUser = null;
        this.conversationDiv = $('#conversation');
        this.messageForm = $('#messageForm');
        this.senderInput = $('#sender');
        this.receiverInput = $('#receiver');
        this.contentInput = $('#content');
    }

    connect() {
        const socket = new SockJS('/ws');
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({},
            (frame) => { // Callback for successful connection
                console.log('Connected: ' + frame);
                this.stompClient.subscribe('/topic/messages', (messageOutput) => {
                    const message = JSON.parse(messageOutput.body);
                    console.log("Message: " + message);
                    this.currentUser = this.senderInput.val();
                    if (message.receiver === this.currentUser) {
                        this.addMessageToConversation(message);
                    } else {
                        this.notifyUser(message);
                    }
                });
            },
            (error) => { // Callback for connection errors
                console.log('Connection error: ' + error);
                // Try to reconnect after 5 seconds
                setTimeout(() => this.connect(), 5000);
            }
        );
    }

    sendMessage() {
        const message = {
            sender: this.senderInput.val(),
            receiver: this.receiverInput.val(),
            content: this.contentInput.val(),
            status: 'unread'
        };
        // Send message to receiver
        this.stompClient.send("/app/send/message", {}, JSON.stringify(message));
        // Also send message to sender
        this.stompClient.send("/app/send/message", {}, JSON.stringify({
            ...message,
            receiver: message.sender,
            sender: message.sender
        }));
    }

    addMessageToConversation(message) {
        const messageDiv = $('<div>').text(message.content);
        if (message.sender === this.currentUser) {
            messageDiv.addClass('outgoing');
        } else {
            messageDiv.addClass('incoming');
        }
        this.conversationDiv.append(messageDiv);
    }

    notifyUser(message) {
        if (!("Notification" in window)) {
            alert("This browser does not support desktop notification");
        } else if (Notification.permission === "granted") {
            new Notification(`New message from ${message.sender}: ${message.content}`);
        } else if (Notification.permission !== "denied") {
            Notification.requestPermission().then(function (permission) {
                if (permission === "granted") {
                    new Notification(`New message from ${message.sender}: ${message.content}`);
                }
            });
        }
    }

    displayConversation(user1, user2) {
        fetch('/conversation/' + user1 + '/' + user2)
            .then(response => response.json())
            .then(messages => {
                this.conversationDiv.empty();
                messages.forEach(message => {
                    const messageDiv = $('<div>').text(message.content);
                    this.conversationDiv.append(messageDiv);
                });
            });
    }

    init() {
        console.log("Index page is ready");
        this.connect();

        this.messageForm.submit((event) => {
            event.preventDefault();
            this.sendMessage();
        });
    }
}

$(document).ready(function() {
    const chatApp = new ChatApp();
    chatApp.init();
});