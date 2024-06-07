function showComments() {
    $.ajax({
        //Get comment html code
        url: `/api/comments/test`,
        method: "GET",
        success: function (response) {
            $('#showComments').html(response);
            getTotalComments()
        }
    })
}

function getTotalComments() {
    $.ajax({
        url: `/api/comments/count`,
        method: "GET",
        success: function (response) {
            $('#totalComments').text(response + ' COMMENTS');
        }
    })
}

$(document).ready(function () {
    showComments();
    //POST/PUT comment
    $('#commentForm').on('submit', function (event) {
        event.preventDefault();
        const formData = $(this).serializeArray();
        const formDataObject = {};
        $.each(formData,
            function (i, v) {
                formDataObject[v.name] = v.value;
            });
        console.log(formDataObject);

        let url = `/api/comments`;
        let method = "POST";
        if ($('#submit').val() === "Edit Comment") {
            const commentId = $('#commentId').val();
            url = `/api/comments/${commentId}`;
            method = "PUT";
        }
        $.ajax({
            //Post comment
            url: url,
            method: method,
            data: formDataObject,
            dataType: 'json',
            xhrFields: {
                withCredentials: false
            },

            success: function () {
                $('#submit').val('Post Comment');
                $('#content').val('');
                $('#parentCommentId').val('');
                $('#commentId').val('');
                showComments();
                alert('Comment posted successfully');
                // reloadPageWithoutScrollEffect();
            },
            error: function () {
                console.error('Failed to post comment');
                alert('Failed to post comment');
            }
        })
    });

    //show reply-box
    $(document).on('click', '.reply', function () {

        const thisClicked = $(this);
        const parentCommentId = thisClicked.closest('.comment-body .reply').attr('id');

        $('.reply-session').html("");
        thisClicked.closest('.comment-body').find('.reply-session')
            .html('<div class="reply-box">\n' +
                '<form id="commentForm" method="POST">\n' +
                '<div class="form-group" style="margin: 12px 0">\n' +
                '<label for="content"></label>\n' +
                '<input class="form-control" id="content" name="content" placeholder="Enter Comment" required/>\n' +
                `<input id="parentCommentId" name="parentCommentId" type="hidden" value="${parentCommentId}" />\n` +
                '</div>\n' +
                '<span id="message"></span>\n' +
                '<div class="form-group">\n' +
                '<input class="btn btn-primary reply-add-btn" id="submit" name="submit" type="submit" value="Post Comment" required/>\n' +
                '<button class="btn btn-danger reply-cancel-btn" id="cancel" name="cancel" type="button">Cancel</button>\n' +
                '</div>\n' +
                '</form>\n' +
                '</div>');
    });

    //cancel reply-box
    $(document).on('click', '.reply-cancel-btn', function () {
        $('.reply-session').html("");
    });

    //reply comment
    $(document).on('click', '.reply-add-btn', function (e) {
        e.preventDefault();
        const formData = $(this).closest('form').serializeArray();
        const formDataObject = {};
        $.each(formData,
            function (i, v) {
                formDataObject[v.name] = v.value;
            });
        console.log(formDataObject);

        $.ajax({
            //Post comment
            url: `/api/comments`,
            method: "POST",
            data: formDataObject,
            success: function () {
                showComments();
                alert('Comment posted successfully');
                // reloadPageWithoutScrollEffect();
            },
            error: function () {
                console.error('Failed to post comment');
                alert('Failed to post comment');
            }
        })
    });

    //edit comment
    $(document).on('click', '.edit', function () {
        const commentId = $(this).closest('.comment-body').find('.reply').attr('id');
        const content = $(this).closest('.comment-body').find('.comment-content p').text();
        let $content = $('#content');
        console.log('Edit comment with ID:', commentId);
        $content.val(content);
        $('#commentId').val(commentId);
        $('#submit').val('Edit Comment');

        $('.reply-box').insertAfter($(this).closest('.comment-body, .reply')).show();
        $content.focus();
    });

    //delete comment
    $(document).on('click', '.delete', function () {
        const commentId = $(this).attr("id");
        console.log('Delete comment with ID:', commentId);
        $.ajax({
            url: `/api/comments/${commentId}`,
            method: 'DELETE',
            success: function () {
                showComments();
            },
            error: function () {
                console.error('Failed to delete comment');
            }
        });
    });
});

// Lưu vị trí cuộn hiện tại trước khi reload
function saveScrollPosition() {
    localStorage.setItem('scrollPosition', window.scrollY);
}

// Load trang mà không có hiệu ứng cuộn
function reloadPageWithoutScrollEffect() {
    const scrollPosition = localStorage.getItem('scrollPosition');
    if (scrollPosition) {
        window.scrollTo(0, scrollPosition);
        localStorage.removeItem('scrollPosition'); // Xóa vị trí cuộn đã lưu sau khi sử dụng
    }
    location.reload();
}
