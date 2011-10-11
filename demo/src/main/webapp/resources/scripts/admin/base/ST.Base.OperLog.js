Ext.BLANK_IMAGE_URL = "./../resources/scripts/ext/resources/images/default/s.gif";

Ext.namespace("ST.base");

ST.base.OperLogView = Ext.extend(ST.ux.ViewGrid, {
	dlgWidth: 360,
	dlgHeight: 260,
	//资源列表查询URL
	displayButton: false,
	urlGridQuery: './../operLog/pageQueryOperLogs.json',

	girdColumns: [
	            {header: '操作人', width: 150, sortable: true, dataIndex: 'userName'},
	            {header: '操作类型', width: 100, sortable: true, dataIndex: 'exeType'},
	            {header: '记录时间', width: 150, sortable: true, dataIndex: 'createDate'},
	            {header: '描述', dataIndex: 'exeOperation',id:'descn'}
	        ],
	
	queryFormItms: [{ 
				layout: 'tableform',
	            layoutConfig: {
	           		columns: 3,
	            	columnWidths: [0.33, 0.33, 0.33]
	            },           
		        items:[{xtype:'textfield', fieldLabel: '操作人', name: 'userName', id: 'userName', anchor:'90%' },
		        	{xtype:'datetimefield', format: 'Y-m-d H:i:s', editable: true, fieldLabel: '记录开始时间', name: 'startTime', anchor:'90%' },
		            {xtype:'datetimefield', format: 'Y-m-d H:i:s', editable: true, fieldLabel: '记录结束时间', name: 'endTime', anchor:'90%' }] 
		    }],
		    
	constructor: function() {
		ST.base.OperLogView.superclass.constructor.call(this, {});
	}
});