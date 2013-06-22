(function(window, $) {  
    var vector = {};
    var baseurl = "/Juva/";   // The prefix url of this web site.
    
    vector.elementInit = function() {
        $('body').prepend($('<div id="message-box"></div>'));
    };
    
    vector._messagebox = function(message, timeout, specialClass) {
        var entity = $('<div class="message-content"></div>')
            .hide()
            .append($('<span class="message-text"></span>').append(message))
            .append($('<a href="javascript:void(0);" class="button message-close-btn">关闭</a>'))
            .addClass(specialClass);
            
        $('#message-box').append(entity);
        
        entity.fadeIn(500);
        
        if (timeout > 0) {
            setTimeout(function(){
                entity.fadeOut(500, function(){
                    $(this).remove();
                });
            }, timeout);
        }
        
        entity.children('a.message-close-btn').click(function(){
            entity.fadeOut(500, function(){
                $(this).remove();
            });
        });
    };

    vector.notice = function(message, timeout) { this._messagebox(message, timeout, 'notice'); };
    vector.alert  = function(message, timeout) { this._messagebox(message, timeout, 'warn');  };
    vector.error  = function(message, timeout) { this._messagebox(message, timeout, 'error'); };
    
    vector.redirect = function(url){ document.location = baseurl + url ;};
    vector.root = function(){ return baseurl;};

    window.vector = window.$S = vector;
})(window, jQuery);

$(function(){
    $S.elementInit();
});

