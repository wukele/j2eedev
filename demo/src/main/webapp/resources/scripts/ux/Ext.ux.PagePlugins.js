/*!
 * Ext JS Library 3.0.0
 * Copyright(c) 2006-2009 Ext JS, LLC
 * licensing@extjs.com
 * http://www.extjs.com/license
 */
/**
* @class Ext.ux.ProgressBarPager
* @extends Object 
* Plugin (ptype = 'tabclosemenu') for displaying a progressbar inside of a paging toolbar instead of plain text
* 
* @ptype progressbarpager 
* @constructor
* Create a new ItemSelector
* @param {Object} config Configuration options
* @xtype itemselector 
*/
Ext.ux.ProgressBarPager  = Ext.extend(Object, {
	/**
 	* @cfg {Integer} progBarWidth
 	* <p>The default progress bar width.  Default is 225.</p>
	*/
	progBarWidth   : 225,
	/**
 	* @cfg {String} defaultText
	* <p>The text to display while the store is loading.  Default is 'Loading...'</p>
 	*/
	defaultText    : "正在加载数据...",
    	/**
 	* @cfg {Object} defaultAnimCfg 
 	* <p>A {@link Ext.Fx Ext.Fx} configuration object.  Default is  { duration : 1, easing : 'bounceOut' }.</p>
 	*/
	defaultAnimCfg : {
		duration   : 1,
		easing     : 'bounceOut'	
	},												  
	constructor : function(config) {
		if (config) {
			Ext.apply(this, config);
		}
	},
	//public
	init : function (parent) {
		
		if(parent.displayInfo){
			this.parent = parent;
			var ind  = parent.items.indexOf(parent.displayItem);
			parent.remove(parent.displayItem, true);
			this.progressBar = new Ext.ProgressBar({
				text    : this.defaultText,
				width   : this.progBarWidth,
				animate :  this.defaultAnimCfg
			});					
		   
			parent.displayItem = this.progressBar;
			
			parent.add(parent.displayItem);	
			parent.doLayout();
			Ext.apply(parent, this.parentOverrides);		
			
			this.progressBar.on('render', function(pb) {
				pb.el.applyStyles('cursor:pointer');

				pb.el.on('click', this.handleProgressBarClick, this);
			}, this);
			
		
			// Remove the click handler from the 
			this.progressBar.on({
				scope         : this,
				beforeDestroy : function() {
					this.progressBar.el.un('click', this.handleProgressBarClick, this);	
				}
			});	
						
		}
		  
	},
	// private
	// This method handles the click for the progress bar
	handleProgressBarClick : function(e){
		var parent = this.parent;
		var displayItem = parent.displayItem;
		
		var box = this.progressBar.getBox();
		var xy = e.getXY();
		var position = xy[0]-box.x;
		var pages = Math.ceil(parent.store.getTotalCount()/parent.pageSize);
		
		var newpage = Math.ceil(position/(displayItem.width/pages));
		parent.changePage(newpage);
	},
	
	// private, overriddes
	parentOverrides  : {
		// private
		// This method updates the information via the progress bar.
		updateInfo : function(){
			if(this.displayItem){
				var count   = this.store.getCount();
				var pgData  = this.getPageData();
				var pageNum = this.readPage(pgData);
				
				var msg    = count == 0 ?
					this.emptyMsg :
					String.format(
						this.displayMsg,
						this.cursor+1, this.cursor+count, this.store.getTotalCount()
					);
					
				pageNum = pgData.activePage; ;	
				
				var pct	= pageNum / pgData.pages;	
				
				this.displayItem.updateProgress(pct, msg, this.animate || this.defaultAnimConfig);
			}
		}
	}
});
Ext.preg('progressbarpager', Ext.ux.ProgressBarPager);

//--------------------------------------------------------------------------------
Ext.ux.PageSizePlugin = function() {
    Ext.ux.PageSizePlugin.superclass.constructor.call(this, {
        store: new Ext.data.SimpleStore({
            fields: ['text', 'value'],
            data: [['10', 10], ['20', 20], ['30', 30], ['50', 50], ['100', 100]]
        }),
        mode: 'local',
        displayField: 'text',
        valueField: 'value',
        editable: false,
        allowBlank: false,
        triggerAction: 'all',
        width: 40
    });
};

Ext.extend(Ext.ux.PageSizePlugin, Ext.form.ComboBox, {
    init: function(paging) {
        paging.on('render', this.onInitView, this);
    },

    onInitView: function(paging) {
        paging.add('-',
            this,
            '-'
        );
        this.setValue(paging.pageSize);
        this.on('select', this.onPageSizeChanged, paging);
    },

    onPageSizeChanged: function(combo) {
        this.pageSize = parseInt(combo.getValue());
        this.doLoad(0);
    }
});

