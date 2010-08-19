//在没一个请求响应头添加参数，表示Extjs Ajax请求
Ext.Ajax.on('beforerequest', function(conn, options){
	if(options.params == null)
		options.params = {}
	
	//清楚提交为空参数
	for(var k in options.params) {
		//start为分页参数，不能被清除掉
		if(options.params[k] == "" && k != "start")
			delete options.params[k];
	}
	
	options.params["REQUEST_MODE"] = "AJAX";
});

//Ajax请求完成执行。判断Session超时，如果超时或者无效，返回的内容为login.jsp页面的内容,页面包含：AJAX-AccessDeniedException
//如果页面包含AJAX-AccessDeniedException，说明session超时或者无效。
Ext.Ajax.on('requestcomplete', function(conn, response, options){
	if(options.params['REQUEST_MODE'] == "AJAX" &&
		response.responseText.indexOf("AJAX-AccessDeniedException") != -1) {
		Ext.Msg.alert('提示', '会话超时，请重新登录!', function(){
            window.location = './login'; 
        });
	}
});

Ext.namespace("ST.core");
ST.core.genWindow = function(c){
	if (c.id && Ext.getCmp(c.id)) {
    	Ext.getCmp(c.id).center();
    	return;
	}
	var win = new Ext.Window(Ext.apply({
        closable : true,
        border   : false,
        width    : 640,
        height   : 520,
        modal    : true,
        plain    : true,
        maximizable: true,
        items:   [],
        layout   : 'fit'
    }, c));
    win.show();
    win.center();
    return win;
}