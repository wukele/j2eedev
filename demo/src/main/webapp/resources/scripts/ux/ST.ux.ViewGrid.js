Ext.namespace("ST.ux");
Ext.QuickTips.init();

ST.ux.ViewGrid = Ext.extend(Ext.Viewport, {
	urlGridQuery : "",  
	urlAdd: "/",
	urlEdit: "/",
	urlLoadData: "/",
	urlRemove: "/",
	dlgWidth: 400,
	dlgHeight: 300,
	girdColumns: [],
	queryFormItms: [],
    addTitle: "增加数据",
    editTitle: "更新数据",
    gridTitle: "数据列表",
    formTitle: '查询条件',
    displayEast: false,
    autoExpandColumn: "descn",
    displayHeader : true,
    enablebbar : true,
    dialogLabelWidth: 70,
    addButtonOnToolbar: function(toolbar, index){},
    //加载combobox的时候对选项进行选择
    loadEditFormSucHandler: function() {},
    clickType: 'rowdblclick',
    rowclickFn: function(){},
    addMenuItem: function(items) {},
    //是否显示增删改按钮
    displayButton: true,
    //增加、更新表单是否支持上传文件
    isFileUpload: false,
    queryFormHeight: 105,
    //操作按钮权限值
    authOperations: [!ST.util.isAuthOperation('core.add'),
                     !ST.util.isAuthOperation('core.update'),
                     !ST.util.isAuthOperation('core.remove'),
                     !ST.util.isAuthOperation('core.distibute'),
                     !ST.util.isAuthOperation('core.recommand')],
    
    eastWidth: 250,
    eastGridTitle: '',
    urlEastGridQuery: '/',
    eastGridColumn: [],
    rowExpander: null,
    buildEastToolbar: function() {},
	
	createForm : function() {
		this.queryForm = new Ext.form.FormPanel({ 
			region: 'north',
		    title: this.formTitle, 
		    id: "form-panel",
		    frame : true,
		    collapsible: false,
		    buttonAlign: 'center',
		    height:this.queryFormHeight, 
		    labelWidth :65,  ///缩小文本与text field间距
		    bodyStyle:'padding:0 0 0 1', 
		    items: this.queryFormItms, 
		    plugins: [Ext.ux.PanelCollapsedTitle],
		    scope: this,
		    buttons: [{ 
				text: '查询', 
				type:'button', 
				id:'login', 
				iconCls:'query',
				handler: this.queryData,
				scope: this,
				margin:'20px auto'
			},{html : '<pre>     </pre>'},{   //距离感调整
				text: '重置', 
				type:'reset', 
				id:'clear', 
				iconCls:'redo',
				handler: this.reset,
				scope: this,
				margin:'20px auto'
			},{html : '<pre>           </pre>'}]  //距离感调整
		});
	},
	// 初始化ColumnModel
    buildColumnModel: function() {
        this.sm = new Ext.grid.CheckboxSelectionModel();
        this.columnHeaders = [];
        if(this.rowExpander != null)
        	this.columnHeaders.push(this.rowExpander);
        this.columnHeaders.push(new Ext.grid.RowNumberer());
        this.columnHeaders.push(this.sm);

        for (var i = 0; i < this.girdColumns.length; i++) {
            var col = this.girdColumns[i];
            if (col.hideGrid) {
                continue;
            }
            //col.renderer = this[col.renderer];
            if(col.fontColor)
            	col.renderer = this.columnColorRenderer.createDelegate(this);
            this.columnHeaders.push(col);
        }
    },
    
    columnColorRenderer: function(value, metadata, record, rowIndex, colIndex) {
    	var col = this.columnHeaders[colIndex];
    	var arr = col.fontColor.split(",");
    	for(var i=1, len=arr.length; i<len;) {
    		if(record.json[arr[0]] == arr[i]) {
    			metadata.attr = 'style="font-weight:bold;color:' + arr[i+1] + ';"';
    			break;
    		}
    		i = i + 2;
    	}
		return value;
    },
    
    buildStore: function() {
    	var fields = [];

        for (var i = 0; i < this.girdColumns.length; i++) {
            var col = this.girdColumns[i];
            col['name'] = col.dataIndex;
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
    	
    	var pageSizeCombo = new Ext.form.ComboBox({
         	name : 'pagesize',
         	allowBlank : false,
        	mode : 'local',
         	triggerAction : 'all',
         	editable : false,
         	width: 70,
         	anchor : '90%',
         	store : new Ext.data.SimpleStore({
               fields : ['text', 'value'],
               data : [['10条/页', '10'], ['20条/页', '20'], ['50条/页', '50'], ['100条/页', '100']]
         	}),
         	value:'20',  
         	valueField : 'value',
         	displayField : 'text',
         	listeners: {
				'select' : function(comboBox, recored, index) {
	    			paging.pageSize = parseInt(comboBox.getValue()); 
			    	this.store.load({
		            	params:{start:0, limit:comboBox.getValue()}
		        	});
		    	}.createDelegate(this)
		    }	
	    });
        
        var paging = new Ext.PagingToolbar({
            pageSize: parseInt(pageSizeCombo.getValue()),
            store: this.store,
            displayInfo: true,
            displayMsg: '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
            emptyMsg: "没有记录",
            scope:this,
            plugins: [new Ext.ux.ProgressBarPager()]
        });
        
	    this.grid = new Ext.grid.GridPanel({
	    	viewConfig: {
	    		templates: {
	    			cell: new Ext.Template(
		    			'<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} x-selectable {css}" style="{style}" tabIndex="0" {cellAttr}>',
	    	            '<div class="x-grid3-cell-inner x-grid3-col-{id}" {attr}>{value}</div>',
	    	            '</td>'
	    			)
	    		}
	    	},
	        store: this.store,
	        region: 'center',
	        bodyStyle:'width:100%',
       		autoWidth:true,
       		frame : true,
       		sm: this.sm,
       		autoScroll : true, 
       		plugins: this.rowExpander,
	        columns: this.columnHeaders,
	        stripeRows: true,
	        autoExpandColumn: this.autoExpandColumn,
	        loadMask:"正在加载表格数据,请稍等...",
	        header : this.displayHeader,  
	        title: this.gridTitle,
	        bbar: paging,
	        tbar: {},
	        scope:this
	    });
	    var index = 11;
	    
	    this.grid.getBottomToolbar().insertButton(index++,'-');
	    this.grid.getBottomToolbar().insertButton(index++, pageSizeCombo);
        //激活bbar
	    if(this.displayButton&& this.enablebbar) {	   
	    	this.grid.getBottomToolbar().insertButton(index++,'-');
	    	this.grid.getBottomToolbar().insertButton(index++,new Ext.Button({text:"添加",iconCls: 'add', id:'addEntity', disabled: this.authOperations[0]}));
	    	this.grid.getBottomToolbar().insertButton(index++,new Ext.Button({text:"修改",iconCls: 'edit', id:'editEntity', disabled: this.authOperations[1]}));
	    	this.grid.getBottomToolbar().insertButton(index++,new Ext.Button({text:"删除",iconCls: 'delete', id:'delEntity', disabled: this.authOperations[2]}));
		    this.addButtonOnToolbar(this.grid.getBottomToolbar(), index);
	    }
	    //激活tbar
	    if(this.displayButton && !this.enablebbar){
	    	this.grid.getTopToolbar().insertButton(index++,new Ext.Button({text:"添加",iconCls: 'add', id:'addEntity', disabled: this.authOperations[0]}));
	    	this.grid.getTopToolbar().insertButton(index++,'-');
	    	this.grid.getTopToolbar().insertButton(index++,new Ext.Button({text:"修改",iconCls: 'edit', id:'editEntity', disabled: this.authOperations[1]}));
	    	this.grid.getTopToolbar().insertButton(index++,'-');
	    	this.grid.getTopToolbar().insertButton(index++,new Ext.Button({text:"删除",iconCls: 'delete', id:'delEntity', disabled: this.authOperations[2]}));
	    	this.grid.getTopToolbar().insertButton(index++,'-');
		    this.addButtonOnToolbar(this.grid.getTopToolbar(), index);
	    }
	    
	    this.grid.addListener(this.clickType, this.rowclickFn, this);
	    this.store.on("beforeload", function(){
            Ext.apply(this.store.lastOptions.params, this.queryForm.getForm().getFieldValues());
      	}, this);
		return this.grid;
	},
    center: null,
    
    queryData: function() {
		Ext.apply(this.store.lastOptions.params, this.queryForm.getForm().getFieldValues());
		this.grid.store.reload();
		if(this.east != null) {
			if(this.east.getXType() == "grid")
				this.east.store.removeAll();
			else
				this.east.getRootNode().removeAll(true);  
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
        }, this);
        
        //编辑 
        btn = Ext.getCmp("editEntity");
        btn.on("click", function(){
        	this.buildEditDialog();
        	if (this.checkOne()) {
	            this.editDialog.show(Ext.get("editEntity"));
	            this.editFormPanel.load({waitMsg : '正在载入数据...', url: this.urlLoadData, params : {id: this.grid.getSelectionModel().selections.items[0].id},success: this.loadEditFormSucHandler});
	        }
        }, this);
        
        //删除
        btn = Ext.getCmp("delEntity");
        btn.on("click", function(){
        	this.delData();
        }, this);
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
            	allowBlank: true,
                anchor: '90%'
            });
        });
        return items;
    },
    
    // 创建弹出式对话框
    buildAddDialog : function() {
    	this.flag = "add";
        this.addFormPanel = new Ext.form.FormPanel({
        	//form layout
        	layout: 'tableform',
            layoutConfig: {
           		columns: 2,
            	columnWidths: [.5,.5], 
            	bodyStyle:'padding:90px'
            },    
        	//form layout
            defaultType: 'textfield',
            labelAlign: 'right',
            labelWidth: this.dialogLabelWidth,
            frame: true,
            id: "addFormPanelID",
            autoScroll: true,
            buttonAlign: 'left',
            url: this.urlAdd,
            items: this.buildItems("add"),
            fileUpload: this.isFileUpload,
            scope: this,
            buttons: [{html : '<pre>          </pre>'},
              { text: '确定',
                scope: this,
                iconCls:'save',
                handler: function() {
                    if (this.addFormPanel.getForm().isValid()) {
                        this.addFormPanel.getForm().submit({
                        	waitMsg : '正在处理，请稍等...',
                            success: function(a, b) {
                                this.addDialog.close();
                                this.grid.store.reload();
                            },
                            failure: function(a, b) {
								Ext.MessageBox.alert("提示", b.result.message);
                            },
                            scope: this
                        });
                    }
                }
            },{html : '<pre>   </pre>'},{
                text: '取消',
                scope: this,
                handler: function() {
			    	this.addDialog.close();
                }
            }]
        });
        
        this.addDialog = new Ext.Window({
            //layout: 'fit',
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
        	layout: 'tableform',
            layoutConfig: {
           		columns: 2,
            	columnWidths: [.5,.5], 
            	bodyStyle:'padding:90px'
            },    
            defaultType: 'textfield',
            labelAlign: 'right',
            labelWidth: 70,
            frame: true,
            id: "editFormPanelID",
            autoScroll: true,
            buttonAlign: 'center',
            url: this.urlEdit,
            loadMask : true,   
            reader: reader,
            items: items,
            fileUpload: this.isFileUpload,
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
                            failure: function(a, b) {
                            	Ext.MessageBox.alert("提示", b.result.message);
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
       		collapsible: false,
	        columns: this.eastGridColumn,
	        stripeRows: true,
	        loadMask:"正在加载......",
	        tbar: this.buildEastToolbar(),
	        title: this.eastGridTitle,
	        scope:this
	    });
    	
    	return egrid;
    },
    
     prepareContext: function(g, rowIndex, e) {
        g.getSelectionModel().selectRow(rowIndex, false);
        g.contextMenu.showAt(e.getXY());
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
		
		this.store.load({
            params:{start:0, limit:this.grid.getBottomToolbar().pageSize}
        });
		
		if(this.displayButton)
			this.registerHandler();
		
		if(this.displayButton) {
			items = [{
	            id      : 'editMenuId',
	            handler : function() {
						var btn = Ext.getCmp("editEntity");
						btn.fireEvent("click");
	            	}.createDelegate(this),
	            iconCls : 'edit',
	            text    : '编辑'
	        }, {
	            id      : 'deleteMenuId',
	            handler : function() {
						var btn = Ext.getCmp("delEntity");
						btn.fireEvent("click");
	            	}.createDelegate(this),
	            iconCls : 'delete',
	            text    : '删除'
	        }];
	        
	        this.addMenuItem(items);
			this.grid.on('rowcontextmenu', this.prepareContext, this);
			this.grid.contextMenu = new Ext.menu.Menu({
	            id    : 'copyCtx',
	            items : items
	        });
			
		}
	}
});