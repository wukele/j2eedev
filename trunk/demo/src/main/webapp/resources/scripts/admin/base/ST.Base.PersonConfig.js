Ext.BLANK_IMAGE_URL = "./resources/scripts/ext/resources/images/default/s.gif";

Ext.namespace("ST.base");
Ext.QuickTips.init();
Ext.form.Field.prototype.msgTarget = 'side';

Ext.reg('genderField', Ext.extend(ST.ux.ExtField.ComboBox, {
	store : new Ext.data.JsonStore({  //填充的数据
    	url : "./dict/queryDictionarys4Code.json",
    	fields : new Ext.data.Record.create( ['itemCode', 'text'])
 	})
}));
Ext.apply(Ext.form.VTypes, {
    password: function(val, field){
        if (field.initialPassField) {
            var pwd = Ext.getCmp(field.initialPassField);
            return (val == pwd.getValue());
        }
        return true;
    },
    passwordText: '密码不匹配'
});
ST.base.PersonConfig = function () {
	var win = null;
	var basicFormPanel;
	var passwdFormPanel;
    return  {   
		showWin: function(id) {
    		if(win == null)
    			this.createWin();
    		win.show(Ext.get(id));
		},
		
		createBasicFormPanel: function() {
			var reader = new Ext.data.JsonReader({}, [
				{name: 'userName'},{name: 'userCode'},{name: 'gender'},{name: 'email'},{name: 'phoneNo'}, {name: 'mPhoneNo'},,{name:'birthday'}
			]);
			basicFormPanel = new Ext.FormPanel({
		        labelWidth: 80,
		        url: './user/updateCurrentUser.json"',
		        method: 'POST',
		        frame: true,
		        reader: reader,
		        width: 460,
		        title: '用户基本信息',
		        defaultType: 'textfield',
		        buttonAlign: 'center',
		        buttons: [{
		            text: '提交',
		            handler: function(){
		                if (basicFormPanel.getForm().isValid()) {
		                    basicFormPanel.getForm().submit({
		                        waitMsg : '正在处理，请稍等...',
	                            success: function() {
	                                Ext.MessageBox.alert("提示", "保存成功");
	                            },
	                            failure: function(a, b) {
	                            	Ext.MessageBox.alert("提示", b.result.message);
	                            }
		                    });
		                }
		            }
		        },{
		            text: '取消',
		            handler: function() {
		        		win.hide();
		            }
		        }],
		        
		        items: [{
		            fieldLabel: '用户名称',
		            width: 150,
		            maxLength: 20,
		            id:'userName',
		            name: 'userName',
		            allowBlank: false,
		            blankText: '用户名称不能为空'
		        },{
		            fieldLabel: '登录账号',
		            width: 150,
		            maxLength: 20,
		            readOnly: true,
		            id:'userCode',
		            name: 'userCode',
		            allowBlank: false,
		            blankText: '登录账号不能为空'
		        }, {
		            fieldLabel: '性别',
		            width: 150,
		            xtype: 'genderField',
		            hiddenName: 'gender',
		            code: 'CORE.GENDER',
		            name: 'gender'
		        }, {
		            fieldLabel: '邮箱',
		            width: 200,
		            maxLength: 50,
		            name: 'email',
		            vtype: 'email',
		            allowBlank: false,
		            blankText: '邮箱不能为空'
		        }, {
		            fieldLabel: '电话号码',
		            width: 150,
		            maxLength: 50,
		            name: 'phoneNo'
		        }, {
		            fieldLabel: '移动号码',
		            width: 150,
		            maxLength: 50,
		            name: 'mPhoneNo'
		        }, {
		            fieldLabel: '出生日期',
		            name: 'birthday',
		            xtype: 'datefield',
		            format: 'Y-m-d',
		            maxValue:new Date(),
		            minValue:'1900-01-01',
		            width: 150,
		            allowBlank: false,
		            blankText: '出生日期不能为空'
		        }]
		    });
			basicFormPanel.load({url: "./user/loadCurrentUser.json"});
			return basicFormPanel;
		},
		
		createPasswdFormPanel: function() {
			passwdFormPanel = new Ext.FormPanel({
		        labelWidth: 80,
		        url: './user/updateCurrentUserPasswd.json"',
		        method: 'POST',
		        frame: true,
		        width: 460,
		        title: '修改密码',
		        defaultType: 'textfield',
		        buttonAlign: 'center',
		        buttons: [{
		            text: '提交',
		            handler: function(){
		                if (passwdFormPanel.getForm().isValid()) {
		                    passwdFormPanel.getForm().submit({
		                        waitMsg : '正在处理，请稍等...',
	                            success: function() {
	                                Ext.MessageBox.alert("提示", "保存成功");
	                            },
	                            failure: function(a, b) {
	                            	Ext.MessageBox.alert("提示", b.result.message);
	                            }
		                    });
		                }
		            }
		        },{
		            text: '取消',
		            handler: function() {
		        		win.hide();
		            }
		        }],
		        
		        items: [{
		            id: 'pass',
		            fieldLabel: '密码',
		            width: 150,
		            name: 'password',
		            minLength: 6,
		            maxLength: 20,
		            inputType: 'password',
		            allowBlank: false,
		            blankText: '密码不能为空'
		        }, {
		            fieldLabel: '确认密码',
		            width: 150,
		            id:'pass-cfrm',
		            name: 'pass-cfrm',
		            minLength: 6,
		            maxLength: 20,
		            inputType: 'password',
		            vtype: 'password',
		            initialPassField: 'pass',
		            allowBlank: false
		        }]
		    });
			return passwdFormPanel;
		},
		
		createWin: function() {
			var tabs = new Ext.TabPanel({
            	activeTab: 0,
             	frame:true,
             	height: 360,
    	        items:[this.createBasicFormPanel(), this.createPasswdFormPanel()]
            });
			win = new Ext.Window({
		        width: 400,
		        height: 360,
		        frame:true,
		        layout: 'fit',
		        resizable: false,
		        closable:false,
		        modal: true,
		        items: tabs
		    });
		}
    };   
}();  