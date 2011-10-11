Ext.namespace("ST.ux");
Ext.QuickTips.init();

ST.ux.PlainTree = Ext.extend(Ext.tree.TreePanel,  {
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
    addTitle: "增加数据",
    editTitle: "更新数据",
    setFieldDefaultValue: function(node) {},
    loadEditFormSucHandler: function() {},
    //增加和更新表单，提交额外的参数
    getFormParams: function() {return {};},
    validHandle: function() {return true;},
    
	buildTbar: function() {
		var tbar = [{
            text    : '根节点',
            iconCls : 'cursor',
            id		: 'selectRootID',
            tooltip : '选择根节点' ,
            handler : this.selectRoot.createDelegate(this)
        },{
            text    : '新增子' + this.typeName,
            iconCls : 'add',
            id		: 'createChildID',
            tooltip : '添加选中节点的下级' + this.typeName,
            handler : this.createChild.createDelegate(this)
        },{
            text    : '修改' + this.typeName,
            iconCls : 'edit',
            id		: 'editNodeID',
            tooltip : '修改选中' + this.typeName,
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
        }];
        return tbar;
	},
	
	buildBbar: function() {
		var bbar = [{
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
        return bbar;
	},
	
	selectRoot: function() {
    	this.getRootNode().select();
    },
	
	createChild: function() {
		if(!this.validHandle()) {
			return false;
		}
    	var node = this.getSelectedNode();
    	if (node == null) {
			node = this.getRootNode();	
    	}

    	this.buildAddDialog(node);
   	 	this.addDialog.show(Ext.get("createChildID"));
    },
    
    updateNode: function() {
    	var node = this.getSelectedNode();
    	if (node == null) {
            Ext.Msg.alert('提示', "请选择一个叶子节点");
        } else {
    		this.buildEditDialog(node);
	   	 	this.editDialog.show(Ext.get("editNodeID"));
	   	 	this.editFormPanel.load({url: this.urlLoadNode, params: {id: node.id}, success: this.loadEditFormSucHandler});
	    }
    },
    
    deleteNode: function() {
    	var node = this.getSelectedNode();
        if (node == null) {
            Ext.Msg.alert('提示', "请选择一个节点");
        } else {
        	//先展开选择的节点
        	node.expand(false, true, function() {
        		if(!node.leaf && node.childNodes.length > 0) {
	        		Ext.Msg.alert('提示', "选择的节点包含子节点，请先删除所有子节点。");
	        		return
	        	}
	            Ext.Msg.confirm("提示", "是否确定删除？", function(btn, text) {
	                if (btn == 'yes') {                  
	                    this.body.mask('提交数据，请稍候...', 'x-mask-loading');
	
	                    Ext.Ajax.request({
				            url     : this.urlRemoveNode,
				            params  : {id: node.id},
				            success : function() {
				                this.body.unmask();
				                Ext.MessageBox.alert('提示', '操作成功！');
				                node.parentNode.reload();
				            },
				            failure : function(){
				            	this.body.unmask();
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
    	if(!this.validHandle()) {
			return false;
		}
    	var node = this.getSelectedNode();
    	if (node == null) {
			node = this.getRootNode();	
    	}
        if (node.leaf) {
            Ext.MessageBox.alert("提示", "请选择一个非叶子节点，将对其下的子节点排序。");
    		return;
        } 
    	this.body.mask('提交数据，请稍候...', 'x-mask-loading');
    	var childs = [];
    	for (var i = 0; i < node.childNodes.length; i++) {
            var child = node.childNodes[i];
            childs.push(child.id);
        }
    	
        Ext.Ajax.request({
            url     : this.urlSaveOrder,
            params  : {childIds: childs.join(","), parentId: node.id},
            success : function() {
                this.body.unmask();
                Ext.MessageBox.alert('提示', '操作成功！');
                var node = this.getSelectedNode();
		    	if (node == null) {
					node = this.getRootNode();	
		    	}
                this.getSelectedNode().reload();
            },
            failure : function(){
            	this.body.unmask();
                Ext.MessageBox.alert('提示', '操作失败！');
            },
            scope   : this
        });
    },
    
    expandAll: function() {
    	if(!this.validHandle()) {
			return false;
		}
    	var node = this.getSelectedNode();
        if (node == null) {
            this.getRootNode().eachChild(function(n) {
                n.expand(false, true);
            });
        } else {
            node.expand(false, true);
        }
    },
    
    collapseAll: function() {
    	if(!this.validHandle()) {
			return false;
		}
    	var node = this.getSelectedNode();
        if (node == null) {
            this.getRootNode().eachChild(function(n) {
                n.collapse(true, true);
            });
        } else {
            node.collapse(true, true);
        }
    },
    
    refresh: function() {
    	if(!this.validHandle()) {
			return false;
		}
    	var node = this.getSelectedNode();
    	if(node == null){
    		this.getRootNode().reload();
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
        return this.getSelectionModel().getSelectedNode();
    },
    
    buildDragDrop: function() {
    	this.on("beforemovenode", function(tree, node, oldParent, newParent, index){
    		return !newParent.leaf;
    	});

        this.on('nodedrop', function(e) {
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
            labelWidth: 70,
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
                    if (this.addFormPanel.getForm().isValid()) {
                    	this.setFieldDefaultValue(parentNode);
                        this.addFormPanel.getForm().submit({
                        	waitMsg : '正在处理，请稍等...',
                            success: function(a,b) {
                                this.addDialog.close();
                                parentNode.reload();
                            },
                            failure: function(a) {
                            },
                            params: this.getFormParams(),
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
    buildEditDialog : function(node) {
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
            url: this.urlEditNode,
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
                                node.parentNode.reload();
                            },
                            failure: function(a) {
                            	//this.hide();
                            },
                            params: this.getFormParams(),
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
    
    buildRoot: function() {
    	return new Ext.tree.AsyncTreeNode({
			id: "0",
			hidden: false,
			disabled: true,
			expanded: false
		});
    },
    
    buildLoader: function() {
    	return new Ext.tree.TreeLoader({
	    	url: this.urlTreeQuery
	    });
    },
    
    createSlibing: function() {
    	var node = this.getSelectedNode();
    	node = node.parentNode;
    	this.buildAddDialog(node);
	   	this.addDialog.show(node);
    },
    
    prepareContext: function(node, e) {
        node.select();
        this.contextMenu.showAt(e.getXY());
    },
	
	// 初始化
    initComponent: function() {
        this.tbar = this.buildTbar();
        this.bbar = this.buildBbar();
        this.loader = this.buildLoader();
		this.root = this.buildRoot();
		this.rootVisible = false;
		this.region = 'east';
		this.autoScroll = true;
		this.animate = true;
		this.enableDD = true;
		this.containerScroll = true;
		this.border = false;

        ST.ux.PlainTree.superclass.initComponent.call(this);
        
        this.on('contextmenu', this.prepareContext, this);
		this.contextMenu = new Ext.menu.Menu({
            id    : 'copyCtx',
            items : [{
                id      : 'createSlibingMenuId',
                handler : this.createSlibing.createDelegate(this),
                iconCls : 'add',
                text    : '添加同级' + this.typeName
            }, {
                id      : 'createChildMenuId',
                handler : this.createChild.createDelegate(this),
                iconCls : 'add',
                text    : '添加子' + this.typeName
            }, {
                id      : 'editNodeMenuId',
                handler : this.updateNode.createDelegate(this),
                iconCls : 'edit',
                text    : '修改' + this.typeName
            }, {
                id      : 'deleteNodeMenuId',
                handler : this.deleteNode.createDelegate(this),
                iconCls : 'delete',
                text    : '删除' + this.typeName
            }, '-', {
                id      : 'saveOrderMenuID',
                handler : this.saveOrder.createDelegate(this),
                iconCls : 'save',
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
            }]
        });
    }
});