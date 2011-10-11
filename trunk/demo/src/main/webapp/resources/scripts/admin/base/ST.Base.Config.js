Ext.BLANK_IMAGE_URL = "./../resources/scripts/ext/resources/images/default/s.gif";

Ext.namespace("ST.base");

ST.base.configView = Ext.extend(ST.ux.ViewGrid, {
	dlgWidth: 400,
	dlgHeight: 300,
	//资源列表查询URL
	urlGridQuery: './../config/pageQueryConfigs.json',
	urlAdd: './../config/insertConfig.json',
	urlEdit: './../config/updateConfig.json',
	urlLoadData: './../config/loadConfig.json',
	urlRemove: './../config/deleteConfig.json',
	addTitle: "增加系统参数",
    editTitle: "更新系统参数",
    gridTitle: "系统参数数据",
	girdColumns: [
				{header: 'ID', width: 30, dataIndex: 'id', sortable: true, hideGrid: true, hideForm: 'add', readOnly: true},
	            {header: '参数键', width: 220, dataIndex: 'configKey',sortable: true, allowBlank:false},
	            {header: '参数值', width: 200, dataIndex: 'configValue', allowBlank:false, fieldtype:'textarea'},
	            {id:'descn',header: '描述', width: 250, dataIndex: 'descn', fieldtype:'textarea'}
	        ],
	queryFormItms: [{ 
				layout: 'tableform',
	            layoutConfig: {
	           		columns: 2,
	            	columnWidths: [0.5, 0.5]
	            },           
		        items:[{xtype:'textfield', fieldLabel: '参数键', name: 'configKey', id: 'configKey', anchor:'80%' },
		            {xtype:'textfield', fieldLabel: '参数值', name: 'configValue', id: 'configValue', anchor:'80%' }] 
		    }],
	constructor: function() {
		ST.base.configView.superclass.constructor.call(this, {});
	}
});