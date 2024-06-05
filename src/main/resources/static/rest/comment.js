function showComments() {
    $.ajax({
        //Get comment html code
        url: "http://localhost:8080/api/comments/test",
        method: "GET",
        success: function (response) {
            $('#showComments').html(response);
        }
    })
}

function showNotification(message) {
    toastr.success(message);
}

$(document).ready(function () {
    if (performance.navigation.type === 1) {
        showSpinningAnimation();
    }

    showComments();
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

            success: function (response) {
                $('#submit').val('Post Comment');
                $('#content').val('');
                $('#parentCommentId').val('');
                $('#commentId').val('');
                showComments();
                // reloadPageWithoutScrollEffect();
            }
        })
    });

    $(document).on('click', '.reply', function () {
        let $parentCommentId = $('#parentCommentId');
        let $content = $('#content');

        $content.val('');
        $parentCommentId.val($(this).attr("id"));
        console.log('Hidden Input Value:', $parentCommentId.val()); // Debugging: Check if the value is set
        $('.reply-box').insertAfter($(this).closest('.comment-body, .reply')).show();
        $content.focus();
    });

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

// Function to show spinning animation
function showSpinningAnimation() {
    // Add a class to the element you want to animate
    $('#loading-spinner').addClass('rotate');
}

// Function to hide spinning animation
function hideSpinningAnimation() {
    // Remove the class added for animation
    $('#loading-spinner').removeClass('rotate');
}
