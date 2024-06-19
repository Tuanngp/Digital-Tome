class ChatApp {
    constructor() {
        this.stompClient = null;
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
                    if (message.receiver === this.senderInput.val()) {
                        this.addMessageToConversation(message);
                    } else {
                        // this.notifyUser(message);
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

        this.contentInput.val('');
        this.contentInput.focus();
    }

    addMessageToConversation(message) {
        const messageLi = $('<li>').addClass('clearfix');
        const messageDataDiv = $('<div>').addClass(message.sender === this.senderInput.val() ? 'message-data d-flex flex-row-reverse align-items-center': 'message-data');
        if (message.avatarSender != null) {
            const messageDataTimeSpan = $('<span>').addClass('message-data-time mx-2').text(message.createdDate)
            const messageDataImg = $('<img>').attr('src', message.avatarSender).attr('alt', 'avatar');
            messageDataDiv.append(messageDataImg);
            messageDataDiv.append(messageDataTimeSpan);
            messageLi.append(messageDataDiv);
        } else {
            messageLi.addClass('mb-0');
        }

        const messageDiv = $('<div>').addClass('message')
            .addClass(message.sender === this.senderInput.val() ? 'message other-message float-right' : 'message my-message')
            .text(message.content);

        messageLi.append(messageDiv);

        if (message.sender === this.senderInput.val()) {
            const deleteDropdown = $('<div>').addClass('dropdown');
            const deleteButton = $('<button>').attr('type', 'button').addClass('btn btn-link light light sharp').attr('data-bs-toggle', 'dropdown').attr('aria-expanded', 'false');

            const deleteIcon = '<svg width="15px" height="15px" viewBox="0 0 24 24" version="1.1">' +
                                            '<g stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">' +
                                                '<rect x="0" y="0" width="24" height="24"></rect>' +
                                                '<circle fill="#000000" cx="5" cy="12" r="2"></circle>' +
                                                '<circle fill="#000000" cx="12" cy="12" r="2"></circle>' +
                                                '<circle fill="#000000" cx="19" cy="12" r="2"></circle>' +
                                            '</g>' +
                                        '</svg>';
            const dropdownMenu = $('<div>').addClass('dropdown-menu dropdown-menu-end');
            const deleteOption = $('<a>').addClass('dropdown-item delete').attr('id', message.id).text('Delete');

            deleteButton.append(deleteIcon);
            dropdownMenu.append(deleteOption);
            deleteDropdown.append(deleteButton);
            deleteDropdown.append(dropdownMenu);

            messageDataDiv.append(deleteDropdown);

            deleteOption.click(() => {
                fetch('/message/' + message.id, {
                    method: 'DELETE'
                }).then(() => {
                    messageLi.remove();
                });
            });
        }

        this.conversationDiv.append(messageLi);
        this.conversationDiv.scrollTop(this.conversationDiv.prop('scrollHeight'));
    }

    getChatUsers() {
        fetch('/chat/users')
            .then(response => response.json())
            .then(users => {
                const userList = $('.chat-list');
                userList.empty();
                users.forEach(user => {
                    const userLi = $('<li>').addClass('clearfix');
                    const userImg = $('<img>').attr('src', user.avatarPath).attr('alt', 'avatar');
                    const aboutDiv = $('<div>').addClass('about');
                    const nameDiv = $('<div>').addClass('name').text(user.fullname);
                    const statusDiv = $('<div>').addClass('status').html('<i class="fa fa-circle offline"></i> left 7 mins ago');

                    aboutDiv.append(nameDiv);
                    aboutDiv.append(statusDiv);
                    userLi.append(userImg);
                    userLi.append(aboutDiv);

                    userLi.click(() => {
                        this.receiverInput.val(user.username);
                        this.displayConversation(this.senderInput.val(), user.username);

                        $('.userInfo img').attr('src', user.avatarPath);
                        $('.userInfo .chat-about h6').text(user.fullname);
                    });
                    userList.append(userLi);
                });
            });
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
                    console.log(message);
                    this.addMessageToConversation(message);
                });
            });
    }

    init() {
        console.log("Index page is ready");
        this.connect();
        this.getChatUsers()
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