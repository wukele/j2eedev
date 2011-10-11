Ext.namespace("ST.ux");
Ext.QuickTips.init();

ST.ux.ViewTree = Ext.extend(Ext.Viewport, {
	typeName: '节点',
	urlSaveOrder: "/",
	urlTreeQuery: "/",
	urlRemoveNode: "/",
	urlAddNode: "/",
	urlEditNode: "/",
	urlLoadNode: "/",
	dlgWidth: 400,
	dlgHeight: 300,
	girdColumns: [],
	addEastToolbar: function(){},
	eastsm: null,
    estore: null,
    egrid: null,
    //checkbox扩展标识
    ckb:false,
    addTitle: "增加数据",
    editTitle: "更新数据",
    setFieldDefaultValue: function(node) {},
    addMenuItem: function(items) {},
    loadEditFormSucHandler: function() {},
    //增加和更新表单提交时，对输入项进行验证
    validFormHandle: function() {return true;},
    showToolBar: false,
    showContextMenu: true,
    authOperations: [!ST.util.isAuthOperation('core.addSiblingNode'),
                     !ST.util.isAuthOperation('core.addChildNode'),
                     !ST.util.isAuthOperation('core.updateNode'),
                     !ST.util.isAuthOperation('core.removeNode'),
                     !ST.util.isAuthOperation('core.orderNode')],
    
    displayEast: false,
    eastWidth: 400,
    eastGridTitle: '',
    urlEastGridQuery: '/',
    eastGridColumn: [],
    eastPageSize: 10,
    beforedblclickFn: function(){},
    
    centerRegion : 'center',
    centerWidth : '280',

	createTree: function() {
		this.tree = new Ext.tree.TreePanel({
		    region: this.centerRegion,
		    autoScroll: true,
		    animate: true,
		    width: this.centerWidth,
		    collapsible: (this.centerRegion=='center' ?  false : true),
		    split: true,
		    enableDD: true,
		    containerScroll: true,
		    border: false,
		    tbar: this.buildToolbar(),
		    loader: new Ext.tree.TreeLoader({
		    	url: this.urlTreeQuery
		    }),
			root: new Ext.tree.AsyncTreeNode({
				id: "0",
				level: "0",
				hidden: false
			}),
			rootVisible: false
		});
		
		this.tree.getRootNode().expand();
		this.tree.addListener("beforedblclick", this.beforedblclickFn, this);
	},
	
	buildToolbar: function() {
		if(!this.showToolBar)
			return null;
		
        var toolbar = [{
            text    : '新增子' + this.typeName,
            iconCls : 'add',
            id		: 'createChildID',
            tooltip : '添加子' + this.typeName,
            handler : this.createChild.createDelegate(this)
        },{
            text    : '更新' + this.typeName,
            iconCls : 'edit',
            id		: 'editNodeID',
            tooltip : '更新选中' + this.typeName,
            handler : this.updateNode.createDelegate(this)
        }, {
            text    : '删除' + this.typeName,
            iconCls : 'delete',
            id		: 'removeNodeID',
            tooltip : '删除一个' + this.typeName,
            handler : this.deleteNode.createDelegate(this)
        }, '-', {
            text    : '保存排序',
            iconCls : 'save',
            id		: 'saveOrderID',
            tooltip : '保存排序结果',
            handler : this.saveOrder.createDelegate(this)
        }, '-', {
            text    : '展开',
            iconCls : 'expand',
            id		: 'expandId',
            tooltip : '展开' + this.typeName,
            handler : this.expandAll.createDelegate(this)
        }, {
            text    : '合拢',
            id		: 'collapseId',
            iconCls : 'collapse',
            tooltip : '合拢' + this.typeName,
            handler : this.collapseAll.createDelegate(this)
        }, {
            text    : '刷新',
            id		: 'refreshId',
            iconCls : 'refresh',
            tooltip : '刷新' + this.typeName,
            handler : this.refresh.createDelegate(this)
        }];
        return toolbar;
    },
    
    createSlibing: function() {
    	var node = this.getSelectedNode();
    	node = node.parentNode;
    	this.buildAddDialog(node);
	   	this.addDialog.show(node);
    },
    
    createChild: function() {
    	var node = this.getSelectedNode();
    	if(node == null)
    		node = this.tree.getRootNode();
        if (node.leaf) {
            Ext.Msg.alert('提示', "请选择一个非叶子节点");
        } else {
    		this.buildAddDialog(node);
	   	 	this.addDialog.show(node);
	    }
    },
    
    updateNode: function() {
    	var node = this.getSelectedNode();
    	if (node == null) {
            Ext.Msg.alert('提示', "请选择一个叶子节点");
        } else {
    		this.buildEditDialog(node);
	   	 	this.editDialog.show(node);
	   	 	this.editFormPanel.load({waitMsg : '正在载入数据...', url: this.urlLoadNode, params: {id: node.id}, success: this.loadEditFormSucHandler});
	    }
    },
    
    deleteNode: function() {
    	var node = this.getSelectedNode();
        if (node == null) {
            Ext.Msg.alert('提示', "请选择一个节点" + this.typeName);
        } else {
        	//先展开选择的节点
        	node.expand(false, true, function() {
        		if(!node.leaf && node.childNodes.length > 0) {
	        		Ext.Msg.alert('提示', "选择的节点包含子节点，请先删除所有子节点。");
	        		return
	        	}
	            Ext.Msg.confirm("提示", "是否确定删除？", function(btn, text) {
	                if (btn == 'yes') {                  
	                    this.tree.body.mask('提交数据，请稍候...', 'x-mask-loading');
	
	                    Ext.Ajax.request({
				            url     : this.urlRemoveNode,
				            params  : {id: node.id},
				            success : function() {
				                this.tree.body.unmask();
				                Ext.MessageBox.alert('提示', '操作成功！');
				                node.parentNode.reload();
				            },
				            failure : function(){
				            	this.tree.body.unmask();
				                Ext.MessageBox.alert('提示', '操作失败！');
				            },
				            scope   : this
				        });
	                }
	            }, this);
        	}, this);
        }
    },
    
    /**
     * 不迭代子节点排序
     * @memberOf {TypeName} 
     * @return {TypeName} 
     */
    saveOrder: function() {
    	var node = this.getSelectedNode();
    	if(node == null || node.leaf) {
    		Ext.MessageBox.alert("提示", "请选择一个非叶子节点，将对其下的子节点排序。");
    		return;
    	}
    	this.tree.body.mask('提交数据，请稍候...', 'x-mask-loading');
    	var childs = [];
    	for (var i = 0; i < node.childNodes.length; i++) {
            var child = node.childNodes[i];
            childs.push(child.id);
        }
    	
        Ext.Ajax.request({
            url     : this.urlSaveOrder,
            params  : {childIds: childs.join(","), parentId: node.id},
            success : function() {
                this.tree.body.unmask();
                Ext.MessageBox.alert('提示', '操作成功！');
                this.getSelectedNode().reload();
            },
            failure : function(){
            	this.tree.body.unmask();
                Ext.MessageBox.alert('提示', '操作失败！');
            },
            scope   : this
        });
    },
    
    expandAll: function() {
    	var node = this.getSelectedNode();
        if (node == null) {
            this.tree.getRootNode().eachChild(function(n) {
                n.expand(false, true);
            });
        } else {
            node.expand(false, true);
        }
    },
    
    collapseAll: function() {
    	var node = this.getSelectedNode();
        if (node == null) {
            this.tree.getRootNode().eachChild(function(n) {
                n.collapse(true, true);
            });
        } else {
            node.collapse(true, true);
        }
    },
    
    refresh: function() {
    	var node = this.getSelectedNode();
    	if(node == null){
    		this.tree.getRootNode().reload();
    		return;
    	}
    	if(node.leaf) {
    		node.parentNode.reload();
    	} else {
    		node.reload();
    		node.expand(false, true);
    	}
    },
    
    // 返回当前选中的节点，可能为null
    getSelectedNode: function() {
        return this.tree.getSelectionModel().getSelectedNode();
    },
    
    buildDragDrop: function() {
    	this.tree.on("beforemovenode", function(tree, node, oldParent, newParent, index){
    		return !newParent.leaf;
    	});

        this.tree.on('nodedrop', function(e) {
        	var n = e.dropNode;
        	n.ui.textNode.style.fontWeight = "bold";
        	n.ui.textNode.style.color = "red";
        	n.ui.textNode.style.border = "1px red dotted";
        	n.select();
        });
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
    buildAddDialog : function(parentNode) {
    	this.flag = "add";
        this.addFormPanel = new Ext.form.FormPanel({
            defaultType: 'textfield',
            labelAlign: 'right',
            labelWidth: 80,
            frame: true,
            id: "addFormPanelID",
            autoScroll: true,
            buttonAlign: 'center',
            url: this.urlAddNode,
            items: this.buildItems("add"),
            scope: this,
            buttons: [{
                text: '确定',
                scope: this,
                handler: function() {
                    if (this.addFormPanel.getForm().isValid() && this.validFormHandle()) {
                    	this.setFieldDefaultValue(parentNode);
                        this.addFormPanel.getForm().submit({
                        	waitMsg : '正在处理，请稍等...',
                            success: function(a,b) {
                                this.addDialog.close();
                                parentNode.reload();
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
    buildEditDialog : function(node) {
    	this.flag = "edit";
    	var items = this.buildItems("edit");
    	var reader = new Ext.data.JsonReader({}, items);
        this.editFormPanel = new Ext.form.FormPanel({
            defaultType: 'textfield',
            labelAlign: 'right',
            labelWidth: 80,
            frame: true,
            id: "editFormPanelID",
            autoScroll: true,
            buttonAlign: 'center',
            url: this.urlEditNode,
            reader: reader,
            items: items,
            scope: this,
            buttons: [{
                text: '确定',
                scope: this,
                handler: function() {
                    if (this.editFormPanel.getForm().isValid() && this.validFormHandle()) {
                        this.editFormPanel.getForm().submit({
                        	waitMsg : '正在处理，请稍等...',
                            success: function() {
                                this.editDialog.close();
                                node.parentNode.reload();
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
            //layout: 'fit',
            title: this.editTitle,
            modal: true,
            width: this.dlgWidth,
            height: this.dlgHeight,
            closeAction: 'close',
            items: [this.editFormPanel]
        });
    },
    
    // 初始化ColumnModel
    buildColumnModel: function() {
        this.eastsm = new Ext.grid.CheckboxSelectionModel();
        this.columnHeaders = [];
        if(this.rowExpander != null)
        	this.columnHeaders.push(this.rowExpander);
        this.columnHeaders.push(new Ext.grid.RowNumberer());
        this.columnHeaders.push(this.eastsm);
        for (var i = 0; i < this.eastGridColumn.length; i++) {
            var col = this.eastGridColumn[i];
            if (col.hideGrid === true) {
                continue;
            }
            col.renderer = this[col.renderer];
            this.columnHeaders.push(col);
        }
    },
       
    buildStore: function() {
    	var fields = [];
        for (var i = 0; i < this.eastGridColumn.length; i++) {
            var col = this.eastGridColumn[i];
            col['name'] = col.dataIndex;
            fields.push(col);
        }
        this.estore = new Ext.data.Store({
        	proxy  : new Ext.data.HttpProxy({url: this.urlEastGridQuery}),
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
    
    getEast: function() {
    	if(this.ckb){
	    	this.buildColumnModel();
	    	this.buildStore();
	    	
	    	var paging = new Ext.PagingToolbar({
	            pageSize: this.eastPageSize,
	            store: this.estore,
	            displayInfo: true,
	            displayMsg: '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
	            emptyMsg: "没有记录",
	            scope:this,
	            plugins: [new Ext.ux.ProgressBarPager()]
	        });
	    	this.egrid = new Ext.grid.GridPanel({
		        store: this.estore,
		        region: 'east',
	       		width: this.eastWidth,
	       		collapsible: true,
	       		sm : this.eastsm,
		        columns: this.columnHeaders,
		        stripeRows: true,
		        loadMask:"正在加载......",
		        tbar: this.addEastToolbar(),
		        bbar: paging,
		        title: this.eastGridTitle,
		        scope:this
		    });
		 	return this.egrid;
    	}else{
	    	var estore = new Ext.data.Store({
            proxy  : new Ext.data.HttpProxy({url: this.urlEastGridQuery}),
            reader : new Ext.data.JsonReader({
                root          : "result",
                totalProperty : "totalCount",
                idProperty    : "id",
                fields        : this.eastGridColumn
            }),
            remoteSort : true,
            scope:this
        });
    	var paging = new Ext.PagingToolbar({
            pageSize: this.eastPageSize,
            store: estore,
            displayInfo: true,
            displayMsg: '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
            emptyMsg: "没有记录",
            scope:this,
            plugins: [new Ext.ux.ProgressBarPager()]
        });
    	var egrid = new Ext.grid.GridPanel({
	        store: estore,
	        region: 'east',
       		width: this.eastWidth,
       		collapsible: true,
	        columns: this.eastGridColumn,
	        stripeRows: true,
	        loadMask:"正在加载......",
	        bbar: paging,
	        title: this.eastGridTitle,
	        scope:this
	    });
    	
    	return egrid;
	    }
    },
    
    prepareContext: function(node, e) {
        node.select();
        this.tree.contextMenu.showAt(e.getXY());
    },
   
    constructor: function() {
		this.createTree();
		this.buildDragDrop();
		
		var items = [this.tree];
		if(this.displayEast) {
			this.east = this.getEast();
			items.push(this.east);
		}
		ST.ux.ViewTree.superclass.constructor.call(this, {
			layout: "border",
			items: items
		});
		
		if(this.showContextMenu) {
			items = [{
	            id      : 'createSlibingMenuId',
	            handler : this.createSlibing.createDelegate(this),
	            iconCls : 'add',
	            disabled: this.authOperations[0],
	            text    : '添加同级' + this.typeName
	        }, {
	            id      : 'createChildMenuId',
	            handler : this.createChild.createDelegate(this),
	            iconCls : 'add',
	            disabled: this.authOperations[1],
	            text    : '添加子' + this.typeName
	        }, {
	            id      : 'editNodeMenuId',
	            handler : this.updateNode.createDelegate(this),
	            iconCls : 'edit',
	            disabled: this.authOperations[2],
	            text    : '修改' + this.typeName
	        }, {
	            id      : 'deleteNodeMenuId',
	            handler : this.deleteNode.createDelegate(this),
	            iconCls : 'delete',
	            disabled: this.authOperations[3],
	            text    : '删除' + this.typeName
	        }, '-', {
	            id      : 'saveOrderMenuID',
	            handler : this.saveOrder.createDelegate(this),
	            iconCls : 'save',
	            disabled: this.authOperations[4],
	            text    : '保存排序'
	        }, '-', {
	            id      : 'expandMenuId',
	            handler : this.expandAll.createDelegate(this),
	            iconCls : 'expand',
	            text    : '展开' + this.typeName
	        }, {
	            id      : 'collapseMenuId',
	            handler : this.collapseAll.createDelegate(this),
	            iconCls : 'collapse',
	            text    : '合拢' + this.typeName
	        }, {
	            id      : 'refreshMenuId',
	            handler : this.refresh.createDelegate(this),
	            iconCls : 'refresh',
	            text    : '刷新' + this.typeName
	        }];
	        this.addMenuItem(items);
			this.tree.on('contextmenu', this.prepareContext, this);
			this.tree.contextMenu = new Ext.menu.Menu({
	            id    : 'copyCtx',
	            items : items
	        });
		}
	}
});