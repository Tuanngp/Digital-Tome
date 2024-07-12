class ChatApp {
    constructor() {
        this.stompClient = null;
        this.conversationDiv = $('#conversation');
        this.messageForm = $('#messageForm');
        this.senderInput = $('#sender');
        this.receiverInput = $('#receiver');
        this.contentInput = $('#content');
        this.userList = $('.chat-list');
        this.searchResults = $('#searchResults');
        this.users = {};
    }

    connect() {
        const socket = new SockJS('/ws');
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({},
            (frame) => { // Callback for successful connection
                console.log('Connected: ' + frame);
                this.subscribeToMessages();
            },
            (error) => { // Callback for connection errors
                console.log('Connection error: ' + error);
                // Try to reconnect after 5 seconds
                setTimeout(() => this.connect(), 5000);
            }
        );
    }

    subscribeToMessages() {
        this.stompClient.subscribe('/topic/messages', messageOutput => {
            const message = JSON.parse(messageOutput.body);
            if (message.receiver === this.receiverInput.val()) {
                this.addMessageToConversation(message);
            } else {
                this.displayConversation(this.senderInput.val(), this.receiverInput.val());
                this.markUserUnread(message.sender);
            }
        });
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

        this.contentInput.val('').focus();
        this.moveUserToTop(message.receiver);
        this.moveUserToTop(message.sender);
        this.addUserToChatList(this.users[message.receiver]);
        this.addUserToChatList(this.users[message.sender]);
    }

    displayConversation(user1, user2) {
        fetch('/conversation/' + user1 + '/' + user2)
            .then(response => response.json())
            .then(messages => {
                this.conversationDiv.empty();
                messages.forEach(message => {
                    this.addMessageToConversation(message);
                });
            });
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
                if (confirm('Are you sure you want to delete this message?')) {
                    fetch('/message/' + message.id, {
                        method: 'DELETE'
                    }).then(() => {
                        messageLi.remove();
                        this.displayConversation(this.senderInput.val(), this.receiverInput.val());
                    });
                }
            });
        }

        this.conversationDiv.append(messageLi);
        this.conversationDiv.scrollTop(this.conversationDiv.prop('scrollHeight'));
    }

    addUserToChatList(user) {
        // Check if user is already in chat list
        const existingUserLi = this.userList.find('li:contains(' + user.fullName + ')');
        if (existingUserLi.length > 0) {
            // If user is already in chat list, move them to the top
            this.userList.prepend(existingUserLi);
        } else {
            // If user is not in chat list, add them
            const userLi = $('<li>').addClass('clearfix');
            const userImg = $('<img>').attr('src', user.avatarPath).attr('alt', 'avatar');
            const aboutDiv = $('<div>').addClass('about');
            const nameDiv = $('<div>').addClass('name').text(user.fullName);
            const usernameDiv = $('<div>').addClass('username').text(user.username).css('display', 'none');
            // const statusDiv = $('<div>').addClass('status').html('<i class="fa fa-circle offline"></i> left 7 mins ago');
            aboutDiv.append(nameDiv);
            aboutDiv.append(usernameDiv);
            // aboutDiv.append(statusDiv);
            userLi.append(userImg);
            userLi.append(aboutDiv);

            this.userList.append(userLi);
            userLi.click(() => {
                this.receiverInput.val(user.username);
                this.displayConversation(this.senderInput.val(), user.username);
                this.markUserRead();
                $('.userInfo img').attr('src', user.avatarPath);
                $('.userInfo .chat-about h6').text(user.fullName);

                // If user has unread messages, remove 'unread' class and set messages to read
                if (this.userList.find(`li:contains(${user.fullName})`).hasClass('unread')) {
                    this.userList.find(`li:contains(${user.fullName})`).removeClass('unread');
                    this.markUserRead();
                }
            });
        }
    }

    // move user to the top of the chat list
    moveUserToTop(username) {
        this.userList.prepend(this.userList.find(`li:contains(${username})`));
    }

    getChatUsers() {
        fetch('/chat/users')
            .then(response => response.json())
            .then(users => {
                users.forEach(user => {
                    console.log(user);
                    this.addUserToChatList(user);
                });
            })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
    }

    searchUsers(keyword) {
        fetch('/api/user/search?keyword=' + keyword)
            .then(response => response.json())
            .then(userOutput => {
                // Clear previous search results
                this.searchResults.empty();
                // Append new search result
                const user = userOutput.userDto;
                const userDiv = $('<div>').text(user.fullName);
                userDiv.css('padding', '12px').hover(() => {
                    userDiv.css({
                        'background-color': 'lightgray',
                        'cursor': 'pointer'
                    });
                });
                userDiv.click(() => {
                    // Display conversation with the selected user
                    this.receiverInput.val(user.username);
                    this.displayConversation(this.senderInput.val(), user.username);

                    $('.userInfo img').attr('src', user.avatarPath);
                    $('.userInfo .chat-about h6').text(user.fullname);
                    // Add user to chat list
                    this.addUserToChatList(user);
                });
                this.searchResults.append(userDiv);
                // Show search results
                this.searchResults.show();
            });
    }

    markUserUnread(username) {
        this.userList.find(`li:contains(${username})`).addClass('unread').css('font-weight', 'bold');
    }

    markUserRead() {
        fetch('/messages/read', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                sender: this.senderInput.val(),
                receiver: this.receiverInput.val(),
            })
        }).then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
        }).catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
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

    init() {
        this.connect();
        this.getChatUsers()
        this.messageForm.submit((event) => {
            event.preventDefault();
            this.sendMessage();
        });

        $('#searchInput').on('input', () => {
            const keyword = $('#searchInput').val();
            if (keyword.length > 0) {
                this.searchUsers(keyword);
            } else {
                // Hide search results when input is empty
                this.searchResults.hide();
            }
        });
    }
}

$(document).ready(function() {
    const chatApp = new ChatApp();
    chatApp.init();
});