Usermgt Integration Tests
==========================
Tags: usermgt

Some Usermgt integration tests


Authentication
--------------
Tags: usermgt, workflow

Test Usermgt Cas Authentication Workflow

* Open Usermgt
* Usermgt-Login "admin" with password "adminpw"
* Logout of Usermgt


REST API u+p
--------------
Tags: usermgt, rest_api

Test API by accessing json-file without Cas Authentication

* Create REST client to access Usermgt API for "admin" with password "adminpw"
* Obtain Usermgt json file
* Close Usermgt API REST client


Single Sign Out
---------------
Tags: usermgt, single_sign_out

Log in to Usermgt and test Single Sign out via cas/logout

* Usermgt-Login "admin" with password "adminpw" for Single Sign out
* Log out from Usermgt via cas/logout 

___

Tear down step to assure log out

* Tear down logout for Usermgt