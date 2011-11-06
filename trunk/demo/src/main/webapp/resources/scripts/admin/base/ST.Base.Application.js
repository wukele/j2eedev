Ext.BLANK_IMAGE_URL = "./../resources/scripts/ext/resources/images/default/s.gif";
Ext.BLANK_AVATER_URL = "./../resources/images/icons/default_icon.png";
Ext.namespace("ST.base");
Ext.reg('appStateField', ST.ux.ExtField.ComboBox);

// 清除截图及路径
function clearImage(id_snap ,id_slot){
	Ext.getCmp(id_snap).reset();
	Ext.get(id_slot).dom.src = Ext.BLANK_AVATER_URL;
}
// 截图默认设置
function setDefaultImage(val,next_slot_id,pressed){
	if(!pressed){   //非选中状态 默认发送第一张截图
		Ext.getCmp('snapIndex').setRawValue(0);
		return; 
	}
	if( Ext.getCmp('id_app_snap0').disabled){// 如果默认截图按钮已经被置灰说明是当前处于编辑状态
		//拼装已经设为默认图片的URL和当前选中图片的URL发送到后台，分隔符#
		Ext.getCmp('snapIndex').setRawValue(Ext.get('id_app_slot1').dom.src+'#'+Ext.get(next_slot_id).dom.src);
	}else{
		Ext.getCmp('snapIndex').setRawValue(val);//否则属于新建状态
	}
}
// 渲染色调
function colorfunc(value, p, record){
	return value == '未发布'? String.format("<b><font color=green>未发布</font></b>"):String.format("<b><font color=red>已发布</font></b>");
}

// 获取ICON信息
function appIconfunc(value, p, record){
	return  '<img src="..'+record.data.iconUrl+'" width="100" height="50">';
}

// 下载次数++
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

ST.base.applicationView = Ext.extend(ST.ux.ViewApp, {
	id : 'id_app_Grid',
	dlgWidth: 828,
	dlgHeight: 548,
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
				{header: 'ID', width: 60, dataIndex: 'id', hideGrid: true, hideForm: 'add', readOnly: true ,hidden : true, colspan:10},
	            {header: '应用名称', width: 110, dataIndex: 'appName', allowBlank:false , name:'appName',colspan:5},
	            {header: "ICON图标", width: 140, dataIndex: 'iconUrl', hideForm:'all',renderer: appIconfunc}, 
	            {header: '应用分类', width: 120, dataIndex: 'typeId' , hiddenName: 'typeId', allowBlank:false, fieldtype:'appStateField', hideGrid:true,name:'typeId',colspan:5,
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
	            {header: '发布状态', width: 120,  dataIndex: 'appState' ,hideGrid: true, hiddenName: 'appState',dictTypeId: '10009', allowBlank:false, fieldtype:'appStateField',name:'appState',colspan:5},
	            {header: '发布状态', width: 120,  dataIndex: 'appState_Name', hideForm: 'all',renderer:colorfunc},
	            {header: '下载次数', width: 120,  dataIndex: 'downTimes', hideForm: 'all'},
	            {header: '创建时间', width: 120,  dataIndex: 'createTime',  hideForm:"all",allowBlank:false/**,renderer: Ext.util.Format.dateRenderer('m/d/Y')*/},	    
	            {header: "客户端下载", width: 140,dataIndex: 'dlClient', hideForm:'all',renderer: dlClientfunc}, 
	            {header: '关键字',     width: 120,  dataIndex: 'keyWords', hideGrid: true ,allowBlank:false , name:'keyWords',colspan:5},
	            {header: '最低版本',width: 120,  dataIndex: 'minSdkVer', name:'minSdkVer',colspan:5},
	            {header: '作者',  width: 120,   dataIndex: 'authorName',name:'authorName',colspan:5},
	            {header: '软件网址',  width: 120,   dataIndex: 'appWebsite',name:'appWebsite',colspan:5,hideForm:'edit'},
	            {header: '技术支持',  width: 120,   dataIndex: 'supportEmail',name:'supportEmail',colspan:5,hideForm:'edit'},
            	{header: '应用摘要',  width: 120,   dataIndex: 'appSummary',name:'appSummary',autoScroll:true ,colspan:5,fieldtype:'textarea'},
            	{header: '应用描述',  width: 120,   dataIndex: 'appDesc',name:'appDesc',autoScroll:true ,colspan:5,fieldtype:'textarea'},
	            {header: '上传图标', width:150, hideGrid: true, id_slot:'id_app_slot0', id:'id_app_icon',width:100, allowBlank:false, name:'fIcon',colspan:5,fieldtype:'snapfield'},
            	{header: '上传apk', width:120, hideGrid: true,inputType:'file', id:'id_app_apk',name:'fApk',colspan:5, fieldtype:'field',
            		//validate
	            	listeners : {
            			'render':function(){
            				var apk_reg = /\.([aA][pP][kK]){1}$/;
            				var apkFileCmp = Ext.get('id_app_apk');
            				apkFileCmp.on('change',function(field,newValue,oldValue){
            					 var apkPath = apkFileCmp.getValue();
            			         var url = 'file:///' + apkPath;
            					if(!apk_reg.test(url)){
            						Ext.MessageBox.alert('提示','格式不正确 ， 仅支持.apk');   
            						this.reset(); //reset 
            					}
            				},this);
            			}
            		},
            	allowBlank:false
            	//vtype:"",//
            	//regex:/\.([aA][pP][kK]){1}$/
            	},
	            //0
            	{header: '预览' ,width: 40 ,hideGrid:true , id : 'id_app_slot0' ,colspan:5 ,fieldtype:'slotfield'}, 
            	//后台默认截图 value =1/2/3/4/5
            	{header:'默认截图索引',colspan:5,value:0,name:'snapIndex' ,id:'snapIndex',hidden : true},// 隐藏域，记录默认截图的索引，传送给后台
	            //1--- snapshot
	            {header: '上传截图',hideGrid: true, id_slot:'id_app_slot1',id:'id_app_snap0',fieldtype:'snapfield',colspan:2},
	            //2
	            {header: '上传截图',hideGrid: true, id_slot:'id_app_slot2',id:'id_app_snap1',fieldtype:'snapfield',colspan:2},
	            //3
	            {header: '上传截图',hideGrid: true, id_slot:'id_app_slot3',id:'id_app_snap2',fieldtype:'snapfield',colspan:2},
	            //4
	            {header: '上传截图',hideGrid: true, id_slot:'id_app_slot4',id:'id_app_snap3',fieldtype:'snapfield',colspan:2},
	            //5
	            {header: '上传截图',hideGrid: true, id_slot:'id_app_slot5',id:'id_app_snap4',fieldtype:'snapfield',colspan:2},
            	//1---preview
            	{header: '预览',  hideGrid:true ,id : 'id_app_slot1' ,fieldtype:'slotfield',colspan:2},
            	//2
            	{header: '预览', hideGrid:true ,id : 'id_app_slot2' ,fieldtype:'slotfield',colspan:2},
            	//3
            	{header: '预览', hideGrid:true ,id : 'id_app_slot3' ,fieldtype:'slotfield',colspan:2},
            	//4
            	{header: '预览', hideGrid:true ,id : 'id_app_slot4' ,fieldtype:'slotfield',colspan:2},
            	//5
            	{header: '预览', hideGrid:true ,id : 'id_app_slot5' ,fieldtype:'slotfield',colspan:2},
            	//clear && setDefault
            	{header: '   ',fieldtype:'button' , text:'设为默认',toggleGroup:'tg-group',enableToggle:true, pressed:true,iconCls:'accept', id:'default_img',
            		toggleHandler: function(btn, pressed){
            			setDefaultImage(0,'id_app_slot1',pressed); 
            		}},
            	{fieldtype:'button' , text:'清除', handler:function(){clearImage('id_app_snap0','id_app_slot1');},iconCls:'cancel',id:'clear0'},
            	{header: '   ',fieldtype:'button' , text:'设为默认',toggleGroup:'tg-group',enableToggle:true,iconCls:'accept', id:'d-snap2',
            		toggleHandler: function(btn, pressed){
            			setDefaultImage(1,'id_app_slot2',pressed);
            		}},
            	{fieldtype:'button' , text:'清除', handler:function(){clearImage('id_app_snap1','id_app_slot2');},iconCls:'cancel',id:'clear1'},
            	{header: '   ',fieldtype:'button' , text:'设为默认',toggleGroup:'tg-group',enableToggle:true,iconCls:'accept',id:'d-snap3',
            		toggleHandler: function(btn, pressed){
            			setDefaultImage(2,'id_app_slot3',pressed);
            		}},
            	{fieldtype:'button' , text:'清除', handler:function(){clearImage('id_app_snap2','id_app_slot3');},iconCls:'cancel',id:'clear2'},
            	{header: '   ',fieldtype:'button' , text:'设为默认',toggleGroup:'tg-group',enableToggle:true,iconCls:'accept',id:'d-snap4',
            		toggleHandler: function(btn, pressed){
            			setDefaultImage(3,'id_app_slot4',pressed);
            		}},
            	{fieldtype:'button' , text:'清除', handler:function(){clearImage('id_app_snap3','id_app_slot4');},iconCls:'cancel',id:'clear3'},
            	{header: '   ',fieldtype:'button' , text:'设为默认',toggleGroup:'tg-group',enableToggle:true,iconCls:'accept',id:'d-snap5',
            		toggleHandler: function(btn, pressed){
            			setDefaultImage(4,'id_app_slot5',pressed);
            		}},
            	{fieldtype:'button' , text:'清除', handler:function(){clearImage('id_app_snap4','id_app_slot5');},iconCls:'cancel',id:'clear4'}
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
			                mode  :'remote', 
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
    		id:'distEntity',scope: this, disabled: true ,//this.authOperations[3],//点击gridrow方可激活
		    		handler: function() {
					   	this.distOper();
					}}));
    	bar.insertButton(index++,'-');
    	bar.insertButton(index++,'推荐选择 ');
    	bar.insertButton(index++,{iconCls: 'oper', menu: menu,disabled: this.authOperations[4] ,scope: this});
    },
    
    //发布状态变更
    onRowClickFn : function(grid,rowIndex,event){
    	var rec = grid.getSelectionModel().getSelected();  
    	if(rec == undefined || rec.data.appState == 1){   //已发布
    		Ext.getCmp('distEntity').disable();
    	}else{
    		Ext.getCmp('distEntity').enable();
    	}
    },
    
    //修改编辑时的图片预览
    loadEditFormSucHandler:function(form , action){
    	var img_reg = /\.([jJ][pP][gG]){1}$|\.([jJ][pP][eE][gG]){1}$|\.([gG][iI][fF]){1}$|\.([pP][nN][gG]){1}$|\.([bB][mM][pP]){1}$/;
    	//处理图标预览
    	var info = Ext.decode(action.response.responseText);
    	 Ext.get('id_app_slot0').dom.src = '..'+info.iconUrl;
    	//处理多截图的预览（见/loadApplication）
    	//默认截图放在第一个位置
    	 Ext.get('id_app_slot1').dom.src = '..'+info.snapUrl;//默认
    	 if(info.snapUrl_1!=undefined && img_reg.test(info.snapUrl_1)){
    		 Ext.get('id_app_slot2').dom.src = '..'+info.snapUrl_1;
    	 }else{
    		 Ext.getCmp('d-snap2').disable();
    	 }   	 
    	 if(info.snapUrl_1!=undefined && img_reg.test(info.snapUrl_2)){
    		 Ext.get('id_app_slot3').dom.src = '..'+info.snapUrl_2;
    	 }else{
    		 Ext.getCmp('d-snap3').disable();
    	 }
    	 if(info.snapUrl_1!=undefined && img_reg.test(info.snapUrl_3)){
    		 Ext.get('id_app_slot4').dom.src = '..'+info.snapUrl_3;
    	 }else{
    		 Ext.getCmp('d-snap4').disable();
    	 }
    	 if(info.snapUrl_1!=undefined && img_reg.test(info.snapUrl_4)){
    		 Ext.get('id_app_slot5').dom.src = '..'+info.snapUrl_4;
    	 }else{
    		 Ext.getCmp('d-snap5').disable();
    	 }
//    	 this.grayUploadBtn();
       	 Ext.getCmp('id_app_snap0').disable();
       	 Ext.getCmp('default_img').setText('当前默认');
       	 Ext.getCmp('default_img').disable();
    	 Ext.getCmp('id_app_snap1').disable();
    	 Ext.getCmp('id_app_snap2').disable();
    	 Ext.getCmp('id_app_snap3').disable();
    	 Ext.getCmp('id_app_snap4').disable();
    	 Ext.getCmp('clear0').disable();
    	 Ext.getCmp('clear1').disable();
    	 Ext.getCmp('clear2').disable();
    	 Ext.getCmp('clear3').disable();
    	 Ext.getCmp('clear4').disable();
    },
    
    
//    grayUploadBtn: function(){
// 
//    },
    
   //发布
   distOper : function(){
	   var rec = this.grid.getSelectionModel().getSelected();  
	   Ext.Ajax.request({
			url : 'distApp.json',
			success : function(response,options){
				Ext.getCmp('distEntity').disable();
				Ext.getCmp('id_app_Grid').store.reload();
				this.grid.getSelectionModel().selectRecords([rec]);
			},
			failure : function(response,options){
				console.info('error msg:',response);
			},
			params:{appId:rec.data.id},
			scope:this
		});	 
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