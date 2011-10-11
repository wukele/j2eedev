Ext.ns('Ext.ux');

Ext.ux.AccordionPanelTool  = Ext.extend(Ext.Button, {
    flipped:false,
    handler: function(button,event) {
            this.parent.layout.setActiveItem(1);
            this.flipped=!this.flipped;
            this.parent.flipComponent(this.component,this.flipped);
            this.parent.doLayout(false,true);
        },
    constructor : function(parent,component,config){
        this.parent = parent;
        this.component = component;
        this.scope=this;
        Ext.apply(this,config);
        Ext.ux.AccordionPanelTool.superclass.initComponent.call(this);

    }
});

Ext.ux.AccordionPanel = Ext.extend(Ext.Panel, {
    layout:'card',
    activeItem: 0,
    comps: [],
    //plugins: [Ext.ux.PanelCollapsedTitle],

    flipComponent: function(comp,flipped) {
        if(flipped) {
           
            var position=-1;
            for(var pos=0;pos<this.accordion.items.length;pos++) {
                if(this.comps[pos].getItemId()==comp.getItemId()) {
                    position=pos;
                    break;
                }
            }
            if(position==-1) {
                return;
            }
            comp.exppos=position;
            comp.expstate=comp.collapsed;
            this.expanded.add(comp);
            this.layout.setActiveItem(1);
            comp.doLayout(false,true);
            comp.expand();
            this.expanded.doLayout(false,true);
        } else {
            this.layout.setActiveItem(0);

            this.accordion.insert(comp.exppos,comp);
            

            if(comp.expstate) {
                comp.collapse();
            } else {
                comp.expand();
            }
            comp.doLayout(false,true);
            this.doLayout(false,true);
        }
    },
    initComponent : function(){
        Ext.ux.AccordionPanel.superclass.initComponent.apply(this);
        var accordioncard = new Ext.Panel({
            layout:'accordion',
            border:false
        });
        var me = this;
        me.accordion = accordioncard;
        var expandedpanel = new Ext.Panel({
            layout:'accordion',
            border:false
        });

        me.expanded = expandedpanel;
        me.add(accordioncard);
        me.add(expandedpanel);
        me.on('add',function(panel,component,index) {

            if(panel!=me) {
                return;
            }
            this.comps.push(component);
            if(component.expset==true) {
                accordioncard.add(component);
                component.expand();
                me.doLayout(false,true);
                return;
            }
            if(component.expandable==true) {
                if(component.tools==undefined) {
                    component.tools = [];
                }
                var up =new Ext.ux.AccordionPanelTool(me,component,{id:'rollup'});
                component.tools.push(up);
                component.expset=true;
            }
             accordioncard.add(component);
             component.expand();
             me.doLayout(false,true);
        });
    }
});