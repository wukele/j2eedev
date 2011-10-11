Ext.BLANK_IMAGE_URL = "./../resources/scripts/ext/resources/images/default/s.gif";

Ext.namespace("ST.base");

var ds = new Ext.data.Store({
    proxy  : new Ext.data.HttpProxy({url: './../entityType/pageQueryEntityTypeByOpr.json'}),
    reader : new Ext.data.JsonReader({
        root          : "result",
        totalProperty : "totalCount",
        idProperty    : "id",
        fields        : [
        	{name: 'id'},
	        {name: 'text'},
	    ]
    })
});

var resultTpl = new Ext.XTemplate(
    '<tpl for="."><div class="x-combo-list-item">',
        '{text}',
    '</div></tpl>'
);
ST.ux.ExtField.EntityCombo = Ext.extend(ST.ux.ExtField.ClearableComboBox, {
    store: ds,
    hiddenName: 'entityType.id',
    editable : false,
    allowBlank: false,
    valueField: 'id',
    displayField:'text',
    listWidth: 360,
    typeAhead: true,
    loadingText: '正在加载数据...',
    pageSize:10,
    tpl: resultTpl,
    forceSelection: true,
    triggerAction: 'all',
    emptyText:'请选择...',
    selectOnFocus:true,
    listeners:{
        'select': function(combo, record, index) {
        	console.info(record.data.id);
        	Ext.getCmp("Ext_entityType").setValue(record.data.id);
        }
   }
});
Ext.reg('entityTypeField', ST.ux.ExtField.EntityCombo);

ST.base.operationView = Ext.extend(ST.ux.ViewGrid, {
	dlgWidth: 400,
	dlgHeight: 400,
	urlGridQuery: './../operation/pageQueryOperations.json',
	urlAdd: './../operation/insertOperation.json',
	urlEdit: './../operation/updateOperation.json',
	urlLoadData: './../operation/loadOperation.json',
	urlRemove: './../operation/deleteOperation.json',
	addTitle: "增加操作",
    editTitle: "更新操作",
    gridTitle: "操作数据",
    displayEast: true,
	girdColumns: [
				{header: 'ID', width: 30, dataIndex: 'id', sortable: true, hideGrid: true, hideForm: 'add', readOnly: true},
				{header: '操作编码', width: 120, dataIndex: 'code',sortable: false},
	            {header: '操作名称', width: 120, dataIndex: 'name', allowBlank:false},
	            {header: '关联实体', width: 120, dataIndex: 'text',hideGrid:true,fieldtype:'entityTypeField',id: 'Ext_entityType'},
	            //{header: '索引顺序', width: 120, dataIndex: 'orderIndex', sortable: true,regex:/[0-9]+/,regexText:"请输入数字"},
	            {id:'descn',header: '描述', width: 120, dataIndex: 'memo', fieldtype:'textarea'}
	            ],
	queryFormItms: [{ 
				layout: 'tableform',
	            layoutConfig: {
	           		columns: 2,
	            	columnWidths: [0.5, 0.5]
	            },           
		        items:[{xtype:'textfield', fieldLabel: '操作编码', name: 'code', id: 'code', anchor:'80%' },
		            {xtype:'textfield', fieldLabel: '操作名称', name: 'name', id: 'name', anchor:'80%' }] 
		    }],
    eastWidth: 250,
	eastGridTitle: '操作授权的角色',
	urlEastGridQuery: './../operation/queryRoles4Opt.json',
	eastGridColumn: [{header: "角色名称", width: 120, dataIndex: 'name', name: 'name'},
            {header: "角色编码", width: 125, dataIndex: 'code', name: 'code'}],
            
    loadEditFormSucHandler: function(form, action) {
    	var opr = action.reader.jsonData;
    	if(opr.entityType != null) {
    		console.info(opr.entityType.text);
    		Ext.getCmp("Ext_entityType").setValue(opr.entityType.text);
    		Ext.getCmp("Ext_entityType").hiddenField.value = opr.entityType.id;
    	}
    },        
		 
    addButtonOnToolbar: function(toolbar, index) {
    	toolbar.insertButton(index++,new Ext.Button({text:"授予角色",iconCls: 'authorization', id:'authRole', disabled: !ST.util.isAuthOperation('core.authRole')}));
    },
    
    authRoleDiag: function() {
    	if(!this.checkOne())
    		return;
    	
    	var grid = new ST.ux.PlainGrid({
    		btnText: '授予',
    		params: {optId: this.grid.getSelectionModel().selections.items[0].data.id},
	    	urlPagedQuery: './../operation/pageQueryRoles4Opt.json',
	    	urlBind: './../operation/bindRole.json',
	    	urlUnBind: './../operation/unBindRole.json',
	    	autoExpandColumn:5,
	        columConfig:[{header:"角色名称",width: 100,name:"name"},
	        	{header:"角色编码",width: 100,name:"code", renderer:this.renderType},
	        	{header:"是否授予",width: 60,name:"counter", renderer:this.authReder},
	        	{header:"角色描述",name:"descn"}]
	    }); 
    	var win = ST.util.genWindow({
            id: 'userSelectWindow',
            title    : '授权操作 -- ' + this.grid.getSelectionModel().selections.items[0].data.name,
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
     * Center 区域 Grid的行rowdblclick事件调用方法
     */
    rowclickFn: function(grid, rowIndex, e) {
    	var data = grid.getStore().getAt(rowIndex).data;
		this.east.store.load({
            params:{optId: data.id}
        });
    },
    
	constructor: function() {
		ST.base.operationView.superclass.constructor.call(this, {});
		
		//授权资源
        btn = Ext.getCmp("authRole");
        btn.on("click", function(){
        	this.authRoleDiag();
        }, this);
	}
});