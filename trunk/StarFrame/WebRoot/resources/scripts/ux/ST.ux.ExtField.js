Ext.namespace("ST.ux.ExtField");
//---------------------------------------------------
var resourceTypeStore = new Ext.data.ArrayStore({
    fields: ['key', 'text'],
    data : [['','请选择...'],['url','URL'], ['method','方法']]
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
    data : [['','请选择...'],['true','有效'], ['false','无效']]
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
