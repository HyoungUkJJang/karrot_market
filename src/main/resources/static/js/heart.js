var heart = {
    init : function () {
        var _this = this;
        $('#btn-heart').on('click', function () {
            _this.save();
        });
        $('#btn-deleteHeart').on('click', function () {
            _this.delete();
        });
    },
    save : function () {
        var productId = $('#pageInfo').val();

        $.ajax({
            type: 'POST',
            url: '/products/'+productId+'/addHeart',
            contentType:'application/json; charset=utf-8'
        }).done(function(msg) {
            console.log("좋아요 완료");
            $("#heart_img").attr("src", "/images/realHeartBtn.png");
            $('#btn-heart').attr('id','btn-deleteHeart');
            $('#btn-deleteHeart').attr("disabled", false);
            $("#btn-heart").unbind();
            $("#btn-deleteHeart").unbind();
            heart.init();
        }).fail(function (error) {
            console.log("실패");
            console.log(error);
            alert(JSON.stringify(error));
        });
    },
    delete : function () {
        var productId = $('#pageInfo').val();

        $.ajax({
            type: 'DELETE',
            url: '/products/'+productId+'/deleteHeart',
            contentType:'application/json; charset=utf-8'
        }).done(function(msg) {
            console.log("좋아요 취소");
            $("#heart_img").attr("src", "/images/heartBtn.png");
            $('#btn-deleteHeart').attr('id','btn-heart');
            $('#btn-heart').attr("disabled", false);
            $("#btn-heart").unbind();
            $("#btn-deleteHeart").unbind();
            heart.init();
        }).fail(function (error) {
            console.log("실패");
            console.log(error);
            alert(JSON.stringify(error));
        });
    }
};

heart.init();
