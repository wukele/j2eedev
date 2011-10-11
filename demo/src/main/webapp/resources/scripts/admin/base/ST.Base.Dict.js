Ext.BLANK_IMAGE_URL = "./../resources/scripts/ext/resources/images/default/s.gif";

Ext.namespace("ST.base");

//create the combo instance
ST.base.MyCombo = new Ext.extend(Ext.form.ComboBox, {
    hiddenName: 'enabled',
    typeAhead: true,
    triggerAction: 'all',
    emptyText:'请选择...',
    lazyRender:true,
    mode: 'local',
    store: new Ext.data.ArrayStore({
        id: 0,
        fields: [
            'type',
            'displayText'
        ],
        data: [['Y', '有效'], ['N', '无效']]
    }),
    valueField: 'type',
    displayField: 'displayText'
});

Ext.reg('comboExEn', ST.base.MyCombo);
ST.base.dictView = Ext.extend(ST.ux.ViewGrid, {
	dlgWidth: 360,
	dlgHeight: 260,
	//资源列表查询URL
	urlGridQuery: './../dict/pageQueryDictBusinTypes.json',
	urlAdd: './../dict/insertDictBusinType.json',
	urlEdit: './../dict/updateDictBusinType.json',
	urlLoadData: './../dict/loadDictBusinType.json',
	urlRemove: './../dict/deleteDictBusinType.json',
	
	urlAddDict: './../dict/insertDictionary.json',
	urlEditDict: "./../dict/updateDictionary.json",
	urlRemoveDict: "./../dict/deleteDictionarys.json",
	urlLoadDict: "./../dict/loadDictionary.json",
	
	urlEastGridQuery: './../dict/pageQueryDict.json',
	addTitle: "增加字典类型",
    editTitle: "更新字典类型",
    gridTitle: "业务字典数据",
    dialogLabelWidth: 80,
    displayEast: true,
    clickType: 'rowclick',
	girdColumns: [
				{header: 'ID', width: 150, dataIndex: 'id', hideGrid: true, hideForm: 'add', readOnly: true},
	            {header: '字典类型名称', width: 100, sortable: true, dataIndex: 'name', allowBlank:false},
	            {header: '字典类型编码', width: 150, sortable: true, dataIndex: 'code', allowBlank:false},
	            {id:'descn',header: '描述', width: 250, dataIndex: 'descn', fieldtype:'textarea'}
	        ],
	
	queryFormItms: [{ 
				layout: 'tableform',
	            layoutConfig: {
	           		columns: 2,
	            	columnWidths: [0.5, 0.5]
	            },           
		        items:[{xtype:'textfield', fieldLabel: '字典类型名称', name: 'name', id: 'name', anchor:'70%' }] 
		    }],
		    
		    
    eastWidth: 400,
	eastGridTitle: '字典类型对应的字典项',
	urlEastGridQuery: './../dict/pageQueryDict.json',
	eastGridColumn: [{header: "ID", width: 120, dataIndex: 'id',name: 'id',hideGrid: true, hideForm: 'add', readOnly: true},
	        {header: "字典项名称", width: 120, dataIndex: 'name',name: 'name'},
            {header: "字典项编码", width: 125, dataIndex: 'code',name: 'code'},
			{header: "索引顺序", width: 125, dataIndex: 'orderIndex',name: 'orderIndex',allowBlank:false, sortable: true,regex:/[0-9]+/,regexText:"请输入数字"},
			{header: 'DICT_TYPE_ID', width: 150, name: 'businType.id', hideGrid: true,fieldtype:'hidden',id: 'businType.id'},
			{header: "是否有效", width: 125, dataIndex: 'enabled',name: 'enabled',allowBlank:false,renderer: "renderEnabled", fieldtype:'comboExEn'}],			        
	
	renderEnabled: function(value, p, record) {
    	if(value == 'Y')
    		return "有效";
    	else if(value == 'N')
    		return "无效";
    	else
    		return "有效";
    },
			
	setDictDefaultValue: function(id){
    	Ext.getCmp("businType.id").setRawValue(this.rid);
    },
			
            
 // 初始化ColumnModel
    buildColumnModelDict: function() {
        this.sm = new Ext.grid.CheckboxSelectionModel();
        this.columnHeaders = [];
        if(this.rowExpander != null)
        	this.columnHeaders.push(this.rowExpander);
        this.columnHeaders.push(new Ext.grid.RowNumberer());
        this.columnHeaders.push(this.sm);

        for (var i = 0; i < this.eastGridColumn.length; i++) {
            var col = this.eastGridColumn[i];
            if (col.hideGrid === true) {
                continue;
            }
            col.renderer = this[col.renderer];
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
    
    buildStoreDict: function() {
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
    	this.buildColumnModelDict();
    	this.buildStoreDict();
    	var paging = new Ext.PagingToolbar({
            pageSize: 20,
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
	        columns: this.columnHeaders,
	        stripeRows: true,
	        loadMask:"正在加载......",
	        tbar: this.buildEastToolbar(),
	        bbar: paging,
	        sm: this.sm,
	        title: this.eastGridTitle,
	        scope:this
	    });
    	
    	return this.egrid;
    },
    
    
    buildDictItems: function(flag) {
    	var items = [];
        for (var i = 0; i < this.eastGridColumn.length; i++) {
            var col = this.eastGridColumn[i];
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
    buildAddDictDialog : function() {
    	this.flag = "add";
        this.addDictFormPanel = new Ext.form.FormPanel({
            defaultType: 'textfield',
            labelAlign: 'right',
            labelWidth: this.dialogLabelWidth,
            frame: true,
            id: "addDictFormPanelID",
            autoScroll: true,
            buttonAlign: 'center',
            url: this.urlAddDict,
            items: this.buildDictItems("add"),
            fileUpload: this.isFileUpload,
            scope: this,
            buttons: [{
                text: '确定',
                scope: this,
                handler: function() {
                    if (this.addDictFormPanel.getForm().isValid()) {
                    	this.setDictDefaultValue();
                        this.addDictFormPanel.getForm().submit({
                        	waitMsg : '正在处理，请稍等...',
                            success: function(a, b) {
                                this.addDictDialog.close();
                                this.egrid.store.reload();
                            },
                            failure: function(a, b) {
								Ext.MessageBox.alert("提示", "操作失败");
                            },
                            scope: this
                        });
                    }
                }
            },{
                text: '取消',
                scope: this,
                handler: function() {
			    	this.addDictDialog.close();
                }
            }]
        });
        
        this.addDictDialog = new Ext.Window({
            title: "新增字典项",
            modal: true,
            width: "300",
            height: "300",
            closeAction: 'close',
            items: [this.addDictFormPanel]
        });
    },
    
    // 创建弹出式对话框
    buildEditDictDialog : function() {
    	this.flag = "edit";
    	var items = this.buildDictItems("edit");
    	var reader = new Ext.data.JsonReader({}, items);
        this.editDictFormPanel = new Ext.form.FormPanel({
            defaultType: 'textfield',
            labelAlign: 'right',
            labelWidth: 70,
            frame: true,
            id: "editDictFormPanelID",
            autoScroll: true,
            buttonAlign: 'center',
            url: this.urlEditDict,
            loadMask : true,   
            reader: reader,
            items: items,
            fileUpload: this.isFileUpload,
            scope: this,
            buttons: [{
                text: '确定',
                scope: this,
                handler: function() {
                    if (this.editDictFormPanel.getForm().isValid()) {
                    	this.setDictDefaultValue();
                        this.editDictFormPanel.getForm().submit({
                        	waitMsg : '正在处理，请稍等...',
                            success: function() {
                                this.editDictDialog.close();
                                this.egrid.store.reload();
                            },
                            failure: function(a, b) {
                            	Ext.MessageBox.alert("提示", "操作失败");
                            },
                            scope: this
                        });
                    }
                }
            },{
                text: '取消',
                scope: this,
                handler: function() {
			    	this.editDictDialog.close();
                }
            }]
        });
        
        this.editDictDialog = new Ext.Window({
            title: "修改字典项",
            modal: true,
            width: "300",
            height: "300",
            closeAction: 'close',
            items: [this.editDictFormPanel]
        });
    },
    
    delDictData: function() {
        if (this.checkManyDict()) {
            Ext.Msg.confirm("提示", "是否确定？", function(btn, text) {
                if (btn == 'yes') {
                    this.egrid.body.mask('正在处理，请稍等...', 'x-mask-loading');
                    var aList = [];
                	for(var i=0,length=this.egrid.getSelectionModel().selections.length;i<length;i++){
                		aList.push(this.egrid.getSelectionModel().selections.items[i].id); 
                	}
                    Ext.Ajax.request({
                        url     : this.urlRemoveDict,
                        params  : {ids : aList},
                        success : function() {
                            this.egrid.body.unmask();
                            Ext.MessageBox.alert('提示', '操作成功！');
                            this.egrid.store.reload();
                        },
                        failure : function(){
                        	this.egrid.body.unmask();
                        },
                        scope   : this
                    });
                }
            }.createDelegate(this));
        }
    },
    
    
    // 检测至少选择一个
    checkOneDict: function() {
        var selections = this.egrid.getSelectionModel().selections;
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
    checkManyDict: function() {
        var selections = this.egrid.getSelectionModel().selections;
        if (selections.length == 0) {
            Ext.MessageBox.alert("提示", "请至少选择一条的记录！");
            return false;
        }
        return true;
    },
    
    /**
     * Center 区域 Grid的行click事件调用方法
     */
    rid: '',
    rowclickFn: function(grid, rowIndex, e) {
    	this.rid = grid.getStore().getAt(rowIndex).data.id;
		this.east.store.load({
            params:{dictTypeId: this.rid,start: 0, limit:20}
        });
    },
    
    buildEastToolbar: function(){
    	var index = 1;
		var oprToolbar = new Ext.Toolbar();
		oprToolbar.insertButton(index++, new Ext.Button({
			text : "添加",
			iconCls : 'add',
			id : 'addDict'
		}));
		oprToolbar.insertButton(index++, new Ext.Button({
			text : "更新",
			iconCls : 'edit',
			id : 'editDict'
		}));
		oprToolbar.insertButton(index++, new Ext.Button({
			text : "删除",
			iconCls : 'delete',
			id : 'delDict'
		}));
		return oprToolbar;
    },
    
	constructor: function() {
		ST.base.dictView.superclass.constructor.call(this, {});
		this.buildEastToolbar();
		
		Ext.get("addDict").on('click', function(){
        	this.buildAddDictDialog();
	    	this.addDictDialog.show();
        }, this);
		
		Ext.get("editDict").on('click', function(){
        	this.buildEditDictDialog();
        	if (this.checkOneDict()) {
	    	this.editDictDialog.show();
	    	this.editDictFormPanel.load({waitMsg : '正在载入数据...', url: this.urlLoadDict, params : {id: this.egrid.getSelectionModel().selections.items[0].id}});
        	}
        }, this);
		
		Ext.get("delDict").on("click", function(){
        	this.delDictData();
        }, this);
		
		this.egrid.store.on('beforeload', function(thiz,options) { 
			Ext.apply(thiz.baseParams, {dictTypeId: this.rid}); 
		}, this);
	}
});

