Ext.BLANK_IMAGE_URL = "./../resources/scripts/ext/resources/images/default/s.gif";

Ext.namespace("ST.base");
var DeptField = Ext.extend(Ext.ux.TreeCombo, {
	rootVisible : false,
	url : './../org/queryOrgs.json',
	allowBlank : false,
	hiddenName : 'org_Id',
	rootName : '机构'
});
Ext.reg('deptfield', DeptField);

Ext.reg('genderField', ST.ux.ExtField.ComboBox);

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
    displayEast: true,
    autoExpandColumn: 3,
	girdColumns: [
				{header: 'ID', width: 150, dataIndex: 'id', hideGrid: true, hideForm: 'add', readOnly: true},
	            {header: '用户名称', width: 100, dataIndex: 'userName', allowBlank:false},
	            {header: '登录账号', width: 100, dataIndex: 'userCode', allowBlank:false},
	            {header: '所属机构', width: 100, dataIndex: 'orgId', hideGrid: true, inputType:"hidden", id:"org_Id"},
	            {header: '所属机构', width: 100, fieldtype:'deptfield', dataIndex: 'orgName', allowBlank:false},
	            {header: '性别', width: 50, hideGrid: true, dataIndex: 'gender', hiddenName: 'gender',dictTypeId: '10001', allowBlank:false, fieldtype:'genderField'},
	            {header: '性别', width: 50, dataIndex: 'gender_Name', hideForm: 'all'},
	            {header: '电话号码', width: 100, dataIndex: 'phoneNo'},
	            {header: '移动号码', width: 100, dataIndex: 'mPhoneNo'},
	            {header: '邮箱', width: 150, dataIndex: 'email'},
	            {header: '出生日期', width: 150, dataIndex: 'birthday', inputType:"birthday"},
	            {header: '上次登录时间', width: 150, dataIndex: 'lastLogin', allowBlank:false, hideForm:"all"},
	            {header: '上次登录IP', width: 150, dataIndex: 'ipAddress', hideForm:"all", allowBlank:false}
	        ],
	        
	eastWidth: 250,
	eastGridTitle: '资源绑定的角色',
	urlEastGridQuery: './../user/queryRoles4User.json',
	eastGridColumn: [{header: "角色名称", width: 120, dataIndex: 'name', name: 'name'},
            {header: "角色编码", width: 125, dataIndex: 'code', name: 'code'}],
	
	queryFormItms: [{ 
				layout: 'tableform',
	            layoutConfig: {
	           		columns: 3,
	            	columnWidths: [0.33, 0.33, 0.33]
	            },           
		        items:[{xtype:'textfield', fieldLabel: '登陆账号', name: 'userCode', id: 'userCode', anchor:'70%' },
		        	{xtype:'textfield', fieldLabel: '用户名称', name: 'userName', id: 'userName', anchor:'70%' },
		            {xtype:'deptfield', fieldLabel: '机构', name: 'orgId', anchor:'70%' }]
		    }],
    
	addButtonOnToolbar: function(toolbar, index) {
    	toolbar.insertButton(index++,new Ext.Button({text:"授予角色",iconCls: 'authorization', id:'authRole', disabled: !ST.util.isAuthOperation('core.authRole')}));
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
    	var win = ST.util.genWindow({
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
		ST.base.userView.superclass.constructor.call(this, {});
		
		//授权资源
        btn = Ext.getCmp("authRole");
        btn.on("click", function(){
        	this.authRoleDiag();
        }, this);
	}
});