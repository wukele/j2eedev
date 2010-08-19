Ext.form.ClearableComboBox = Ext.extend(Ext.form.ComboBox, {
    initComponent: function() {
        this.triggerConfig = {
            tag:'span', cls:'x-form-twin-triggers', cn:[
            {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger x-form-clear-trigger"},
            {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger"}        
        ]};
        Ext.form.ClearableComboBox.superclass.initComponent.call(this);
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
Ext.reg('clearcombo', Ext.form.ClearableComboBox);

Ext.namespace("ST.ux.ExtField");
//---------------------------------------------------
var resourceTypeStore = new Ext.data.ArrayStore({
    fields: ['key', 'text'],
    data : [['','请选择...'],['url','URL'], ['method','方法'], ['menu', '菜单']]
});
ST.ux.ExtField.ResourceTypeCombo = Ext.extend(Ext.form.ComboBox, {
    store: resourceTypeStore,
    hiddenName: 'type',
    allowBlank: false,
    editable : false,
    valueField: 'key',
    displayField:'text',
    typeAhead: true,
    mode: 'local',
    forceSelection: true,
    triggerAction: 'all',
    emptyText:'请选择...',
    selectOnFocus:true
});
Ext.reg('resourceTypeField', ST.ux.ExtField.ResourceTypeCombo);

var enabledStore = new Ext.data.ArrayStore({
    fields: ['key', 'text'],
    data : [['','请选择...'],['Y', '有效'], ['N', '无效']]
});
ST.ux.ExtField.EnabledCombo = Ext.extend(Ext.form.ComboBox, {
    store: enabledStore,
    hiddenName: 'enabled',
    allowBlank: false,
    editable : false,
    valueField: 'key',
    displayField:'text',
    typeAhead: true,
    mode: 'local',
    forceSelection: true,
    triggerAction: 'all',
    emptyText:'请选择...',
    selectOnFocus:true
});
Ext.reg('enabledField', ST.ux.ExtField.EnabledCombo);

var menuNodeTypeStore = new Ext.data.ArrayStore({
    fields: ['key', 'text'],
    data : [['','请选择...'],['Y', '叶子节点'], ['N', '非叶子节点']]
});
ST.ux.ExtField.MenuNodeTypeCombo = Ext.extend(Ext.form.ComboBox, {
    store: menuNodeTypeStore,
    hiddenName: 'isLeaf',
    allowBlank: false,
    editable : false,
    valueField: 'key',
    displayField:'text',
    typeAhead: true,
    mode: 'local',
    forceSelection: true,
    triggerAction: 'all',
    emptyText:'请选择...',
    selectOnFocus:true
});
Ext.reg('menuNodeTypeField', ST.ux.ExtField.MenuNodeTypeCombo);
