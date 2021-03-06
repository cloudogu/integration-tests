Nexus Integration Tests
==========================
Tags: nexus

Some Nexus integration tests


Authentication
--------------
Tags: nexus, workflow

Test Nexus Cas Authentication Workflow

* Open Nexus
* Nexus-Login "admin" with password "adminpw"
* Logout of Nexus


REST API u+p
--------------
Tags: nexus, rest_api

Test API by accessing json-file without Cas Authentication

* Create REST client to access Nexus API for "admin" with password "adminpw"
* Obtain Nexus json file
* Close Nexus API REST client


REST token API key
------------------
Tags: nexus, rest_api_token

Obtain key from Nexus and use it to log in

* Obtain Nexus key with "admin" and "adminpw"
* Nexus-Login with key


Single Sign Out
---------------
Tags: nexus, single_sign_out

Log in to Nexus and test Single Sign out via cas/logout

* Nexus-Login "admin" with password "adminpw" for Single Sign out
* Log out from Jenkins via cas/logout


Groups
------
Tags: nexus, groups

Access Nexus-API as user in group with admin rights, test if administration
privileges are given and test opposite with user not in group with admin rights

* Access Nexus API as "admin" with password "adminpw" with admin rights
* Check if access to Nexus file accepted
* Quit Nexus client with admin rights
* Create "dummyuser" with password "dummyuser123" in Nexus
* Access Nexus API without admin rights
* Check if access to Nexus file not accepted
* Quit Nexus client without admin rights

___

Tear down step to assure log out

* Tear down logout for Nexus