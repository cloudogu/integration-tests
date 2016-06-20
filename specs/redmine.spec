Redmine Integration Tests
==========================
Tags: redmine

Some Redmine integration tests

Authentication
--------------
Tags: redmine_auth

Test Redmine Cas Authentication Workflow

* Open Redmine
* Redmine-Login "admin" with password "admin123"
* Logout of Redmine

REST API u+p
--------------
Tags: redmine_api_up

Test API by accessing json-file without Cas Authentication

* Access Redmine API via REST client for "admin" with password "admin123"

REST token API key
------------------
Tags: redmine_api_key

Obtain key from Redmine and use it to log in

* Obtain Redmine key with "admin" and "admin123"
* Redmine-Login with key 