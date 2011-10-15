Ext.namespace('Ext.ux');
Ext.BLANK_IMAGE_URL = "./resources/scripts/ext/resources/images/default/s.gif";

Ext.ux.LoginWindow = function(config) {
    Ext.apply(this, config);
    var css = "#login-logo .x-plain-body {background:#f9f9f9 url('"
            + this.basePath + "/" + this.winBanner + "') no-repeat;}"
            + "#login-form  {background: " + this.formBgcolor + " none;}"
            + ".ux-auth-header-icon {background: url('" + this.basePath
            + "/locked.gif') 0 4px no-repeat !important;}"
            + ".ux-auth-form {padding:10px;}"
            + ".ux-auth-login {background-image: url('" + this.basePath
            + "/key.gif') !important}"
            + ".ux-auth-close {background-image: url('" + this.basePath
            + "/close.gif') !important}";

    Ext.util.CSS.createStyleSheet(css, this._cssId);
    this.addEvents( {
        'show' : true,
        'reset' : true,
        'submit' : true
    });
    Ext.ux.LoginWindow.superclass.constructor.call(this, config);
    Ext.Ajax.on('beforerequest', this.onBeforerequest, this);

    this._logoPanel = new Ext.Panel( {
        baseCls : 'x-plain',
        id : 'login-logo',
        region : 'center'
    });

    this.usernameId = Ext.id();
    this.passwordId = Ext.id();
    this.themeId = Ext.id();
    this._loginButtonId = Ext.id();
    this._resetButtonId = Ext.id();
    this.cp = new Ext.state.CookieProvider({
        expires: new Date(new Date().getTime()+(1000*60*60*24*30)) //30 dias
    });
   
    Ext.state.Manager.setProvider(this.cp);
    this._formPanel = new Ext.form.FormPanel( {
        region : 'south',
        border : false,
        bodyStyle : "padding: 5px;",
        baseCls : 'x-plain',
        id : 'login-form',
        waitMsgTarget : true,
        labelWidth : 80,
        defaults : {
            width : 300
        },
        baseParams : {
            task : 'login'
        },
        height : 140,
        items : [{
                    xtype : 'textfield',
                    id : this.usernameId,
                    name : "j_username",
                    fieldLabel : "用户名",
                    vtype : "alphanum",
                    validateOnBlur : false,
                    allowBlank : false
                }, {
                    xtype : 'textfield',
                    inputType : 'password',
                    id : this.passwordId,
                    name : "j_password",
                    fieldLabel : "密码",
                    vtype : "alphanum",
                    validateOnBlur : false,
                    allowBlank : false
                }
            ]
    });
    var buttons = [ {
        id : this._loginButtonId,
        text : "Login",
        handler : this.submit,
        scale : 'medium',
        scope : this
    } ];
    var keys = [ {
        key : [ 10, 13 ],
        handler : this.submit,
        scope : this
    } ];

    if (typeof this.resetButton == 'string') {
        buttons.push( {
            id : this._resetButtonId,
            text : this.resetButton,
            handler : this.reset,
            scale : 'medium',
            scope : this
        });
        keys.push( {
            key : [ 27 ],
            handler : this.reset,
            scope : this
        });
    }
    //Cria a janela principal de login
    this._window = new Ext.Window( {
        width : 429,
        height : 340,
        closable : false,
        resizable : false,
        draggable : true,
        modal : this.modal,
        iconCls : 'ux-auth-header-icon',
        title : "Login",
        layout : 'border',
        bodyStyle : 'padding:5px;',
        buttons : buttons,
        buttonAlign : 'center',
        keys : keys,
        plain : false,
        items : [ this._logoPanel, this._formPanel ]
    });

    this._window.on('show', function() {
        this.fireEvent('show', this);
    }, this);
};

Ext.extend(Ext.ux.LoginWindow, Ext.util.Observable, {
    Passwordtitle : '',
    resetButton : 'Clear',
    url : '',
    locationUrl : '',
    basePath : 'img',
    winBanner : '',
    formBgcolor : '',
    modal : false,
    _cssId : 'ux-LoginWindow-css',
    _logoPanel : null,
    _formPanel : null,
    _window : null,
     
    show : function(el) {
        this._window.show(el);
        (function(){
        	if(Ext.getCmp(this.usernameId).getValue() != "")
        		Ext.getCmp(this.passwordId).focus();
        	else
        		Ext.getCmp(this.usernameId).focus();
       }).defer(1000, this);
    },

    reset : function() {
        if (this.fireEvent('reset', this)) {
            Ext.getDom(this.usernameId).value = '';
            Ext.getDom(this.passwordId).value = '';
            Ext.select('#errorMsg').remove();
        }
    },

    submit : function() {
        var form = this._formPanel.getForm();

        if (form.isValid()) {
            if (this.fireEvent('submit', this, form.getValues())) {
                form.submit( {
                    url : this.url,
                    method : "POST",
                    waitMsg : "Sending data...",
                    success : this.onSuccess,
                    failure : this.onFailure,
                    scope : this
                });
            }
        }
    },
    
    onBeforerequest : function() {
        Ext.select('#errorMsg').remove();
    },

    onSuccess : function(form, action) {
    	Ext.state.Manager.set('username', Ext.getCmp(this.usernameId).getValue());
        if (action && action.result) {
           	window.location = "./";
        }
    },

    onFailure : function(form, action) { // enable buttons
    	console.info(action)
        Ext.getCmp(this._loginButtonId).enable();
        if (Ext.getCmp(this._resetButtonId)) {
            Ext.getCmp(this._resetButtonId).enable();
        }
        if(action.result.type == "UsernameNotFound") {
        	Ext.getCmp(this.usernameId).focus(true,true);
        	Ext.getDom(this.passwordId).value = '';
        } else if(action.result.type == "BadCredentials") {
        	Ext.getDom(this.passwordId).value = '';
        	Ext.getCmp(this.passwordId).focus();
        }
        var html = '<div id="errorMsg" style="text-align:center;padding-top:10px;color:red;">'+action.result.message+'</div>';
        Ext.select('.x-form-clear-left').each(function(o,g,i){
            if (i==1)
            	o.insertSibling(html,'after');
        });
    }
});


Ext.onReady( function() {
    Ext.QuickTips.init();
    var LoginWindow = new Ext.ux.LoginWindow( {
        modal : true,
        //formBgcolor:'#f0edce',
        basePath : './resources/images/core/login',
        winBanner : 'login.png',
        url : 'user/login.json',
        locationUrl : 'main.action'
    });
    LoginWindow.show();
    
    if(Ext.state.Manager.get('username')){
        Ext.getCmp(LoginWindow.usernameId).setValue(Ext.state.Manager.get('username'));
    }else{
        Ext.getCmp(LoginWindow.usernameId).emptyText = 'Please enter your user name';
    }
});
