const bookId = $('#bookId').val();
let currentPage = 0;
let size = 5;

function sendAjaxRequest(url, method, data) {
    return $.ajax({
        url,
        method,
        data
    });
}

function showComments(page) {
    currentPage = page;
    sendAjaxRequest(`/api/comments/${bookId}?page=${page}`, "GET")
        .then(response => {
            $('#showComments').html(response);
            return getTotalComments(bookId);
        })
        .then(totalComments => {
            $('#totalComments').text(`${totalComments} COMMENTS`);
            $('.page-text').text(`Showing ${Math.min((currentPage + 1) * size, totalComments)} from ${totalComments} data`);
            updatePaginationLinks(currentPage, Math.ceil(totalComments / size));
        })
        .catch(error => {
            console.error('Failed to load comments:', error);
        });
}

function getTotalComments(bookId) {
    return sendAjaxRequest(`/api/comments/count/${bookId}`, "GET");
}

function updatePaginationLinks(currentPage, totalPages) {
    const pagination = $('.pagination');
    pagination.empty();

    if (currentPage > 0) {
        pagination.append(`<li class="page-item"><a class="page-link prev" href="javascript:showComments(${currentPage - 1});">Prev</a></li>`);
    }

    for (let i = 0; i < totalPages; i++) {
        pagination.append(`<li class="page-item"><a class="page-link ${i === currentPage ? 'active' : ''}" href="javascript:${i === currentPage ? 'void(0)' : `showComments(${i})`};">${i+1}</a></li>`);
    }

    if (currentPage + 1 < totalPages) {
        pagination.append(`<li class="page-item"><a class="page-link next" href="javascript:showComments(${currentPage + 1});">Next</a></li>`);
    }
}

function handleCommentSubmit(event, url, method, successMessage) {
    event.preventDefault();

    if ($('#accountId').length === 0) {
        $('#loginModal').modal('show');
        return;
    }

    const formData = $(event.target).serializeArray();
    const formDataObject = formData.reduce((obj, item) => ({...obj, [item.name]: item.value}), {});
    formDataObject.bookId = $('#bookId').val();
    formDataObject.accountId = $('#accountId').val();

    sendAjaxRequest(url, method, (formDataObject))
        .then((comment) => {
            console.log(comment);
            $('#submit').val('Post Comment');
            $('#content, #parentCommentId, #commentId').val('');
            showComments(0);
            toastr.success(successMessage);
        })
        .catch((jqXHR, error) => {
            const errorMessage = jqXHR.responseJSON?.error || error || 'Failed to submit comment';
            toastr.error(errorMessage);
        });
}

$(document).ready(function () {
    showComments(currentPage);
    //POST && PUT comment
    $('#commentForm').on('submit', event => handleCommentSubmit(event, `/api/comments/${bookId}`, "POST", 'Comment added successfully'));

    //show reply form
    $(document).on('click', '.reply', function () {
        if ($('#accountId').length === 0) {
            $('#loginModal').modal('show');
            return;
        }
        const parentCommentId = $(this).attr('id');
        const replyName = $(this).closest('.comment-body').find('.comment-author .fn').text().trim();
        const replyBox = $(this).closest('.comment-body').find('.reply-session');

        replyBox.html(generateReplyBox('', parentCommentId, '', 'Post Reply'));
        replyBox.find('#content').val(`<b>${replyName}</b> `).focus();
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

        const commentId = formDataObject.commentId;
        const isEdit = $(this).val() === "Edit Comment";
        const url = isEdit ? `/api/comments/${commentId}` : `/api/comments/${bookId}`;
        const method = isEdit ? "PUT" : "POST";
        const msg = isEdit ? "Comment edited successfully" : "Reply added successfully";
        sendAjaxRequest(url, method, formDataObject,
            function (comment) {
                console.log(comment);
                showComments(currentPage);
                toastr.success(msg);
            },
            function (jqXHR, error) {
                const errorMessage = jqXHR.responseJSON?.error || error || 'Failed to submit comment';
                toastr.error(errorMessage);
            });
    });

    //cancel reply-box
    $(document).on('click', '.reply-cancel-btn', () => $('.reply-session').empty());

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
        sendAjaxRequest(`/api/comments/${commentId}`, "DELETE")
            .then(() => {
                showComments(currentPage);
                toastr.success('Comment deleted successfully');
            })
            .catch(jqXHR => {
                const errorMessage = jqXHR.responseJSON?.error || 'Failed to delete comment';
                toastr.error(errorMessage);
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