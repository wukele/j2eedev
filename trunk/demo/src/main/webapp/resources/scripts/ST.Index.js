Ext.BLANK_IMAGE_URL = "./resources/scripts/ext/resources/images/default/s.gif";
Ext.namespace('Index');

Index.Logout = function() {
	Ext.MessageBox.confirm('退出系统', "您确认退出系统吗？", function(a,b,c){
		if(a == 'yes')
			window.location.href = "./j_spring_security_logout";
	});
};

var IndexPage = Ext.extend(Ext.Viewport, {
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
				Index.Logout();
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
	            }]
		});
		IndexPage.superclass.constructor.call(this, {
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
		var panel = new Ext.tree.TreePanel({
			id: 'myhomeid',
			title: 'My Home',
			layout: "fit",
			animate: true,
			border: false,
			expandable:true,
			autoScroll: true,
			loader: new Ext.tree.TreeLoader({
		    	url:"./menu/queryMenus4User.json"
		    }),
			root: new Ext.tree.AsyncTreeNode({
				id: '1',
				hidden: true
			}),
			rootVisible: false
		});
		westPanel.add(panel);
		westPanel.doLayout();
	}
});

Ext.onReady(function(){
    new IndexPage();
});