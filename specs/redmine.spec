Redmine Integration Tests
==========================
Tags: redmine

Some Redmine integration tests


Authentication
--------------
Tags: redmine, workflow

Test Redmine Cas Authentication Workflow

* Open Redmine
* Redmine-Login "admin" with password "adminpw"
* Logout of Redmine


REST API u+p
--------------
Tags: redmine, rest_api

Test API by accessing json-file without Cas Authentication

* Create REST client to access Redmine API for "admin" with password "adminpw"
* Obtain Redmine json file
* Close Redmine API REST client


REST token API key
------------------
Tags: redmine, rest_api_token

Obtain key from Redmine and use it to log in

* Obtain Redmine key with "admin" and "adminpw"
* Redmine-Login with key


Single Sign Out
---------------
Tags: redmine, single_sign_out

Log in to Redmine and test Single Sign out via cas/logout

* Redmine-Login "admin" with password "adminpw" for Single Sign out
* Log out from Redmine via cas/logout


Groups
------
Tags: redmine, groups

Login to Redmine as user in group with admin rights, test if administration
privileges are given and test opposite with user not in group with admin rights

* Redmine-Login "admin" with password "adminpw" with admin rights
* Access Administration of Redmine page
* Logout of Redmine as user with admin rights
* Create "dummyuser" with password "dummyuser123" in Redmine
* Redmine-Login without admin rights
* Try to access Administration of Redmine page
* Logout of Redmine as user without admin rights


User Attributes
---------------
Tags: redmine, attributes

Login to Redmine as admin user and look up if user data are the same as in 
usermgt

* Obtain user attributes of "admin" with "adminpw" from usermgt for Redmine
* Switch to Redmine user site
* Compare user attributes with data of Redmine
* Log out of Redmine User Attributes 

___

Tear down step to assure log out

* Tear down logout for Redmine