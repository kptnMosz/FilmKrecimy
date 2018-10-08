$(document).ready(function () {

    var urlForPosts = $('#urlForPosts').attr('value');
    var postcontainer = $('#postsContainer');


    var refreshPosts = function () {
        $.ajax({url: urlForPosts})
            .done(function (json) {
                console.log(json);
                for (var i = 0; i < json.length; i++) {
                    var newPostContainer = $('<div>');
                    newPostContainer.addClass('post');
                    var author = $('<span>');

                    var postText = $('<div class="postText">');
                    postText.text(json[i].postText);
                    var authorPostImagetag = '<img class="postAvatar" src="' + json[i].authorsImageURI + '"/>';
                    var authorsImage = $(authorPostImagetag);
                    author.prepend(authorsImage);
                    var postHeader = $('<span class="postHeader">');
                    postHeader.text('on ' + json[i].created + ' ' + json[i].authorsName + ' posted:');
                    author.append(postHeader);
                    var br = $('<br>');
                    author.append(br);

                    newPostContainer.append(author);
                    newPostContainer.append(postText);
                    postcontainer.append(newPostContainer);
                }
            });


    }

    refreshPosts();
});