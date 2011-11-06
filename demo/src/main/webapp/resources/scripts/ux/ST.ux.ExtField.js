Ext.namespace("ST.ux.ExtField");
Ext.BLANK_AVATER_URL = "./../resources/images/icons/default_icon.png";
ST.ux.ExtField.ClearableComboBox = Ext.extend(Ext.form.ComboBox, {
    initComponent: function() {
        this.triggerConfig = {
            tag:'span', cls:'x-form-twin-triggers', cn:[
            {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger x-form-clear-trigger"},
            {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger"}        
        ]};
        ST.ux.ExtField.ClearableComboBox.superclass.initComponent.call(this);
    },
    onTrigger1Click : function()
    {
        this.collapse();
        this.reset();                       // clear contents of combobox
        this.fireEvent('cleared');          // send notification that contents have been cleared
    },

    getTrigger: Ext.form.TwinTriggerField.prototype.getTrigger,
    initTrigger: Ext.form.TwinTriggerField.prototype.initTrigger,
    onTrigger2Click: Ext.form.ComboBox.prototype.onTriggerClick,
    trigger1Class: Ext.form.ComboBox.prototype.triggerClass,
    trigger2Class: Ext.form.ComboBox.prototype.triggerClass
});
Ext.reg('clearcombo', ST.ux.ExtField.ClearableComboBox);

ST.ux.ExtField.ComboBox = Ext.extend(ST.ux.ExtField.ClearableComboBox, {
    store : new Ext.data.JsonStore({  //填充的数据
    	url : "./../dict/queryDictionarys.json",
    	fields : new Ext.data.Record.create( ['code', 'name'])
 	}),
    allowBlank: false,
    editable : false,
    valueField: 'code',
    displayField:'name',
    typeAhead: true,
    forceSelection: true,
    mode:'local', 
    triggerAction: 'all',
    emptyText:'请选择...',
    selectOnFocus:true,
    autoLoad:true,
    listeners: {
		'beforequery' : function(e) {
			e.combo.store.baseParams.dictTypeId = e.combo.dictTypeId;
    	},	
    	'afterrender' : function(combo) {
			if(combo.autoLoad) {
				combo.store.baseParams.dictTypeId = combo.dictTypeId;
				combo.store.load();
			} else {
				combo.mode="remote";
			}
    	}
    }
});

//预览
ST.ux.DisplayIconField = Ext.extend(Ext.BoxComponent,{
	height : 100,
	boxMaxWidth: 125,
	autoEl : {
	    tag : 'img',
	    src : Ext.BLANK_AVATER_URL,  
	    style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);'
}});
Ext.reg('slotfield', ST.ux.DisplayIconField);

//截图上传
ST.ux.ScreenCutUploadField = Ext.extend(Ext.form.Field,{
	inputType :'file',
	listeners : {
		'render':function(){
			var img_reg = /\.([jJ][pP][gG]){1}$|\.([jJ][pP][eE][gG]){1}$|\.([gG][iI][fF]){1}$|\.([pP][nN][gG]){1}$|\.([bB][mM][pP]){1}$/;
			this.on('change',function(field,newValue,oldValue){
			         var picPath = this.getValue();
			         var url = 'file:///' + picPath;
			         if(img_reg.test(url)){  //格式验证
			        	  if(Ext.isIE){
                              var image = Ext.get(this.id_slot).dom;  
    				          //image.src = Ext.BLANK_AVATER_URL;
    				          image.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = url; 
    			         }else{
    				          //仅支持ff7.0+
    			        	  Ext.get(this.id_slot).dom.src = window.URL.createObjectURL(this.getEl().dom.files.item(0));
    			         }
			         }else{
			        	 Ext.MessageBox.alert('提示','格式不正确，请重新选择图片');
			        	 this.reset(); //reset 
			         }
	        },this);
         }
	}
//	onRender : function(ct, position){
//		ST.ux.ScreenCutUploadField.superclass.onRender.call(this, ct, position);
//		 this.wrap = this.el.wrap();
//		 //this.el.setWidth(2);
//	}
});
Ext.reg('snapfield', ST.ux.ScreenCutUploadField);


