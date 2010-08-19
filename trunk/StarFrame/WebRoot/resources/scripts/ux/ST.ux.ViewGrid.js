Ext.namespace("ST.ux");
Ext.QuickTips.init();

ST.ux.ViewGrid = Ext.extend(Ext.Viewport, {
	urlGridQuery : "",  
	urlAdd: "/",
	urlEdit: "/",
	urlLoadData: "/",
	urlRemove: "/",
	pageSize: 10,
	dlgWidth: 400,
	dlgHeight: 300,
	girdColumns: [],
	queryFormItms: [],
    addTitle: "增加数据",
    editTitle: "更新数据",
    gridTitle: "数据列表",
    displayEast: false,
    autoExpandColumn: "descn",
    addButtonOnToolbar: function(toolbar, index){},
    rowclickFn: function(){},
    
    eastWidth: 250,
    eastGridTitle: '',
    urlEastGridQuery: '/',
    eastGridColumn: [],
	
	createForm : function() {
		this.queryForm = new Ext.form.FormPanel({ 
			region: 'north',
		    title: "查询条件", 
		    id: "form-panel",
		    frame : true,
		    collapsible: true,
		    buttonAlign: 'center',
		    height:92, 
		    bodyStyle:'padding:0 0 0 2', 
		    items: this.queryFormItms, 
		    scope: this,
		    buttons: [{ 
				text: '查询', 
				type:'button', 
				id:'login', 
				iconCls:'query',
				handler: this.queryData,
				scope: this
			},{ 
				text: '重置', 
				type:'reset', 
				id:'clear', 
				iconCls:'redo',
				handler: this.reset,
				scope: this
			}]
		})
	},
	// 初始化ColumnModel
    buildColumnModel: function() {
        this.sm = new Ext.grid.CheckboxSelectionModel({});
        this.columnHeaders = [];
        this.columnHeaders[0] = new Ext.grid.RowNumberer();
        this.columnHeaders[1] = this.sm;

        for (var i = 0; i < this.girdColumns.length; i++) {
            var col = this.girdColumns[i];
            if (col.hideGrid === true) {
                continue;
            }
            col.renderer = this[col.renderer];
            this.columnHeaders.push(col);
        }
    },
    
    buildStore: function() {
    	var fields = [];

        for (var i = 0; i < this.girdColumns.length; i++) {
            var col = this.girdColumns[i];
            col['name'] = col.dataIndex
            fields.push(col);
        }
    	this.store = new Ext.data.Store({
            proxy  : new Ext.data.HttpProxy({url: this.urlGridQuery}),
            reader : new Ext.data.JsonReader({
                root          : "result",
                totalProperty : "totalCount",
                idProperty    : "id",
                fields        : fields
            }),
            remoteSort : true,
            scope:this
        });
    },
    
	createGrid: function() {
    	this.buildColumnModel();
    	this.buildStore();
        
        var paging = new Ext.PagingToolbar({
            pageSize: this.pageSize,
            store: this.store,
            displayInfo: true,
            displayMsg: '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
            emptyMsg: "没有记录",
            scope:this,
            plugins: [new Ext.ux.ProgressBarPager()]
        });
        
        this.store.load({
            params:{start:0, limit:paging.pageSize}
        });

	    this.grid = new Ext.grid.GridPanel({
	        store: this.store,
	        region: 'center',
	        bodyStyle:'width:100%',
       		autoWidth:true,
	        columns: this.columnHeaders,
	        stripeRows: true,
	        autoExpandColumn: this.autoExpandColumn,
	        loadMask:"正在加载......",
	        title: this.gridTitle,
	        bbar: paging,
	        scope:this
	    });
	    var index = 11;
	    this.grid.getBottomToolbar().insertButton(index++,'-');
	    this.grid.getBottomToolbar().insertButton(index++,new Ext.Button({text:"添加",iconCls: 'add', id:'addEntity'}));
	    this.grid.getBottomToolbar().insertButton(index++,new Ext.Button({text:"编辑",iconCls: 'edit', id:'editEntity'}));
	    this.grid.getBottomToolbar().insertButton(index++,new Ext.Button({text:"删除",iconCls: 'delete', id:'delEntity'}));
	    this.addButtonOnToolbar(this.grid.getBottomToolbar(), index);
	    
	    this.grid.addListener('rowclick', this.rowclickFn, this);
		return this.grid;
	},
    center: null,
    
    queryData: function() {
		Ext.apply(this.grid.store.lastOptions.params, this.queryForm.getForm().getFieldValues());
		this.grid.store.reload();
		if(this.east != null) {
			var ds = this.east.getStore();
			ds.removeAll();      
		}
    },
    
    reset: function() {
    	this.queryForm.getForm().reset();
    },
    
    // 检测至少选择一个
    checkOne: function() {
        var selections = this.grid.getSelectionModel().selections;
        if (selections.length == 0) {
            Ext.MessageBox.alert("提示", "请选择一条的记录！");
            return false;
        } else if (selections.length != 1) {
            Ext.MessageBox.alert("提示", "不能选择多行！");
            return false;
        }
        return true;
    },
    
    // 检测必须选择一个
    checkMany: function() {
        var selections = this.grid.getSelectionModel().selections;
        if (selections.length == 0) {
            Ext.MessageBox.alert("提示", "请至少选择一条的记录！");
            return false;
        }
        return true;
    },
    
    registerHandler: function() {
    	//添加
    	var btn = Ext.getCmp("addEntity");
        btn.on("click", function(){
        	this.buildAddDialog();
	    	this.addDialog.show(Ext.get("addEntity"));
        }, this)
        
        //编辑 
        btn = Ext.getCmp("editEntity");
        btn.on("click", function(){
        	this.buildEditDialog();
        	if (this.checkOne()) {
	            this.editDialog.show(Ext.get("editEntity"));
	            this.editFormPanel.load({url: this.urlLoadData, params : {id: this.grid.getSelectionModel().selections.items[0].id}});
	        }
        }, this)
        
        //删除
        btn = Ext.getCmp("delEntity");
        btn.on("click", function(){
        	this.delData();
        }, this)
    },
    
    buildItems: function(flag) {
    	var items = [];
        for (var i = 0; i < this.girdColumns.length; i++) {
            var col = this.girdColumns[i];
            if (col.hideForm == flag || col.hideForm == "all") {
                continue;
            }
            col['fieldLabel'] = col.header;
            if(col.fieldtype) col.xtype = col.fieldtype;
            items.push(col);
        }
        
        Ext.each(items, function(item) {
            Ext.applyIf(item, {
                anchor: '90%'
            });
        });
        return items;
    },
    
    // 创建弹出式对话框
    buildAddDialog : function() {
    	this.flag = "add";
        this.addFormPanel = new Ext.form.FormPanel({
            defaultType: 'textfield',
            labelAlign: 'right',
            labelWidth: 70,
            frame: true,
            id: "addFormPanelID",
            autoScroll: true,
            buttonAlign: 'center',
            url: this.urlAdd,
            items: this.buildItems("add"),
            scope: this,
            buttons: [{
                text: '确定',
                scope: this,
                handler: function() {
                    if (this.addFormPanel.getForm().isValid()) {
                        this.addFormPanel.getForm().submit({
                        	waitMsg : '正在处理，请稍等...',
                            success: function(a,b) {
                                this.addDialog.close();
                                this.grid.store.reload();
                            },
                            failure: function(a) {
								
                            },
                            scope: this
                        });
                    }
                }
            },{
                text: '取消',
                scope: this,
                handler: function() {
			    	this.addDialog.close();
                }
            }]
        });
        
        this.addDialog = new Ext.Window({
            layout: 'fit',
            title: this.addTitle,
            modal: true,
            width: this.dlgWidth,
            height: this.dlgHeight,
            closeAction: 'close',
            items: [this.addFormPanel]
        });
    },
    
    // 创建弹出式对话框
    buildEditDialog : function() {
    	this.flag = "edit";
    	var items = this.buildItems("edit");
    	var reader = new Ext.data.JsonReader({}, items);
        this.editFormPanel = new Ext.form.FormPanel({
            defaultType: 'textfield',
            labelAlign: 'right',
            labelWidth: 70,
            frame: true,
            id: "editFormPanelID",
            autoScroll: true,
            buttonAlign: 'center',
            url: this.urlEdit,
            reader: reader,
            items: items,
            scope: this,
            buttons: [{
                text: '确定',
                scope: this,
                handler: function() {
                    if (this.editFormPanel.getForm().isValid()) {
                        this.editFormPanel.getForm().submit({
                        	waitMsg : '正在处理，请稍等...',
                            success: function() {
                                this.editDialog.close();
                                this.grid.store.reload();
                            },
                            failure: function(a) {
                            	//this.hide();
                            },
                            scope: this
                        });
                    }
                }
            },{
                text: '取消',
                scope: this,
                handler: function() {
			    	this.editDialog.close();
                }
            }]
        });
        
        this.editDialog = new Ext.Window({
            layout: 'fit',
            title: this.editTitle,
            modal: true,
            width: this.dlgWidth,
            height: this.dlgHeight,
            closeAction: 'close',
            items: [this.editFormPanel]
        });
    },
    
    delData: function() {
        if (this.checkMany()) {
            Ext.Msg.confirm("提示", "是否确定？", function(btn, text) {
                if (btn == 'yes') {
                    this.grid.body.mask('正在处理，请稍等...', 'x-mask-loading');
                    Ext.Ajax.request({
                        url     : this.urlRemove,
                        params  : {id : this.grid.getSelectionModel().selections.items[0].id},
                        success : function() {
                            this.grid.body.unmask();
                            Ext.MessageBox.alert('提示', '操作成功！');
                            this.grid.store.reload();
                        },
                        failure : function(){
                        	this.grid.body.unmask();
                            Ext.MessageBox.alert('提示', '操作失败！');
                        },
                        scope   : this
                    });
                }
            }.createDelegate(this));
        }
    },

	getEast: function() {
    	var estore = new Ext.data.Store({
            proxy  : new Ext.data.HttpProxy({url: this.urlEastGridQuery}),
            reader : new Ext.data.JsonReader({
                idProperty    : "id",
                fields        : this.eastGridColumn
            }),
            scope:this
        });
    	var egrid = new Ext.grid.GridPanel({
	        store: estore,
	        region: 'east',
       		width: this.eastWidth,
       		collapsible: true,
	        columns: this.eastGridColumn,
	        stripeRows: true,
	        loadMask:"正在加载......",
	        title: this.eastGridTitle,
	        scope:this
	    });
    	
    	return egrid;
    },
    
    constructor: function() {
		this.createForm();
		this.createGrid();
		var items = [this.queryForm, this.grid];
		if(this.displayEast) {
			this.east = this.getEast();
			items.push(this.east);
		}
		ST.ux.ViewGrid.superclass.constructor.call(this, {
			layout: "border",
			items: items
		});
		this.registerHandler();
	}
});