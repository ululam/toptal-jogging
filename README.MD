Project demo is hosted at http://jogging-app.cfapps.io/
Currently, it uses in-memory SQL database, so all the data except initial test data is erased on restart

There are three predefined users:
1) user:user - simple user
2) manager:manager - simple user + user management permissions
3) admin:admin - manager + has permission to RUD other user's

To create user, enter login and password in Login dialog and press Sing Up button.

REST API documentation can be found under /doc folder (pull and open index.htm in any modern browser)
It can be verified using Postman Chrome extension, or directly via browser - for GET requests.
Basic authorization is used for accessing REST API. But anyone is allowed to create user to access API.
