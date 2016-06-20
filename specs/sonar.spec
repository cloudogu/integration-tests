Sonar Integration Tests
==========================
Tags: sonar

Some Sonar integration tests

Authentication
--------------
Tags: sonar_auth

Test Sonar Cas Authentication Workflow

* Open Sonar
* Sonar-Login "admin" with password "admin123"
* Logout of Sonar

REST API u+p
--------------
Tags: sonar_api_up

Test API by accessing json-file without Cas Authentication

* Access Sonar API via REST client for "admin" with password "admin123"

REST token API key
------------------
Tags: sonar_api_token

Obtain token from Sonar and use it to log in

* Obtain Sonar token with "admin" and "admin123"
* Sonar-Login with token

Single Sign Out
---------------
Tags: sonar_sso

Log in to Sonar and test Single Sign out via cas/logout

* Sonar-Login "admin" with password "admin123" for Single Sign out
* Log out from Sonar via cas/logout 