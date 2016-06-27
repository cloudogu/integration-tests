Jenkins Integration Tests
==========================
Tags: jenkins

Some Jenkins integration tests


Authentication
--------------
Tags: jenkins_auth

Test Jenkins Cas Authentication Workflow

* Open Jenkins
* Jenkins-Login "admin" with password "password"
* Logout of Jenkins


REST API u+p
------------
Tags: jenkins_api_up

Test API by accessing json-file without Cas Authentication

* Access Jenkins API via REST client for "admin" with password "password"
* Obtain Jenkins json file
* Close Jenkins API REST client


REST token API key
------------------
Tags: jenkins_api_token

Obtain token from Jenkins and use it to log in

* Obtain Jenkins token with "admin" and "password"
* Jenkins-Login with token 


Single Sign Out
---------------
Tags: jenkins_sso

Login to Jenkins and test Single Sign out via cas/logout

* Jenkins-Login "admin" with password "password" for Single Sign out
* Log out from Jenkins via cas/logout


Groups
------
Tags: jenkins_groups

Login to Jenkins as user in group with admin rights, test if administration
privileges are given and test opposite with user not in group with admin rights

* Jenkins-Login "admin" with password "password" with admin rights
* Access Manage Jenkins
* Logout of Jenkins as user with admin rights
* Jenkins-Login "dummyuser" with password "dummyuser123" without admin rights
* Try to Access Manage Jenkins
* Logout of Jenkins as user without admin rights 


User Attributes
---------------
Tags: jenkins_user_att

Login to Jenkins as admin user and look up if user data are the same as in 
usermgt

* Obtain user attributes of "admin" with "password" from usermgt for Jenkins
* Switch to Jenkins user site
* Compare user attributes with data of Jenkins
* Log out of Jenkins User Attributes