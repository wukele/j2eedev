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
    addTitle: "增加数据",
    editTitle: "更新数据",
    setFieldDefaultValue: function(node) {},
    loadEditFormSucHandler: function() {},

	createTree: function() {
		this.tree = new Ext.tree.TreePanel({
		    region: 'center',
		    autoScroll: true,
		    animate: true,
		    enableDD: true,
		    containerScroll: true,
		    border: false,
		    tbar: this.buildToolbar(),
		    loader: new Ext.tree.TreeLoader({
		    	url: this.urlTreeQuery
		    }),
			root: new Ext.tree.AsyncTreeNode({
				id: "0",
				hidden: false
			}),
			rootVisible: false
		});
		
		this.tree.getRootNode().expand();
	},
	
	buildToolbar: function() {
        var toolbar = [{
            text    : '新增下级' + this.typeName,
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
    
    createChild: function() {
    	var node = this.getSelectedNode();
        if (node == null || node.leaf) {
            Ext.Msg.alert('提示', "请选择一个非叶子节点");
        } else {
    		this.buildAddDialog(node);
	   	 	this.addDialog.show(Ext.get("createChildID"));
	    }
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
    	})

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
   
    constructor: function() {
		this.createTree();
		this.buildDragDrop();
		
		var items = [this.tree];
		ST.ux.ViewTree.superclass.constructor.call(this, {
			layout: "border",
			items: items
		});
	}
});