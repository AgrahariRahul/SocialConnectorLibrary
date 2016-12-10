# SocialConnectorLibrary
This library can perform login , sharing and app invite features on facebook, google plus and twitter. 
creating a application on facebook, google and twitter developer console and then after add this library 
in your project and write a single lines of code to perform three operation login, sharing and app invite on facebok , google plus and twitter,



   Remember: Enable Google Sign In for this application for use of google login. link "https://developers.google.com/mobile/add?platform=android&cntapi=signin&cnturl=https:%2F%2Fdevelopers.google.com%2Fidentity%2Fsign-in%2Fandroid%2Fsign-in%3Fconfigured%3Dtrue&cntlbl=Continue%20Adding%20Sign-In"

    
	
	For facebook login
	
	
                SocialConnector  socialConnector = new SocialConnector.SocialBuilder(MainActivity.this, SocialConnection.FACEBOOK, ConnectionType.LOGIN)
                    .setSocialConnection(this).build();
					
      
	For google login
	  
                SocialConnector socialConnector = new SocialConnector.SocialBuilder(MainActivity.this, SocialConnection.GPLUS, ConnectionType.LOGIN)
                    .setSocialConnection(this).build();
					

	For twitter login
       
                SocialConnector socialConnector = new SocialConnector.SocialBuilder(MainActivity.this, SocialConnection.TWITTER, ConnectionType.LOGIN)
                    .setConsumerKey("enter consumer key")
                    .setConsumerSecret("enter consumer secret").setSocialConnection(this).build();
        