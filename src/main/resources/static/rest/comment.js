const bookId = $('#bookId').val();
let currentPage = 0;
let size = 5;

function sendAjaxRequest(url, method, data, successCallback, errorCallback) {
    $.ajax({
        url: url,
        method: method,
        data: data,
        success: successCallback,
        error: errorCallback
    });
}

function showComments(page) {
    currentPage = page;
    sendAjaxRequest(`/api/comments/${bookId}?page=${page}`, "GET", null, function (response) {
        $('#showComments').html(response);
        getTotalComments(bookId).then(totalComments => {
            $('#totalComments').text(totalComments + ' COMMENTS');
            $('.page-text').text(`Showing ${ Math.min((currentPage + 1) * size, totalComments)} from ${totalComments} data`);
            updatePaginationLinks(currentPage, Math.ceil(totalComments / size));
        }).catch(error => {
            console.error('Failed to get total comments:', error);
        });
    }, function () {
        console.log('Failed to load comments');
    });
}

function getTotalComments(bookId) {
    return new Promise((resolve, reject) => {
        sendAjaxRequest(`/api/comments/count/${bookId}`, "GET", null, function (response) {
            resolve(response);
        }, function (error) {
            reject(error);
        });
    });
}

function updatePaginationLinks(currentPage, totalPages) {
    let html = '';
    if (currentPage+1 > 1) {
        html += `<li class="page-item"><a class="page-link prev" href="javascript:showComments(${currentPage - 1});">Prev</a></li>`;
    }
    for (let i = 0; i < totalPages; i++) {
        if (i === currentPage) {
            html += `<li class="page-item"><a class="page-link active" href="javascript:void(0);">${i+1}</a></li>`;
        } else {
            html += `<li class="page-item"><a class="page-link" href="javascript:showComments(${i});">${i+1}</a></li>`;
        }
    }
    if (currentPage+1 < totalPages) {
        html += `<li class="page-item"><a class="page-link next" href="javascript:showComments(${currentPage + 1});">Next</a></li>`;
    }
    $('.pagination').html(html);
}

$(document).ready(function () {
    showComments(currentPage);
    //POST && PUT comment
    $('#commentForm').on('submit', function (event) {
        event.preventDefault();
        const formData = $(this).serializeArray();
        const formDataObject = {};
        $.each(formData, function (i, v) {
            formDataObject[v.name] = v.value;
        });

        // Thêm bookId và accountId vào formDataObject
        formDataObject['bookId'] = $('#bookId').val();
        formDataObject['accountId'] = $('#accountId').val();

        let url = `/api/comments/${bookId}`;
        let method = "POST";
        if ($('#submit').val() === "Edit Comment") {
            const commentId = $('#commentId').val();
            url = `/api/comments/${commentId}`;
            method = "PUT";
        }

        sendAjaxRequest(url, method, formDataObject, function () {
            $('#submit').val('Post Comment');
            $('#content').val('');
            $('#parentCommentId').val('');
            $('#commentId').val('');
            showComments(0);
        }, function () {
        });
    });

    //show reply-box
    // $(document).on('click', '.reply', function () {
    //     const thisClicked = $(this);
    //     const parentCommentId = thisClicked.closest('.comment-body .reply').attr('id');
    //     const replyBoxHtml = generateReplyBox('', parentCommentId, '', 'Post Reply');
    //
    //     $('.reply-session').html("");
    //     thisClicked.closest('.comment-body').find('.reply-session').html(replyBoxHtml);
    //     const replyName = $(this).closest('.comment-body').find('.comment-author').find('.fn').val();
    //     $(this).closest('.comment-body').find('.reply-session').find('#content').attr('value', replyName);
    //     $(this).closest('.comment-body').find('.reply-session').find('#content').focus();
    // });
    $(document).on('click', '.reply', function () {
        const thisClicked = $(this);
        const parentCommentId = thisClicked.closest('.comment-body').find('.reply').attr('id');
        const replyBoxHtml = generateReplyBox('', parentCommentId, '', 'Post Reply');

        $('.reply-session').html("");
        thisClicked.closest('.comment-body').find('.reply-session').html(replyBoxHtml);

        const replyName = thisClicked.closest('.comment-body').find('.comment-author').find('.fn').text().trim();
        const contentValue = `<b>${replyName}</b> `;
        const replySessionContentField = thisClicked.closest('.comment-body').find('.reply-session').find('#content');

        replySessionContentField.val(contentValue);
        replySessionContentField.focus();
    });

    //add reply
    $(document).on('click', '.reply-add-btn', function (e) {
        e.preventDefault();
        const formData = $(this).closest('form').serializeArray();
        const formDataObject = {};
        $.each(formData, function (i, v) {
            formDataObject[v.name] = v.value;
        });
        // Thêm bookId và accountId vào formDataObject
        formDataObject['bookId'] = $('#bookId').val();
        formDataObject['accountId'] = $('#accountId').val();

        console.log(formDataObject);

        sendAjaxRequest(`/api/comments/${bookId}`, "POST", formDataObject, function () {
            showComments(currentPage);
        }, function () {
        });
    });

    //cancel reply-box
    $(document).on('click', '.reply-cancel-btn', function () {
        $('.reply-session').html("");
    });

    //edit comment
    $(document).on('click', '.edit', function () {
        const commentId = $(this).closest('.comment-body').find('.reply').attr('id');
        const content = $(this).closest('.comment-body').find('.comment-content p').text();
        const replyBoxHtml = generateReplyBox(commentId,'', content, 'Edit Comment');

        $(this).closest('.comment-body').find('.reply-session').html(replyBoxHtml);
    });

    //delete comment
    $(document).on('click', '.delete', function () {
        const commentId = $(this).attr("id");
        console.log('Delete comment with ID:', commentId);
        sendAjaxRequest(`/api/comments/${commentId}`, "DELETE", null, function () {
            showComments(currentPage);
        }, function () {
            console.error('Failed to delete comment');

        });
    });
});

// Generate the reply box HTML
function generateReplyBox(commentId, parentCommentId, content, value) {
    return `
        <div class="reply-box">
            <form id="commentForm" method="POST">
                <div class="form-group" style="margin: 12px 0">
                    <label for="content"></label>
                    <input class="form-control" id="content" name="content" placeholder="Enter Comment" value="${content}" required/>
                    <input id="commentId" name="commentId" type="hidden" value="${commentId}" />
                    <input id="parentCommentId" name="parentCommentId" type="hidden" value="${parentCommentId}" />
                </div>
                <span id="message"></span>
                <div class="form-group">
                    <input class="btn btn-primary reply-add-btn" id="submit" name="submit" type="submit" value="${value}" required/>
                    <button class="btn btn-danger reply-cancel-btn" id="cancel" name="cancel" type="button">Cancel</button>
                </div>
            </form>
        </div>`;
}