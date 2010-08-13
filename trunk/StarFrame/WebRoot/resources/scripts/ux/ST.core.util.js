//在没一个请求响应头添加参数，表示Extjs Ajax请求
Ext.Ajax.on('beforerequest', function(conn, options){
	if(options.params == null)
		options.params = {}
	options.params["REQUEST_MODE"] = "EXTJS";
	
	//清楚提交为空参数
	for(var k in options.params) {
		if(options.params[k] == "" && k != "start")
			delete options.params[k];
	}
		
});