Ext.BLANK_IMAGE_URL = "./resources/scripts/ext-3.2.1/resources/images/default/s.gif";
Ext.namespace('Home');

Home.Logout = function() {
	Ext.MessageBox.confirm('退出系统', "您确认退出系统吗？", function(a,b,c){
		if(a == 'yes')
			window.location.href = "./j_spring_security_logout";
	});
};
Home.ClickNode = function(node) {
	if (!node.leaf) {
		return;
	}
	Home.ClickTopTab(node);
};
Home.ClickTopTab = function(node) {
	var b = Ext.getCmp("centerTabPanel");
	var d = b.getItem(node.id);
	if (d == null) {
		var MIF = new Ext.ux.ManagedIFrame.Panel({id:node.id, defaultSrc: node.attributes.resource.action,
			loadMask:true, title: node.attributes.text});
		d = b.add(MIF);
		b.activate(d);
	} else {
		b.activate(d);
	}
};

var HomePage = Ext.extend(Ext.Viewport, {
	north: new Ext.Panel({
        region: 'north',
        height: 32,
        border: false,
        frame: true,
        contentEl: "app-header",
        id: "north-Panel"
    }),
	center: null,
	west: new Ext.Panel({
		region: 'west',
        id : 'west-panel', 
        title: '导航菜单',
        split: true,
        width: 200,
        minSize: 175,
        maxSize: 400,
        collapsible: true,
        margins: '0 0 0 2',
        layout: {
            type: 'accordion',
            animate: true
        },
        items: []
	}),
	south: new Ext.Panel({
		region: "south",
		id: 'south-panel',
		border: false,
		margins: '0 0 0 2',
		bbar: [{
			text: "退出系统",
			iconCls: "btn-logout",
			handler: function() {
				Home.Logout();
			}
		}, "-", {
			text: "在线用户",
			iconCls: "btn-onlineUser",
			handler: function() {
				OnlineUserSelector.getView().show();
			}
		}, "->", {
			xtype: "tbfill"
		}, {
			xtype: "tbseparator"
		}, new Ext.Toolbar.TextItem('技术支持 <a href=http://www.starit.com.cn target="_blank">苏州科大恒星信息技术有限公司</a>'), {
			xtype: "tbseparator"
		},{
			pressed: false,
			text: "与我们联系",
			handler: function() {
				Ext.ux.Toast.msg("联系我们", " 电话：0551-5396784<br/>网址：http://www.starit.com.cn");
			}
		}, "-", {
			text: "收展",
			iconCls: "btn-expand",
			handler: function() {
				var a = Ext.getCmp("north-Panel");
				if (a.collapsed) {
					a.expand(true);
				} else {
					a.collapse(true);
				}
			}
		}, "-", {
			xtype: "combo",
			mode: "local",
			editable: false,
			value: "切换皮肤",
			width: 100,
			triggerAction: "all",
			store: [["ext-all", "缺省浅蓝"]],
			listeners: {
				scope: this,
				"select": function(d, b, c) {
					if (d.value != "") {
						var a = new Date();
						a.setDate(a.getDate() + 300);
						setCookie("theme", d.value, a, __ctxPath);
						Ext.util.CSS.swapStyleSheet("theme", __ctxPath + "/ext3/resources/css/" + d.value + ".css");
					}
				}
			}
		}]
	}),
	constructor: function() {
		this.center = new Ext.TabPanel({
			id: "centerTabPanel",
			region: "center",
			deferredRender: true,
			enableTabScroll: true,
			activeTab: 0,
			defaults: {
				autoScroll: true,
				closable: true
			},
			items: [{
	                contentEl: 'home-panel',
	                title: '主页',
	                closable: false,
	                iconCls: 'home',
	                autoScroll: true
	            }],
			plugins: new Ext.ux.TabCloseMenu()
		});
		HomePage.superclass.constructor.call(this, {
			layout: "border",
			items: [this.north, this.west, this.center, this.south]
		});
		this.afterPropertySet();
		this.loadWestMenu();
	},
	afterPropertySet: function() {
		//
	},
	loadWestMenu: function() {
		var westPanel = Ext.getCmp("west-panel");
		Ext.Ajax.request({
			url: "./menu/findMenus4User.json",
			success: function(response, options) {
				var arr = eval(response.responseText);
				for (var i = 0; i < arr.length; i++) {
					var root = arr[i];
					var panel = new Ext.tree.TreePanel({
						id: root.id,
						title: root.text,
						iconCls: root.iconCls,
						layout: "fit",
						animate: true,
						border: false,
						autoScroll: true,
						loader: new Ext.tree.TreeLoader({
					    	url:"./menu/findMenus4User.json"
					    }),
						root: new Ext.tree.AsyncTreeNode({
							id: root.id,
							hidden: true
						}),
						listeners: {
							"click": Home.ClickNode
						},
						rootVisible: false
					});
					westPanel.add(panel);
					panel.on("expand", function(p) {
						//alert();
					});
				}
				westPanel.doLayout();
			}
		});
	}
});

Ext.onReady(function(){
    new HomePage();
    Ext.select("#south-panel .x-panel-body").hide();
});