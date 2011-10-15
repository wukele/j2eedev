Ext.BLANK_IMAGE_URL = "./../resources/scripts/ext/resources/images/default/s.gif";

Ext.namespace("ST.base");

ST.base.commentView = Ext.extend(ST.ux.ViewGrid, {
	dlgWidth: 700,
	dlgHeight: 400,
	//资源列表查询URL
	urlGridQuery: './../comment/pageQueryComments.json',
	urlAdd: './../comment/insertComment.json',
	urlEdit: './../comment/updateComment.json',
	urlLoadData: './../comment/loadComment.json',
	urlRemove: './../comment/deleteComment.json',
	addTitle: "增加评论",
    editTitle: "更新评论",
    gridTitle: "评论数据",
    autoExpandColumn: 9,
    rowExpander: new Ext.ux.grid.RowExpander({
        tpl : new Ext.Template(
                '<div><p><b><span style="color:green">评论内容:</span></b><br/> {detail}</p><br></div>'
            )
        }),
	girdColumns: [
				{header: 'ID', width: 60, dataIndex: 'id', hideGrid: true, hideForm: 'add', readOnly: true},
	            {header: '应用名称', width: 100, dataIndex: 'appName', allowBlank:false},
	            {header: '应用版本', width: 100, dataIndex: 'appVer', allowBlank:false},
	            {header: '用户ID', width: 100, dataIndex: 'userId', allowBlank:false},
	            {header: '用户名称', width: 100, dataIndex: 'userName', allowBlank:false},
	            {header: '用户昵称', width: 100, dataIndex: 'userNickname', allowBlank:false},
	            {header: '用户评分', width: 100, dataIndex: 'softLevel', allowBlank:false},
	            {header: '评论IP', width: 120, dataIndex: 'ipAddr', allowBlank:false},
	            {header: '创建时间', width: 130, dataIndex: 'createTime', hideForm: 'all', allowBlank:false},
	            {header: '评论内容', width: 120, dataIndex: 'detail', hideGrid: true, allowBlank:false}
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
		ST.base.commentView.superclass.constructor.call(this, {});
	}
});