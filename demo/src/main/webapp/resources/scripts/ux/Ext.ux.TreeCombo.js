Ext.ux.TreeCombo = Ext.extend(Ext.form.TriggerField, {

    triggerClass: 'x-form-tree-trigger',
    

    initComponent : function(){
		Ext.applyIf(this, {
			rootVisible: true,
			url : './list.json',
			hiddenName : 'hiddenId',
			rootName : 'Root',
			code : ''
	    });
		
		new Ext.form.TextField({id:this.hiddenName});
		
        this.editable = false;
        Ext.ux.TreeCombo.superclass.initComponent.call(this);
        this.on('specialkey', function(f, e){
            if(e.getKey() == e.ENTER){
                this.onTriggerClick();
            }
        }, this);
        this.getTree();
    },
    
    onRender : function(ct, position) {
    	Ext.ux.TreeCombo.superclass.onRender.call(this, ct, position);
    },

    onTriggerClick: function() {
        this.getTree().show();
        this.getTree().getEl().alignTo(this.wrap, 'tl-bl?');
    },

    getTree: function() {
        if (!this.treePanel) {
            if (!this.treeWidth) {
                this.treeWidth = Math.max(200, this.width || 200);
            }
            if (!this.treeHeight) {
                this.treeHeight = 200;
            }

            this.treePanel = new Ext.tree.TreePanel({
                renderTo: Ext.getBody(),
                loader: new Ext.tree.TreeLoader({
                    url:this.url,
                    baseParams: {code: this.code},
                    listeners:{
                        "beforeload":function(treeLoader,node) {
                            treeLoader.baseParams.id=node.id;
                        }
                    }
        	    }),
                root: new Ext.tree.AsyncTreeNode({
                    text : this.rootName,
                    id   : '0'
                }),
                rootVisible: (typeof this.rootVisible != 'undefined') ? this.rootVisible : true,
                floating: true,
                autoScroll: true,
                minWidth: 200,
                minHeight: 200,
                width: this.treeWidth,
                height: this.treeHeight,
                listeners: {
                    hide: this.onTreeHide,
                    show: this.onTreeShow,
                    click: this.onTreeNodeClick,
                    expandnode: this.onExpandOrCollapseNode,
                    collapsenode: this.onExpandOrCollapseNode,
                    resize: this.onTreeResize,
                    scope: this
                }
            });
            this.treePanel.render();
            this.treePanel.getRootNode().expand();
        }
        return this.treePanel;
    },

    onExpandOrCollapseNode: function() {
        if (!this.maxHeight || this.resizable)
            return;  // -----------------------------> RETURN
        var treeEl = this.treePanel.getTreeEl();
        var heightPadding = treeEl.getHeight() - treeEl.dom.clientHeight;
        var ulEl = treeEl.child('ul');  // Get the underlying tree element
        var heightRequired = ulEl.getHeight() + heightPadding;
        if (heightRequired > this.maxHeight)
            heightRequired = this.maxHeight;
        this.treePanel.setHeight(heightRequired);
    },

    onTreeResize: function() {
        if (this.treePanel)
            this.treePanel.getEl().alignTo(this.wrap, 'tl-bl?');
    },

    onTreeShow: function() {
        Ext.getDoc().on('mousewheel', this.collapseIf, this);
        Ext.getDoc().on('mousedown', this.collapseIf, this);
    },

    onTreeHide: function() {
        Ext.getDoc().un('mousewheel', this.collapseIf, this);
        Ext.getDoc().un('mousedown', this.collapseIf, this);
    },

    collapseIf : function(e){
        if(!e.within(this.wrap) && !e.within(this.getTree().getEl())){
            this.collapse();
        }
    },

    collapse: function() {
        this.getTree().hide();
        if (this.resizer)
            this.resizer.resizeTo(this.treeWidth, this.treeHeight);
    },

    // private
    validateBlur : function(){
        return !this.treePanel || !this.treePanel.isVisible();
    },

    setValue: function(v) {
        this.setRawValue(v);
    },
    
    getValue: function() {
        return this.value;
    },
    
    reset : function(){
        Ext.ux.TreeCombo.superclass.reset.call(this);
        this.value = "";
    },

    onTreeNodeClick: function(node, e) {
        this.setRawValue(node.text);
        this.value = node.id;
        Ext.getCmp(this.hiddenName).setValue(node.id);
        this.fireEvent('select', this, node);
        this.collapse();
    }
});
