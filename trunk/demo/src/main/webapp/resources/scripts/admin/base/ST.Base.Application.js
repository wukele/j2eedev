Ext.BLANK_IMAGE_URL = "./../resources/scripts/ext/resources/images/default/s.gif";

Ext.namespace("ST.base");

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
	            {header: '应用名称', width: 150, dataIndex: 'appName', allowBlank:false}
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