var status = {
    init : function () {
        var _this = this;
        $('#productStatus').on('changed', function () {
            _this.change();
        });
    },
    change : function () {
        var productId = $('#pageInfo');
         var data = {
            productStatus : $('#productStatus').val(),
         };

        $.ajax({
            type: 'PUT',
            url: '/products/'+productId+'/setStatus',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data),
        }).done(function(msg) {
            console.log("좋아요 완료");
            heart.init();
        }).fail(function (error) {
            console.log("실패");
        });
    }
};

status.init();
