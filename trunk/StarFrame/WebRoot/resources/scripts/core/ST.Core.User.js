Ext.BLANK_IMAGE_URL = "./../resources/scripts/ext-3.2.1/resources/images/default/s.gif";

Ext.namespace("ST.core");

ST.core.userView = Ext.extend(ST.ux.ViewGrid, {
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
    displayEast: true,
    autoExpandColumn: 3,
	girdColumns: [
				{header: 'ID', width: 150, dataIndex: 'id', hideGrid: true, hideForm: 'add', readOnly: true},
	            {header: '用户名称', width: 100, dataIndex: 'operatorName', allowBlank:false},
	            {header: '登录账号', width: 100, dataIndex: 'userId', allowBlank:false},
	            {header: '所属机构', width: 100, dataIndex: 'orgName', allowBlank:false},
	            {header: '性别', width: 50, dataIndex: 'employee.gender', allowBlank:false, renderer: "renderGender"},
	            {header: '上次登录时间', width: 150, dataIndex: 'lastLogin', allowBlank:false, hideForm:"all", renderer: "formatDate"},
	            {header: '上次登录IP', width: 150, dataIndex: 'ipAddress', hideForm:"all", allowBlank:false},
	            {header: '邮箱', width: 150, dataIndex: 'email', allowBlank:true}
	        ],
	        
	eastWidth: 250,
	eastGridTitle: '资源绑定的角色',
	urlEastGridQuery: './../user/queryRoles4User.json',
	eastGridColumn: [{header: "角色名称", width: 120, dataIndex: 'name', name: 'name'},
            {header: "角色编码", width: 125, dataIndex: 'code', name: 'code'}],
	
	queryFormItms: [{ 
		        layout: 'column', 
		        items:[{ 
		            columnWidth:.33, layout: 'form', 
		            items: [{xtype:'textfield', fieldLabel: '用户名称', name: 'operatorName', id: 'operatorName', anchor:'70%' }] 
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
    		params: {operatorId: this.grid.getSelectionModel().selections.items[0].data.id},
	    	urlPagedQuery: './../user/pageQueryRoles4User.json',
	    	urlBind: './../user/bindRole.json',
	    	urlUnBind: './../user/unBindRole.json',
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
    
    renderGender: function(value, p, record) {
    	if(value == "M")
    		return "男"
    	else if(value == "F")
    		return "女"
    	else
    		return "未知"
    },
    
    formatDate: function(value, p, record) {   
	    if (Ext.isEmpty(value)) {//判断是否是日期类型的数据   
	        return '';   
	    } else {   
	        if (Ext.isDate(value))   
	            return Ext.util.Format.date(value, 'Y-m-d H:i:s');// 用于时间控件返回值   
	        else   
	            return Ext.util.Format.date(new Date(value), 'Y-m-d H:i:s');// 转换为Date类型   
	    }   
	},
		    
	renderEnabled: function(value, p, record) {
        if(record.data['enabled'] == 'Y') {
            return String.format("<b><font color=green>有效</font></b>");
        } else if(record.data['enabled'] == 'N') {
            return String.format("<b><font color=red>无效</font></b>");
        }
    },

    
    /**
     * Center 区域 Grid的行click事件调用方法
     */
    rowclickFn: function(grid, rowIndex, e) {
    	var data = grid.getStore().getAt(rowIndex).data;
		this.east.store.load({
            params:{operatorId: data.id}
        });
    },
    
	constructor: function() {
		ST.core.userView.superclass.constructor.call(this, {});
		
		//授权资源
        btn = Ext.getCmp("authRole");
        btn.on("click", function(){
        	this.authRoleDiag();
        }, this)
	}
});