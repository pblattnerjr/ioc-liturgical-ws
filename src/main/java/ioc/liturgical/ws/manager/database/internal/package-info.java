/**
 * Classes to provide CRUD operations against the underlying datastore.
 * 
 * The underlying datastore is an H2 (relational) database.
 * 
 * However, conceptually, the data is stored as JsonObjects with a topic-key
 * as the unique identifier.  So, it is, in fact, a Json datastore.
 * 
 * @since 1.0
 * @version 1.0

 * @author mac002
 *
 */
package ioc.liturgical.ws.manager.database.internal;