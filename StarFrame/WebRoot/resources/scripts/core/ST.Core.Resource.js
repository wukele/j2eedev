Ext.BLANK_IMAGE_URL = "./../resources/scripts/ext-3.2.1/resources/images/default/s.gif";

Ext.namespace("ST.core");

ST.core.resourceView = Ext.extend(ST.ux.ViewGrid, {
	dlgWidth: 360,
	dlgHeight: 300,
	//资源列表查询URL
	urlGridQuery: './../resource/pageQueryResources.json',
	urlAdd: './../resource/insertResource.json',
	urlEdit: './../resource/updateResource.json',
	urlLoadData: './../resource/loadResource.json',
	urlRemove: './../resource/deleteResource.json',
	addTitle: "增加资源",
    editTitle: "更新资源",
    gridTitle: "资源数据",
    displayEast: true,
	girdColumns: [
				{header: 'ID', width: 150, dataIndex: 'id', hideGrid: true, hideForm: 'add', readOnly: true},
	            {header: '资源名称', width: 150, dataIndex: 'name', allowBlank:false},
	            {header: '资源路径', width: 200, dataIndex: 'action', allowBlank:false},
	            {header: '资源类型', width: 75, sortable: true, fieldtype:'resourceTypeField', dataIndex: 'type', renderer:"renderType"},
	            {header: '有效性', width: 75, sortable: true, fieldtype:'enabledField', dataIndex: 'enabled', renderer:"renderEnabled"},
	            {header: '优先级', width: 60, sortable: true, dataIndex: 'priority', allowBlank:false,regex:/[0-9]+/,regexText:"请输入数字"},
	            {id:'descn',header: '描述', width: 250, dataIndex: 'descn', fieldtype:'textarea'}
	        ],
	eastWidth: 250,
	eastGridTitle: '资源绑定的角色',
	urlEastGridQuery: './../resource/queryRoles4Res.json',
	eastGridColumn: [{header: "角色名称", width: 120, dataIndex: 'name', name: 'name'},
            {header: "角色编码", width: 125, dataIndex: 'code', name: 'code'}],
	
	queryFormItms: [{ 
		        layout: 'column', 
		        items:[{ 
		            columnWidth:.33, layout: 'form', 
		            items: [{xtype:'textfield', fieldLabel: '资源名称', name: 'name', id: 'name', anchor:'70%' }] 
		        },{ 
		            columnWidth:.33, layout: 'form', 
		            items: [{xtype:'resourceTypeField', fieldLabel: '资源类型',anchor:'70%', allowBlank:true }] 
		        },{ 
		            columnWidth:.33, layout: 'form', 
		            items: [{xtype:'enabledField', fieldLabel: '优先级',anchor:'70%', allowBlank:true }] 
		        }] 
		    }],
		    
	addButtonOnToolbar: function(toolbar, index) {
    	toolbar.insertButton(index++,new Ext.Button({text:"授予角色",iconCls: 'authorization', id:'authRole'}));
    },
    
    authRoleDiag: function() {
    	if(!this.checkOne())
    		return;
    	
    	var grid = new ST.ux.PlainGrid({
    		btnText: '授予',
    		params: {resId: this.grid.getSelectionModel().selections.items[0].data.id},
	    	urlPagedQuery: './../resource/pageQueryRoles4Res.json',
	    	urlBind: './../resource/bindRole.json',
	    	urlUnBind: './../resource/unBindRole.json',
	    	autoExpandColumn:5,
	        columConfig:[{header:"角色名称",width: 100,name:"name"},
	        	{header:"角色编码",width: 100,name:"code", renderer:this.renderType},
	        	{header:"是否授予",width: 60,name:"counter", renderer:this.authReder},
	        	{header:"角色描述",name:"descn"}]
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
            return String.format("<b><font color=red>未授予</font></b>");
        } else if(record.data['counter'] == 1) {
            return String.format("<b><font color=green>授予</font></b>");
        }
    },
		    
	renderEnabled: function(value, p, record) {
        if(record.data['enabled'] == 'Y') {
            return String.format("<b><font color=green>有效</font></b>");
        } else if(record.data['enabled'] == 'N') {
            return String.format("<b><font color=red>无效</font></b>");
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
    
    /**
     * Center 区域 Grid的行click事件调用方法
     */
    rowclickFn: function(grid, rowIndex, e) {
    	var data = grid.getStore().getAt(rowIndex).data;
		this.east.store.load({
            params:{resId: data.id}
        });
    },
    
	constructor: function() {
		ST.core.resourceView.superclass.constructor.call(this, {});
		
		//授权资源
        btn = Ext.getCmp("authRole");
        btn.on("click", function(){
        	this.authRoleDiag();
        }, this)
	}
});