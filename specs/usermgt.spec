Usermgt Integration Tests
==========================
Tags: usermgt

Some Usermgt integration tests


Authentication
--------------
Tags: usermgt_auth

Test Usermgt Cas Authentication Workflow

* Open Usermgt
* Usermgt-Login "admin" with password "password"
* Logout of Usermgt


REST API u+p
--------------
Tags: usermgt_api_up

Test API by accessing json-file without Cas Authentication

* Access Usermgt API via REST client for "admin" with password "password"


Single Sign Out
---------------
Tags: usermgt_sso

Log in to Usermgt and test Single Sign out via cas/logout

* Usermgt-Login "admin" with password "password" for Single Sign out
* Log out from Usermgt via cas/logout 