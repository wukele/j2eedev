Ext.BLANK_IMAGE_URL = "./resources/scripts/ext/resources/images/default/s.gif";
Ext.namespace('Home');

Home.Logout = function() {
	Ext.MessageBox.confirm('退出系统', "您确认退出系统吗？", function(a,b,c){
		if(a == 'yes')
			window.location.href = "./user/logout";
	});
};

Home.ClickTopTab = function(node) {
	var b = Ext.getCmp("centerTabPanel");
	var panelId = "HomePanel_" + node.id;
	var d = b.getItem(panelId);
	var src = node.attributes.action;
	
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
	west: new Ext.tree.TreePanel({
		title: "导航菜单",
		layout: "fit",
		region: "west",
		split: true,
        animate: true,
        width: 200,
        minSize: 175,
        maxSize: 400,
        collapsible: true,
		border: false,
		expandable:true,
		margins: '0 0 0 2',
		autoScroll: true,
		root: new Ext.tree.AsyncTreeNode({
            children : [{
                text : "APP管理",  
                action: "./application/index",
                leaf : true
            },{  
                text : "公告管理",  
                action: "./notice/index",
                leaf : true 
            },{  
                text : "推荐管理",  
                action: "./recommend/index",
                leaf : true 
            },{  
                text : "评论管理",  
                action: "./comment/index",
                leaf : true 
            },{  
                text : "用户管理",  
                action: "./user/index",
                leaf : true 
            },{  
                text : "静态数据管理",  
                action: "./dict/index",
                leaf : true 
            }]  
        }),  
		listeners: {
			"click": Home.ClickTopTab
		},
		rootVisible: false
	}),
	south: new Ext.Panel({
		region: "south",
		id: 'south-panel',
		border: false,
		margins: '0 0 0 2',
		bbar: [{
			text: "退出",
			iconCls: "btn-logout",
			handler: function() {
				Home.Logout();
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
	}
});

Ext.onReady(function(){
    new HomePage();
    Ext.select("#south-panel .x-panel-body").hide();
});