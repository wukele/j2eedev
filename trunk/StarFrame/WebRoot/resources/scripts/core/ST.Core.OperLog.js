Ext.BLANK_IMAGE_URL = "./../resources/scripts/ext-3.2.1/resources/images/default/s.gif";

Ext.namespace("ST.core");

ST.core.OperLogView = Ext.extend(ST.ux.ViewGrid, {
	dlgWidth: 360,
	dlgHeight: 260,
	//资源列表查询URL
	urlGridQuery: './../operLog/pageQueryOperLogs.json',

	girdColumns: [
	            {header: '操作人', width: 150, sortable: true, dataIndex: 'operatorName'},
	            {header: '操作类型', width: 100, sortable: true, dataIndex: 'exeType'},
	            {header: '时间', width: 150, sortable: true, dataIndex: 'createtime',renderer:"formatDate"},
	            {header: '描述', dataIndex: 'exeOperation',id:'descn'}
	        ],
	        
	formatDate: function(value) {   
	    if (Ext.isEmpty(value)) {//判断是否是日期类型的数据   
	        return '';   
	    } else {   
	        if (Ext.isDate(value))   
	            return Ext.util.Format.date(value, 'Y-m-d H:i:s');// 用于时间控件返回值   
	        else   
	            return Ext.util.Format.date(new Date(value), 'Y-m-d H:i:s');// 转换为Date类型   
	    }   
	},
	
	queryFormItms: [{ 
		        layout: 'column', 
		        items:[{ 
		            columnWidth:.5, layout: 'form', 
		            items: [{xtype:'textfield', fieldLabel: '操作人', name: 'userName', id: 'userName', anchor:'70%' }] 
		        }] 
		    }],
		    
	constructor: function() {
		ST.core.OperLogView.superclass.constructor.call(this, {});
	}
});