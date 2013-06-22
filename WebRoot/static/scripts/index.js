$(function(){
    $("#reg-btn").click(function(){
        $("#login").slideUp();
        $("#register").slideDown();
        var captcha_url = $("#reg-captcha").attr("src");
        $("#reg-captcha").attr("src", captcha_url);
    });
    $("#cancel-btn").click(function(){
        $("#register").slideUp();
        $("#login").slideDown();
        var captcha_url = $("#login-captcha").attr("src");
        $("#login-captcha").attr("src", captcha_url);
    });
});
