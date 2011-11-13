Ext.BLANK_IMAGE_URL = "./../resources/scripts/ext/resources/images/default/s.gif";

Ext.namespace("ST.base");
Ext.reg('recommendTypeField', ST.ux.ExtField.ComboBox);

ST.base.zhuantiView = Ext.extend(ST.ux.ViewGrid, {
	dlgWidth: 400,
	dlgHeight: 300,
	//资源列表查询URL
	urlGridQuery: './../recommend/pageQueryRecommends1.json',
	urlAdd: './../recommend/insertRecommend1.json',
	urlEdit: './../recommend/updateRecommend1.json',
	urlLoadData: './../recommend/loadRecommend.json',
	urlRemove: './../recommend/deleteRecommend.json',
	addTitle: "增加专题",
    editTitle: "更新专题",
    gridTitle: "专题数据",
    autoExpandColumn: 4,
	girdColumns: [
				{header: 'ID', width: 60, dataIndex: 'id', hideGrid: true, hideForm: 'add', readOnly: true},
	            {header: '专题', width: 180, dataIndex: 'name', allowBlank:false},
	            {header: '创建时间', width: 130, dataIndex: 'createTime', hideForm: 'all', allowBlank:false},
	            {header: '更新时间', width: 130, dataIndex: 'updateTime', hideForm: 'all', allowBlank:false}
	        ],
	
	queryFormItms: [{ 
				layout: 'tableform',
	            layoutConfig: {
	           		columns: 3,
	            	columnWidths: [0.33, 0.33, 0.33]
	            },           
		        items:[{xtype:'textfield', fieldLabel: '专题名称', name: 'name', id: 'name', anchor:'70%' }]
		    }],
    
	constructor: function() {
		ST.base.zhuantiView.superclass.constructor.call(this, {});
	}
});