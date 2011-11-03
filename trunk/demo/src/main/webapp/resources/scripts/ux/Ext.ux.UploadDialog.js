Ext.namespace("Ext.ux.Utils");
Ext.ux.Utils.EventQueue = function(handler, scope) {
	if (!handler) {
		throw "Handler is required.";
	}
	this.handler = handler;
	this.scope = scope || window;
	this.queue = [];
	this.is_processing = false;
	this.postEvent = function(event, data) {
		data = data || null;
		this.queue.push({
					event : event,
					data : data
				});
		if (!this.is_processing) {
			this.process();
		}
	};
	this.flushEventQueue = function() {
		this.queue = [];
	}, this.process = function() {
		while (this.queue.length > 0) {
			this.is_processing = true;
			var event_data = this.queue.shift();
			this.handler.call(this.scope, event_data.event, event_data.data);
		}
		this.is_processing = false;
	};
};
Ext.ux.Utils.FSA = function(initial_state, trans_table, trans_table_scope) {
	this.current_state = initial_state;
	this.trans_table = trans_table || {};
	this.trans_table_scope = trans_table_scope || window;
	Ext.ux.Utils.FSA.superclass.constructor.call(this, this.processEvent, this);
};
Ext.extend(Ext.ux.Utils.FSA, Ext.ux.Utils.EventQueue, {
	current_state : null,
	trans_table : null,
	trans_table_scope : null,
	state : function() {
		return this.current_state;
	},
	processEvent : function(event, data) {
		var transitions = this.currentStateEventTransitions(event);
		if (!transitions) {
			throw "State '" + this.current_state
					+ "' has no transition for event '" + event + "'.";
		}
		for (var i = 0, len = transitions.length; i < len; i++) {
			var transition = transitions[i];
			var predicate = transition.predicate || transition.p || true;
			var action = transition.action || transition.a || Ext.emptyFn;
			var new_state = transition.state || transition.s
					|| this.current_state;
			var scope = transition.scope || this.trans_table_scope;
			if (this.computePredicate(predicate, scope, data, event)) {
				this.callAction(action, scope, data, event);
				this.current_state = new_state;
				return
			}
		}
		throw "State '" + this.current_state
				+ "' has no transition for event '" + event
				+ "' in current context";
	},
	currentStateEventTransitions : function(event) {
		return this.trans_table[this.current_state]
				? this.trans_table[this.current_state][event] || false
				: false;
	},
	computePredicate : function(predicate, scope, data, event) {
		var result = false;
		switch (Ext.type(predicate)) {
			case "function" :
				result = predicate.call(scope, data, event, this);
				break;
			case "array" :
				result = true;
				for (var i = 0, len = predicate.length; result && (i < len); i++) {
					if (Ext.type(predicate[i]) == "function") {
						result = predicate[i].call(scope, data, event, this);
					} else {
						throw ["Predicate: ", predicate[i],
								' is not callable in "', this.current_state,
								'" state for event "', event].join("");
					}
				}
				break;
			case "boolean" :
				result = predicate;
				break;
			default :
				throw ["Predicate: ", predicate, ' is not callable in "',
						this.current_state, '" state for event "', event]
						.join("");
		}
		return result;
	},
	callAction : function(action, scope, data, event) {
		switch (Ext.type(action)) {
			case "array" :
				for (var i = 0, len = action.length; i < len; i++) {
					if (Ext.type(action[i]) == "function") {
						action[i].call(scope, data, event, this);
					} else {
						throw ["Action: ", action[i], ' is not callable in "',
								this.current_state, '" state for event "',
								event].join("");
					}
				}
				break;
			case "function" :
				action.call(scope, data, event, this);
				break;
			default :
				throw ["Action: ", action, ' is not callable in "',
						this.current_state, '" state for event "', event]
						.join("");
		}
	}
});
Ext.namespace("Ext.ux.UploadDialog");
Ext.ux.UploadDialog.BrowseButton = Ext.extend(Ext.Button, {
	input_name : "files",
	input_file : null,
	original_handler : null,
	original_scope : null,
	initComponent : function() {
		Ext.ux.UploadDialog.BrowseButton.superclass.initComponent.call(this);
		this.original_handler = this.handler || null;
		this.original_scope = this.scope || window;
		this.handler = null;
		this.scope = null;
	},
	onRender : function(ct, position) {
		Ext.ux.UploadDialog.BrowseButton.superclass.onRender.call(this, ct,
				position);
		this.createInputFile();
	},
	createInputFile : function() {
		//var button_container = this.el.child(".x-btn-center"); //2.x版本ext使用(<td class="x-btn-center">) swb update 20091223
		var button_container = this.el.child(".x-btn-mc"); //3.x版本ext使用(<td class="x-btn-mc">) swb update 20091223
		button_container.position("relative");
		this.input_file = Ext.DomHelper.append(button_container, {
			tag : "input",
			type : "file",
			size : 1,
			name : this.input_name || Ext.id(this.el),
			style : "position: absolute; display: block; border: none; cursor: pointer"
		}, true);
		var button_box = button_container.getBox();
		this.input_file.setStyle("font-size", (button_box.width * 0.5) + "px");
		var input_box = this.input_file.getBox();
		var adj = {
			x : 3,
			y : 3
		};
		if (Ext.isIE) {
			adj = {
				x : 0,
				y : 3
			};
		}
		this.input_file.setLeft(button_box.width - input_box.width + adj.x
				+ "px");
		this.input_file.setTop(button_box.height - input_box.height + adj.y
				+ "px");
		this.input_file.setOpacity(0);
		if (this.handleMouseEvents) {
			this.input_file.on("mouseover", this.onMouseOver, this);
			this.input_file.on("mousedown", this.onMouseDown, this);
		}
		if (this.tooltip) {
			if (typeof this.tooltip == "object") {
				Ext.QuickTips.register(Ext.apply({
							target : this.input_file
						}, this.tooltip));
			} else {
				this.input_file.dom[this.tooltipType] = this.tooltip;
			}
		}
		this.input_file.on("change", this.onInputFileChange, this);
		this.input_file.on("click", function(e) {
					e.stopPropagation();
				});
	},
	detachInputFile : function(no_create) {
		var result = this.input_file;
		no_create = no_create || false;
		if (typeof this.tooltip == "object") {
			Ext.QuickTips.unregister(this.input_file);
		} else {
			this.input_file.dom[this.tooltipType] = null;
		}
		this.input_file.removeAllListeners();
		this.input_file = null;
		if (!no_create) {
			this.createInputFile();
		}
		return result;
	},
	getInputFile : function() {
		return this.input_file;
	},
	disable : function() {
		Ext.ux.UploadDialog.BrowseButton.superclass.disable.call(this);
		this.input_file.dom.disabled = true;
	},
	enable : function() {
		Ext.ux.UploadDialog.BrowseButton.superclass.enable.call(this);
		this.input_file.dom.disabled = false;
	},
	destroy : function() {
		var input_file = this.detachInputFile(true);
		input_file.remove();
		input_file = null;
		Ext.ux.UploadDialog.BrowseButton.superclass.destroy.call(this);
	},
	onInputFileChange : function() {
		if (this.original_handler) {
			this.original_handler.call(this.original_scope, this);
		}
	}
});
Ext.ux.UploadDialog.TBBrowseButton = Ext.extend(
		Ext.ux.UploadDialog.BrowseButton, {
			hideParent : true,
			onDestroy : function() {
				Ext.ux.UploadDialog.TBBrowseButton.superclass.onDestroy
						.call(this);
				if (this.container) {
					this.container.remove();
				}
			}
		});
Ext.ux.UploadDialog.FileRecord = Ext.data.Record.create([{
			name : "id"
		}, {
			name : "fileName"
		}, {
			name : "state",
			type : "int"
		}, {
			name : "note"
		}, {
			name : "input_element"
		}]);
Ext.ux.UploadDialog.FileRecord.STATE_QUEUE = 0;
Ext.ux.UploadDialog.FileRecord.STATE_FINISHED = 1;
Ext.ux.UploadDialog.FileRecord.STATE_FAILED = 2;
Ext.ux.UploadDialog.FileRecord.STATE_PROCESSING = 3;
Ext.ux.UploadDialog.Dialog = function(config) {
	var default_config = {
		border : false,
		isView : false,
		collapsible : true,// 可折叠
		width : 450,
		height : 300,
		minWidth : 450,
		minHeight : 300,
		plain : true,
		constrainHeader : true,
		draggable : true,
		closable : true,
		maximizable : false,
		minimizable : false,
		resizable : true,
		autoDestroy : true,
		closeAction : "hide",
		title : this.i18n.title,
		cls : "ext-ux-uploaddialog-dialog",
		url : "",
		base_params : {},
		permitted_extensions : [],
		reset_on_hide : true,
		allow_close_on_upload : false,
		upload_autostart : false
		// permitted_extensions: ['jpg', 'jpeg', 'gif']
	};
	config = Ext.applyIf(config || {}, default_config);
	config.layout = "absolute";
	Ext.ux.UploadDialog.Dialog.superclass.constructor.call(this, config);
};
Ext.extend(Ext.ux.UploadDialog.Dialog, Ext.Window, {
	fsa : null,
	state_tpl : null,
	form : null,
	grid_panel : null,
	progress_bar : null,
	is_uploading : false,
	initial_queued_count : 0,
	upload_frame : null,
	initComponent : function() {
		Ext.ux.UploadDialog.Dialog.superclass.initComponent.call(this);
		var tt = {
			"created" : {
				"window-render" : [{
					action : [this.createForm, this.createProgressBar,
							this.createGrid],
					state : "rendering"
				}],
				"destroy" : [{
							action : this.flushEventQueue,
							state : "destroyed"
						}]
			},
			"rendering" : {
				"grid-render" : [{
							action : [this.fillToolbar, this.updateToolbar],
							state : "ready"
						}],
				"destroy" : [{
							action : this.flushEventQueue,
							state : "destroyed"
						}]
			},
			"ready" : {
				"file-selected" : [{
							predicate : [this.fireFileTestEvent,
									this.isPermittedFile],
							action : this.addFileToUploadQueue,
							state : "adding-file"
						}, {}],
				"grid-selection-change" : [{
							action : this.updateToolbar
						}],
				"remove-files" : [{
							action : [this.removeFiles,
									this.fireFileRemoveEvent]
						}],
				"reset-queue" : [{
							action : [this.resetQueue, this.fireResetQueueEvent]
						}],
				"start-upload" : [{
					predicate : this.hasUnuploadedFiles,
					action : [this.setUploadingFlag,
							this.saveInitialQueuedCount, this.updateToolbar,
							this.updateProgressBar, this.prepareNextUploadTask,
							this.fireUploadStartEvent],
					state : "uploading"
				}, {}],
				"stop-upload" : [{}],
				"hide" : [{
							predicate : [this.isNotEmptyQueue,
									this.getResetOnHide],
							action : []
							//action : [this.resetQueue, this.fireResetQueueEvent]
						}, {}],
				"destroy" : [{
							action : this.flushEventQueue,
							state : "destroyed"
						}]
			},
			"adding-file" : {
				"file-added" : [{
					predicate : this.isUploading,
					action : [this.incInitialQueuedCount,
							this.updateProgressBar, this.fireFileAddEvent],
					state : "uploading"
				}, {
					predicate : this.getUploadAutostart,
					action : [this.startUpload, this.fireFileAddEvent],
					state : "ready"
				}, {
					action : [this.updateToolbar, this.fireFileAddEvent],
					state : "ready"
				}]
			},
			"uploading" : {
				"file-selected" : [{
							predicate : [this.fireFileTestEvent,
									this.isPermittedFile],
							action : this.addFileToUploadQueue,
							state : "adding-file"
						}, {}],
				"grid-selection-change" : [{}],
				"start-upload" : [{}],
				"stop-upload" : [{
					predicate : this.hasUnuploadedFiles,
					action : [this.resetUploadingFlag, this.abortUpload,
							this.updateToolbar, this.updateProgressBar,
							this.fireUploadStopEvent],
					state : "ready"
				}, {
					action : [this.resetUploadingFlag, this.abortUpload,
							this.updateToolbar, this.updateProgressBar,
							this.fireUploadStopEvent,
							this.fireUploadCompleteEvent],
					state : "ready"
				}],
				"file-upload-start" : [{
					action : [this.uploadFile, this.findUploadFrame,
							this.fireFileUploadStartEvent]
				}],
				"file-upload-success" : [{
					predicate : this.hasUnuploadedFiles,
					action : [this.resetUploadFrame, this.updateRecordState,
							this.updateProgressBar, this.prepareNextUploadTask,
							this.fireUploadSuccessEvent]
				}, {
					action : [this.resetUploadFrame, this.resetUploadingFlag,
							this.updateRecordState, this.updateToolbar,
							this.updateProgressBar,
							this.fireUploadSuccessEvent,
							this.fireUploadCompleteEvent],
					state : "ready"
				}],
				"file-upload-error" : [{
					predicate : this.hasUnuploadedFiles,
					action : [this.resetUploadFrame, this.updateRecordState,
							this.updateProgressBar, this.prepareNextUploadTask,
							this.fireUploadErrorEvent]
				}, {
					action : [this.resetUploadFrame, this.resetUploadingFlag,
							this.updateRecordState, this.updateToolbar,
							this.updateProgressBar, this.fireUploadErrorEvent,
							this.fireUploadCompleteEvent],
					state : "ready"
				}],
				"file-upload-failed" : [{
					predicate : this.hasUnuploadedFiles,
					action : [this.resetUploadFrame, this.updateRecordState,
							this.updateProgressBar, this.prepareNextUploadTask,
							this.fireUploadFailedEvent]
				}, {
					action : [this.resetUploadFrame, this.resetUploadingFlag,
							this.updateRecordState, this.updateToolbar,
							this.updateProgressBar, this.fireUploadFailedEvent,
							this.fireUploadCompleteEvent],
					state : "ready"
				}],
				"hide" : [{
							predicate : this.getResetOnHide,
							action : [this.stopUpload, this.repostHide]
						}, {}],
				"destroy" : [{
					predicate : this.hasUnuploadedFiles,
					action : [this.resetUploadingFlag, this.abortUpload,
							this.fireUploadStopEvent, this.flushEventQueue],
					state : "destroyed"
				}, {
					action : [this.resetUploadingFlag, this.abortUpload,
							this.fireUploadStopEvent,
							this.fireUploadCompleteEvent, this.flushEventQueue],
					state : "destroyed"
				}]
			},
			"destroyed" : {}
		};
		this.fsa = new Ext.ux.Utils.FSA("created", tt, this);
		this.addEvents({
					"filetest" : true,
					"fileadd" : true,
					"fileremove" : true,
					"resetqueue" : true,
					"uploadsuccess" : true,
					"uploaderror" : true,
					"uploadfailed" : true,
					"uploadstart" : true,
					"uploadstop" : true,
					"uploadcomplete" : true,
					"fileuploadstart" : true
				});
		this.on("render", this.onWindowRender, this);
		this.on("beforehide", this.onWindowBeforeHide, this);
		this.on("hide", this.onWindowHide, this);
		this.on("destroy", this.onWindowDestroy, this);
		this.state_tpl = new Ext.Template("<div class='ext-ux-uploaddialog-state ext-ux-uploaddialog-state-{state}'>&#160;</div>")
				.compile();
	},
	createForm : function() {
		this.form = Ext.DomHelper.append(this.body, {
			tag : "form",
			method : "post",
			action : this.url,
			// enctype : 'multipart/form-data',
			style : "position: absolute; left: -100px; top: -100px; width: 100px; height: 100px"
		});
	},
	createProgressBar : function() {
		if (this.isView == false) {
			this.progress_bar = this.add(new Ext.ProgressBar({
						x : 0,
						y : 0,
						anchor : "0",
						value : 0,
						text : this.i18n.progress_waiting_text
					}));
		}
	},
	createGrid : function() {
		var store = new Ext.data.Store({
					proxy : new Ext.data.MemoryProxy([]),
					reader : new Ext.data.JsonReader({
								totalProperty : "totalSize",
								root : "collection",
								id : 'id'
							}, Ext.ux.UploadDialog.FileRecord),
					sortInfo : {
						field : "state",
						direction : "DESC"
					},
					pruneModifiedRecords : true
				});
		var cm = new Ext.grid.ColumnModel([{
					header : this.i18n.state_col_title,
					width : this.i18n.state_col_width,
					resizable : false,
					dataIndex : "state",
					sortable : true,
					renderer : this.renderStateCell.createDelegate(this)
				}, {
					header : this.i18n.filename_col_title,
					width : this.i18n.filename_col_width,
					dataIndex : "fileName",
					sortable : true,
					renderer : this.renderFilenameCell.createDelegate(this)
				}, {
					header : this.i18n.note_col_title,
					width : this.i18n.note_col_width,
					dataIndex : "note",
					sortable : true,
					renderer : this.renderNoteCell.createDelegate(this)
				}]);
		this.grid_panel = new Ext.grid.GridPanel({
					ds : store,
					cm : cm,
					x : 0,
					y : 22,
					anchor : "0 -22",
					border : true,
					viewConfig : {
						autoFill : true,
						forceFit : true
					},
					bbar : new Ext.Toolbar(),
					listeners : {
						rowdblclick : function(t) {
							var record = t.getSelectionModel().getSelected();
							if (!Ext.isEmpty(record.get('id'))) {
								// Ext.Ajax.request({
								// url : 'download.action',
								// params : {
								// fileId : record.get('id')
								// },
								// method : "POST"
								// })
								// window.open('download.action?fileId='+record.get('id'));
								document.location = 'download.action?fileId='
										+ record.get('id');
							}
						}
					}
				});
		this.grid_panel.on("render", this.onGridRender, this);
		this.add(this.grid_panel);
		this.grid_panel.getSelectionModel().on("selectionchange",
				this.onGridSelectionChange, this);
	},
	fillToolbar : function() {
		var tb = this.grid_panel.getBottomToolbar();
		tb.x_buttons = {};
		
		if (this.isView == false) {
			tb.x_buttons.add = tb
					.addItem(new Ext.ux.UploadDialog.TBBrowseButton({
								text : this.i18n.add_btn_text,
								tooltip : this.i18n.add_btn_tip,
								iconCls : "ext-ux-uploaddialog-addbtn",
								handler : this.onAddButtonFileSelected,
								scope : this
							}));
			tb.x_buttons.remove = tb.addButton({
						text : this.i18n.remove_btn_text,
						tooltip : this.i18n.remove_btn_tip,
						iconCls : "ext-ux-uploaddialog-removebtn",
						handler : this.onRemoveButtonClick,
						scope : this
					});
					
			tb.x_buttons.reset = tb.addButton({
						text : this.i18n.reset_btn_text,
						tooltip : this.i18n.reset_btn_tip,
						iconCls : "ext-ux-uploaddialog-resetbtn",
						handler : this.onResetButtonClick,
						scope : this
					});
			tb.add("-");
			tb.x_buttons.upload = tb.addButton({
						text : this.i18n.upload_btn_start_text,
						tooltip : this.i18n.upload_btn_start_tip,
						iconCls : "ext-ux-uploaddialog-uploadstartbtn",
						handler : this.onUploadButtonClick,
						scope : this
					});
			tb.add("-");
			tb.x_buttons.indicator = tb
					.addItem(new Ext.Toolbar.Item(Ext.DomHelper.append(tb
									.getEl(), {
								id : 'indicatorId', //兼容3.0 swb update 20091221 
								tag : "div",
								cls : "ext-ux-uploaddialog-indicator-stoped",
								html : "&#160"
							})));
		}
		
		tb.add("->");
		tb.x_buttons.close = tb.addButton({
					text : this.i18n.close_btn_text,
					tooltip : this.i18n.close_btn_tip,
					handler : this.onCloseButtonClick,
					scope : this,
					iconCls : 'close'
				});
	},
	renderStateCell : function(data, cell, record, row_index, column_index,
			store) {
		return this.state_tpl.apply({
					state : data
				});
	},
	renderFilenameCell : function(data, cell, record, row_index, column_index,
			store) {
		var view = this.grid_panel.getView();
		var f = function() {
			try {
				Ext.fly(view.getCell(row_index, column_index))
						.child(".x-grid3-cell-inner").dom["qtip"] = data;
			} catch (e) {
			}
		};
		f.defer(1000);
		return data;
	},
	renderNoteCell : function(data, cell, record, row_index, column_index,
			store) {
		var view = this.grid_panel.getView();
		var f = function() {
			try {
				Ext.fly(view.getCell(row_index, column_index))
						.child(".x-grid3-cell-inner").dom["qtip"] = data;
			} catch (e) {
			}
		};
		f.defer(1000);
		return data;
	},
	getFileExtension : function(filename) {
		var result = null;
		var parts = filename.split(".");
		if (parts.length > 1) {
			result = parts.pop();
		}
		return result;
	},
	isPermittedFileType : function(filename) {
		var result = true;
		if (this.permitted_extensions.length > 0) {
			result = this.permitted_extensions.indexOf(this
					.getFileExtension(filename)) != -1;
		}
		return result;
	},
	isPermittedFile : function(browse_btn) {
		var result = false;
		var filename = browse_btn.getInputFile().dom.value;
		if (this.isPermittedFileType(filename)) {
			result = true;
		} else {
			Ext.Msg
					.alert(
							this.i18n.error_msgbox_title,
							String
									.format(
											this.i18n.err_file_type_not_permitted,
											filename,
											this.permitted_extensions
													.join(this.i18n.permitted_extensions_join_str)));
			result = false;
		}
		return result;
	},
	fireFileTestEvent : function(browse_btn) {
		return this.fireEvent("filetest", this,
				browse_btn.getInputFile().dom.value) !== false;
	},
	addFileToUploadQueue : function(browse_btn) {
		var input_file = browse_btn.detachInputFile();
		input_file.appendTo(this.form);
		input_file.setStyle("width", "100px");
		input_file.dom.disabled = true;
		var store = this.grid_panel.getStore();
		store.add(new Ext.ux.UploadDialog.FileRecord({
					state : Ext.ux.UploadDialog.FileRecord.STATE_QUEUE,
					fileName : input_file.dom.value,
					note : this.i18n.note_queued_to_upload,
					input_element : input_file
				}));
		this.fsa.postEvent("file-added", input_file.dom.value);
	},
	fireFileAddEvent : function(filename) {
		this.fireEvent("fileadd", this, filename);
	},
	updateProgressBar : function() {
		if (this.is_uploading) {
			var queued = this.getQueuedCount(true);
			var value = 1 - queued / this.initial_queued_count;
			this.progress_bar.updateProgress(value, String.format(
							this.i18n.progress_uploading_text,
							this.initial_queued_count - queued,
							this.initial_queued_count));
		} else {
			this.progress_bar
					.updateProgress(0, this.i18n.progress_waiting_text);
		}
	},
	updateToolbar : function() {
		if (this.isView == false) {
			var tb = this.grid_panel.getBottomToolbar();
			if (this.is_uploading) {
				tb.x_buttons.remove.disable();
				tb.x_buttons.reset.disable();
				tb.x_buttons.upload.enable();
				if (!this.getAllowCloseOnUpload()) {
					tb.x_buttons.close.disable();
				}
				Ext.fly(tb.x_buttons.indicator.getEl()).replaceClass(
						"ext-ux-uploaddialog-indicator-stoped",
						"ext-ux-uploaddialog-indicator-processing");
				tb.x_buttons.upload
						.setIconClass("ext-ux-uploaddialog-uploadstopbtn");
				tb.x_buttons.upload.setText(this.i18n.upload_btn_stop_text);
//				tb.x_buttons.upload.getEl()
//						.child(tb.x_buttons.upload.buttonSelector).dom[tb.x_buttons.upload.tooltipType] = this.i18n.upload_btn_stop_tip
			} else {
				tb.x_buttons.remove.enable();
				tb.x_buttons.reset.enable();
				tb.x_buttons.close.enable();
				Ext.fly(tb.x_buttons.indicator.getEl()).replaceClass(
						"ext-ux-uploaddialog-indicator-processing",
						"ext-ux-uploaddialog-indicator-stoped");
				tb.x_buttons.upload
						.setIconClass("ext-ux-uploaddialog-uploadstartbtn");
				tb.x_buttons.upload.setText(this.i18n.upload_btn_start_text);
//				tb.x_buttons.upload.getEl()
//						.child(tb.x_buttons.upload.buttonSelector).dom[tb.x_buttons.upload.tooltipType] = this.i18n.upload_btn_start_tip;
				if (this.getQueuedCount() > 0) {
					tb.x_buttons.upload.enable();
				} else {
					tb.x_buttons.upload.disable();
				}
				if (this.grid_panel.getSelectionModel().hasSelection()) {
					tb.x_buttons.remove.enable();
				} else {
					tb.x_buttons.remove.disable();
				}
				if (this.grid_panel.getStore().getCount() > 0) {
					tb.x_buttons.reset.enable();
				} else {
					tb.x_buttons.reset.disable();
				}
			}
		}
	},
	saveInitialQueuedCount : function() {
		this.initial_queued_count = this.getQueuedCount();
	},
	incInitialQueuedCount : function() {
		this.initial_queued_count++;
	},
	setUploadingFlag : function() {
		this.is_uploading = true;
	},
	resetUploadingFlag : function() {
		this.is_uploading = false;
	},
	prepareNextUploadTask : function() {
		var store = this.grid_panel.getStore();
		var record = null;
		store.each(function(r) {
			if (!record
					&& r.get("state") == Ext.ux.UploadDialog.FileRecord.STATE_QUEUE) {
				record = r;
			} else {
				if (r && r.get("input_element")
						&& !Ext.isEmpty(r.get("input_element"))) {
					r.get("input_element").dom.disabled = true ;// editted by
					// shou
				}
			}
		});
		record.get("input_element").dom.disabled = false;
		record.set("state", Ext.ux.UploadDialog.FileRecord.STATE_PROCESSING);
		record.set("note", this.i18n.note_processing);
		record.commit();
		this.fsa.postEvent("file-upload-start", record);
	},
	fireUploadStartEvent : function() {
		this.fireEvent("uploadstart", this);
	},
	removeFiles : function(file_records) {
		var p = this;
		var delIds = "";
		Ext.MessageBox.confirm('确认删除', '确认要删除所选附件吗?', function(btn) {
					if (btn == 'yes') {
						var store = p.grid_panel.getStore();
						for (var i = 0, len = file_records.length; i < len; i++) {
							var r = file_records[i];
							if (r && r.get("input_element")
									&& !Ext.isEmpty(r.get("input_element"))) {
								r.get("input_element").remove();
							}
							// delIds.push(r.get('id'));
							delIds += r.get('id') + ",";

							store.remove(r);

						}
						// 去除最后一个分隔符
						if (delIds.length > 0) {
							delIds = delIds.substr(0, delIds.length - 1);
						}
						if (!Ext.isEmpty(delIds)) {
							Ext.Ajax.request({
										url : 'fileupload!deleteAffixsByAjax.action',
										method : 'POST',
										success : function(result, request) {
											Ext.Msg.info({
														message : '删除附件成功！'
													});
										},
										failure : function(result, request) {
											Ext.Msg.info({
														success : false,
														message : '删除附件失败！'
													});
										},
										params : {
											fileIds : delIds
										}
									});
						}
					}

				});
	},
	fireFileRemoveEvent : function(file_records) {
		for (var i = 0, len = file_records.length; i < len; i++) {
			this.fireEvent("fileremove", this, file_records[i].get("fileName"));
		}
	},
	resetQueue : function() {
		var store = this.grid_panel.getStore();
		store.each(function(r) {
					if (r && r.get("input_element")) {
						r.get("input_element").remove();
					}
				});
		store.removeAll();
	},
	fireResetQueueEvent : function() {
		this.fireEvent("resetqueue", this);
	},
	uploadFile : function(record) {
		// this.form.submit();

		Ext.Ajax.request({
					url : this.url,
					params : this.base_params || this.baseParams || this.params,
					method : 'POST',
					form : this.form,
					isUpload : true,
					scope : this,
					record : record,
					success : this.onAjaxSuccess,
					failure : this.onAjaxFailure
				});
	},
	fireFileUploadStartEvent : function(record) {
		this.fireEvent("fileuploadstart", this, record.get("fileName"));
	},
	updateRecordState : function(data) {
		if ("success" in data.response && data.response.success) {
			data.record.set("state",
					Ext.ux.UploadDialog.FileRecord.STATE_FINISHED);
			data.record.set("note", data.response.message
							|| data.response.error
							|| this.i18n.note_upload_success);
		} else {
			data.record.set("state",
					Ext.ux.UploadDialog.FileRecord.STATE_FAILED);
			data.record.set("note", data.response.message
							|| data.response.error
							|| this.i18n.note_upload_error);
		}
		data.record.commit();
	},
	fireUploadSuccessEvent : function(data) {
		this.fireEvent("uploadsuccess", this, data.record.get("fileName"),
				data.response);
	},
	fireUploadErrorEvent : function(data) {
		this.fireEvent("uploaderror", this, data.record.get("fileName"),
				data.response);
	},
	fireUploadFailedEvent : function(data) {
		this.fireEvent("uploadfailed", this, data.record.get("fileName"));
	},
	fireUploadCompleteEvent : function() {
		this.fireEvent("uploadcomplete", this);
	},
	findUploadFrame : function() {
		this.upload_frame = Ext.getBody().child("iframe.x-hidden:last");
	},
	resetUploadFrame : function() {
		this.upload_frame = null;
	},
	removeUploadFrame : function() {
		if (this.upload_frame) {
			this.upload_frame.removeAllListeners();
			this.upload_frame.dom.src = "about:blank";
			this.upload_frame.remove();
		}
		this.upload_frame = null;
	},
	abortUpload : function() {
		this.removeUploadFrame();
		var store = this.grid_panel.getStore();
		var record = null;
		store.each(function(r) {
			if (r.get("state") == Ext.ux.UploadDialog.FileRecord.STATE_PROCESSING) {
				record = r;
				return false;
			}
		});
		record.set("state", Ext.ux.UploadDialog.FileRecord.STATE_FAILED);
		record.set("note", this.i18n.note_aborted);
		record.commit();
	},
	fireUploadStopEvent : function() {
		this.fireEvent("uploadstop", this);
	},
	repostHide : function() {
		this.fsa.postEvent("hide");
	},
	flushEventQueue : function() {
		this.fsa.flushEventQueue();
	},
	onWindowRender : function() {
		this.fsa.postEvent("window-render");
	},
	onWindowBeforeHide : function() {
		return this.isUploading() ? this.getAllowCloseOnUpload() : true;
	},
	onWindowHide : function() {
		this.fsa.postEvent("hide");
	},
	onWindowDestroy : function() {
		this.fsa.postEvent("destroy");
	},
	onGridRender : function() {
		this.fsa.postEvent("grid-render");
	},
	onGridSelectionChange : function() {
		this.fsa.postEvent("grid-selection-change");
	},
	onAddButtonFileSelected : function(btn) {
		this.fsa.postEvent("file-selected", btn);
	},
	onUploadButtonClick : function() {
		if (this.is_uploading) {
			this.fsa.postEvent("stop-upload");
		} else {
			this.fsa.postEvent("start-upload");
		}
	},
	onRemoveButtonClick : function() {
		var selections = this.grid_panel.getSelectionModel().getSelections();
		this.fsa.postEvent("remove-files", selections);
	},
	onResetButtonClick : function() {
		this.fsa.postEvent("reset-queue");
	},
	onCloseButtonClick : function() {
		this[this.closeAction].call(this);
	},
	onAjaxSuccess : function(response, options) {
		var json_response = {
			"success" : false,
			"error" : this.i18n.note_upload_error
		};
		try {
			var text = response.responseText;
			// alert(text);
			var json_response = Ext.util.JSON.decode(text);
			if (this.hiddenFieldId) {
				var v = Ext.getCmp(this.hiddenFieldId).getValue();
				if (Ext.isEmpty(v))
					Ext.getCmp(this.hiddenFieldId)
							.setValue(json_response.collection[0]);
				else
					Ext.getCmp(this.hiddenFieldId).setValue(v + ","
							+ json_response.collection[0]);
			}
		} catch (e) {
		}
		var data = {
			record : options.record,
			response : json_response
		};
		if ("success" in json_response && json_response.success) {
			this.fsa.postEvent("file-upload-success", data);
		} else {
			this.fsa.postEvent("file-upload-error", data);
		}
	},
	onAjaxFailure : function(response, options) {
		var data = {
			record : options.record,
			response : {
				"success" : false,
				"error" : this.i18n.note_upload_failed
			}
		};
		this.fsa.postEvent("file-upload-failed", data);
	},
	startUpload : function() {
		this.fsa.postEvent("start-upload");
	},
	stopUpload : function() {
		this.fsa.postEvent("stop-upload");
	},
	getUrl : function() {
		return this.url;
	},
	setUrl : function(url) {
		this.url = url;
	},
	getBaseParams : function() {
		return this.base_params;
	},
	setBaseParams : function(params) {
		this.base_params = params;
	},
	getUploadAutostart : function() {
		return this.upload_autostart;
	},
	setUploadAutostart : function(value) {
		this.upload_autostart = value;
	},
	getAllowCloseOnUpload : function() {
		return this.allow_close_on_upload;
	},
	setAllowCloseOnUpload : function(value) {
		this.allow_close_on_upload;
	},
	getResetOnHide : function() {
		return this.reset_on_hide;
	},
	setResetOnHide : function(value) {
		this.reset_on_hide = value;
	},
	getPermittedExtensions : function() {
		return this.permitted_extensions;
	},
	setPermittedExtensions : function(value) {
		this.permitted_extensions = value;
	},
	isUploading : function() {
		return this.is_uploading;
	},
	isNotEmptyQueue : function() {
		return this.grid_panel.getStore().getCount() > 0;
	},
	getQueuedCount : function(count_processing) {
		var count = 0;
		var store = this.grid_panel.getStore();
		store.each(function(r) {
			if (r.get("state") == Ext.ux.UploadDialog.FileRecord.STATE_QUEUE) {
				count++;
			}
			if (count_processing
					&& r.get("state") == Ext.ux.UploadDialog.FileRecord.STATE_PROCESSING) {
				count++;
			}
		});
		return count;
	},
	hasUnuploadedFiles : function() {
		return this.getQueuedCount() > 0;
	}
});
var p = Ext.ux.UploadDialog.Dialog.prototype;
p.i18n = {
	title : "上传文件",
	state_col_title : "状态",
	state_col_width : 70,
	filename_col_title : "文件名",
	filename_col_width : 230,
	note_col_title : "备注",
	note_col_width : 150,
	add_btn_text : "添加",
	add_btn_tip : "添加文件到上传队列",
	remove_btn_text : "删除",
	remove_btn_tip : "从上传队列删除文件",
	reset_btn_text : "重置",
	reset_btn_tip : "重置队列",
	upload_btn_start_text : "开始上传",
	upload_btn_stop_text : "中断上传",
	upload_btn_start_tip : "上传文件对列",
	upload_btn_stop_tip : "停止上传",
	close_btn_text : "关闭",
	close_btn_tip : "关闭上传对话框",
	progress_waiting_text : "等待...",
	progress_uploading_text : "上传中: {0} 的 {1} 文件集合成功",
	error_msgbox_title : "错误",
	permitted_extensions_join_str : ",",
	err_file_type_not_permitted : "不支持上传该类型文件.<br/>请选择下列类型的文件集合 {1}",
	note_queued_to_upload : "上传的队列",
	note_processing : "上传中...",
	note_upload_failed : "当前请求过多，服务器正忙，不能及时响应或者因特网服务器发生错误",
	note_upload_success : "成功",
	note_upload_error : "上传文件出错",
	note_aborted : "已经被用户中断"
};