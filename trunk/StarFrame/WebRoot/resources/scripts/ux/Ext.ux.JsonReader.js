Ext.namespace("Ext.ux.JsonReader");
Ext.ux.JsonReader = function(config) {
	Ext.ux.JsonReader.superclass.constructor.call(this, config);
}

Ext.extend(Ext.ux.JsonReader, Ext.data.JsonReader, {
	read : function(response){
	    var json = response.responseText;
	    var o = Ext.decode(json);
	    if(!o) {
	        throw {message: 'JsonReader.read: Json object not found'};
	    }
	    return this.readRecords(o.page);
	}
});