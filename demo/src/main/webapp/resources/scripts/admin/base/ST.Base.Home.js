Ext.BLANK_IMAGE_URL = "./resources/scripts/ext/resources/images/default/s.gif";
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
	var panelId = "HomePanel_" + node.id;
	var d = b.getItem(panelId);
	var src = node.attributes.resource.action;
	
	if(node.attributes.resource.module=='test')
		src = "http://localhost:8080/TestFrame" + src;
	
	if (d == null) {
		var MIF = new Ext.ux.ManagedIFrame.Panel({id:panelId, defaultSrc: src,
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
	west: new Ext.ux.AccordionPanel({
		region: 'west',
        id : 'west-panel', 
        title: 'Navigator',
        split: true,
        animate: true,
        width: 200,
        minSize: 175,
        maxSize: 400,
        collapsible: true,
		plugins: [Ext.ux.PanelCollapsedTitle],
        margins: '0 0 0 2',
        items: []
	}),
	south: new Ext.Panel({
		region: "south",
		id: 'south-panel',
		border: false,
		margins: '0 0 0 2',
		bbar: [{
			text: "Logout",
			iconCls: "btn-logout",
			handler: function() {
				Home.Logout();
			}
		}, "-", {
			text: "Personal Settings",
			id: "personConfig",
			iconCls: "setting",
			handler: function() {
				ST.base.PersonConfig.showWin("personConfig");
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
	                title: 'Main',
	                closable: false,
	                iconCls: 'home',
	                autoScroll: true,
	                
                  	frame:true,  
                  	html:''  
	            }],
			plugins: new Ext.ux.TabCloseMenu()
		});
		HomePage.superclass.constructor.call(this, {
			layout: "border",
			layoutConfig:{animate:true},
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
			url: "./menu/queryMenus4User.json",
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
						expandable:true,
						autoScroll: true,
						loader: new Ext.tree.TreeLoader({
					    	url:"./menu/queryMenus4User.json"
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