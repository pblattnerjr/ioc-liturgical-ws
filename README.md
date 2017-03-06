# ioc-liturgical-ws
IOC Liturgical Web Services

Provides core web services for other applications to access the IOC Liturgical Database.

1. Provides a security service for authentication and authorization for access to the database.
2. Provides basic CRUD services.
3. All documents are stored as JSON.

Note: the main class is ioc.liturgical.ws.ServiceProvider.  You have to pass a password to it when you start it.  This is done in the Eclipse Run Configuration for the class.


Note: if the Java compiler complains about jfxrt, then follow the instructions here to take care of the problem:

http://stackoverflow.com/questions/22812488/using-javafx-in-jre-8

## Forms

Docs returned by the api include uischema, a schema, and the json for the doc.  This allows use of the npm package react-jsonschema-form client side.  Using this package results in automatically producing a form for the doc, with a submit button, and automatic error checking.

## Adding a New Endpoint

Following are the steps required to add a new endpoint to the REST api.

1.  Add it to ioc.liturgical.ws.constants.ADMIN_ENDPOINTS, e.g. users/contact.
2. Add a handler in the controller for it, e.g. controllers.admin.UsersContactController.
3. Add the db and form classes to constants.SCHEMA_CLASSES


## Trouble Shooting

### The Resources Path Does Not Include an Endpoint I Added

1. Did you add the endpoint to ioc.liturgical.ws.constants.ADMIN_ENDPOINTS?  If so, did you set INCLUDE_IN_RESOURCE_LIST.YES.value?
2. Getting a 404 for posts and puts to the endpoint.  Did you add the handler to ServiceProvider?
