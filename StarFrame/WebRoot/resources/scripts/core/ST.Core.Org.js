Ext.BLANK_IMAGE_URL = "./../resources/scripts/ext-3.2.1/resources/images/default/s.gif";

Ext.namespace("ST.core");

ST.core.orgView = Ext.extend(ST.ux.ViewTree, {
	dlgWidth: 300,
	dlgHeight: 250,
	urlSaveOrder: "./../org/saveOrgOrder.json",
	urlTreeQuery: "./../org/findOrgs.json",
	urlRemoveNode: "./../org/deleteOrg.json",
	urlAddNode: "./../org/insertOrg.json",
	urlEditNode: "./../org/updateOrg.json",
	urlLoadNode: "./../org/loadOrg.json",
	addTitle: "增加机构",
    editTitle: "更新机构",
	
	girdColumns: [
				{header: 'ID', width: 150, name: 'id', hideForm: 'add', readOnly: true},
	            {header: '机构名称', width: 150, name: 'text', allowBlank:false},
	            {header: 'parentId', width: 200, name: 'parentId', fieldtype:'hidden', id: 'Ext-parentId'},
	            {header: 'level', width: 200, name: 'level', fieldtype:'hidden', id:'Ext-level'},
	            {header: '顺序', width: 60, sortable: true, name: 'theSort', allowBlank:false,regex:/[0-9]+/,regexText:"请输入数字"},
	            {header: '描述', width: 250, name: 'descn', fieldtype:'textarea'}
	        ],
	
    setFieldDefaultValue: function(node) {
    	Ext.getCmp("Ext-parentId").setRawValue(node.id);
    	Ext.getCmp("Ext-level").setRawValue(node.attributes.level+1);
    },
    
	constructor: function() {
		ST.core.orgView.superclass.constructor.call(this, {});
	}
});