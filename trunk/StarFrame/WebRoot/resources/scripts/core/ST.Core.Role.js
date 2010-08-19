Ext.BLANK_IMAGE_URL = "./../resources/scripts/ext-3.2.1/resources/images/default/s.gif";

Ext.namespace("ST.core");

ST.core.roleView = Ext.extend(ST.ux.ViewGrid, {
	dlgWidth: 360,
	dlgHeight: 260,
	//资源列表查询URL
	urlGridQuery: './../role/pageQueryRoles.json',
	urlAdd: './../role/insertRole.json',
	urlEdit: './../role/updateRole.json',
	urlLoadData: './../role/loadRole.json',
	urlRemove: './../role/deleteRole.json',
	addTitle: "增加角色",
    editTitle: "更新角色",
    gridTitle: "角色数据",
	girdColumns: [
				{header: 'ID', width: 150, dataIndex: 'id', hideGrid: true, hideForm: 'add', readOnly: true},
	            {header: '角色名称', width: 150, sortable: true, dataIndex: 'name', allowBlank:false},
	            {header: '角色编码', width: 150, sortable: true, dataIndex: 'code', allowBlank:false},
	            {header: '有效性', width: 75, sortable: true, fieldtype:'enabledField', dataIndex: 'enabled', renderer:"renderEnabled"},
	            {id:'descn',header: '描述', width: 250, dataIndex: 'descn', fieldtype:'textarea'}
	        ],
	
	queryFormItms: [{ 
		        layout: 'column', 
		        items:[{ 
		            columnWidth:.5, layout: 'form', 
		            items: [{xtype:'textfield', fieldLabel: '角色名称', name: 'name', id: 'name', anchor:'70%' }] 
		        }, { 
		            columnWidth:.5, layout: 'form', 
		            items: [{xtype:'enabledField', fieldLabel: '优先级',anchor:'70%', allowBlank:true }] 
		        }] 
		    }],
		    
	renderEnabled: function(value, p, record) {
        if(record.data['enabled'] == 'Y') {
            return String.format("<b><font color=green>有效</font></b>");
        } else if(record.data['enabled'] == 'N') {
            return String.format("<b><font color=red>无效</font></b>");
        }
    },
    
    addButtonOnToolbar: function(toolbar, index) {
    	this.grid.getBottomToolbar().insertButton(index++,'-');
    	toolbar.insertButton(index++,new Ext.Button({text:"授权资源",iconCls: 'authorization', id:'authRes'}));
    	toolbar.insertButton(index++,new Ext.Button({text:"授权用户",iconCls: 'authorization', id:'authUser'}));
    },
    
    authResDiag: function() {
    	if(!this.checkOne())
    		return;
    	
    	var grid = new ST.ux.PlainGrid({
    		btnText: '授权',
    		params: {roleId: this.grid.getSelectionModel().selections.items[0].data.id},
	    	urlPagedQuery: './../role/queryResources4Role.json',
	    	urlBind: './../role/bindResource.json',
	    	urlUnBind: './../role/unBindResource.json',
	    	autoExpandColumn:6,
	        columConfig:[{header:"资源名称",name:"name"},
	        	{header:"资源类型",width: 60,name:"type", renderer:this.renderType},
	        	{header:"是否授权",width: 60,name:"counter", renderer:this.authReder},
	        	{header:"资源路径",name:"action"},
	        	{header:"资源描述",name:"descn"}]
	    }); 
    	var win = ST.core.genWindow({
            id: 'userSelectWindow',
            title    : '授权资源 -- ' + this.grid.getSelectionModel().selections.items[0].data.name,
            width    : 700,
            height   : 320,
            items    : [grid],
            border   : true
        });
    },
    
    authReder: function(value, p, record) {
    	if(record.data['counter'] == 0) {
            return String.format("<b><font color=red>未授权</font></b>");
        } else if(record.data['counter'] == 1) {
            return String.format("<b><font color=green>授权</font></b>");
        }
    },
    
    renderType: function(value, p, record) {
        if(record.data['type'] == 'url') {
            return "URL";
        } else if(record.data['type'] == 'method') {
            return "方法";
        } else if(record.data['type'] == 'menu') {
            return "菜单";
        }
    },
    
	constructor: function() {
		ST.core.roleView.superclass.constructor.call(this, {});
		//授权资源
        btn = Ext.getCmp("authRes");
        btn.on("click", function(){
        	this.authResDiag();
        }, this)
	}
});