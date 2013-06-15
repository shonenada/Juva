$('#weibo-form').ajaxForm({
    dataType:'json',
    success: function(response){
        if (response.success == "true") {
            $S.notice(response.message, 5000);
            setTimeout(function(){
                $S.redirect("main");
            }, 2000);
        }
        else{
            $S.alert(response.message, 5000);
        }
    },
    error: function() {
        $S.error('发生技术问题，导致你的报名失败。请联系管理员！');
    }
});

$('.comment-form').ajaxForm({
    dataType:'json',
    success: function(response){
        if (response.success == "true") {
            $S.notice(response.message, 5000);
            setTimeout(function(){
                refresh();
            }, 2000);
        }
        else{
            $S.alert(response.message, 5000);
        }
    },
    error: function() {
        $S.error('发生技术问题，导致你的报名失败。请联系管理员！');
    }
});

$(function(){
    $("#focus-btn").click(function(){
        var user_name = $("#focus-btn").attr("href").replace("#", "");
        $.post($S.root() + "focus", {'dst_user': user_name});
    });

    $(".repost-btn").click(function(){
        var id = $(this).attr("id").replace("comment-btn-", "");
        
        var ele = $("#comment-" + id);

        $.get($S.root() + "comment", {'aid': id}, function(data){
            ele.html(data);
        });

        $("#comment-framework-" + id).slideToggle();

    });
});