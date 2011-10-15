Ext.BLANK_IMAGE_URL = "./../resources/scripts/ext/resources/images/default/s.gif";

Ext.namespace("ST.base");

ST.base.noticeView = Ext.extend(ST.ux.ViewGrid, {
	dlgWidth: 700,
	dlgHeight: 400,
	//资源列表查询URL
	urlGridQuery: './../notice/pageQueryNotices.json',
	urlAdd: './../notice/insertNotice.json',
	urlEdit: './../notice/updateNotice.json',
	urlLoadData: './../notice/loadNotice.json',
	urlRemove: './../notice/deleteNotice.json',
	addTitle: "增加公告",
    editTitle: "更新公告",
    gridTitle: "公告数据",
    rowExpander: new Ext.ux.grid.RowExpander({
        tpl : new Ext.Template(
                '<div style="colr:red;"><p><b><span style="color:green">公告内容:</span></b><br/> {content}</p><br></div>'
            )
        }),
    autoExpandColumn: 6,
	girdColumns: [
				{header: 'ID', width: 60, dataIndex: 'id', hideGrid: true, hideForm: 'add', readOnly: true},
	            {header: '公告标题', width: 150, dataIndex: 'title', allowBlank:false},
	            {header: '创建时间', width: 130, dataIndex: 'createTime', hideForm: 'all', allowBlank:false},
	            {header: '创建人', width: 120, dataIndex: 'creator', hideForm: 'all', allowBlank:false},
	            {header: '公告失效时间', width: 130, format: 'Y-m-d H:i:s', dataIndex: 'expiredTime', fieldtype:'datetimefield', allowBlank:false},
	            {header: '公告内容', width: 120, dataIndex: 'content', hideGrid: true, allowBlank:false, fieldtype:'htmleditor'}
	        ],
	
	queryFormItms: [{ 
				layout: 'tableform',
	            layoutConfig: {
	           		columns: 3,
	            	columnWidths: [0.33, 0.33, 0.33]
	            },           
		        items:[{xtype:'textfield',  fieldLabel: '公告标题', name: 'title', id: 'title', anchor:'70%' }]
		    }],
    
	constructor: function() {
		ST.base.noticeView.superclass.constructor.call(this, {});
	}
});