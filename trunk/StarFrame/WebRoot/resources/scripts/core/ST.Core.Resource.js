Ext.BLANK_IMAGE_URL = "./../resources/scripts/ext-3.2.1/resources/images/default/s.gif";

Ext.namespace("ST.core");

ST.core.resourceView = Ext.extend(ST.ux.ViewGrid, {
	dlgWidth: 300,
	dlgHeight: 280,
	//资源列表查询URL
	urlGridQuery: './../resource/pageQueryResources.json',
	urlAdd: './../resource/insertResource.json',
	urlEdit: './../resource/updateResource.json',
	urlLoadData: './../resource/loadResource.json',
	urlRemove: './../resource/deleteResource.json',
	rootJson: 'resource',
	addTitle: "增加资源",
    editTitle: "更新资源",
    gridTitle: "资源数据",
	girdColumns: [
				{header: 'ID', width: 150, dataIndex: 'id', hideGrid: true, hideForm: 'add', readOnly: true},
	            {header: '资源名称', width: 150, dataIndex: 'name', allowBlank:false},
	            {header: 'action', width: 200, dataIndex: 'action', allowBlank:false},
	            {header: '资源类型', width: 75, sortable: true, fieldtype:'resourceTypeField', dataIndex: 'type'},
	            {header: '有效性', width: 75, sortable: true, fieldtype:'enabledField', dataIndex: 'enabled', renderer:"renderEnabled"},
	            {header: '优先级', width: 60, sortable: true, dataIndex: 'priority', allowBlank:false,regex:/[0-9]+/,regexText:"请输入数字"},
	            {id:'descn',header: '描述', width: 250, dataIndex: 'descn'}
	        ],
	
	queryFormItms: [{ 
		        layout: 'column', 
		        items:[{ 
		            columnWidth:.33, layout: 'form', 
		            items: [{xtype:'textfield', fieldLabel: '资源名称', name: 'name', id: 'name', anchor:'70%' }] 
		        },{ 
		            columnWidth:.33, layout: 'form', 
		            items: [{xtype:'resourceTypeField', fieldLabel: '资源类型',anchor:'70%' }] 
		        },{ 
		            columnWidth:.33, layout: 'form', 
		            items: [{xtype:'enabledField', fieldLabel: '优先级',anchor:'70%' }] 
		        }] 
		    }],
	constructor: function() {
		ST.core.resourceView.superclass.constructor.call(this, {});
	}
});