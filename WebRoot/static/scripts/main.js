$(function(){

  $('#weibo-form').ajaxForm({
    dataType:'json',
    success: function(response){
      if (response.success == "true") {
        $S.notice(response.message, 2000);
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

  $('.repost-form').ajaxForm({
    dataType:'json',
    success: function(response){
      if (response.success == "true") {
        $S.notice(response.message, 2000);
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
      if (response[0] == "true") {
        $S.notice(response[1], 2000);
        get_comment(response[2]);
      }
      else{
        $S.alert(response[1], 5000);
      }
    },
    error: function() {
      $S.error('发生技术问题，导致你的报名失败。请联系管理员！');
    }
  });

  var get_comment = function(aid){
    var ele = $("#comment-" + aid);
    $.ajax({
      url: $S.root() + "comment?aid=" + aid,
      type: 'get',
      async: true ,
      dataType: 'json',
      before: function(){
        ele.html("Loading~");
      },
      success:function(response){
        var length = response.length;
        if (length > 0){
          ele.empty();
          ele.hide();
          for (i=0;i<length;i++){
            var username = response[i][0];
            var time = response[i][1];
            var content = response[i][2];
            ele.fadeIn();
            ele.append("<li><a href='"+$S.root()+"weibo?user="+username+"' >" + username + "</a> 评论了你的微博：" + content + " (" + time + ")</li>");
          }
        }else{
          ele.html("暂时还没有评论哦~");
        }
      }
    });
  };

  var del_weibo = function(aid){
    $.ajax({
      url: $S.root() + "weibo",
      type: 'delete',
      async: true ,
      dataType: 'json',
      data: {"aid": aid},
      success:function(response){
        if (response.success == "true") {
          $S.notice(response.message, 500);
          $("#weibo-"+aid).slideUp();
        }
        else{
          $S.alert(response.message, 5000);
        }
      },
      error: function(){
        $S.error('发生技术问题，导致你的报名失败。请联系管理员！');
      }
    });
  };

  var focus = function(username, type){
    $.ajax({
      url: $S.root() + "focus",
      type: 'post',
      async: true ,
      dataType: 'json',
      data: {"dst_user": username, "type": type},
      success:function(response){
        if (response.success == "true") {
          $S.notice(response.message, 1000);
        }
        else{
          $S.alert(response.message, 5000);
        }
      },
      error: function(){
        $S.error('发生技术问题，导致你的报名失败。请联系管理员！');
      }
    });
  };

  $("#focus-btn").click(function(){
    var user_name = $("#focus-btn").attr("href").replace("#", "");
    focus(user_name, "show");
  });

  $("#h-focus-btn").click(function(){
    var user_name = $("#focus-btn").attr("href").replace("#", "");
    focus(user_name, "hidden");
  });

  $(".del-btn").click(function(){
    var aid = $(this).attr("id").replace("del-btn-", "");
    del_weibo(aid);
  });

  $(".comment-btn").click(function(){
    var aid = $(this).attr("id").replace("comment-btn-", "");
    get_comment(aid);
    $("#comment-framework-" + aid).slideToggle();
  });

  $(".repost-btn").click(function(){
    var aid = $(this).attr("id").replace("repost-btn-", "");
    $(".repost-content").val("");
    $(".repost-framework").hide();
    $("#repost-framework-" + aid).fadeIn("fast");
  });

  $(".repost-cancel-btn").click(function(){
    $(".repost-framework").fadeOut();
  });
});