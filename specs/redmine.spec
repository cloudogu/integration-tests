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

Single Sign Out
---------------
Tags: redmine_sso

Log in to Redmine and test Single Sign out via cas/logout

* Redmine-Login "admin" with password "admin123" for Single Sign out
* Log out from Redmine via cas/logout

Groups
------
Tags: redmine_groups

Login to Redmine as user in group with admin rights, test if administration
privileges are given and test opposite with user not in group with admin rights

* Redmine-Login "admin" with password "admin123" with admin rights
* Access Administration
* Logout of Redmine as user with admin rights
* Redmine-Login "dummyuser" with password "dummyuser123" without admin rights
* Try to access Administration
* Logout of Redmine as user without admin rights 