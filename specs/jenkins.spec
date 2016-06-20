Jenkins Integration Tests
==========================
Tags: jenkins

Some Jenkins integration tests

Authentication
--------------
Tags: jenkins_auth

Test Jenkins Cas Authentication Workflow

* Open Jenkins
* Jenkins-Login "admin" with password "admin123"
* Logout of Jenkins

REST API u+p
------------
Tags: jenkins_api_up

Test API by accessing json-file without Cas Authentication

* Access Jenkins API via REST client for "admin" with password "admin123"

REST token API key
------------------
Tags: jenkins_api_token

Obtain token from Jenkins and use it to log in

* Obtain Jenkins token with "admin" and "admin123"
* Jenkins-Login with token 

Single Sign Out
---------------
Tags: jenkins_sso

Log in to Jenkins and test Single Sign out via cas/logout

* Jenkins-Login "admin" with password "admin123" for Single Sign out
* Log out from Jenkins via cas/logout