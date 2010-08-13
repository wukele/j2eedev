Ext.namespace("ST.ux");
Ext.QuickTips.init();

ST.ux.ViewTree = Ext.extend(Ext.Viewport, {
	typeName: '节点',

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
		    	url:"./../menu/findMenus.json"
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
            id		: 'updateNodeID',
            tooltip : '修改选中' + this.typeName,
            handler : this.updateNode.createDelegate(this)
        }, {
            text    : '删除' + this.typeName,
            iconCls : 'delete',
            id		: 'removeNodeID',
            tooltip : '删除一个' + this.typeName,
            handler : this.removeNode.createDelegate(this)
        }, '-', {
            text    : '保存排序',
            iconCls : 'save',
            id		: 'saveOrderID',
            tooltip : '保存排序结果',
            disabled: !this.enableDD,
            handler : this.saveOrder.createDelegate(this)
        }, '-', {
            text    : '展开',
            iconCls : 'expand',
            id		: 'expandId',
            tooltip : '展开所有' + this.typeName,
            handler : this.expandAll.createDelegate(this)
        }, {
            text    : '合拢',
            id		: 'collapseId',
            iconCls : 'collapse',
            tooltip : '合拢所有' + this.typeName,
            handler : this.collapseAll.createDelegate(this)
        }, {
            text    : '刷新',
            id		: 'refreshId',
            iconCls : 'refresh',
            tooltip : '刷新所有' + this.typeName,
            handler : this.refresh.createDelegate(this)
        }];
        return toolbar;
    },
    
    createChild: function() {},
    
    updateNode: function() {},
    
    removeNode: function() {},
    
    saveOrder: function() {},
    
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
    		console.info(newParent)
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
   
    constructor: function() {
		this.beforePropertySet();
		this.createTree();
		this.buildDragDrop();
		
		var items = [this.tree];
		ST.ux.ViewTree.superclass.constructor.call(this, {
			layout: "border",
			items: items
		});
		this.afterPropertySet();
	},
	
	beforePropertySet: function() {
		
	},
	
	afterPropertySet: function() {
		
	}
});