Sonar Integration Tests
==========================
Tags: sonar

Some Sonar integration tests


Authentication
--------------
Tags: sonar, workflow

Test Sonar Cas Authentication Workflow

* Open Sonar
* Sonar-Login "admin" with password "password"
* Logout of Sonar


REST API u+p
--------------
Tags: sonar, rest_api

Test API by accessing json-file without Cas Authentication

* Create REST client to access Sonar API for "admin" with password "password"
* Obtain Sonar json file
* Close Sonar API REST client


REST token API key
------------------
Tags: sonar, rest_api_token

Obtain token from Sonar and use it to log in

* Obtain Sonar token with "admin" and "password"
* Sonar-Login with token


Single Sign Out
---------------
Tags: sonar, single_sign_out

Log in to Sonar and test Single Sign out via cas/logout

* Sonar-Login "admin" with password "password" for Single Sign out
* Log out from Sonar via cas/logout


Groups
------
Tags: sonar, groups

Login to Sonar as user in group with admin rights, test if administration
privileges are given and test opposite with user not in group with admin rights

* Sonar-Login "admin" with password "password" with admin rights
* Access Administration of Sonar page
* Logout of Sonar as user with admin rights
* Create "dummyuser" with password "dummyuser123" in Sonar
* Sonar-Login without admin rights
* Try to access Administration of Sonar page
* Logout of Sonar as user without admin rights


User Attributes
---------------
Tags: sonar, attributes

Login to Sonar as admin user and look up if user data are the same as in 
usermgt

* Obtain user attributes of "admin" with "password" from usermgt for Sonar
* Compare user attributes with data of Sonar
* Log out of Sonar User Attributes  