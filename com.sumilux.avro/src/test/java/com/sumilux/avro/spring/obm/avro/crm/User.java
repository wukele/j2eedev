/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.sumilux.avro.spring.obm.avro.crm;

@SuppressWarnings("all")
public class User extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
    public static final org.apache.avro.Schema SCHEMA$ = org.apache.avro.Schema.parse("{\"type\":\"record\",\"name\":\"User\",\"namespace\":\"org.springframework.obm.avro.crm\",\"fields\":[{\"name\":\"email\",\"type\":[\"string\",\"null\"]},{\"name\":\"password\",\"type\":[\"string\",\"null\"]}]}");
    public java.lang.CharSequence email;
    public java.lang.CharSequence password;

    public org.apache.avro.Schema getSchema() {
        return SCHEMA$;
    }

    // Used by DatumWriter.  Applications should not call.
    public java.lang.Object get(int field$) {
        switch (field$) {
            case 0:
                return email;
            case 1:
                return password;
            default:
                throw new org.apache.avro.AvroRuntimeException("Bad index");
        }
    }

    // Used by DatumReader.  Applications should not call.
    @SuppressWarnings(value = "unchecked")
    public void put(int field$, java.lang.Object value$) {
        switch (field$) {
            case 0:
                email = (java.lang.CharSequence) value$;
                break;
            case 1:
                password = (java.lang.CharSequence) value$;
                break;
            default:
                throw new org.apache.avro.AvroRuntimeException("Bad index");
        }
    }
}
