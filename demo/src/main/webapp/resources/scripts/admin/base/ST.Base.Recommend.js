Ext.BLANK_IMAGE_URL = "./../resources/scripts/ext/resources/images/default/s.gif";

Ext.namespace("ST.base");
Ext.reg('recommendTypeField', ST.ux.ExtField.ComboBox);

ST.base.recommendView = Ext.extend(ST.ux.ViewGrid, {
	dlgWidth: 400,
	dlgHeight: 300,
	//资源列表查询URL
	urlGridQuery: './../recommend/pageQueryRecommends.json',
	urlAdd: './../recommend/insertRecommend.json',
	urlEdit: './../recommend/updateRecommend.json',
	urlLoadData: './../recommend/loadRecommend.json',
	urlRemove: './../recommend/deleteRecommend.json',
	addTitle: "增加推荐",
    editTitle: "更新推荐",
    gridTitle: "推荐数据",
    autoExpandColumn: 6,
	girdColumns: [
				{header: 'ID', width: 60, dataIndex: 'id', hideGrid: true, hideForm: 'add', readOnly: true},
	            {header: '推荐名称', width: 150, dataIndex: 'name', allowBlank:false},
	            {header: '推荐类型', width: 50, hideGrid: true, dataIndex: 'type', hiddenName: 'type',dictTypeId: '10008', allowBlank:false, fieldtype:'recommendTypeField'},
	            {header: '推荐类型', width: 80, dataIndex: 'type_Name', hideForm: 'all'},
	            {header: '创建时间', width: 130, dataIndex: 'createTime', hideForm: 'all', allowBlank:false},
	            {header: '创建时间', width: 130, dataIndex: 'updateTime', hideForm: 'all', allowBlank:false},
	            {header: '推荐描述', width: 80, dataIndex: 'desc'},
	        ],
	
	queryFormItms: [{ 
				layout: 'tableform',
	            layoutConfig: {
	           		columns: 3,
	            	columnWidths: [0.33, 0.33, 0.33]
	            },           
		        items:[{xtype:'textfield',  fieldLabel: '推荐名称', name: 'name', id: 'name', anchor:'80%' },
		               {xtype:'recommendTypeField',hiddenName: 'type',dictTypeId: '10008', fieldLabel: '推荐类型',anchor:'80%', allowBlank:true }]
		    }],
    
	constructor: function() {
		ST.base.recommendView.superclass.constructor.call(this, {});
	}
});