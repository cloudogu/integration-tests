Sonar Integration Tests
==========================
Tags: sonar

Some Sonar integration tests


Authentication
--------------
Tags: sonar_auth

Test Sonar Cas Authentication Workflow

* Open Sonar
* Sonar-Login "admin" with password "password"
* Logout of Sonar


REST API u+p
--------------
Tags: sonar_api_up

Test API by accessing json-file without Cas Authentication

* Access Sonar API via REST client for "admin" with password "password"


REST token API key
------------------
Tags: sonar_api_token

Obtain token from Sonar and use it to log in

* Obtain Sonar token with "admin" and "password"
* Sonar-Login with token


Single Sign Out
---------------
Tags: sonar_sso

Log in to Sonar and test Single Sign out via cas/logout

* Sonar-Login "admin" with password "password" for Single Sign out
* Log out from Sonar via cas/logout


Groups
------
Tags: sonar_groups

Login to Sonar as user in group with admin rights, test if administration
privileges are given and test opposite with user not in group with admin rights

* Sonar-Login "admin" with password "password" with admin rights
* Access Administration of Sonar page
* Logout of Sonar as user with admin rights
* Sonar-Login "dummyuser" with password "dummyuser123" without admin rights
* Try to access Administration of Sonar page
* Logout of Sonar as user without admin rights  