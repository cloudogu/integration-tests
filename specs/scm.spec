SCM Integration Tests
==========================
Tags: scm
Some SCM integration tests


Authentication
--------------
Tags: scm_auth

Test SCM Cas Authentication Workflow

* Open SCM
* SCM-Login "admin" with password "password"
* Logout of SCM


REST API u+p
--------------
Tags: scm_api_up

Test API by accessing json-file without Cas Authentication

* Access SCM API via REST client for "admin" with password "password"
* Obtain SCM json file
* Close SCM API REST client


Single Sign Out
---------------
Tags: scm_sso

Log in to SCM and test Single Sign out via cas/logout

* SCM-Login "admin" with password "password" for Single Sign out
* Log out from SCM via cas/logout


Groups
------
Tags: scm_groups

Access SCM-API as user in group with admin rights, test if administration
privileges are given and test opposite with user not in group with admin rights

* Access SCM API as "admin" with password "password" with admin rights
* Check if access accepted
* Quit client with admin rights
* Access SCM API as "dummyuser" with password "dummyuser123" without admin rights
* Check if access not accepted
* Quit client without admin rights


User Attributes
---------------
Tags: scm_user_att

Login to SCM as admin user and look up if user data are the same as in 
usermgt

* Obtain user attributes of "admin" with "password" from usermgt for SCM
* Switch to SCM user site
* Compare user attributes with data of SCM
* Log out of SCM User Attributes