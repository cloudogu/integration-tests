SCM Integration Tests
==========================
Tags: scm
Some SCM integration tests


Authentication
--------------
Tags: scm, workflow

Test SCM Cas Authentication Workflow

* Open SCM
* SCM-Login "admin" with password "password"
* Logout of SCM


REST API u+p
--------------
Tags: scm, rest_api

Test API by accessing json-file without Cas Authentication

* Create REST client to access SCM API for "admin" with password "password"
* Obtain SCM json file
* Close SCM API REST client


Single Sign Out
---------------
Tags: scm, single_sign_out

Log in to SCM and test Single Sign out via cas/logout

* SCM-Login "admin" with password "password" for Single Sign out
* Log out from SCM via cas/logout


Groups
------
Tags: scm, groups

Access SCM-API as user in group with admin rights, test if administration
privileges are given and test opposite with user not in group with admin rights

* Access SCM API as "admin" with password "password" with admin rights
* Check if access accepted
* Quit client with admin rights
* Create "dummyuser" with password "dummyuser123" in SCM
* Access SCM API without admin rights
* Check if access not accepted
* Quit client without admin rights


User Attributes
---------------
Tags: scm, attributes

Login to SCM as admin user and look up if user data are the same as in 
usermgt

* Obtain user attributes of "admin" with "password" from usermgt for SCM
* Compare user attributes with data of SCM
* Log out of SCM User Attributes

___

Tear down step to assure log out

* Tear down logout for SCM