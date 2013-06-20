$('#login-form').ajaxForm({
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
        $S.error('发生技术问题，导致你的操作失败。请联系管理员！');
    }
});