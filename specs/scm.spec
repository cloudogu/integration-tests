SCM Integration Tests
==========================
Tags: scm
Some SCM integration tests

Authentication
--------------
Tags: scm_auth

Test SCM Cas Authentication Workflow

* Open SCM
* SCM-Login "admin" with password "admin123"
* Logout of SCM

REST API u+p
--------------
Tags: scm_api_up

Test API by accessing json-file without Cas Authentication

* Access SCM API via REST client for "admin" with password "admin123"

Single Sign Out
---------------
Tags: scm_sso

Log in to SCM and test Single Sign out via cas/logout

* SCM-Login "admin" with password "admin123" for Single Sign out
* Log out from SCM via cas/logout