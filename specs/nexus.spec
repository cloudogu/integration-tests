Nexus Integration Tests
==========================
Tags: nexus

Some Nexus integration tests

Authentication
--------------
Tags: nexus_auth

Test Nexus Cas Authentication Workflow

* Open Nexus
* Nexus-Login "admin" with password "admin123"
* Logout of Nexus

REST API u+p
--------------
Tags: nexus_api_up

Test API by accessing json-file without Cas Authentication

* Access Nexus API via REST client for "admin" with password "admin123"

Single Sign Out
---------------
Tags: nexus_sso

Log in to Nexus and test Single Sign out via cas/logout

* Nexus-Login "admin" with password "admin123" for Single Sign out
* Log out from Jenkins via cas/logout