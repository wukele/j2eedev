Ext.BLANK_IMAGE_URL = "./../resources/scripts/ext/resources/images/default/s.gif";

Ext.namespace("ST.base");
Ext.reg('appStateField', ST.ux.ExtField.ComboBox);

ST.base.applicationView = Ext.extend(ST.ux.ViewGrid, {
	dlgWidth: 700,
	dlgHeight: 400,
	//资源列表查询URL
	urlGridQuery: './../application/pageQueryApplications.json',
	urlAdd: './../application/insertApplication.json',
	urlEdit: './../application/updateApplication.json',
	urlLoadData: './../application/loadApplication.json',
	urlRemove: './../application/deleteApplication.json',
	addTitle: "增加App应用",
    editTitle: "更新App应用",
    gridTitle: "App应用数据",
    autoExpandColumn: 2,
	girdColumns: [
				{header: 'ID', width: 60, dataIndex: 'id', hideGrid: true, hideForm: 'add', readOnly: true},
	            {header: '应用名称', width: 150, dataIndex: 'appName', allowBlank:false},
	            {header: '发布状态', width: 180, hideGrid: true, dataIndex: 'appState', hiddenName: 'appState',dictTypeId: '10009', allowBlank:false, fieldtype:'appStateField'},
	            {header: '发布状态', width: 180, dataIndex: 'appState_Name', hideForm: 'all'},
	            {header: '下载次数', width: 60, dataIndex: 'downTimes'},
	        ],
	
	queryFormItms: [{ 
				layout: 'tableform',
	            layoutConfig: {
	           		columns: 3,
	            	columnWidths: [0.33, 0.33, 0.33]
	            },           
		        items:[{xtype:'textfield',  fieldLabel: '应用名称', name: 'appName', id: 'appName', anchor:'70%' }]
		    }],
    
	constructor: function() {
		ST.base.applicationView.superclass.constructor.call(this, {});
	}
});