Ext.BLANK_IMAGE_URL = "./../resources/scripts/ext/resources/images/default/s.gif";
Ext.BLANK_AVATER_URL = "./../resources/images/icons/default_icon.png";
Ext.namespace("ST.base");
Ext.reg('appStateField', ST.ux.ExtField.ComboBox);

//渲染色调
function colorfunc(value, p, record){
	return value == '未发布'? String.format("<b><font color=green>未发布</font></b>"):String.format("<b><font color=red>已发布</font></b>");
}

//获取ICON信息
function appIconfunc(value, p, record){
	return  '<img src="..'+record.data.iconUrl+'" width="100" height="50">';
}

//下载次数++
function Ajax4Accumulator(appId){
	 Ext.Ajax.request({
			url : 'updateAppDownTimes.json',
			success : function(response,options){
				Ext.getCmp('id_app_Grid').store.reload();
			},
			failure : function(response,options){
				console.info('error msg:',response);
			},
			params:{appId:appId}
		});	 
}

//下载
function dlClientfunc(value, p, record){
	if(record.data.dlClient=='' || record.data.appState==2){
		return '<b>占未发布</b>';
	}else{  //setTimeout() 解决session并发问题,这里需要延迟发送请求
		return '<a href="..'+record.data.dlClient+'" onclick="setTimeout(function(){Ajax4Accumulator('+record.data.id+')},1000)"><b><font color=#00ff00 size=3>点此下载</font></b></a>';
	}
}
ST.base.applicationView = Ext.extend(ST.ux.ViewGrid, {
	id : 'id_app_Grid',
	dlgWidth: 672,
	dlgHeight: 400,
	//资源列表查询URL
	urlGridQuery: 'pageQueryApplications.json',
	urlAdd  : 'insertApplication.json',
	urlEdit : 'updateApplication.json',
	urlLoadData: 'loadApplication.json',
	urlRemove: 'deleteApplication.json',
	addTitle : "增加应用",
    editTitle: "更新应用",
    gridTitle: "App应用数据",
    formTitle: "应用查询",
    isFileUpload: true,   // 开启form upload
    displayHeader : false,// 隐藏grid title
    enablebbar : false,   // 激活top bar
    autoExpandColumn: 2,
	girdColumns: [
				{header: 'ID', width: 60, dataIndex: 'id', hideGrid: true, hideForm: 'add', readOnly: true ,hidden : true, colspan:2},
	            {header: '应用名称', width: 110, dataIndex: 'appName', allowBlank:false , name:'appName'},
	            {header: "ICON图标", width: 140, dataIndex: 'iconUrl', hideForm:'all',renderer: appIconfunc }, 
	            {header: '应用分类', width: 120, dataIndex: 'typeId' , hiddenName: 'typeId', allowBlank:false, fieldtype:'appStateField', hideGrid:true,name:'typeId',
	                valueField  :'id',
	                displayField:'typeName',
	                mode  :'remote', 
	                store : new Ext.data.JsonStore({
	                	url : "./../apptype/queryAllAppType.json",
	                	fields : new Ext.data.Record.create( ['id', 'typeName'])
	             	}),
	             	listeners: {}
	            },
	            {header: '应用分类', width: 120,  dataIndex: 'typeId_Name', hideForm: 'all'},
	            {header: '发布状态', width: 120,  dataIndex: 'appState' ,hideGrid: true, hiddenName: 'appState',dictTypeId: '10009', allowBlank:false, fieldtype:'appStateField',name:'appState'},
	            {header: '发布状态', width: 120,  dataIndex: 'appState_Name', hideForm: 'all',renderer:colorfunc},
	            {header: '下载次数', width: 120,  dataIndex: 'downTimes', hideForm: 'all'},
	            {header: '创建时间', width: 120,  dataIndex: 'createTime',  hideForm:"all",allowBlank:false/**,renderer: Ext.util.Format.dateRenderer('m/d/Y')*/},	    
	            {header: "客户端下载", width: 140,dataIndex: 'dlClient', hideForm:'all',renderer: dlClientfunc}, 
	            {header: '关键字',     width: 120,  dataIndex: 'keyWords', hideGrid: true ,allowBlank:false , name:'keyWords'},
	            {header: '最低版本',width: 120,  dataIndex: 'minSdkVer', name:'minSdkVer'},
	            {header: '作者',  width: 120,   dataIndex: 'authorName',name:'authorName'},
	            //icon
	            {header: '图标上传', width:120, hideGrid: true, id:'id_app_icon', inputType:'file',width:100,allowBlank:false, name:'fIcon',
	            	listeners : {
	            		'render':function(){
	            			var img_reg = /\.([jJ][pP][gG]){1}$|\.([jJ][pP][eE][gG]){1}$|\.([gG][iI][fF]){1}$|\.([pP][nN][gG]){1}$|\.([bB][mM][pP]){1}$/;
	            			var iconFileCmp = Ext.get('id_app_icon');
	            			iconFileCmp.on('change',function(field,newValue,oldValue){
	            			         var picPath = iconFileCmp.getValue();
	            			         var url = 'file:///' + picPath;
	            			         if(img_reg.test(url)){  //格式验证
	            			        	  if(Ext.isIE){
	    	                                  var image = Ext.get('id_app_pic').dom;  
		            				          image.src = Ext.BLANK_IMAGE_URL;
		            				          image.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = url; 
		            			         }else{
		            				          //仅支持ff7.0+
		            			        	  Ext.get('id_app_pic').dom.src = window.URL.createObjectURL(Ext.get('id_app_icon').dom.files.item(0));
		            			         }
	            			         }else{
	            			        	 console.info(field);
	            			        	 Ext.MessageBox.alert('提示','文件格式只能是.jpg|.jpeg|.png|.bmp|.gif');
	            			         }
            		        },this);
            	         }
	            	}
	            },
	            //-----snap
	            {header: '截图上传', width:120, hideGrid: true, id:'id_app_snap', inputType:'file',width:100,allowBlank:false, name:'fsnap',
	            	listeners : {
	            		'render':function(){
	            			var img_reg = /\.([jJ][pP][gG]){1}$|\.([jJ][pP][eE][gG]){1}$|\.([gG][iI][fF]){1}$|\.([pP][nN][gG]){1}$|\.([bB][mM][pP]){1}$/;
	            			var iconFileCmp = Ext.get('id_app_snap');
	            			iconFileCmp.on('change',function(field,newValue,oldValue){
	            			         var picPath = iconFileCmp.getValue();
	            			         var url = 'file:///' + picPath;
	            			         if(img_reg.test(url)){  //格式验证
	            			        	  if(Ext.isIE){
	    	                                  var image = Ext.get('id_app_slot').dom;  
		            				          image.src = Ext.BLANK_IMAGE_URL;
		            				          image.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = url; 
		            			         }else{
		            				          //仅支持ff7.0+
		            			        	  Ext.get('id_app_slot').dom.src = window.URL.createObjectURL(Ext.get('id_app_snap').dom.files.item(0));
		            			         }
	            			         }else{
	            			        	 console.info(field);
	            			        	 Ext.MessageBox.alert('提示','文件格式只能是.jpg|.jpeg|.png|.bmp|.gif');
	            			         }
            		        },this);
            	         }
	            	}
	            },
	            //-----icon
            	{header: '图标预览', width: 50, hideGrid:true ,fieldtype:'box',
	            	id : 'id_app_pic',
	            	height : 120,
	            	boxMaxWidth: 125,
	            	autoEl : {
	            	    tag : 'img',
	            	    src : Ext.BLANK_AVATER_URL,  
	            	    style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);'
            	}},
            	//--- snap
            	{header: '截图预览', width: 50, hideGrid:true ,fieldtype:'box',
	            	id : 'id_app_slot',
	            	height : 120,
	            	boxMaxWidth: 125,
	            	autoEl : {
	            	    tag : 'img',
	            	    src : Ext.BLANK_AVATER_URL,  
	            	    style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);'
            	}},
            	{header: '应用摘要',  width: 120,   dataIndex: 'appSummary',name:'appSummary',autoScroll:true ,fieldtype:'textarea'},
            	{header: '应用描述',  width: 120,   dataIndex: 'appDesc',name:'appDesc',autoScroll:true ,fieldtype:'textarea'},
            	{header: 'apk上传', width:120, hideGrid: true, hideForm:'edit', hideGrid:true, inputType:'file', id:'id_app_apk',width:100, allowBlank:false,name:'fApk', fieldtype:'field',
            		listeners : {
            			'render':function(){
            				var apk_reg = /\.([aA][pP][kK]){1}$/;
            				var apkFileCmp = Ext.get('id_app_apk');
            				apkFileCmp.on('change',function(field,newValue,oldValue){
            					 var apkPath = apkFileCmp.getValue();
            			         var url = 'file:///' + apkPath;
            					if(!apk_reg.test(url)){
            						Ext.MessageBox.alert('提示','文件格式只能是.apk|.APK');       						
            					}
            				},this);
            			}
            		}
            	//regex:/\.([aA][pP][kK]){1}$/
            	},
	        ],
	
	queryFormItms: [{ 
				layout: 'tableform',
	            layoutConfig: {
	           		columns: 4,
	            	columnWidths: [.15,.25,.25], 
	            	bodyStyle:'padding:90px'
	            },           
		        items:[{html : '<pre>       </pre>'}, 
		               {xtype:'textfield',  fieldLabel: '名称', name: 'appName', id: 'appName'},
		               {xtype:'textfield',  fieldLabel: '关键词', name: 'keywords', id: 'keywords'},
		               {xtype:'appStateField',  fieldLabel: '分类', hiddenName: 'typeId',
		            	    valueField  :'id',
			                displayField:'typeName',
			                emptyText:'  请选择应用分类',
			                listAlign:'center',
			                mode  :'remote', 
			                align : 'center',
			                store : new Ext.data.JsonStore({
			                	url : "./../apptype/queryAllAppType.json",
			                	fields : new Ext.data.Record.create( ['id', 'typeName'])
			             	}),
			             	listeners: {   //初始化
			             		afterRender: function(combo) {
			             	           combo.setValue(1);               
			             	           combo.setRawValue('全部应用');
			             	         }  
			             	}
		               }]
		    }],
	
		  
    addButtonOnToolbar:function(bar,index){
    	var menu = new Ext.menu.Menu({
	        items: [{text:"推荐",iconCls: 'recommend', id:'recomEntity',scope: this,
			        	handler: function() {
			        		this.recommendOper();
					}}, 
	                {text:"取消推荐",iconCls: 'cancelRecomm', id:'cancelRecomEntity',scope: this,
	                	handler: function() {
	        			   	this.cancelRecommOper();
	        			}}
	        ]});
    	bar.insertButton(index++,new Ext.Button({text:"发布",iconCls: 'btn-onlineUser',
    		id:'distEntity',scope: this, disabled: this.authOperations[3],
		    		handler: function() {
					   	this.distOper();
					}}));
    	bar.insertButton(index++,'-');
    	bar.insertButton(index++,'推荐选择 ');
    	bar.insertButton(index++,{iconCls: 'oper', menu: menu,disabled: this.authOperations[4] ,scope: this});
    },
    
   //发布
   distOper : function(){
	   alert('undeveloped');
   },
   
   //推荐
   recommendOper : function(){
	   //选中列会显示已推荐或未推荐状态
	   alert('undeveloped');
   },
   
   //取消推荐
   cancelRecommOper : function(){
	   alert('undeveloped');
   },

	constructor: function() {
		ST.base.applicationView.superclass.constructor.call(this, {});
	}
});