Loading = {
    show: function (){
        $.blockUI({
            message: '<img src="/static/image/loading.gif"/>',
            css:{
                //水平居中
                zIndex: "10011",
                padding: "10px",
                left: "50%",
                width: "80px",
                marginLeft: "-40px",
            }
        });
    },

    hide: function (){
        //本地查询速度快 做延迟
        setTimeout(function (){
            $.unblockUI();
        },500)
    }
};