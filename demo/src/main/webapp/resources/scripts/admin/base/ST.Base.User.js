Ext.BLANK_IMAGE_URL = "./../resources/scripts/ext/resources/images/default/s.gif";

Ext.namespace("ST.base");

Ext.reg('deleFlagField', ST.ux.ExtField.ComboBox);

ST.base.userView = Ext.extend(ST.ux.ViewGrid, {
	dlgWidth: 360,
	dlgHeight: 300,
	//资源列表查询URL
	urlGridQuery: './../user/pageQueryUsers.json',
	urlAdd: './../user/insertUser.json',
	urlEdit: './../user/updateUser.json',
	urlLoadData: './../user/loadUser.json',
	urlRemove: './../user/deleteUser.json',
	addTitle: "增加用户",
    editTitle: "更新用户",
    gridTitle: "用户数据",
    autoExpandColumn: 2,
	girdColumns: [
				{header: 'ID', width: 150, dataIndex: 'id', hideGrid: true, hideForm: 'add', readOnly: true},
	            {header: '用户名称', width: 100, dataIndex: 'userName', allowBlank:false},
	            {header: '用户状态', width: 50, hideGrid: true, dataIndex: 'delFlag', hiddenName: 'delFlag',dictTypeId: '10007', allowBlank:false, fieldtype:'deleFlagField'},
	            {header: '用户状态', width: 80, dataIndex: 'delFlag_Name', hideForm: 'all'},
	            {header: '创建时间', width: 130, dataIndex: 'createTime', hideForm: 'all', allowBlank:false}
	        ],
	
	queryFormItms: [{ 
				layout: 'tableform',
	            layoutConfig: {
	           		columns: 3,
	            	columnWidths: [0.33, 0.33, 0.33]
	            },           
		        items:[{xtype:'textfield', fieldLabel: '用户名称', name: 'userName', id: 'userName', anchor:'70%' }]
		    }],
    
	constructor: function() {
		ST.base.userView.superclass.constructor.call(this, {});
	}
});