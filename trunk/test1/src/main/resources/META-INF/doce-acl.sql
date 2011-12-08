CREATE TABLE acl_class (
	id IDENTITY PRIMARY KEY,
	class VARCHAR(255) NOT NULL
);

CREATE TABLE acl_entry (
	id IDENTITY PRIMARY KEY,
	acl_object_identity BIGINT NOT NULL,
	ace_order integer NOT NULL,
	sid BIGINT NOT NULL,
	mask integer NOT NULL,
	granting integer NOT NULL,
	audit_success integer NOT NULL,
	audit_failure integer NOT NULL
);

CREATE TABLE acl_object_identity (
	id IDENTITY PRIMARY KEY,
	object_id_class BIGINT NOT NULL,
	object_id_identity VARCHAR(24) NOT NULL,
	parent_object BIGINT,
	owner_sid BIGINT,
	entries_inheriting integer NOT NULL
);

CREATE TABLE acl_sid (
	id IDENTITY PRIMARY KEY,
	principal integer NOT NULL,
	sid VARCHAR(100) NOT NULL
);