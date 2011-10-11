Ext.namespace("ST.ux");
Ext.QuickTips.init();

ST.ux.PlainGrid = Ext.extend(Ext.grid.GridPanel, {
    loadMask: true,
    urlPagedQuery: "./pagedQuery.do",
    pageSize: 10,
    stripeRows: true,
    bodyStyle:'width:100%',
	autoWidth:true,
	autoExpandColumn: 5,
    params: {},
    urlBind: "/",
    urlUnBind: "/",
    region: '',
    isDisBut: true,
    btnText: '',
    buildButtons: function() {
    	var index = 11;
        this.getBottomToolbar().insertButton(index++,'-');
	    this.getBottomToolbar().insertButton(index++,new Ext.Button({text:this.btnText,iconCls: 'bind', id:'bindEntityId'}));
	    this.getBottomToolbar().insertButton(index++,new Ext.Button({text:"取消"+this.btnText,iconCls: 'unbind', id:'unBindEntityId'}));
    },
    
    registerHandler: function() {
    	var btn = Ext.getCmp("bindEntityId");
        btn.on("click", function(){
        	this.handler(this.urlBind);
        }, this);
        
        btn = Ext.getCmp("unBindEntityId");
        btn.on("click", function(){
        	this.handler(this.urlUnBind);
        }, this);
    },
    
    handler: function(url) {
    	if(this.checkOne()) {
    		this.body.mask('正在处理，请稍等...', 'x-mask-loading');
            Ext.Ajax.request({
                url     : url,
                params  : this.getIds(),
                success : function() {
                    this.body.unmask();
                    Ext.MessageBox.alert('提示', '操作成功！');
                    this.store.reload();
                },
                failure : function(){
                	this.body.unmask();
                    Ext.MessageBox.alert('提示', '操作失败！');
                },
                scope   : this
            });
    	}
    },
    
    getIds: function() {
    	var items = this.getSelectionModel().selections.items;
    	var ids = [];
    	for(var i=0, len=items.length; i<len; i++) {
    		ids.push(items[i].id);
    	}
    	var p = {};
    	for(var k in this.params) {
    		p[k] = this.params[k];
    	}
    	p["ids"] = ids.join(",");
    	return p;
    },
    
    unBindEntity: function() {
    	if(checkOne()) {
    		
    	}
    },

    // 检测至少选择一个
    checkOne: function() {
        var selections = this.getSelectionModel().selections;
        if (selections.length == 0) {
            Ext.MessageBox.alert("提示", "请选择一条的记录！");
            return false;
        }
        return true;
    },
    
    // 初始化
    initComponent: function() {
        this.buildColumnModel();
        this.buildRecord();
        this.buildDataStore();
        this.buildToolbar();

        ST.ux.PlainGrid.superclass.initComponent.call(this);
        
        if(this.isDisBut)
        	this.buildButtons();
        this.registerHandler();
    },

    // 初始化ColumnModel
    buildColumnModel: function() {
        this.sm = new Ext.grid.CheckboxSelectionModel();
        var columnHeaders = new Array();
        columnHeaders[0] = new Ext.grid.RowNumberer();
        columnHeaders[1] = this.sm;

        for (var i = 0; i < this.columConfig.length; i++) {
            var col = this.columConfig[i];
            col.dataIndex = col.name;
            columnHeaders.push(col);
        }
        this.cm = new Ext.grid.ColumnModel(columnHeaders);
        this.cm.defaultSortable = true;
        this.columnModel = this.cm;
    },

    buildRecord: function() {
        this.dataRecord = Ext.data.Record.create(this.columConfig);
    },

    buildDataStore: function() {
        this.store = new Ext.data.Store({
        	baseParams: this.params,
            proxy  : new Ext.data.HttpProxy({url:this.urlPagedQuery}),
            reader : new Ext.data.JsonReader({
                root          : "result",
                totalProperty : "totalCount",
                id            : "id"
            }, this.dataRecord),
            remoteSort : true
        });
        // this.store.setDefaultSort("id", "DESC");
    },

    buildToolbar: function() {
        // 把分页工具条，放在页脚
        var paging = new Ext.PagingToolbar({
            pageSize: this.pageSize,
            store: this.store,
            displayInfo: true,
            displayMsg: '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
            emptyMsg: "没有记录",
            plugins: [new Ext.ux.ProgressBarPager()]
        });

        this.store.load({
            params:{start:0, limit:paging.pageSize}
        });
        this.bbar = paging;
    }
});